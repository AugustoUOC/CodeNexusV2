package modelo.dao;

import modelo.*;
import utilidad.*;

import java.sql.*;
import java.util.ArrayList;


public class SociosDAO {

    private Connection conexion;

    private ConexionBBDD bdd;

    public SociosDAO() {
        bdd = new ConexionBBDD();
    }


    public void agregarSocio(Socio socio) throws SQLException {
        conexion = null;
        try {
            conexion = bdd.obtenerConexion();
            if (conexion == null) {
                throw new SQLException("No se pudo obtener la conexión a la base de datos");
            }
            conexion.setAutoCommit(false);  // Inicia la transacción
            // Insertar en la tabla de socios y obtener el ID generado
            String sqlSocio = "INSERT INTO socio (nombre, tipoSocio) VALUES (?, ?)";
            try (PreparedStatement stmtSocio = conexion.prepareStatement(sqlSocio, Statement.RETURN_GENERATED_KEYS)) {
                stmtSocio.setString(1, socio.getNombre());
                stmtSocio.setString(2, socio.getTipoSocio());
                int filasActualizadas = stmtSocio.executeUpdate();
                System.out.println(filasActualizadas);
                if (filasActualizadas == 0) {
                    throw new SQLException("La creación del socio falló, ninguna fila afectada.");
                }

                // Obtener el ID generado para el socio
                try (ResultSet generatedKeys = stmtSocio.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        socio.setIdSocio(generatedKeys.getInt(1)); // Suponiendo que tienes un setter para idSocio
                        System.out.println(socio.getIdSocio());
                    } else {
                        throw new SQLException("La creación del socio falló, no se obtuvo ningún ID.");
                    }
                }
            }

            // Insertar en la tabla específica según el tipo de socio
            if (socio instanceof Estandar) {
                System.out.println(socio.getIdSocio());
                Estandar estandar = (Estandar) socio;
                String sqlEstandar = "INSERT INTO estandar (idSocio, nif, seguroContratado) VALUES (?, ?, ?)";
                try (PreparedStatement stmtEstandar = conexion.prepareStatement(sqlEstandar)) {
                    stmtEstandar.setInt(1, estandar.getIdSocio());
                    stmtEstandar.setString(2, estandar.getNif());
                    stmtEstandar.setInt(3, estandar.getSeguroContratado().getIdSeguro());
                    stmtEstandar.executeUpdate();
                }
            } else if (socio instanceof Federado) {
                Federado federado = (Federado) socio;
                String sqlFederado = "INSERT INTO federado (idSocio, nif, idFederacion) VALUES (?, ?, ?)";
                try (PreparedStatement stmtFederado = conexion.prepareStatement(sqlFederado)) {
                    stmtFederado.setInt(1, federado.getIdSocio());
                    stmtFederado.setString(2, federado.getNif());
                    stmtFederado.setInt(3, federado.getFederacion().getIdFederacion());
                    stmtFederado.executeUpdate();
                }
            } else if (socio instanceof Infantil) {
                Infantil infantil = (Infantil) socio;
                String sqlInfantil = "INSERT INTO infantil (idSocio, idTutor) VALUES (?, ?)";
                try (PreparedStatement stmtInfantil = conexion.prepareStatement(sqlInfantil)) {
                    stmtInfantil.setInt(1, infantil.getIdSocio());
                    stmtInfantil.setInt(2, infantil.getIdTutor());
                    stmtInfantil.executeUpdate();
                }
            }
            conexion.commit();  // Confirma la transacción
        } catch (SQLException e) {
            if (conexion != null) {
                conexion.rollback();  // Revierte la transacción en caso de error
            }
            throw e;
        } finally {
            if (conexion != null) {
                conexion.setAutoCommit(true);  // Restablece el modo de auto-commit
                conexion.close();
            }
        }
    }

    public boolean eliminarSocioPorId(int idSocio) throws SQLException {
        conexion = null;
        Socio socio = buscarSocioPorId(idSocio);
        if (socio == null) {
            System.out.println("No se encontró el socio con el ID: " + idSocio);
            return false;
        }
        String tipoSocio = socio.getClass().getSimpleName(); // Obtiene el nombre de la clase, que es el tipo de socio
        try {
            conexion = bdd.obtenerConexion();
            if (conexion == null) {
                throw new SQLException("No se pudo obtener la conexión a la base de datos");
            }
            conexion.setAutoCommit(false); // Inicia la transacción

            // Eliminar de la tabla específica según el tipo de socio
            String sqlDetalle = "DELETE FROM " + tipoSocio.toLowerCase() + " WHERE idSocio = ?";
            try (PreparedStatement stmtDetalle = conexion.prepareStatement(sqlDetalle)) {
                stmtDetalle.setInt(1, idSocio);
                stmtDetalle.executeUpdate();
            }

            // Eliminar el socio de la tabla principal
            String sqlSocio = "DELETE FROM socio WHERE idSocio = ?";
            try (PreparedStatement stmtSocio = conexion.prepareStatement(sqlSocio)) {
                stmtSocio.setInt(1, idSocio);
                int filasEliminadas = stmtSocio.executeUpdate();
                conexion.commit(); // Confirma la transacción
                return filasEliminadas > 0;
            }
        } catch (SQLException e) {
            if (conexion != null) {
                conexion.rollback(); // Revierte la transacción en caso de error
            }
            System.err.println("Error al eliminar el socio: " + e.getMessage());
            throw e;
        } finally {
            if (conexion != null) {
                conexion.setAutoCommit(true); // Restablece auto-commit
                conexion.close();
            }
        }
    }


    public Socio buscarSocioPorId(int idSocio) {
        Socio socio = null;
        conexion = bdd.obtenerConexion();
        String sql = "SELECT s.idSocio, s.nombre, s.tipoSocio, e.nif, e.seguroContratado, f.idFederacion, i.idTutor " +
                "FROM socio s " +
                "LEFT JOIN estandar e ON s.idSocio = e.idSocio " +
                "LEFT JOIN federado f ON s.idSocio = f.idSocio " +
                "LEFT JOIN infantil i ON s.idSocio = i.idSocio " +
                "WHERE s.idSocio = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idSocio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tipoSocio = rs.getString("tipoSocio");
                switch (tipoSocio) {
                    case "Estandar":
                        Seguro seguro = obtenerSeguro(rs.getInt("seguroContratado"));
                        socio = new Estandar(rs.getInt("idSocio"), rs.getString("nombre"), rs.getString("nif"), seguro);
                        break;
                    case "Federado":
                        Federacion federacion = obtenerFederacion(rs.getInt("idFederacion"));
                        socio = new Federado(rs.getInt("idSocio"), rs.getString("nombre"), federacion, rs.getString("nif"));
                        break;
                    case "Infantil":
                        socio = new Infantil(rs.getInt("idSocio"), rs.getString("nombre"), rs.getInt("idTutor"));
                        break;
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar el socio por ID: " + e.getMessage());
        }
        return socio;

    }

    public Seguro obtenerSeguro(int idSeguro) throws SQLException {
        Seguro seguro = null;
        conexion = bdd.obtenerConexion();
        String sql = "SELECT idSeguro, seguroContratado, precio FROM Seguro WHERE idSeguro = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idSeguro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                seguro = new Seguro(rs.getInt("idSeguro"), rs.getString("seguroContratado"), rs.getDouble("precio"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar el seguro por la ID: " + e.getMessage());
        }
        return seguro;

    }

    public void actualizarSeguroDeSocio(Estandar estandar) throws SQLException {
        String sql = "UPDATE estandar SET seguroContratado = ? WHERE idSocio = ?";
        try (Connection conexion = bdd.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, estandar.getSeguroContratado().getIdSeguro());
            stmt.setInt(2, estandar.getIdSocio());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Actualización del seguro fallida, no se modificó ninguna fila.");
            }
        }
    }




    public Federacion obtenerFederacion(int idFederacion) throws SQLException {
        Federacion federacion = null;
        String sql = "SELECT idFederacion, nombreFederacion FROM Federacion WHERE idFederacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idFederacion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                federacion = new Federacion(rs.getInt("idFederacion"), rs.getString("nombreFederacion"));
            }
            rs.close();
        }
        return federacion;
    }

    public Federacion obtenerFederacionPorNombre(String nombreFederacion) throws SQLException {
        Federacion federacion = null;
        conexion = bdd.obtenerConexion();
        String sql = "SELECT idFederacion, nombreFederacion FROM Federacion WHERE nombreFederacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreFederacion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                federacion = new Federacion(rs.getInt("idFederacion"), rs.getString("nombreFederacion"));
            } else {
                if (Teclado.confirmarAccion("Federación no encontrada, ¿desea crear una nueva?")) {
                    federacion = crearFederacion(nombreFederacion);
                } else {
                    System.out.println("Creacion de la Federacion cancelada.");
                }
            }
            rs.close();
        }
        return federacion;
    }

    private Federacion crearFederacion(String nombreFeredarion) throws SQLException {
        conexion = bdd.obtenerConexion();
        String insertSql = "INSERT INTO Federacion (nombreFederacion) VALUES (?)";
        try (PreparedStatement insertStmt = conexion.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, nombreFeredarion);
            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Crear federación falló, no se insertaron filas.");
            }

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idFederacion = generatedKeys.getInt(1);
                    return new Federacion(idFederacion, nombreFeredarion);
                } else {
                    throw new SQLException("Crear federación falló, no se obtuvo el ID generado.");
                }
            }
        }
    }

    public ArrayList<Socio> obtenerListaSocios() throws SQLException {
        conexion = bdd.obtenerConexion();
        ArrayList<Socio> listaSocios = new ArrayList<Socio>();
        String sql = "SELECT * FROM socio";
        try (PreparedStatement statement = conexion.prepareStatement(sql)){
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                Socio aux = new Socio();
                aux.setIdSocio(resultado.getInt("idSocio"));
                aux.setNombre(resultado.getString("nombre"));
                aux.setTipoSocio(resultado.getString("tipoSocio"));
                listaSocios.add(aux);
            }

        } catch (SQLException e) {
            System.out.println("Error al mostrar los socios: " + e.getMessage());
        }finally {
            bdd.cerrarConexion(conexion);
        }
        return listaSocios;
    }

    public ArrayList<Socio> obtenerListaSociosPorTipo() throws SQLException {
        conexion = bdd.obtenerConexion();
        ArrayList<Socio> listaSocios = new ArrayList<Socio>();





        return listaSocios;
    }

    public void mostrarListaSocios(ArrayList<Socio> listaSocios) {

        if (!listaSocios.isEmpty()) {
            System.out.println("Lista de Excursiones:");
            for (Socio exc : listaSocios) {
                System.out.println("ID: " + exc.getIdSocio() + ", Nombre: " + exc.getNombre() +
                        ", Tipo de Socio: " + exc.getTipoSocio());
            }
        } else {
            System.out.println("No se encontraron Socios");
        }
    }


}
