package vista;

import utilidad.ConexionBBDD;

import java.sql.Connection;

import static utilidad.Teclado.pedirString;

public class Test {
    public static void main(String[] args) {

        String datoRecibido = pedirString("Esto es leer el teclado: ");
        System.out.println(datoRecibido);

    }
}
