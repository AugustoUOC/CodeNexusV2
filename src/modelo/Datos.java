package modelo;


import modelo.dao.*;
import utilidad.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;



public class Datos {

    //Contadores

    public static int contadorExcursiones = 0;
    public static int contadorSocios = 0;
    public static int contadorFederaciones = 0;
    public static int contadorInscripciones = 0;

    //Listados
    public static List<Excursion> listaExcursiones = new ArrayList<>();
    public static List<Socio> listaSocios = new ArrayList<>();
    public static List<Inscripcion> listaInscripciones = new ArrayList<>();




    //Métodos para excursiones
    public static void crearExcursion() {
        Excursion excursion = new Excursion();
        excursion.setDescripcion(Teclado.pedirString("Descripción de la Excursión: "));
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
        ExcursionDAO excursionDAO = new ExcursionDAO();
        excursionDAO.agregarExcursion(excursion);
    }


    public static void borrarExcursion() {

        int idExcursionAEliminar = Teclado.pedirInt("Inserta el ID de la Excursion que quieres eliminar");
        ExcursionDAO excursionDAO = new ExcursionDAO();
        excursionDAO.eliminarExcursion(idExcursionAEliminar);
        boolean exito = excursionDAO.eliminarExcursion(idExcursionAEliminar);

        if (exito) {
            System.out.println("La excursión ha sido eliminada exitosamente.");
        } else {
            System.out.println("Hubo un error al eliminar la excursión.");
        }

    }

    public static void mostrarExcursionesPorFechas() throws ParseException, SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de inicio (dd/MM/yyyy): "));
        Date fechaFin = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de fin (dd/MM/yyyy): "));
        if (fechaInicio.after(fechaFin)) {
            System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }
        // Convierte java.util.Date a java.sql.Date
        java.sql.Date fechaInicioSQL = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date fechaFinSQL = new java.sql.Date(fechaFin.getTime());

        ExcursionDAO excursionDAO = new ExcursionDAO();
        ArrayList<Excursion> listaExcursiones = excursionDAO.obtenerListaExcursiones(fechaInicioSQL, fechaFinSQL);
        excursionDAO.mostrarListaExcursiones(listaExcursiones);
    }

     //Métodos para Socios
    public static void borrarSocio() throws SQLException {
        int idSocioABorrar = Teclado.pedirInt("Inserta el ID del Socio que quieres eliminar: ");
        SociosDAO sociosDAO = new SociosDAO();
        boolean exito = sociosDAO.eliminarSocioPorId(idSocioABorrar);

        if (exito) {
            System.out.println("El Socio ha sido borrado correctamente.");
        } else {
            System.out.println("Hubo un error al eliminar el Socio.");
        }

    }
    public static void crearSocio() throws SQLException {
        Socio nuevoSocio = null;
        SociosDAO sociosDAO = new SociosDAO();
        Seguro seguroElegido;

        String nombre = Teclado.pedirString("Ingrese el nombre del nuevo socio: ");
        System.out.println("Seleccione el tipo de socio:");
        System.out.println("1. Socio Estandar\n2. Socio Federado\n3. Socio Infantil");
        int tipoSocio = Teclado.pedirInt("Ingrese la opción deseada: ");
        String nif;

        switch (tipoSocio) {
            case 1:
                nif = Teclado.pedirString("Ingrese el NIF del socio: ");
                int opcionSeguro = Teclado.pedirInt("Seleccione el tipo de seguro:\n1. Básico - $10\n2. Completo - $20\nIngrese la opción deseada: ");
                seguroElegido = sociosDAO.obtenerSeguro(opcionSeguro == 1 ? 1 : 2);  // Asumiendo que los ID de seguros son 1 y 2 en la DB
                nuevoSocio = new Estandar(0, nombre, nif, seguroElegido);
                break;
            case 2:
                nif = Teclado.pedirString("Ingrese el NIF del socio: ");
                String nombreFederacion = Teclado.pedirString("Ingrese el nombre de la federación: ");
                Federacion federacion = sociosDAO.obtenerFederacionPorNombre(nombreFederacion);
                nuevoSocio = new Federado(0, nombre, federacion, nif);
                break;
            case 3:
                int idTutor = Teclado.pedirInt("Elige el ID del tutor: ");
                Socio tutor = sociosDAO.buscarSocioPorId(idTutor);
                if (tutor != null && Teclado.pedirInt("El tutor seleccionado es: " + tutor.getNombre() + " (ID: " + tutor.getIdSocio() + ")\n1. Confirmar tutor\n2. Cancelar\nIngrese la opción deseada: ") == 1) {
                    nuevoSocio = new Infantil(0, nombre, tutor.getIdSocio());
                } else {
                    System.out.println("Creación de socio infantil cancelada o no se encontró un tutor con el ID proporcionado.");
                }
                break;
            default:
                System.out.println("Opción no válida. Por favor, reintente.");
                return;
        }

        if (nuevoSocio != null) {
            sociosDAO.agregarSocio(nuevoSocio);

        } else {
            System.out.println("No se ha podido agregar el Socio");
        }
    }


    public static void modificarSeguro(int idSocio) throws SQLException {
        SociosDAO sociosDAO = new SociosDAO();
        Socio socio = sociosDAO.buscarSocioPorId(idSocio);
        if (socio instanceof Estandar) {
            Estandar estandar = (Estandar) socio;
            Seguro seguroActual = estandar.getSeguroContratado();
            System.out.println(seguroActual);
            if (seguroActual != null) {
                System.out.println("El socio es del tipo Estandar con seguro actual: " + seguroActual.getTipo());
                // Determinamos el nuevo seguro basandonos en el actual que posee el socio indicado
                int nuevoIdSeguro = seguroActual.getIdSeguro() == 1 ? 2 : 1;
                String nuevoNombreSeguro = nuevoIdSeguro == 1 ? "Básico" : "Completo";
                double nuevoPrecio = nuevoIdSeguro == 1 ? 10 : 20;
                if (Teclado.confirmarAccion("¿Desea cambiar al seguro " + nuevoNombreSeguro + " que vale $" + nuevoPrecio + "?")) {
                    estandar.setSeguroContratado(new Seguro(nuevoIdSeguro, nuevoNombreSeguro, nuevoPrecio));
                    sociosDAO.actualizarSeguroDeSocio(estandar);
                    System.out.println("Seguro cambiado al seguro " + nuevoNombreSeguro);
                } else {
                    System.out.println("Cambio de seguro cancelado.");
                }
            } else {
                System.out.println("Este socio Estandar no tiene un seguro configurado actualmente.");
            }
        } else {
            System.out.println("El socio con ID " + idSocio + " no es un socio Estandar.");
        }
    }


    public static void mostrarSocios() throws SQLException {
        SociosDAO sociosDAO = new SociosDAO();
        ArrayList<Socio> listaSocios = sociosDAO.obtenerListaSocios();
        sociosDAO.mostrarListaSocios(listaSocios);
    }

    public static void mostrarSociosPorTipo() {


    }

