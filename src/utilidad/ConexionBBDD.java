package utilidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static constante.PropiedadesBBDD.URL_BBDD;

public class ConexionBBDD {

    private Connection conexion = null;

    public ConexionBBDD() { //Hacemos conexion a la BBDD

    }

    public Connection obtenerConexion() {
        try {
            if (conexion == null) {
                conexion = DriverManager.getConnection(URL_BBDD);
                System.out.println("Te has conectado a la BBDD");
            }
        } catch (SQLException ex) {
            System.err.println("Error al Conectar a la BBDD: Error " + ex.getErrorCode());

        }
        return conexion;
    }

    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }


}
