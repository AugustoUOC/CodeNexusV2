package vista;

import modelo.Datos;
import modelo.Excursion;
import utilidad.ConexionBBDD;
import utilidad.Teclado;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static utilidad.Teclado.pedirInt;

public class Menu {

    private Connection conexion;

    public Menu() {
    }

    public void menuPrincipal() throws ParseException, SQLException {


        boolean finalizarPrograma = false;
        System.out.println("Bienvenido");
        while (!finalizarPrograma) {


            System.out.println("1. Gestión de excursiones");
            System.out.println("2. Gestión de socios");
            System.out.println("3. Gestión de inscripciones");
            System.out.println("0. Salir del programa");

            int opcion = pedirInt("Elige lo que quieres hacer: ");

            switch (opcion) {
                case 1:
                    menuExcursiones();
                    break;
                case 2:
                    menuSocios();
                    break;
                case 3:
                    menuInscripciones();
                    break;
                case 0:
                    finalizarPrograma = true;
                    break;
                default:
                    System.out.println("Elige una opcion Valida");
                    break;
            }


        }


    }

    public void menuExcursiones() throws ParseException, SQLException {
        boolean salirMenuExcursiones = false;
        System.out.println("\n-------------------------------------------------------");
        System.out.println("     Entrando al menú de la gestión de excursiones");
        System.out.println("-------------------------------------------------------");

        while (!salirMenuExcursiones) {
            System.out.println("1. Añadir excursión");
            System.out.println("2. Mostrar excursiones");
            System.out.println("0. Volver al menú principal");

            int opcion = pedirInt("Elige lo que quieres hacer: ");

            switch (opcion) {
                case 1:
                    Datos.crearExcursion();
                    break;
                case 2:
                    // hay que cambiar la funcion para mostrar inscripciones cuando ya este la base de datos creada.
                    Datos.mostrarExcursionesPorFechas();
                    break;
                case 3:
                    salirMenuExcursiones = true;
                    break;
                default:
                    System.out.println("Elige una opcion Valida");
                    break;
            }
        }

    }

    private void menuSocios() {
        boolean salirMenuSocios = false;
        System.out.println("\n--------------------------------------------------");
        System.out.println("     Entrando al menú de la gestión de socios");
        System.out.println("--------------------------------------------------");

        while(!salirMenuSocios) {
            System.out.println("1. Añadir un nuevo socio");
            System.out.println("2. Modificar el tipo de seguro de socio Estándar existente");
            System.out.println("3. Eliminar un socio");
            System.out.println("4. Mostrar los socios");
            System.out.println("5. Mostrar la factura mensual de un socio");
            System.out.println("0. Volver al menú principal");

            int opcion = pedirInt("Elige lo que quieres hacer: ");

            switch (opcion) {
                case 1:
                    // hay que refactorizar la funcion añadir socio cuando la base de datos este creada
                    break;
                case 2:
                    // hay que refactorizar la funcion modificarSeguro del socio cuando la base de datos este creada
                    break;
                case 3:
                    // hay que refactorizar la funcion EliminarSocio cuando la base de datos este creada
                    break;
                case 4:
                    // hay que refactorizar la funcion MostrarSocios cuando la base de datos este creada
                    break;
                case 5:

                    break;
                case 6:
                    salirMenuSocios = true;
                    break;
                default:
                    System.out.println("Elige una opcion Valida");
                    break;

            }
        }

    }

    private void menuInscripciones() {
        boolean salirMenuInscripciones = false;

    }
}
