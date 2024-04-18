package modelo.dao;

import utilidad.*;
import modelo.*;

import java.sql.*;
import java.util.ArrayList;

public class ExcursionDAO {

    private Connection conexion;

    private ConexionBBDD bdd;

    public ExcursionDAO() {
        bdd = new ConexionBBDD();
    }

    public void agregarExcursion(Excursion excursion) {
        conexion = bdd.obtenerConexion();
        String sql = "INSERT INTO excursion (descripcion, fechaExcursion, duracionDias, precioInscripcion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, excursion.getDescripcion());
            statement.setDate(2, new java.sql.Date(excursion.getFechaExcursion().getTime()));
            statement.setInt(3, excursion.getDuracionDias());
            statement.setDouble(4, excursion.getPrecioInscripcion());

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
               System.out.println("Excursión agregada correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la excursión: " + e.getMessage());
        }

        bdd.cerrarConexion(conexion);

    }

    // creo una funcion para eliminar las excursiones que inserte para ir realizando pruebas en la bbdd

    public boolean eliminarExcursion(int idExcursion) {
        conexion = bdd.obtenerConexion();
        String sql = "DELETE FROM Excursion WHERE idExcursion = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, idExcursion);
            int filasEliminadas = statement.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Excursión eliminada correctamente.");
                return true;
            } else {
                System.out.println("No se encontró la excursión con ID: " + idExcursion);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la excursión: " + e.getMessage());
            return false;
        } finally {
            bdd.cerrarConexion(conexion);
        }
    }


    public ArrayList<Excursion> obtenerListaExcursiones(Date fechaInicio, Date fechaFin) {
        conexion = bdd.obtenerConexion();
        ArrayList<Excursion> listaExcursiones = new ArrayList<Excursion>();
        String sql = "SELECT * FROM excursion WHERE fechaExcursion BETWEEN ? AND ? ORDER BY fechaExcursion";
        try (PreparedStatement statement = conexion.prepareStatement(sql)){
            statement.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            statement.setDate(2, new java.sql.Date(fechaFin.getTime()));
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                Excursion aux = new Excursion();
                aux.setIdExcursion(resultado.getInt("idExcursion"));
                aux.setDescripcion(resultado.getString("descripcion"));
                aux.setFechaExcursion(resultado.getDate("fechaExcursion"));
                aux.setDuracionDias(resultado.getInt("duracionDias"));
                aux.setPrecioInscripcion(resultado.getDouble("precioInscripcion"));
                listaExcursiones.add(aux);
            }
        } catch (SQLException e) {
            System.out.println("Error al Mostrar la excursión: " + e.getMessage());
        } finally {
            bdd.cerrarConexion(conexion);
        }
        return listaExcursiones;
    }

    public void mostrarListaExcursiones(ArrayList<Excursion> listaExcursiones) {

        if (!listaExcursiones.isEmpty()) {
            System.out.println("Lista de Excursiones:");
            for (Excursion exc : listaExcursiones) {
                System.out.println("ID: " + exc.getIdExcursion() + ", Descripción: " + exc.getDescripcion() +
                        ", Fecha: " + exc.getFechaExcursion() + ", Duración (días): " + exc.getDuracionDias() +
                        ", Precio: $" + exc.getPrecioInscripcion());
            }
        } else {
            System.out.println("No se encontraron excursiones en el rango de fechas indicado.");
        }
    }


}

