package modelo;

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la descripción de la excursión:");
        String descripcion = scanner.nextLine();

        System.out.println("Ingrese la fecha de la excursión (formato: dd/MM/yyyy):");
        Date fechaExcursion = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while (fechaExcursion == null) {
            try {
                fechaExcursion = dateFormat.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Formato de fecha incorrecto. Intente nuevamente.");
            }
        }

        System.out.println("Ingrese la duración en días de la excursión:");
        int duracionDias = scanner.nextInt();

        System.out.println("Ingrese el precio de inscripción:");
        double precioInscripcion = scanner.nextDouble();

        Excursion excursion = new Excursion(++contadorExcursiones, descripcion, fechaExcursion, duracionDias, precioInscripcion);
        listaExcursiones.add(excursion);
        System.out.println("\n------------------------------------------");
        System.out.println("     Excursión agregada correctamente");
        System.out.println("------------------------------------------\n\n" + excursion + "\n");
    }
    public static void mostrarExcursionesPorFechas(List<Excursion> listaExcursiones) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Ingrese la fecha de inicio (dd/MM/yyyy): ");
        Date fechaInicio = leerFecha(scanner, dateFormat);

        System.out.println("Ingrese la fecha de fin (dd/MM/yyyy): ");
        Date fechaFin = leerFecha(scanner, dateFormat);

        if (fechaInicio.after(fechaFin)) {
            System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }
        System.out.println("\n----------------------------------------------------");
        System.out.println("     Excursiones entre " + dateFormat.format(fechaInicio) + " y " + dateFormat.format(fechaFin) + ":");
        System.out.println("----------------------------------------------------\n");
        boolean excursionesEncontradas = false;
        for (Excursion excursion : listaExcursiones) {
            Date fechaExcursion = excursion.getFechaExcursion();
            if ((fechaExcursion.after(fechaInicio) && fechaExcursion.before(fechaFin)) || (fechaExcursion.equals(fechaInicio) && fechaExcursion.before(fechaFin)) || (fechaExcursion.after(fechaInicio) && fechaExcursion.equals(fechaFin)))   {
                System.out.println(excursion);
                excursionesEncontradas = true;
            }

        }
        if (!excursionesEncontradas) {
            System.out.println("No se encontraron excursiones en el rango de fechas especificado.\n");
        }
    }

   //Subfunción de mostrarExcursionesPorFechas
    private static Date leerFecha(Scanner scanner, SimpleDateFormat dateFormat) {
        Date fecha = null;
        boolean fechaValida = false;
        while (!fechaValida) {
            try {
                String fechaStr = scanner.nextLine();
                fecha = dateFormat.parse(fechaStr);
                fechaValida = true;
            } catch (Exception e) {
                System.out.println("Formato de fecha inválido. Ingrese la fecha en formato dd/MM/yyyy: ");
            }
        }
        return fecha;
    }


    //Métodos para Socios
    public static void borrarSocio(List<Socio> socios, List<Inscripcion> listaInscripciones) {
        if (socios.isEmpty()) {
            System.out.println("No hay ningún socio agregado para eliminar.\n");
            return;
        }
        boolean continuarBorrado = true;
        Socio socioAEliminar = null;
        // Mostrar lista de socios con números asociados
        System.out.println("La lista de socios que hay agregados es:");
        int contadorBorrado = 1;
        while (continuarBorrado) {
            for (int i = 0; i < socios.size(); i++) {
                System.out.println((i + 1) + ". " + socios.get(i).getNombre());
            }
            // Pedir al usuario que elija un número de socio para eliminar
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el número de socio que desea eliminar: ");
            int opcion = scanner.nextInt();

            if (opcion < 1 || opcion > socios.size()) {
                if (contadorBorrado < 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorBorrado + " de 3 intentos.\n");
                    System.out.println("Introduzca un número de socio de la lista.");
                    contadorBorrado = contadorBorrado + 1;
                } else if (contadorBorrado == 2) {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Lleva " + contadorBorrado + " de 3 intentos.");
                    System.out.println("Al próximo error, se detendrá la posibilidad de borrar un socio.\n");
                    System.out.println("Introduzca un número de socio de la lista.");
                    contadorBorrado = contadorBorrado + 1;
                } else {
                    System.out.println("\n----------------------------------");
                    System.out.println("     Esta opción no es válida");
                    System.out.println("----------------------------------\n");
                    System.out.println("Ha consumido las tres posibilidades de elección de socio.");
                    System.out.println("Se ha detenido la posibilidad de eliminar un socio.\n");
                    continuarBorrado = false;
                    return;
                }
            } else {
                // Eliminar el socio seleccionado
                socioAEliminar = socios.get(opcion - 1);
                continuarBorrado = false;
            }
        }
        //Confirmamos si ha hecho alguna excursion
        boolean confirmacion = true;
        for (Inscripcion inscripcion : listaInscripciones) {
            if (inscripcion.getIdSocio() == socioAEliminar.getIdSocio()) {
                confirmacion = false; // El socio participó en esta excursión
                break;
            }
        }

        if (confirmacion) {
            socios.remove(socioAEliminar);
            System.out.println("\n----------------------------------------------------");
            System.out.println("     Número de socio " + socioAEliminar.getIdSocio() + " eliminado correctamente");
            System.out.println("----------------------------------------------------\n\n" );
        } else {
            System.out.println("\nEste socio ha participado en alguna excursión, por eso no puede ser borrado.\n");
        }
    }
    public static void crearSocio() {

        Socio nuevoSocio = null; // Inicialización por defecto
        Scanner scanner = new Scanner(System.in);
        boolean continuarTipo = true;
        System.out.println("Ingrese el nombre del nuevo socio:");
        String nombre = scanner.nextLine();
        System.out.println("Seleccione el tipo de socio:");
        int contadorTipo = 1;
        double precioSeguro = 0;
        var tipoSocio = "1";
        System.out.println("1. Socio Estandar\n2. Socio Federado\n3. Socio Infantil");
        while (continuarTipo) {
            if (contadorTipo <= 3){
                tipoSocio = scanner.nextLine();
            } else {
                tipoSocio = "1";
            }
            switch (tipoSocio.toLowerCase()) {
                case "1":
                    continuarTipo = false;
                    boolean continuarSeguro = true;
                    System.out.println("Ingrese el NIF del socio:");
                    String nifEstandar = scanner.nextLine();
                    System.out.println("Seleccione el tipo de seguro:");
                    boolean seguro = false;
                    int contadorSeguro = 1;
                    while (continuarSeguro) {
                        System.out.println("1. Básico");
                        System.out.println("2. Completo");
                        int opcionSeguro = scanner.nextInt();
                        scanner.nextLine(); // Limpiar el buffer del scanner
                        switch (opcionSeguro) {
                            case 1:
                                continuarSeguro = false;
                                System.out.println("Se ha seleccionado el seguro básico.");
                                precioSeguro = 10;
                                break;
                            case 2:
                                seguro = true;
                                continuarSeguro = false;
                                System.out.println("Se ha seleccionado el seguro completo.");
                                precioSeguro = 20;
                                break;
                            default:
                                if (contadorSeguro < 2) {
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Lleva " + contadorSeguro + " de 3 intentos.\n");
                                    System.out.println("Introduzca un tipo de seguro válido.");
                                    contadorSeguro = contadorSeguro + 1;
                                } else if (contadorSeguro == 2) {
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Lleva " + contadorSeguro + " de 3 intentos.");
                                    System.out.println("Al próximo error, se le asignará el seguro básico por defecto.\n");
                                    System.out.println("Introduzca un tipo de seguro válido.");
                                    contadorSeguro = contadorSeguro + 1;
                                } else {
                                    System.out.println("\n----------------------------------");
                                    System.out.println("     Esta opción no es válida");
                                    System.out.println("----------------------------------\n");
                                    System.out.println("Ha consumido las tres posibilidades de elección de seguro.\n");
                                    System.out.println("Se ha asignado el seguro básico por defecto.");
                                    precioSeguro = 10;
                                    continuarSeguro = false;
                                }
                                break;
                        }
                    }
                    Seguro seguroEstandar = new Seguro(seguro, precioSeguro);
                    nuevoSocio = new Estandar(++contadorSocios, nombre, nifEstandar, seguroEstandar);
                    break;
                case "2":
                    continuarTipo = false;
                    System.out.println("Ingrese el NIF del socio:");
                    String nifFederado = scanner.nextLine();
                    System.out.println("Ingrese el nombre de la federación:");
                    String nombreFederacion = scanner.nextLine();
                    Federacion federacion = new Federacion(++contadorFederaciones, nombreFederacion);
                    nuevoSocio = new Federado(++contadorSocios, nombre, federacion, nifFederado);
                    break;
                case "3":
                    continuarTipo = false;
                    boolean continuarBusqueda = true;
                    System.out.println("Elige el ID del tutor:");
                    String nombreTutor = "";
                    int contadorBusqueda = 1;
                    int idTutor = 0;
                    Socio tutor = null;
                    while (continuarBusqueda) {
                        for (int i = 0; i < listaSocios.size(); i++) {
                            System.out.println((i + 1) + ". " + listaSocios.get(i).getNombre());
                        }

                        idTutor = scanner.nextInt();
                        if (idTutor < 1 || idTutor > listaSocios.size()) {
                            if (contadorBusqueda < 2) {
                                System.out.println("\nNúmero de socio para ser tutor no válido.");
                                System.out.println("Lleva " + contadorBusqueda + " de 3 intentos.\n");
                                System.out.println("Introduzca un número de socio de la lista:");
                                contadorBusqueda = contadorBusqueda + 1;
                            } else if (contadorBusqueda == 2) {
                                System.out.println("\nNúmero de socio para ser tutor no válido.");
                                System.out.println("Lleva " + contadorBusqueda + " de 3 intentos.");
                                System.out.println("Al próximo error, se detendrá la posibilidad de elegir un tutor para el socio infantil.\n");
                                System.out.println("Introduzca un número de socio de la lista:");
                                contadorBusqueda = contadorBusqueda + 1;
                            } else {
                                System.out.println("\nHa consumido las tres posibilidades de elección de socio para tutor.");
                                System.out.println("Se ha detenido la búsqueda de un tutor y por tanto no se ha creado el socio infantil.\n");
                                continuarBusqueda = false;
                            }
                        } else {
                            continuarBusqueda = false;
                            for (Socio s : listaSocios) {
                                if (s.getIdSocio() == idTutor) {
                                    tutor = s;
                                    nombreTutor = s.getNombre();
                                    break;
                                }
                            }
                        }
                    }
                    // Verifica si se encontró al tutor
                    boolean continuarConfirmacion = true;
                    if (tutor != null) {
                        // Pregunta al usuario si el tutor encontrado es el correcto
                        System.out.println("\nEl id: " + tutor.getIdSocio() + ", está asociado al socio " + tutor.getTipoSocio() + " que se llama: " + nombreTutor + ".");
                        System.out.println("\n¿Es este el tutor que desea asociar al socio infantil que se llama " + nombre + "? ");
                        int contadorConfirmacion = 1;
                        while (continuarConfirmacion) {
                            System.out.println("1. Si");
                            System.out.println("2. No");

                            int opcionconfirmacion = scanner.nextInt();
                            scanner.nextLine(); // Limpiar el buffer del scanner
                            switch (opcionconfirmacion) {
                                case 1:
                                    nuevoSocio = new Infantil(++contadorSocios, nombre, idTutor);
                                    continuarConfirmacion = false;
                                    break;
                                case 2:
                                    continuarConfirmacion = false;
                                    System.out.println("El socio llamado " + tutor.getNombre() + " con número de socio " + tutor.getIdSocio() + ", no se ha asociado como tutor al socio infantil.");
                                    System.out.println("\n--------------------------------------");
                                    System.out.println("     El socio no ha sido agregado");
                                    System.out.println("--------------------------------------\n");
                                    System.out.println("\n------------------------------------------------");
                                    System.out.println("     Volviendo al menú de gestión de socios");
                                    System.out.println("------------------------------------------------\n");
                                    break;
                                default:
                                    if (contadorConfirmacion < 2) {
                                        System.out.println("\n----------------------------------");
                                        System.out.println("     Esta opción no es válida");
                                        System.out.println("----------------------------------\n");
                                        System.out.println("Lleva " + contadorConfirmacion + " de 3 intentos.\n");
                                        System.out.println("¿Es este el tutor que desea asociar al socio infantil que se llama " + nombre + "?");
                                        contadorConfirmacion = contadorConfirmacion + 1;
                                    } else if (contadorConfirmacion == 2) {
                                        System.out.println("\n----------------------------------");
                                        System.out.println("     Esta opción no es válida");
                                        System.out.println("----------------------------------\n");;
                                        System.out.println("Lleva " + contadorConfirmacion + " de 3 intentos.");
                                        System.out.println("Al próximo error, se le asignará el socio Estándar por defecto.\n");
                                        System.out.println("¿Es este el tutor que desea asociar al socio infantil que se llama " + nombre + "?");
                                        contadorConfirmacion = contadorConfirmacion + 1;
                                    } else {
                                        System.out.println("\n----------------------------------");
                                        System.out.println("     Esta opción no es válida");
                                        System.out.println("----------------------------------\n");
                                        System.out.println("Ha consumido las tres posibilidades de confirmación.");
                                        System.out.println("No se ha creado el nuevo socio.\n");
                                        continuarConfirmacion = false;
                                    }
                                    break;
                            }
                        }
                    }
                    break;
                default:
                    if (contadorTipo < 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Lleva " + contadorTipo + " de 3 intentos.\n");
                        System.out.println("Introduzca un tipo de socio válido:");
                        contadorTipo = contadorTipo + 1;
                        System.out.println("1. Socio Estandar\n2. Socio Federado\n3. Socio Infantil");
                    } else if (contadorTipo == 2) {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");;
                        System.out.println("Lleva " + contadorTipo + " de 3 intentos.");
                        System.out.println("Al próximo error, se le asignará el socio Estándar por defecto.\n");
                        System.out.println("Introduzca un tipo de socio válido:");
                        contadorTipo = contadorTipo + 1;
                        System.out.println("1. Socio Estandar\n2. Socio Federado\n3. Socio Infantil");
                    } else {
                        System.out.println("\n----------------------------------");
                        System.out.println("     Esta opción no es válida");
                        System.out.println("----------------------------------\n");
                        System.out.println("Ha consumido las tres posibilidades de elección de socio.");
                        System.out.println("Se ha asignado el socio Estándar por defecto.\n");
                        contadorTipo = contadorTipo + 1;
                    }

            }
        }
        if (nuevoSocio != null) {
            listaSocios.add(nuevoSocio);
            System.out.println("\n--------------------------------------");
            System.out.println("     Socio agregado correctamente");
            System.out.println("--------------------------------------\n\n" + nuevoSocio + "\n");
        }
    }
    public static void modificarSeguro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del socio estándar para modificar su seguro:");
        int idSocio = scanner.nextInt();
        scanner.nextLine();

        Socio socio = obtenerSocioPorId(idSocio, listaSocios);
        if (socio == null) {
            System.out.println("No se ha encontrado ningún socio con el ID introducido.\n");
            return;
        }

        // Verificar si el socio es de tipo estándar
        if (socio instanceof Estandar socioEstandar) {
            // Mostrar el seguro actual del socio
            String tipoSeguro = "Básico";
            String tipoComtrario = "Completo";
            double precioBasico = 10;
            double precioCompleto = 20;
            double precioSeguro = socioEstandar.getSeguroContratado().getPrecio();
            double precioContrario = 20;
            if (socioEstandar.getSeguroContratado().tipo) {
                tipoSeguro = "Completo";
                tipoComtrario = "Básico";
                precioContrario = precioBasico;
            } else {
                tipoSeguro = "Básico";
                tipoComtrario = "Completo";
            }
            System.out.println("El id " + idSocio + " pertenece al socio que se llama " + socioEstandar.getNombre() + ".");
            System.out.println("Su seguro actual es: " + tipoSeguro + ".");
            boolean cambiarTipoSeguro = true;
            System.out.println("\n¿Quieres cambiar el seguro de " + tipoSeguro + " a " + tipoComtrario + "?");
            int contadorCambio = 1;
            while (cambiarTipoSeguro) {
                System.out.println("1. Si");
                System.out.println("2. No");
                int opcionSeguro = scanner.nextInt();
                scanner.nextLine();
                // Asignar el nuevo seguro al socio
                switch (opcionSeguro) {
                    case 1:
                        if (!socioEstandar.getSeguroContratado().tipo) {
                            socioEstandar.getSeguroContratado().tipo = !socioEstandar.getSeguroContratado().tipo;
                            socioEstandar.getSeguroContratado().precio = precioContrario;
                            System.out.println("\nSe ha cambiado el tipo de seguro del socio llamado " + socioEstandar.getNombre() + " con número de socio " + idSocio + ".");
                            System.out.println("El nuevo seguro es: " + tipoComtrario + " y su precio son: " + precioContrario + " euros.\n");
                        } else {
                            socioEstandar.getSeguroContratado().tipo = !socioEstandar.getSeguroContratado().tipo;
                            socioEstandar.getSeguroContratado().precio = precioContrario;
                            System.out.println("\nSe ha cambiado el tipo de seguro del socio llamado " + socioEstandar.getNombre() + " con número de socio " + idSocio + ".");
                            System.out.println("El nuevo seguro es: " + tipoComtrario + " y su precio son: " + precioContrario + " euros.\n");
                        }
                        cambiarTipoSeguro = false;
                        break;
                    case 2:
                        System.out.println("\nNo se ha cambiado el tipo de seguro del socio llamado " + socioEstandar.getNombre() + " con número de socio " + idSocio + ".");
                        System.out.println("Sigue siendo tipo " + tipoSeguro + ".\n");
                        cambiarTipoSeguro = false;
                        break;
                    default:
                        if (contadorCambio < 2) {
                            System.out.println("\n----------------------------------");
                            System.out.println("     Esta opción no es válida");
                            System.out.println("----------------------------------\n");
                            System.out.println("Lleva " + contadorCambio + " de 3 intentos.\n");
                            System.out.println("¿Quieres cambiar el seguro de " + tipoSeguro + " a " + tipoComtrario + "?");
                            contadorCambio = contadorCambio + 1;
                        } else if (contadorCambio == 2) {
                            System.out.println("\n----------------------------------");
                            System.out.println("     Esta opción no es válida");
                            System.out.println("----------------------------------\n");
                            System.out.println("Lleva " + contadorCambio + " de 3 intentos.");
                            System.out.println("Al próximo error, se le mantendrá el mismo tipo de seguro.\n");
                            System.out.println("¿Quieres cambiar el seguro de " + tipoSeguro + " a " + tipoComtrario + "?");
                            contadorCambio = contadorCambio + 1;
                        } else {
                            System.out.println("\n----------------------------------");
                            System.out.println("     Esta opción no es válida");
                            System.out.println("----------------------------------\n");
                            System.out.println("Ha consumido las tres posibilidades para el cambio de seguro.");
                            System.out.println("No se ha cambiado el tipo de seguro del socio llamado " + socioEstandar.getNombre() + " con número de socio " + idSocio + ".\n");
                            cambiarTipoSeguro = false;
                            break;
                        }
                }
            }
        } else {
            System.out.println("El socio no es de tipo estándar. No se puede modificar el seguro.");
        }
    }
    public static void mostrarSocio(List<Socio> socios) {
        Scanner scanner = new Scanner(System.in);
        boolean continuarMuestreo = true;
        int contadorMuestreo = 1;
        boolean continuarMuestreoTipo = true;
        int contadorMuestreoTipo = 1;
        System.out.println("¿Qué listado de socios quieres?");

        while (continuarMuestreo) {
            System.out.println("1. Mostrar todos los socios");
            System.out.println("2. Mostrar socios por tipo");
            System.out.println("0. Volver al menú anterior");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

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
    }
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
                Datos.crearSocio();
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
                    Datos.crearExcursion();
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
                    mostrarInscripcionPorFecha(listaInscripciones, listaSocios, listaExcursiones);
                    break;
                case 4:
                    continuarMuestreo = false;
                    mostrarInscripcionPorSocioYFecha(listaInscripciones, listaSocios, listaExcursiones);
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
    //Mostrarporfechas
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

    }
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
