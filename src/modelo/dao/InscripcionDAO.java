package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Inscripcion;
import utilidad.ConexionBBDD;

public class InscripcionDAO {

    private final ConexionBBDD conexionBBDD;

    public InscripcionDAO() {
        this.conexionBBDD = new ConexionBBDD();
    }

    public void insertarInscripcion(Inscripcion inscripcion) {
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(
                     "INSERT INTO Inscripcion (idSocio, idExcursion, fechaInscripcion) VALUES (?, ?, ?)")) {

            statement.setInt(1, inscripcion.getIdSocio());
            statement.setInt(2, inscripcion.getIdExcursion());
            statement.setDate(3, new java.sql.Date(inscripcion.getFechaInscripcion().getTime()));

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Inscripcion> seleccionarTodasLasInscripciones() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement("SELECT * FROM Inscripcion");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setIdInscripcion(resultSet.getInt("idInscripcion"));
                inscripcion.setIdSocio(resultSet.getInt("idSocio"));
                inscripcion.setIdExcursion(resultSet.getInt("idExcursion"));
                inscripcion.setFechaInscripcion(resultSet.getDate("fechaInscripcion"));
                inscripciones.add(inscripcion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return inscripciones;
    }

    public void eliminarInscripcion(int idInscripcion) {
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement("DELETE FROM Inscripcion WHERE idInscripcion = ?")) {

            statement.setInt(1, idInscripcion);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
