package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Federacion;
import modelo.*;
import utilidad.ConexionBBDD;

public class SocioDAO {

    private static final String INSERTAR_SOCIO_SQL = "INSERT INTO Socio (nombre, tipoSocio) VALUES (?, ?)";
    private static final String SELECCIONAR_SOCIO_POR_ID_SQL = "SELECT * FROM Socio WHERE idSocio = ?";
    private static final String SELECCIONAR_TODOS_LOS_SOCIOS_SQL = "SELECT * FROM Socio";
    private static final String ACTUALIZAR_SOCIO_SQL = "UPDATE Socio SET nombre = ?, tipoSocio = ? WHERE idSocio = ?";
    private static final String ELIMINAR_SOCIO_SQL = "DELETE FROM Socio WHERE idSocio = ?";

    public void insertarSocio(Socio socio) throws SQLException {
        try (Connection conexion = ConexionBBDD.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement(INSERTAR_SOCIO_SQL)) {
            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setString(2, socio.getTipoSocio());
            preparedStatement.executeUpdate();
        }
    }

    public Socio seleccionarSocioPorId(int id) throws SQLException {
        Socio socio = null;
        try (Connection conexion = ConexionBBDD.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement(SELECCIONAR_SOCIO_POR_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                socio = construirSocio(resultSet);
            }
        }
        return socio;
    }

    public List<Socio> seleccionarTodosLosSocios() throws SQLException {
        List<Socio> socios = new ArrayList<>();
        try (Connection conexion = ConexionBBDD.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement(SELECCIONAR_TODOS_LOS_SOCIOS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Socio socio = construirSocio(resultSet);
                socios.add(socio);
            }
        }
        return socios;
    }

    private Socio construirSocio(ResultSet resultSet) throws SQLException {
        int idSocio = resultSet.getInt("idSocio");
        String nombre = resultSet.getString("nombre");
        String tipoSocio = resultSet.getString("tipoSocio");

        // Determinar qué subclase de Socio instanciar según el tipo de socio
        if (tipoSocio.equals("Estandar")) {
            // Obtener el NIF de la consulta
            String nif = resultSet.getString("nif");
            // Obtener el valor booleano del seguro contratado de la consulta
            boolean seguroContratado = resultSet.getBoolean("seguroContratado");
            // Obtener el precio del seguro contratado de la consulta
            double precioSeguro = resultSet.getDouble("precio");
            // Crear un objeto Seguro con los datos obtenidos
            Seguro seguro = new Seguro(seguroContratado, precioSeguro);
            return new Estandar(idSocio, nombre, nif, seguro);
        } else if (tipoSocio.equals("Federado")) {
            // Obtener el NIF de la consulta
            String nif = resultSet.getString("nif");
            // Obtener el id de la federación de la consulta
            int idFederacion = resultSet.getInt("idFederacion");
            // Obtener el nombre de la federación usando el id
            String nombreFederacion = obtenerNombreFederacionPorId(idFederacion);
            // Crear un objeto Federacion con los datos obtenidos
            Federacion federacion = new Federacion(nombreFederacion);
            federacion.setIdFederacion(idFederacion);
            return new Federado(idSocio, nombre, federacion, nif);
        } else if (tipoSocio.equals("Infantil")) {
            // Obtener el id del tutor de la consulta
            int idTutor = resultSet.getInt("idTutor");
            return new Infantil(idSocio, nombre, idTutor);
        }

        // Si no se reconoce el tipo de socio, puedes devolver null o lanzar una excepción,
        // dependiendo de cómo quieras manejar esta situación.
        return null;
    }

    private String obtenerNombreFederacionPorId(int idFederacion) throws SQLException {
        // Implementa la lógica para obtener el nombre de la federación usando el id
        // Esto podría implicar una nueva consulta a la base de datos
        // Aquí se muestra solo un esquema de cómo podrías hacerlo
        String nombreFederacion = null;
        try (Connection conexion = ConexionBBDD.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement("SELECT nombreFederacion FROM Federacion WHERE idFederacion = ?")) {
            preparedStatement.setInt(1, idFederacion);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                nombreFederacion = resultSet.getString("nombreFederacion");
            }
        }
        return nombreFederacion;
    }






    public boolean actualizarSocio(Socio socio) throws SQLException {
        boolean actualizado = false;
        try (Connection conexion = ConexionBBDD.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement(ACTUALIZAR_SOCIO_SQL)) {
            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setString(2, socio.getTipoSocio());
            preparedStatement.setInt(3, socio.getIdSocio());
            actualizado = preparedStatement.executeUpdate() > 0;
        }
        return actualizado;
    }

    public boolean eliminarSocio(int id) throws SQLException {
        boolean eliminado = false;
        try (Connection conexion = ConexionBBDD.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement(ELIMINAR_SOCIO_SQL)) {
            preparedStatement.setInt(1, id);
            eliminado = preparedStatement.executeUpdate() > 0;
        }
        return eliminado;
    }
}
