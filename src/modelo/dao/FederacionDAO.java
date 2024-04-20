package modelo.dao;

import modelo.Federacion;
import utilidad.ConexionBBDD;
import utilidad.Teclado;

import java.sql.*;

public class FederacionDAO {


    private Connection conexion;

    private ConexionBBDD bdd;

    public FederacionDAO() {
        bdd = new ConexionBBDD();
    }

    public Federacion obtenerFederacion(int idFederacion) throws SQLException {
        Federacion federacion = null;
        conexion = bdd.obtenerConexion();
        String sql = "SELECT idFederacion, nombreFederacion FROM Federacion WHERE idFederacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idFederacion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                federacion = new Federacion(rs.getInt("idFederacion"), rs.getString("nombreFederacion"));
            }
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


}
