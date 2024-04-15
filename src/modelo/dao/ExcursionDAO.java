package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Excursion;
import utilidad.ConexionBBDD;

public class ExcursionDAO {

    private final ConexionBBDD conexionBBDD;

    public ExcursionDAO() {
        this.conexionBBDD = new ConexionBBDD();
    }

    public void insertarExcursion(Excursion excursion) {
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(
                     "INSERT INTO Excursion (descripcion, fechaExcursion, duracionDias, precioInscripcion) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, excursion.getDescripcion());
            statement.setDate(2, new java.sql.Date(excursion.getFechaExcursion().getTime()));
            statement.setInt(3, excursion.getDuracionDias());
            statement.setDouble(4, excursion.getPrecioInscripcion());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Excursion seleccionarExcursionPorId(int idExcursion) {
        Excursion excursion = null;
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement("SELECT * FROM Excursion WHERE idExcursion = ?")) {

            statement.setInt(1, idExcursion);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    excursion = new Excursion();
                    excursion.setIdExcursion(resultSet.getInt("idExcursion"));
                    excursion.setDescripcion(resultSet.getString("descripcion"));
                    excursion.setFechaExcursion(resultSet.getDate("fechaExcursion"));
                    excursion.setDuracionDias(resultSet.getInt("duracionDias"));
                    excursion.setPrecioInscripcion(resultSet.getDouble("precioInscripcion"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return excursion;
    }

    public List<Excursion> seleccionarTodasLasExcursiones() {
        List<Excursion> excursiones = new ArrayList<>();
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement("SELECT * FROM Excursion");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Excursion excursion = new Excursion();
                excursion.setIdExcursion(resultSet.getInt("idExcursion"));
                excursion.setDescripcion(resultSet.getString("descripcion"));
                excursion.setFechaExcursion(resultSet.getDate("fechaExcursion"));
                excursion.setDuracionDias(resultSet.getInt("duracionDias"));
                excursion.setPrecioInscripcion(resultSet.getDouble("precioInscripcion"));
                excursiones.add(excursion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return excursiones;
    }

    public void actualizarExcursion(Excursion excursion) {
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(
                     "UPDATE Excursion SET descripcion = ?, fechaExcursion = ?, duracionDias = ?, precioInscripcion = ? WHERE idExcursion = ?")) {

            statement.setString(1, excursion.getDescripcion());
            statement.setDate(2, new java.sql.Date(excursion.getFechaExcursion().getTime()));
            statement.setInt(3, excursion.getDuracionDias());
            statement.setDouble(4, excursion.getPrecioInscripcion());
            statement.setInt(5, excursion.getIdExcursion());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void eliminarExcursion(int idExcursion) {
        try (Connection conexion = conexionBBDD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement("DELETE FROM Excursion WHERE idExcursion = ?")) {

            statement.setInt(1, idExcursion);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
