package modelo.dao;

import modelo.Inscripcion;
import utilidad.ConexionBBDD;

import java.sql.*;
import java.util.ArrayList;

public class InscripcionDAO {

    private Connection conexion;

    private ConexionBBDD bdd;

    public InscripcionDAO() {
        bdd = new ConexionBBDD();
    }

    public void agregarInscripcion(Inscripcion inscripcion){
        conexion = bdd.obtenerConexion();
        String sql = "INSERT INTO inscripcion (idSocio, idExcursion, fechaInscripcion) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, inscripcion.getIdSocio());
            statement.setInt(2, inscripcion.getIdExcursion());
            statement.setDate(3, new java.sql.Date(inscripcion.getFechaInscripcion().getTime()));
            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0){
                System.out.println("La Inscripcion se ha insertado correctamente. ");
            } else {
                System.out.println("No se pudo insertar la inscripción.");
            }

        }catch (SQLException e){
            System.err.println("Error al insertar Inscripcion");
            System.err.println(e.getErrorCode() + " " + e.getMessage());
        } finally {
            bdd.cerrarConexion(conexion);
        }
    }

    public Inscripcion buscarInscripcionPorID(int idInscripcion){
        Inscripcion i = null;
        conexion = bdd.obtenerConexion();
        String sql = "SELECT * FROM inscripcion WHERE idInscripcion = ?";
        try {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idInscripcion);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                i = new Inscripcion(
                        resultado.getInt("idInscripcion"),
                        resultado.getInt("idSocio"),
                        resultado.getInt("idExcursion"),
                        resultado.getDate("fechaInscripcion")
                );
            }
        }catch (SQLException e){
            System.err.println("No existe la ID de la Inscripcion indicada: " + e.getMessage());
        }
        return i;
    }

    public void eliminarInscripcion(int idInscripcion){
        conexion = bdd.obtenerConexion();
        String sql = "DELETE FROM inscripcion WHERE idInscripcion = ? AND fechaInscripcion < (" +
                "SELECT fechaExcursion FROM (" +
                "    SELECT e.fechaExcursion FROM excursion e " +
                "    INNER JOIN inscripcion i ON e.idExcursion = i.idExcursion " +
                "    WHERE i.idInscripcion = ?) AS subquery);";
        try {
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idInscripcion);
            statement.setInt(2, idInscripcion);
            int filasEliminadas = statement.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Inscripcion eliminada correctamente.");
            } else {
                System.out.println("No se ha podido eliminar la Inscripcion con ID: " + idInscripcion);
            }
        }catch (SQLException e){
            System.out.println("Error al eliminar la inscripcion: " + e.getMessage());
        } finally {
            bdd.cerrarConexion(conexion);
        }
    }

    public ArrayList<Inscripcion> obtenerListaInscripciones(){
        ArrayList<Inscripcion> listaInscripcion = new ArrayList<Inscripcion>();
        conexion = bdd.obtenerConexion();
        String sql = "SELECT * FROM inscripcion";
        try {
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()){
                Inscripcion i = new Inscripcion();
                i.setIdInscripcion(resultado.getInt("idInscripcion"));
                i.setIdSocio(resultado.getInt("idSocio"));
                i.setIdExcursion(resultado.getInt("idExcursion"));
                i.setFechaInscripcion(resultado.getDate("fechaInscripcion"));
                listaInscripcion.add(i);
            }
        }catch (SQLException e){
            System.out.println("Error al Obtener la lista de Inscripciones");
        } finally {
            bdd.cerrarConexion(conexion);
        }
        return listaInscripcion;
    }

    public ArrayList<Inscripcion> obtenerInscripcionesporSocio(int idSocio){
        ArrayList<Inscripcion> listaInscripcion = new ArrayList<Inscripcion>();
        conexion = bdd.obtenerConexion();
        String sql = "SELECT * FROM inscripcion WHERE idSocio = ?";
        try {
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idSocio);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()){
                Inscripcion i = new Inscripcion();
                i.setIdInscripcion(resultado.getInt("idInscripcion"));
                i.setIdSocio(resultado.getInt("idSocio"));
                i.setIdExcursion(resultado.getInt("idExcursion"));
                i.setFechaInscripcion(resultado.getDate("fechaInscripcion"));
                listaInscripcion.add(i);
            }
        }catch (SQLException e){
            System.out.println("Error al Obtener la lista de Inscripciones");
        }
        return listaInscripcion;
    }

    public ArrayList<Inscripcion> obtenerInscripcionesFiltroFechas(Date fechaInicio, Date fechaFin){
        conexion = bdd.obtenerConexion();
        ArrayList<Inscripcion> listaInscripciones = new ArrayList<Inscripcion>();
        String sql = "SELECT * FROM inscripcion WHERE fechaInscripcion BETWEEN ? AND ? ORDER BY fechaInscripcion";
        try {
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            statement.setDate(2, new java.sql.Date(fechaFin.getTime()));
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()){
                Inscripcion aux = new Inscripcion();
                aux.setIdInscripcion(resultado.getInt("idInscripcion"));
                aux.setIdSocio(resultado.getInt("idSocio"));
                aux.setIdExcursion(resultado.getInt("idExcursion"));
                aux.setFechaInscripcion(resultado.getDate("fechaInscripcion"));
                listaInscripciones.add(aux);
            }
        } catch (SQLException e) {
            System.out.println("Error al Mostrar las Inscripciones: " + e.getMessage());
        } finally {
            bdd.cerrarConexion(conexion);
        }
        return listaInscripciones;
    }


    public void mostrarListaInscripciones(ArrayList<Inscripcion> listaInscripcion){
        if (!listaInscripcion.isEmpty()){
            System.out.println("Lista de Inscripciones: ");
            for (Inscripcion i : listaInscripcion){
                System.out.println("IdInscripcion: " + i.getIdInscripcion() + ", IdSocio: " + i.getIdSocio() +
                        ", IdExcursion: " + i.getIdExcursion() + ", FechaInscripcion: " + i.getFechaInscripcion());
            }
        }else {
            System.out.println("No se encontraron Inscripciones registradas");
        }
    }


}
