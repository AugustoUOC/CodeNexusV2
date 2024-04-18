package utilidad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Teclado {

    private static Scanner scanner;

    public static String pedirString(String mensaje) {
        scanner = new Scanner(System.in);
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public static int pedirInt(String mensaje) {
        scanner = new Scanner(System.in);
        System.out.print(mensaje);
        return scanner.nextInt();
    }

    public static double pedirDouble(String mensaje) {
        scanner = new Scanner(System.in);
        System.out.print(mensaje);
        return scanner.nextDouble();
    }

    public static boolean confirmarAccion(String mensaje) {
        String respuesta = "";
        while (true) {
            respuesta = pedirString(mensaje + "(si/no): ");
            if(respuesta.equals("si")) {
                return true;
            }else if (respuesta.equals("no")){
                return false;
            }
            System.out.println("Te has equivocado prueba otra vez");
        }
    }

}
