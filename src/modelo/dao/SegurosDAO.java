package modelo.dao;

import modelo.Estandar;
import modelo.Seguro;
import utilidad.ConexionBBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SegurosDAO {

    private Connection conexion;

    private ConexionBBDD bdd;

    public SegurosDAO() {
        bdd = new ConexionBBDD();
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


}
