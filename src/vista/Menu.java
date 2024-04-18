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
            System.out.println("3. Volver al menú principal");

            int opcion = pedirInt("Elige lo que quieres hacer: ");

            switch (opcion) {
                case 1:
                    Datos.crearExcursion();
                    break;
                case 2:
                    Datos.mostrarExcursionesPorFechas();
                    break;
                case 3:
                    salirMenuExcursiones = true;
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("               Saliendo al Menu Principal");
                    System.out.println("-------------------------------------------------------");
                    break;
                default:
                    System.out.println("Elige una opcion Valida");
                    break;
            }
        }

    }

    private void menuSocios() throws SQLException {
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
                    Datos.crearSocio();
                    System.out.println("Socio Agregado Correctamente");
                    break;
                case 2:
                    int idSocio = Teclado.pedirInt("Ingrese el ID del socio cuyo seguro quieres modificar: ");
                    Datos.modificarSeguro(idSocio);
                    break;
                case 3:
                    Datos.borrarSocio();
                    break;
                case 4:
                    Datos.mostrarSocios();
                    break;
                case 5:

                    break;
                case 6:
                    salirMenuSocios = true;
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("               Saliendo al Menu Principal");
                    System.out.println("-------------------------------------------------------");
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
