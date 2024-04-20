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
        SegurosDAO segurosDAO = new SegurosDAO();
        FederacionDAO federacionDAO = new FederacionDAO();
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
                        Seguro seguro = segurosDAO.obtenerSeguro(rs.getInt("seguroContratado"));
                        socio = new Estandar(rs.getInt("idSocio"), rs.getString("nombre"), rs.getString("nif"), seguro);
                        break;
                    case "Federado":
                        Federacion federacion = federacionDAO.obtenerFederacion(rs.getInt("idFederacion"));
                        socio = new Federado(rs.getInt("idSocio"), rs.getString("nombre"), federacion, rs.getString("nif"));
                        break;
                    case "Infantil":
                        socio = new Infantil(rs.getInt("idSocio"), rs.getString("nombre"), rs.getInt("idTutor"));
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el socio por ID: " + e.getMessage());
        }
        return socio;

    }

    public ArrayList<Socio> obtenerListaSocios(){
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

    public ArrayList<Socio> obtenerListaSociosPorTipo(String tipoSocio){
        conexion = bdd.obtenerConexion();
        ArrayList<Socio> listaSocios = new ArrayList<Socio>();
        String sql = construirConsultaSQL(tipoSocio);

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            ResultSet resultados = sentencia.executeQuery();
            while (resultados.next()) {
                Socio socio = crearSocioDesdeResultSet(resultados, tipoSocio);
                listaSocios.add(socio);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar los socios: " + e.getMessage());
        }finally {
            bdd.cerrarConexion(conexion);
        }
        return listaSocios;
    }

    private String construirConsultaSQL(String tipoSocio) {
        switch (tipoSocio) {
            case "Estandar":
                return "SELECT s.*, e.nif, sg.idSeguro, sg.seguroContratado, sg.precio FROM Socio s " +
                        "LEFT JOIN Estandar e ON s.idSocio = e.idSocio " +
                        "LEFT JOIN Seguro sg ON e.seguroContratado = sg.idSeguro " +
                        "WHERE s.tipoSocio = 'estandar'";
            case "Federado":
                return "SELECT s.*, f.nif, fd.idFederacion, fd.nombreFederacion FROM Socio s " +
                        "LEFT JOIN Federado f ON s.idSocio = f.idSocio " +
                        "LEFT JOIN Federacion fd ON f.idFederacion = fd.idFederacion " +
                        "WHERE s.tipoSocio = 'federado'";
            case "Infantil":
                return "SELECT s.*, i.idTutor FROM Socio s " +
                        "LEFT JOIN Infantil i ON s.idSocio = i.idSocio " +
                        "WHERE s.tipoSocio = 'infantil'";
            default:
                return "";
        }
    }

    private Socio crearSocioDesdeResultSet(ResultSet resultados, String tipoSocio) throws SQLException {
        int idSocio = resultados.getInt("idSocio");
        String nombre = resultados.getString("nombre");

        switch (tipoSocio) {
            case "Estandar":
                String nif = resultados.getString("nif");
                Seguro seguro = new Seguro(resultados.getInt("idSeguro"), resultados.getString("seguroContratado"), resultados.getDouble("precio"));
                return new Estandar(idSocio, nombre, nif, seguro);
            case "Federado":
                nif = resultados.getString("nif");
                Federacion federacion = new Federacion(resultados.getInt("idFederacion"), resultados.getString("nombreFederacion"));
                return new Federado(idSocio, nombre, federacion, nif);
            case "Infantil":
                int idTutor = resultados.getInt("idTutor");
                return new Infantil(idSocio, nombre, idTutor);
            default:
                return null;
        }
    }

    public void mostrarListaSocios(ArrayList<Socio> listaSocios) {

        if (!listaSocios.isEmpty()) {
            System.out.println("Lista de Socios:");
            for (Socio exc : listaSocios) {
                System.out.println("ID: " + exc.getIdSocio() + ", Nombre: " + exc.getNombre() +
                        ", Tipo de Socio: " + exc.getTipoSocio());
            }
        } else {
            System.out.println("No se encontraron Socios");
        }
    }

    public void mostrarListaSociosPorTipo(ArrayList<Socio> listaSocios) {
        if (!listaSocios.isEmpty()) {
            System.out.println("Lista de Socios:");
            for (Socio socio : listaSocios) {
                if (socio != null) {
                    System.out.println("ID: " + socio.getIdSocio() + ", Nombre: " + socio.getNombre() +
                            ", Tipo de Socio: " + socio.getTipoSocio());

                    if (socio instanceof Estandar) {
                        Estandar estandar = (Estandar) socio;
                        System.out.println("       NIF: " + estandar.getNif() +
                                ", Seguro: " + (estandar.getSeguroContratado() != null ? estandar.getSeguroContratado().getTipo() : "N/A") +
                                ", Precio del Seguro: " + (estandar.getSeguroContratado() != null ? estandar.getSeguroContratado().getPrecio() : "N/A"));
                    } else if (socio instanceof Federado) {
                        Federado federado = (Federado) socio;
                        System.out.println("       NIF: " + federado.getNif() +
                                ", Federación: " + (federado.getFederacion() != null ? federado.getFederacion().getNombre() : "N/A"));
                    } else if (socio instanceof Infantil) {
                        Infantil infantil = (Infantil) socio;
                        System.out.println("       ID Tutor: " + infantil.getIdTutor());
                    }
                } else {
                    System.out.println("Se encontró un socio null en la lista");
                }
            }
        } else {
            System.out.println("No se encontraron Socios");
        }
    }



}
