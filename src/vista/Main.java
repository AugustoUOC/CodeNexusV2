package vista;
import modelo.*;
import modelo.Datos;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true; // Variable para controlar si se debe continuar ejecutando el programa
        // Bucle principal del menú
        System.out.println("\nBienvenido.\n\n¿Qué gestión le gustaría realizar?");
        while (continuar) {
            System.out.println("1. Gestión de excursiones");
            System.out.println("2. Gestión de socios");
            System.out.println("3. Gestión de inscripciones");
            System.out.println("0. Salir del programa");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            boolean continuarExcursion = true;
            boolean continuarSocio = true;
            boolean continuarInscripcion = true;
            switch (opcion) {
                case 1:
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("     Entrando al menú de la gestión de excursiones");
                    System.out.println("-------------------------------------------------------");
                    System.out.println("\n¿Qué le gustaría hacer?");
                    while (continuarExcursion) {
                        System.out.println("1. Añadir excursión");
                        System.out.println("2. Mostrar excursiones");
                        System.out.println("0. Volver al menú principal");
                        int opcionExcursion = scanner.nextInt();
                        scanner.nextLine();
                        switch (opcionExcursion) {
                            case 1:
                                Datos.crearExcursion();
                                System.out.println("¿Quieres realizar alguna otra gestión de las excursiones?");
                                System.out.println("1. Si");
                                System.out.println("2. No");
                                System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                int posibildadVolver1 = scanner.nextInt();
                                if (posibildadVolver1 == 1) {
                                    System.out.println("\n¿Qué otra gestíón de excursiones le gustaría hacer?");
                                } else {
                                    continuarExcursion = false;
                                    System.out.println("\n-------------------------------------");
                                    System.out.println("     Volviendo al menú principal");
                                    System.out.println("-------------------------------------\n");
                                }
                                break;
                            case 2:
                                Datos.mostrarExcursionesPorFechas(Datos.listaExcursiones);
                                System.out.println("¿Quieres realizar alguna otra gestión de las excursiones?");
                                System.out.println("1. Si");
                                System.out.println("2. No");
                                System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                int posibildadVolver2 = scanner.nextInt();
                                if (posibildadVolver2 == 1) {
                                    System.out.println("\n¿Qué otra gestíón de excursiones le gustaría hacer?");
                                } else {
                                    continuarExcursion = false;
                                    System.out.println("\n-------------------------------------");
                                    System.out.println("     Volviendo al menú principal");
                                    System.out.println("-------------------------------------\n");
                                }
                                break;
                            case 0:
                                continuarExcursion = false;
                                System.out.println("\n-------------------------------------");
                                System.out.println("     Volviendo al menú principal");
                                System.out.println("-------------------------------------\n");
                                break;
                            default:
                                System.out.println("\n----------------------------------");
                                System.out.println("     Esta opción no es válida");
                                System.out.println("----------------------------------\n");
                                System.out.println("Por favor elija otra opción:");
                                break;
                        }
                    }
                    System.out.println("¿Le gustaría realizar alguna otra gestión?");
                    break;
                    case 2:
                        System.out.println("\n--------------------------------------------------");
                        System.out.println("     Entrando al menú de la gestión de socios");
                        System.out.println("--------------------------------------------------");
                        System.out.println("\n¿Qué le gustaría hacer?");
                        while (continuarSocio) {
                            System.out.println("1. Añadir un nuevo socio");
                            System.out.println("2. Modificar el tipo de seguro de socio Estándar existente");
                            System.out.println("3. Eliminar un socio");
                            System.out.println("4. Mostrar los socios");
                            System.out.println("5. Mostrar la factura mensual de un socio");
                            System.out.println("0. Volver al menú principal");
                            int opcionSocio = scanner.nextInt();
                            scanner.nextLine();
                            switch (opcionSocio) {
                                case 1:
                                    Datos.crearSocio();
                                    System.out.println("¿Quieres realizar alguna otra gestión de las socios?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver1 = scanner.nextInt();
                                    if (posibildadVolver1 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de socios le gustaría hacer?");
                                    } else {
                                        continuarSocio = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 2:
                                    Datos.modificarSeguro();
                                    System.out.println("¿Quieres realizar alguna otra gestión de las socios?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver2 = scanner.nextInt();
                                    if (posibildadVolver2 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de socios le gustaría hacer?");
                                    } else {
                                        continuarSocio = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 3:
                                    Datos.borrarSocio(Datos.listaSocios, Datos.listaInscripciones);
                                    System.out.println("¿Quieres realizar alguna otra gestión de las socios?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver3 = scanner.nextInt();
                                    if (posibildadVolver3 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de socios le gustaría hacer?");
                                    } else {
                                        continuarSocio = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 4:
                                    Datos.mostrarSocio(Datos.listaSocios);
                                    System.out.println("¿Quieres realizar alguna otra gestión de las socios?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver4 = scanner.nextInt();
                                    if (posibildadVolver4 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de socios le gustaría hacer?");
                                    } else {
                                        continuarSocio = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 5:
                                    Datos.mostrarFacturaTotal(Datos.listaSocios, Datos.listaExcursiones, Datos.listaInscripciones);
                                    System.out.println("¿Quieres realizar alguna otra gestión de las socios?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver5 = scanner.nextInt();
                                    if (posibildadVolver5 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de socios le gustaría hacer?");
                                    } else {
                                        continuarSocio = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 0:
                                    continuarSocio = false;
                                    System.out.println("\n-------------------------------------");
                                    System.out.println("     Volviendo al menú principal");
                                    System.out.println("-------------------------------------\n");
                                    break;
                                default:
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Por favor elija otra opción:");
                                    break;
                            }
                        }
                        System.out.println("¿Le gustaría realizar alguna otra gestión?");
                        break;
                    case 3:
                        System.out.println("\n---------------------------------------------------------");
                        System.out.println("     Entrando al menú de la gestión de inscripciones");
                        System.out.println("---------------------------------------------------------");
                        System.out.println("\n¿Qué le gustaría hacer?");
                        while (continuarInscripcion) {
                            System.out.println("1. Añadir una inscripción");
                            System.out.println("2. Eliminar una inscripción");
                            System.out.println("3. Mostrar las inscripciones");
                            System.out.println("0. Volver al menú principal");
                            int opcionInscripcion = scanner.nextInt();
                            scanner.nextLine();
                            switch (opcionInscripcion) {
                                case 1:
                                    Date fechaActual = new Date();
                                    Datos.crearInscripcion(Datos.listaSocios, Datos.listaExcursiones, fechaActual);
                                    System.out.println("¿Quieres realizar alguna otra gestión de las inscripciones?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver1 = scanner.nextInt();
                                    if (posibildadVolver1 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de inscripciones le gustaría hacer?");
                                    } else {
                                        continuarInscripcion = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 2:
                                    Datos.eliminarInscripcion(Datos.listaExcursiones, Datos.listaInscripciones);
                                    System.out.println("¿Quieres realizar alguna otra gestión de las inscripciones?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver2 = scanner.nextInt();
                                    if (posibildadVolver2 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de inscripciones le gustaría hacer?");
                                    } else {
                                        continuarInscripcion = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 3:
                                    Datos.mostrarInscripcion(Datos.listaInscripciones, Datos.listaSocios, Datos.listaExcursiones);
                                    System.out.println("¿Quieres realizar alguna otra gestión de las inscripciones?");
                                    System.out.println("1. Si");
                                    System.out.println("2. No");
                                    System.out.println("(Cualquier otra opción tomará la opción 'No')");
                                    int posibildadVolver3 = scanner.nextInt();
                                    if (posibildadVolver3 == 1) {
                                        System.out.println("\n¿Qué otra gestíón de inscripciones le gustaría hacer?");
                                    } else {
                                        continuarInscripcion = false;
                                        System.out.println("\n-------------------------------------");
                                        System.out.println("     Volviendo al menú principal");
                                        System.out.println("-------------------------------------\n");
                                    }
                                    break;
                                case 0:
                                    continuarInscripcion = false;
                                    System.out.println("\n-------------------------------------");
                                    System.out.println("     Volviendo al menú principal");
                                    System.out.println("-------------------------------------\n");
                                    break;
                                default:
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Por favor elija otra opción:");
                                    break;
                            }
                        }
                        System.out.println("¿Le gustaría realizar alguna otra gestión?");
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("\n-------------------------------");
                        System.out.println("     Saliendo del programa");
                        System.out.println("-------------------------------");
                        break;
                    default:
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Por favor elija otra opción:");
                        break;
                }
        }
        scanner.close();
    }
}