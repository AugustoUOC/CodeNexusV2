package utilidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static constante.PropiedadesBBDD.URL_BBDD;

public class ConexionBBDD {

    private Connection conexion = null;

    public ConexionBBDD() { //Hacemos conexion a la BBDD
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection obtenerConexion() {
        try {
            if (conexion == null) {
                conexion = DriverManager.getConnection(URL_BBDD);
            }
        } catch (SQLException ex) {
            System.err.println("Error al Conectar a la BBDD: Error " + ex.getErrorCode());

        }
            return conexion;
    }

    public void cerrarConexion() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }


}
