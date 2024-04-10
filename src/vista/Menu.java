package vista;

import modelo.Excursion;
import utilidad.Teclado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static utilidad.Teclado.pedirInt;

public class Menu {

    public Menu() {}

    private void menuPrincipal(){
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

    private void menuExcursiones() {
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
                    Excursion excursion = new Excursion();
                    excursion.setDescripcion(Teclado.pedirString("Descripcion de la Excursion: "));
                    Date fechaExcursion = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    while (fechaExcursion == null) {
                        try {
                            fechaExcursion = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de la excursión (formato: dd/MM/yyyy):"));
                            excursion.setFechaExcursion(fechaExcursion);
                        } catch (ParseException e) {
                            System.out.println("Formato de fecha incorrecto. Intente nuevamente.");
                        }
                    }
                    excursion.setDuracionDias(Teclado.pedirInt("Ingrese la duración en días de la excursión:"));
                    excursion.setPrecioInscripcion(Teclado.pedirDouble("Ingrese el precio de inscripción:"));
                   // pendiente a que este la BBDD System.out.println("\n------------------------------------------");
                   // System.out.println("     Excursión agregada correctamente");
                   // System.out.println("------------------------------------------\n\n" + excursion + "\n");
                    break;
                case 2:
                    // hay que cambiar la funcion para mostrar inscripciones cuando ya este la base de datos creada.
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
