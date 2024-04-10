package vista;

import utilidad.ConexionBBDD;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) {

        ConexionBBDD cb = new ConexionBBDD();
        Connection con = cb.obtenerConexion();

    }
}