/*    public static void mostrarSocio() {
        boolean continuarMuestreo = true;
        int contadorMuestreo = 1;
        boolean continuarMuestreoTipo = true;
        int contadorMuestreoTipo = 1;
        while (continuarMuestreo) {
            int opcion = Teclado.pedirInt("¿Qué listado de socios quieres?");
            System.out.println("1. Mostrar todos los socios");
            System.out.println("2. Mostrar socios por tipo");
            System.out.println("0. Volver al menú anterior");

            switch (opcion) {
                case 1:
                    continuarMuestreo = false;
                    int contadorMostrarSocios = 0;
                    System.out.println("\nLista de todos los socios:");
                    System.out.println("--------------------------\n");
                    for (Socio socio : socios) {
                        contadorMostrarSocios = contadorMostrarSocios + 1;
                        System.out.println(contadorMostrarSocios + " - " + socio + "\n");
                    }
                    if (contadorMostrarSocios == 0) {
                        System.out.println("----------------------------------------------");
                        System.out.println("     No hay socios agregados para mostrar");
                        System.out.println("----------------------------------------------\n");
                    }
                    break;
                case 2:
                    System.out.println("¿Qué tipo de socio deseas mostrar?");
                    continuarMuestreo = false;
                    int contadorSociosTipo = 0;
                    while (continuarMuestreoTipo) {
                        System.out.println("1. Estándar");
                        System.out.println("2. Federado");
                        System.out.println("3. Infantil");
                        int tipoSocio = scanner.nextInt();
                        scanner.nextLine(); // Consumir el salto de línea
                        switch (tipoSocio) {
                            case 1:
                                continuarMuestreoTipo = false;
                                String nombreTipo1 = "estándar";
                                System.out.println("\nLista de socios de tipo " + nombreTipo1 + ":");
                                System.out.println("---------------------------------\n");
                                for (Socio socio : socios) {
                                    if (socio instanceof Estandar) {
                                        contadorSociosTipo = contadorSociosTipo + 1;
                                        System.out.println(contadorSociosTipo + " - " + socio + "\n");
                                    }
                                }
                                break;
                            case 2:
                                continuarMuestreoTipo = false;
                                String nombreTipo2 = "federado";
                                System.out.println("\nLista de socios de tipo " + nombreTipo2 + ":");
                                System.out.println("---------------------------------\n");
                                for (Socio socio : socios) {
                                    if (socio instanceof Federado) {
                                        contadorSociosTipo = contadorSociosTipo + 1;
                                        System.out.println(contadorSociosTipo + " - " + socio + "\n");
                                    }
                                }
                                break;
                            case 3:
                                continuarMuestreoTipo = false;
                                String nombreTipo = "infantil";
                                System.out.println("\nLista de socios de tipo " + nombreTipo + ":");
                                System.out.println("---------------------------------\n");
                                for (Socio socio : socios) {
                                    if (socio instanceof Infantil) {
                                        contadorSociosTipo = contadorSociosTipo + 1;
                                        System.out.println(contadorSociosTipo + " - " + socio + "\n");
                                    }
                                }
                                break;
                            default:
                                if (contadorMuestreoTipo < 2) {
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Lleva " + contadorMuestreoTipo + " de 3 intentos.\n");
                                    System.out.println("Introduzca un opción de la lista:");
                                    contadorMuestreoTipo = contadorMuestreoTipo + 1;
                                    break;
                                } else if (contadorMuestreoTipo == 2) {
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Lleva " + contadorMuestreoTipo + " de 3 intentos.");
                                    System.out.println("Al próximo error, se cancelará la función.\n");
                                    System.out.println("Introduzca un opción de la lista:");
                                    contadorMuestreoTipo = contadorMuestreoTipo + 1;
                                    break;
                                } else {
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Ha consumido las tres posibilidades de elección de tipo de socio.\n");
                                    continuarMuestreoTipo = false;
                                    System.out.println("\n---------------------------------------------");
                                    System.out.println("     No se ha podido realizar el listado");
                                    System.out.println("---------------------------------------------\n");
                                    contadorSociosTipo = -1;
                                    break;
                                }
                        }
                    }
                    if (contadorSociosTipo == 0) {
                        System.out.println("----------------------------------------------");
                        System.out.println("     No hay socios agregados para mostrar");
                        System.out.println("----------------------------------------------\n");
                    }
                    break;
                case 0:
                    continuarMuestreo = false;
                    System.out.println("\n------------------------------------------------");
                    System.out.println("     Volviendo al menú de gestión de socios");
                    System.out.println("------------------------------------------------\n");
                default:
                    if (contadorMuestreo < 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorMuestreo + " de 3 intentos.\n");
                        System.out.println("Introduzca un opción de la lista:");
                        contadorMuestreo = contadorMuestreo + 1;
                        break;
                    } else if (contadorMuestreo == 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorMuestreo + " de 3 intentos.");
                        System.out.println("Al próximo error, se cancelará la función.\n");
                        System.out.println("Introduzca un opción de la lista:");
                        contadorMuestreo = contadorMuestreo + 1;
                        break;
                    } else {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Ha consumido las tres posibilidades de elección.");
                        continuarMuestreo = false;
                        System.out.println("\n---------------------------------------------");
                        System.out.println("     No se ha podido realizar el listado");
                        System.out.println("---------------------------------------------\n");
                        break;
                    }

            }

        }
    }*/
    //Funcion para mostrar el Importe total de la Factura segun el Socio y las excursiones que tiene asignadas
    public static void mostrarFacturaTotal(List<Socio> listaSocios, List<Excursion> listaExcursiones, List<Inscripcion> listaInscripciones) {
        Scanner scanner = new Scanner(System.in);
        boolean continuarFactura = true;
        int contadorFallos = 1;
        System.out.println("Ingrese el ID del socio para mostrar su factura:");
        while (continuarFactura) {
            int idSocio = scanner.nextInt();
            if (idSocio <= contadorSocios) {
                continuarFactura = false;
                Socio socioFactura = obtenerSocioPorId(idSocio, listaSocios);
                System.out.println("Id del Socio: " + socioFactura.getIdSocio());
                System.out.println("Nombre del Socio: " + socioFactura.getNombre());
                System.out.println("\nFactura mensual del socio numero: " + socioFactura.getIdSocio());
                mostrarFactura(socioFactura);
                System.out.println(mostrarFactura(socioFactura));
                break;
            } else {
                if (contadorFallos < 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorFallos + " de 3 intentos.\n");
                    System.out.println("Introduzca un socio de la lista.");
                    contadorFallos = contadorFallos + 1;
                } else if (contadorFallos == 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorFallos + " de 3 intentos.");
                    System.out.println("Al próximo error, no se mostrará la factura mensual.\n");
                    System.out.println("Introduzca un socio de la lista.");
                    contadorFallos = contadorFallos + 1;
                } else {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Ha consumido las tres posibilidades de mostrar la factura mensual.");
                    System.out.println("Se ha anulado la muestra de la factura.\n");
                    continuarFactura = false;
                }
            }
        }
    }
    public static double mostrarFactura (Socio socio){
        ArrayList<Inscripcion> inscripciones = new ArrayList<>();
        double coste = 0;
        double costeExcursiones = 0;
        for (Inscripcion inscripcion : listaInscripciones) {
            if (inscripcion.getIdSocio() == socio.getIdSocio()) {
                inscripciones.add(inscripcion);
            }
        }

        for (Inscripcion inscripcion : inscripciones) {
            for (Excursion excursion : listaExcursiones) {
                if (inscripcion.getIdExcursion() == excursion.getIdExcursion()) {
                    costeExcursiones += calcularCosteExcursion(socio, excursion);
                }
            }
        }
        coste = calcularCuota(socio) + costeExcursiones;
        return coste;

    }
    // Funcion para la logica de calcular la cuota + el coste de las inscripciones segun el Socio
    public static double calcularCosteExcursion(Socio socio, Excursion excursion) {
        double precio = 0;
        if (socio instanceof Estandar) {
            precio = excursion.getPrecioInscripcion() + ((Estandar) socio).getSeguroContratado().getPrecio();
        } else if (socio instanceof Federado) {
            double precioTemporal = excursion.getPrecioInscripcion();
            precio = precioTemporal * 0.9;
        } else if (socio instanceof Infantil) {
            precio =  excursion.getPrecioInscripcion();
        }
        return precio;
    }

    // Funcion para Calcular la cuenta segun el tipo de Socio que sea
    public static double calcularCuota(Socio socio) {
        double cuotaBase = 10.0; // Cuota base
        if (socio instanceof Estandar) {
            // La cuota para Estandar es la cuotaBase sin cambios
        } else if (socio instanceof Federado) {
            // Federado tiene un descuento en la cuota
            cuotaBase *= 0.95;
        } else if (socio instanceof Infantil) {
            // Infantil tiene un 50% de descuento
            cuotaBase *= 0.5;
        }
        return cuotaBase;

    }

    //Métodos para inscripciones
    public static void crearInscripcion(List<Socio> listaSocios, List<Excursion> listaExcursiones, Date fechaInscripcion) {
        Scanner scanner = new Scanner(System.in);
        boolean continuarSocio = true;
        System.out.print("¿Cuál es el socio que desea realizar la inscripción?\n");
        int contadorSociosInscripcion = 0;
        int contadorVolver = 0;
        int contadorFalloSocio = 1;
        Socio socioElegido = null;
        while (continuarSocio) {
            contadorSociosInscripcion = 1;
            for (Socio socio : listaSocios) {
                System.out.println(contadorSociosInscripcion +". ID: " + socio.getIdSocio() + " - Nombre: " + socio.getNombre());
                contadorSociosInscripcion = contadorSociosInscripcion + 1;
            }
            if (contadorSociosInscripcion == 1 && contadorFalloSocio == 1) {
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("     No hay ningún socio agregado que pueda realizar la inscripción");
                System.out.println("------------------------------------------------------------------------\n");
                System.out.print("¿Quieres agregar un nuevo usuario o volver al menu anterior?\n");
                System.out.print("1. Agregar nuevo usuario\n");
                System.out.print("2. Volver al menu anterior\n");
                contadorVolver = contadorSociosInscripcion + 1;
            } else {
                contadorVolver = contadorSociosInscripcion + 1;
                System.out.print(contadorSociosInscripcion + ". Agregar nuevo usuario\n");
                System.out.print(contadorVolver +  ". Volver al menu anterior\n");
            }
            int numeroSocioElegido = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (numeroSocioElegido == contadorSociosInscripcion) {
                continuarSocio = false;
                // Agregar un nuevo socio
                //Datos.crearSocio();
                socioElegido = listaSocios.get(listaSocios.size() - 1);
                numeroSocioElegido = socioElegido.getIdSocio(); // Obtener el ID del nuevo socio
                break;
            } else if (numeroSocioElegido < contadorSociosInscripcion) {
                continuarSocio = false;
                // Verificar si el número de socio elegido es válido
                contadorSociosInscripcion = 0;
                for (Socio socio : listaSocios) {
                    contadorSociosInscripcion = contadorSociosInscripcion + 1;
                    if (numeroSocioElegido == contadorSociosInscripcion){
                        numeroSocioElegido = socio.getIdSocio();
                        socioElegido = socio;
                        break;
                    }
                }
                break;
            } else if (numeroSocioElegido == contadorVolver) {
                continuarSocio= false;
                System.out.println("\n-------------------------------------------------------");
                System.out.println("     Volviendo al menú de gestión de inscripciones");
                System.out.println("-------------------------------------------------------\n");
                return;
            } else {
                if (contadorFalloSocio < 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorFalloSocio + " de 3 intentos.\n");
                    if (contadorSociosInscripcion == 1) {
                        System.out.print("Quieres agregar un nuevo socio o volver al menu anterior?\n");
                    } else {
                        System.out.println("Elija un socio de la lista o agrega un nuevo socio:");
                    }
                    contadorFalloSocio = contadorFalloSocio + 1;
                } else if (contadorFalloSocio == 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorFalloSocio + " de 3 intentos.");
                    System.out.println("Al próximo error, se le anulará la posibilidad de elección de socio.\n");
                    if (contadorSociosInscripcion == 1) {
                        System.out.print("Quieres agregar un nuevo socio o volver al menu anterior?\n");
                    } else {
                        System.out.println("Elija un socio de la lista o agrega un nuevo socio:");
                    }
                    contadorFalloSocio = contadorFalloSocio + 1;
                } else {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Ha consumido las tres posibilidades de elección de socio.\nSe ha cancelado la inscripción.\n");
                    continuarSocio= false;
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("     Volviendo al menú de gestión de inscripciones");
                    System.out.println("-------------------------------------------------------\n");
                    return;
                }
            }
        }
            // Mostrar los detalles del socio elegido y verificar si es correcto
        System.out.println("\n"  + socioElegido);
            System.out.println("\nEstos son los datos del socio que quiere hacer la inscripción:");
            System.out.println(" - Número de socio: " + socioElegido.getIdSocio() + ".");
            System.out.println(" - Nombre: " + socioElegido.getNombre() + ".\n");
            boolean continuarConfirmacion = true;
            System.out.println("¿Es este el socio que desea inscribirse?");
            int contadorConfirmacion = 1;
            while (continuarConfirmacion) {
                System.out.println("1. Si");
                System.out.println("2. No");
                int opcionConfirmacion = scanner.nextInt();
                scanner.nextLine();
                // Asignar el nuevo seguro al socio
                switch (opcionConfirmacion) {
                    case 1:

                        continuarConfirmacion = false;
                        break;
                    case 2:
                        System.out.println("Se ha anulado la inscripción del socio llamado " + socioElegido.getNombre() + " con número de socio " + socioElegido.getIdSocio() + ".\n");
                        continuarConfirmacion = false;
                        System.out.println("\n-------------------------------------------------------");
                        System.out.println("     Volviendo al menú de gestión de inscripciones");
                        System.out.println("-------------------------------------------------------\n");
                        return;
                    default:
                        if (contadorConfirmacion < 2) {
                            System.out.println("\n----------------------------------");
                            System.out.println("     Esta opción no es válida");
                            System.out.println("----------------------------------\n");
                            System.out.println("Lleva " + contadorConfirmacion + " de 3 intentos.\n");
                            System.out.println("¿Es este el socio que desea inscribirse?");
                            contadorConfirmacion = contadorConfirmacion + 1;
                        } else if (contadorConfirmacion == 2) {
                            System.out.println("\n----------------------------------");
                            System.out.println("     Esta opción no es válida");
                            System.out.println("----------------------------------\n");
                            System.out.println("Lleva " + contadorConfirmacion + " de 3 intentos.");
                            System.out.println("Al próximo error, se le anulará la inscripción.\n");
                            System.out.println("¿Es este el socio que desea inscribirse?");
                            contadorConfirmacion = contadorConfirmacion + 1;
                        } else {
                            System.out.println("\n----------------------------------");
                            System.out.println("     Esta opción no es válida");
                            System.out.println("----------------------------------\n");
                            System.out.println("Ha consumido las tres posibilidades para el cambio de seguro.");
                            System.out.println("Se ha anulado la inscripción del socio llamado " + socioElegido.getNombre() + " con número de socio " + socioElegido.getIdSocio() + ".\n");
                            continuarConfirmacion = false;
                            System.out.println("\n-------------------------------------------------------");
                            System.out.println("     Volviendo al menú de gestión de inscripciones");
                            System.out.println("-------------------------------------------------------\n");
                            return;
                        }
                }
            }

            // Mostrar un listado de excursiones disponibles con sus IDs y nombres
            System.out.println("¿A qué excursión quiere el socio que se llama " + socioElegido.getNombre() + " inscribirse?");
            boolean continuarExcursion = true;
            int contadorExcursion;
            int contadorVolverExcursion;
            int contadorFalloExcursion = 1;
            Excursion excursionElegida = null;
            while(continuarExcursion) {
                contadorExcursion = 1;
                for (Excursion excursion : listaExcursiones) {
                    System.out.println(contadorExcursion + ". ID: " + excursion.getIdExcursion() + " - Nombre: " + excursion.getDescripcion());
                    contadorExcursion = contadorExcursion + 1;
                }

                if (contadorExcursion == 1 && contadorFalloExcursion == 1) {
                    System.out.println("\n----------------------------------------------------------------------------");
                    System.out.println("     No hay ninguna excursión agregada a la que realizar la inscripción");
                    System.out.println("----------------------------------------------------------------------------\n");
                    System.out.print("Quieres agregar un nueva excursión o volver al menu anterior?\n");
                    System.out.print("1. Agregar nueva excursión\n");
                    System.out.print("2. Volver al menu anterior\n");
                    contadorVolverExcursion = contadorExcursion + 1;
                } else {
                    contadorVolverExcursion = contadorExcursion + 1;
                    System.out.print(contadorExcursion + ". Agregar nueva excursión\n");
                    System.out.print(contadorVolverExcursion + ". Volver al menu anterior\n");
                }
                int numeroExcursionElegida = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                if (numeroExcursionElegida == contadorExcursion) {
                    continuarExcursion = false;
                    // Agregar una nueva excursión
                    //Datos.crearExcursion();
                    excursionElegida = listaExcursiones.get(listaExcursiones.size() - 1);
                    numeroExcursionElegida = excursionElegida.getIdExcursion(); // Obtener la ID de la nueva excursión
                    break;
                } else if (numeroExcursionElegida < contadorExcursion) {
                    continuarExcursion = false;
                    // Verificar si el número de socio elegido es válido

                    for (Excursion excursion : listaExcursiones) {
                        if (excursion.getIdExcursion() == numeroExcursionElegida) {
                            excursionElegida = excursion;
                            break;
                        }
                    }
                    break;
                } else if (numeroExcursionElegida == contadorVolverExcursion) {
                    continuarExcursion= false;
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("     Volviendo al menú de gestión de inscripciones");
                    System.out.println("-------------------------------------------------------\n");
                    return;
                } else {
                    if (contadorFalloExcursion < 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorFalloExcursion + " de 3 intentos.\n");
                        if (contadorExcursion == 1) {
                            System.out.print("¿Quieres agregar una nueva excursión o volver al menu anterior?\n");
                        } else {
                            System.out.println("Elija una excursión de la lista o agrega una nueva:");
                        }
                        contadorFalloExcursion = contadorFalloExcursion + 1;
                    } else if (contadorFalloExcursion== 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorFalloExcursion + " de 3 intentos.");
                        System.out.println("Al próximo error, se anulará la eleción de excursión.\n");
                        if (contadorExcursion == 1) {
                            System.out.print("¿Quieres agregar una nueva excursión o volver al menu anterior?\n");
                        } else {
                            System.out.println("Elija una excursión de la lista o agrega una nueva:");
                        }
                        contadorFalloExcursion = contadorFalloExcursion + 1;
                    } else {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Ha consumido las tres posibilidades de elección de excursión.\nSe ha camcelado la inscripción.\n");
                        continuarSocio= false;
                        System.out.println("\n-------------------------------------------------------");
                        System.out.println("     Volviendo al menú de gestión de inscripciones");
                        System.out.println("-------------------------------------------------------\n");
                        return;
                    }
                }
            }

            Date fechaActual = new Date();
            // Crear la inscripción con el ID de la excursión elegida
            Inscripcion inscripcion = new Inscripcion(++contadorInscripciones, socioElegido.getIdSocio(), excursionElegida.getIdExcursion(), fechaActual);
            listaInscripciones.add(inscripcion);
        System.out.println("\n--------------------------------------------");
        System.out.println("     Inscripción agregada correctamente");
        System.out.println("--------------------------------------------\n\n" + inscripcion + "\n");
        }
    public static void eliminarInscripcion(List<Excursion> listaExcursiones, List<Inscripcion> listaInscripciones) {
        Scanner scanner = new Scanner(System.in);
        Date fechaActual = new Date(); //fecha actual
        boolean continuarEliminacion = true;
        int contadorFallos = 1;
        System.out.println("Elige el número de la Inscripcion que quieres eliminar:");
        while (continuarEliminacion) {
            int contadorExcursionesBorrables = 1;
            for(Inscripcion inscripcion : listaInscripciones) {
                int idExcursion = inscripcion.getIdExcursion();
                Excursion excursion = obtenerExcursionPorId(idExcursion, listaExcursiones);
                Calendar calExcursion = Calendar.getInstance();
                calExcursion.setTime(fechaActual);
                calExcursion.add(Calendar.DAY_OF_YEAR, 1);
                Date fechaSiguienteExcursion = calExcursion.getTime();
                if (fechaSiguienteExcursion.before(excursion.getFechaExcursion() )) {
                    System.out.println("\n" + contadorExcursionesBorrables + ". " + inscripcion);
                    contadorExcursionesBorrables = contadorExcursionesBorrables + 1;
                }
            }
            if (contadorExcursionesBorrables == 1) {
                System.out.println("\n-----------------------------------------------------------");
                System.out.println("     No hay inscripciones que puedan ser eliminadas.");
                System.out.println("-----------------------------------------------------------\n" );
                return;
            }
            int idSocio = scanner.nextInt();
            scanner.nextLine();
            if (idSocio <= contadorExcursionesBorrables ) {
                continuarEliminacion = false;
                Inscripcion inscripcionParaBorrar = obtenerInscripcionPorId(idSocio, listaInscripciones);
                listaInscripciones.remove(inscripcionParaBorrar);
                System.out.println("\n----------------------------------------------------------");
                System.out.println("     Número de inscripcion " + inscripcionParaBorrar.getIdInscripcion() + " eliminado correctamente");
                System.out.println("----------------------------------------------------------\n\n" );
                break;
            } else {
                if (contadorFallos < 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorFallos + " de 3 intentos.\n");
                    System.out.println("Introduzca un socio de la lista.");
                    contadorFallos = contadorFallos + 1;
                } else if (contadorFallos == 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorFallos + " de 3 intentos.");
                    System.out.println("Al próximo error, no eliminará la inscripción.\n");
                    System.out.println("Introduzca un socio de la lista.");
                    contadorFallos = contadorFallos + 1;
                } else {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Ha consumido las tres posibilidades de elegir socio.");
                    System.out.println("Se ha anulado la posibilidad de eliminar la inscripción.\n");
                    continuarEliminacion = false;
                }
            }
        }
    }

    public static void mostrarInscripcion(List<Inscripcion> listaInscripciones, List<Socio> listaSocios,List<Excursion> listaExcursiones) {
        Scanner scanner = new Scanner(System.in);
        boolean continuarMuestreo = true;
        int contadorMuestreo = 1;
        boolean continuarMuestreoTipo = true;
        int contadorMuestreoTipo = 1;
        System.out.println("\nSeleccione una opción:");
        while (continuarMuestreo) {
            System.out.println("1. No aplicar filtros");
            System.out.println("2. Aplicar filtro por socio");
            System.out.println("3. Aplicar filtro por fecha");
            System.out.println("4. Aplicar ambos filtros");
            System.out.println("0. Volver al menú anterior");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    continuarMuestreo = false;
                    mostrarTodasLasInscripciones(listaInscripciones, listaSocios, listaExcursiones);
                    break;
                case 2:
                    continuarMuestreo = false;
                    mostrarInscripcionPorSocio(listaInscripciones, listaSocios, listaExcursiones);
                    break;
                case 3:
                    continuarMuestreo = false;
                   // mostrarInscripcionPorFecha(listaInscripciones, listaSocios, listaExcursiones);
                    break;
                case 4:
                    continuarMuestreo = false;
                    //mostrarInscripcionPorSocioYFecha(listaInscripciones, listaSocios, listaExcursiones);
                    break;
                case 0:
                    continuarMuestreo = false;
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("     Volviendo al menú de gestión de inscripciones");
                    System.out.println("-------------------------------------------------------\n");
                    break;
                default:
                    if (contadorMuestreo < 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorMuestreo + " de 3 intentos.\n");
                        System.out.println("Introduzca un opción de la lista:");
                        contadorMuestreo = contadorMuestreo + 1;
                        break;
                    } else if (contadorMuestreo == 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorMuestreo + " de 3 intentos.");
                        System.out.println("Al próximo error, se cancelará la función.\n");
                        System.out.println("Introduzca un opción de la lista:");
                        contadorMuestreo = contadorMuestreo + 1;
                        break;
                    } else {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Ha consumido las tres posibilidades de elección.");
                        continuarMuestreo = false;
                        System.out.println("\n---------------------------------------------");
                        System.out.println("     No se ha podido realizar el listado");
                        System.out.println("---------------------------------------------\n");
                        break;
                    }
            }
        }
    }

    private static void mostrarTodasLasInscripciones(List<Inscripcion> listaInscripciones, List<Socio> listaSocios, List<Excursion> listaExcursiones) {
        if (listaInscripciones.isEmpty()) {
            System.out.println("\n-----------------------------------------------------");
            System.out.println("     No hay inscripciones agregadas para mostrar");
            System.out.println("-----------------------------------------------------\n");
            return;
        } else {
            for (Inscripcion inscripcion : listaInscripciones) {

                System.out.println("\nNúmero de socio: " + inscripcion.getIdSocio());

                // Buscar el nombre del socio correspondiente
                Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocios);
                if (socio != null) {
                    System.out.println("Nombre del socio: " + socio.getNombre());
                } else {
                    System.out.println("No se ha encontrado ningún socio asociado a esa ID");
                    return;
                }
                // Buscar la excursión correspondiente a la inscripción
                Excursion excursion = obtenerExcursionPorId(inscripcion.getIdExcursion(), listaExcursiones);
                if (excursion != null) {
                    // Mostrar fecha de la excursión y descripción
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                    System.out.println("Fecha de la excursión: " + fechaTransformada);
                    System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                    ;
                    // Calcular e imprimir el importe con los cargos o descuentos aplicados
                    double importeTotal = calcularImporteTotal(excursion, socio);
                    System.out.println("Importe total: " + importeTotal + " euros.");
                } else {
                    System.out.println("No se encontró información de la excursión para esta inscripción.");
                }

                System.out.println(); // Separador entre cada inscripción
            }
        }
    }

    //Mostrarporsocio
    public static void mostrarInscripcionPorSocio(List<Inscripcion> listaInscripciones, List<Socio> listaSocios, List<Excursion> listaExcursiones) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del socio:");
        int idSocioInscripciones = scanner.nextInt();
        scanner.nextLine();

        if (listaInscripciones.isEmpty()) {
            System.out.println("\n----------------------------------------------------");
            System.out.println("     No hay inscripciones agregadas al socio " + idSocioInscripciones);
            System.out.println("----------------------------------------------------\n");
            return;
        } else {
        for (Inscripcion inscripcion : listaInscripciones) {
            if (idSocioInscripciones == inscripcion.getIdSocio()) {
                System.out.println("\nNúmero de socio: " + inscripcion.getIdSocio());

                // Buscar el nombre del socio correspondiente
                Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocios);
                if (socio != null) {
                    System.out.println("Nombre del socio: " + socio.getNombre());
                }

                // Buscar la excursión correspondiente a la inscripción
                Excursion excursion = obtenerExcursionPorId(inscripcion.getIdExcursion(), listaExcursiones);
                if (excursion != null) {
                    // Mostrar fecha de la excursión y descripción
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                    System.out.println("Fecha de la excursión: " + fechaTransformada);
                    System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                    ;
                    // Calcular e imprimir el importe con los cargos o descuentos aplicados
                    double importeTotal = calcularImporteTotal(excursion, socio);
                    System.out.println("Importe total: " + importeTotal + " euros.");
                } else {
                    System.out.println("No se encontró información de la excursión para esta inscripción.");
                }

                System.out.println(); // Separador entre cada inscripción
            }
        }
        }
    }
    /*Mostrarporfechas
    public static void mostrarInscripcionPorFecha(List<Inscripcion> listaInscripciones, List<Socio> listaSocios, List<Excursion> listaExcursiones) {

        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Solicitamos al usuario las fechas de inicio y fin para aplicar el filtro
            System.out.println("Ingrese la fecha de inicio (dd/MM/yyyy): ");
            Date fechaInicio = leerFecha(scanner, dateFormat);

            System.out.println("Ingrese la fecha de fin (dd/MM/yyyy): ");
            Date fechaFin = leerFecha(scanner, dateFormat);

            if (fechaInicio.after(fechaFin)) {
                System.out.println("\n--------------------------------------------------------------------");
                System.out.println("     La fecha de inicio no puede ser posterior a la fecha final");
                System.out.println("--------------------------------------------------------------------\n");
                return;
            }

            System.out.println("\n----------------------------------------------------");
            System.out.println("     Inscripciones entre " + dateFormat.format(fechaInicio) + " y " + dateFormat.format(fechaFin) + ":");
            System.out.println("----------------------------------------------------\n");

            boolean inscripcionesEncontradas = false;
            for (Inscripcion inscripcion : listaInscripciones) {
                Date fechaInscripcion = inscripcion.getFechaInscripcion();
                if ((fechaInscripcion.after(fechaInicio) && fechaInscripcion.before(fechaFin)) || (fechaInscripcion.equals(fechaInicio) && fechaInscripcion.before(fechaFin)) || (fechaInscripcion.after(fechaInicio) && fechaInscripcion.equals(fechaFin)))  {
                    System.out.println("Número de socio: " + inscripcion.getIdSocio());

                    // Buscar el nombre del socio correspondiente
                    Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocios);
                    if (socio != null) {
                        System.out.println("Nombre del socio: " + socio.getNombre());
                    } else {
                        System.out.println("Nombre del socio: No encontrado");
                    }

                    // Buscar la excursión correspondiente a la inscripción
                    Excursion excursion = obtenerExcursionPorId(inscripcion.getIdExcursion(), listaExcursiones);
                    if (excursion != null) {
                        // Mostrar fecha de la excursión y descripción
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                        System.out.println("Fecha de la excursión: " + fechaTransformada);
                        System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                        ;
                        // Calcular e imprimir el importe con los cargos o descuentos aplicados
                        double importeTotal = calcularImporteTotal(excursion, socio);
                        System.out.println("Importe total: " + importeTotal + " euros.");
                    } else {
                        System.out.println("No se encontró información de la excursión para esta inscripción.");
                    }
                    inscripcionesEncontradas = true;
                }
            }
            if (!inscripcionesEncontradas) {

                System.out.println(" No hay inscripciones realizadas entre el día " + dateFormat.format(fechaInicio) + " y el día " + dateFormat.format(fechaFin) + "\n");

                return;
            }

    }

    public static void mostrarInscripcionPorSocioYFecha(List<Inscripcion> listaInscripciones, List<Socio> listaSocios, List<Excursion> listaExcursiones){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del socio:");
        int idSocioInscripciones = scanner.nextInt();
        scanner.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Solicitamos al usuario las fechas de inicio y fin para aplicar el filtro
        System.out.println("Ingrese la fecha de inicio (dd/MM/yyyy): ");
        Date fechaInicio = leerFecha(scanner, dateFormat);

        System.out.println("Ingrese la fecha de fin (dd/MM/yyyy): ");
        Date fechaFin = leerFecha(scanner, dateFormat);

        if (fechaInicio.after(fechaFin)) {
            System.out.println("\n--------------------------------------------------------------------");
            System.out.println("     La fecha de inicio no puede ser posterior a la fecha final");
            System.out.println("--------------------------------------------------------------------\n");
            return;
        }

        System.out.println("\n----------------------------------------------------------------------------------------------");
        System.out.println("     Inscripciones realizadas por el socio " + idSocioInscripciones + " entre el día " + dateFormat.format(fechaInicio) + " y el día " + dateFormat.format(fechaFin));
        System.out.println("----------------------------------------------------------------------------------------------\n");

        boolean inscripcionesEncontradas = false;
        for (Inscripcion inscripcion : listaInscripciones) {
            Date fechaInscripcion = inscripcion.getFechaInscripcion();
            if ((fechaInscripcion.after(fechaInicio) && fechaInscripcion.before(fechaFin)) || (fechaInscripcion.equals(fechaInicio) && fechaInscripcion.before(fechaFin)) || (fechaInscripcion.after(fechaInicio) && fechaInscripcion.equals(fechaFin)))  {
                if (idSocioInscripciones == inscripcion.getIdSocio()) {
                    System.out.println("Número de socio: " + inscripcion.getIdSocio());

                    // Buscar el nombre del socio correspondiente
                    Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocios);
                    if (socio != null) {
                        System.out.println("\nNombre del socio: " + socio.getNombre());
                    } else {
                        System.out.println("Nombre del socio: No encontrado");
                    }

                    // Buscar la excursión correspondiente a la inscripción
                    Excursion excursion = obtenerExcursionPorId(inscripcion.getIdExcursion(), listaExcursiones);
                    if (excursion != null) {
                        // Mostrar fecha de la excursión y descripción
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                        System.out.println("Fecha de la excursión: " + fechaTransformada);
                        System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                        ;
                        // Calcular e imprimir el importe con los cargos o descuentos aplicados
                        double importeTotal = calcularImporteTotal(excursion, socio);
                        System.out.println("Importe total: " + importeTotal + " euros.");
                    } else {
                        System.out.println("No se encontró información de la excursión para esta inscripción.");
                    }
                    inscripcionesEncontradas = true;
                }
            }
        }
        if (!inscripcionesEncontradas) {

            System.out.println(" No hay inscripciones realizadas para el socio " + idSocioInscripciones + " entre el día " + dateFormat.format(fechaInicio) + " y el día " + dateFormat.format(fechaFin) + "\n");

        }

    }*/
    //Subfunciones
    private static Excursion obtenerExcursionPorId(int idExcursion, List<Excursion> listaExcursiones) {
        for (Excursion excursion : listaExcursiones) {
            if (excursion.getIdExcursion() == idExcursion) {
                return excursion;
            }
        }
        return null; // Retorna null si no se encuentra la excursión con el ID dado
    }
    private static Inscripcion obtenerInscripcionPorId(int idInscripcion, List<Inscripcion> listaInscripciones) {
        for (Inscripcion inscripcion : listaInscripciones) {
            if (inscripcion.getIdInscripcion() == idInscripcion) {
                return inscripcion;
            }
        }
        return null; // Retorna null si no se encuentra la excursión con el ID dado
    }
    private static Socio obtenerSocioPorId(int idSocio, List<Socio> listaSocios) {
        for (Socio socio : listaSocios) {
            if (socio.getIdSocio() == idSocio) {
                return socio;
            }
        }
        return null; // Retorna null si no se encuentra el socio con el ID dado
    }

    public static double calcularImporteTotal(Excursion excursion, Socio socio) {
        double precioInscripcion = excursion.getPrecioInscripcion();

        // Aplicar descuento según el tipo de socio
        switch (socio.getTipoSocio()) {
            case "Estandar":
                // Para socios estándar, se suma el precio de la inscripción con el precio del seguro
                Estandar estandar = (Estandar) socio;
                precioInscripcion += estandar.getSeguroContratado().getPrecio();
                break;
            case "Infantil":
                // Para socios infantiles, no hay descuento ni otros cargos
                break;
            case "Federado":
                // Para socios federados, se aplica un descuento del 10%
                precioInscripcion *= 0.9;
                break;
            default:
                System.out.println("Tipo de socio no reconocido.");
        }

        return precioInscripcion;
    }
}
