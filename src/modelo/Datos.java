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
                fechaExcursion = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de la excursión (formato: dd/MM/yyyy): "));
                excursion.setFechaExcursion(fechaExcursion);
            } catch (ParseException e) {
                System.out.println("Formato de fecha incorrecto. Intente nuevamente.");
            }
        }
        excursion.setDuracionDias(Teclado.pedirInt("Ingrese la duración en días de la excursión: "));
        excursion.setPrecioInscripcion(Teclado.pedirDouble("Ingrese el precio de inscripción: "));
        System.out.println("\n");
        ExcursionDAO excursionDAO = new ExcursionDAO();
        excursionDAO.agregarExcursion(excursion);
    }


    public static void borrarExcursion() {

        int idExcursionAEliminar = Teclado.pedirInt("Inserta el ID de la Excursion que quieres eliminar");
        ExcursionDAO excursionDAO = new ExcursionDAO();
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
        ArrayList<Excursion> listaExcursiones = excursionDAO.obtenerListaExcursionesFiltroFecha(fechaInicioSQL, fechaFinSQL);
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
        SegurosDAO segurosDAO = new SegurosDAO();
        FederacionDAO federacionDAO = new FederacionDAO();
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
                seguroElegido = segurosDAO.obtenerSeguro(opcionSeguro == 1 ? 1 : 2);  // Asumiendo que los ID de seguros son 1 y 2 en la DB
                nuevoSocio = new Estandar(0, nombre, nif, seguroElegido);
                break;
            case 2:
                nif = Teclado.pedirString("Ingrese el NIF del socio: ");
                String nombreFederacion = Teclado.pedirString("Ingrese el nombre de la federación: ");
                Federacion federacion = federacionDAO.obtenerFederacionPorNombre(nombreFederacion);
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
        SegurosDAO segurosDAO = new SegurosDAO();
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
                    segurosDAO.actualizarSeguroDeSocio(estandar);
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


    public static void mostrarSocios() {
        SociosDAO sociosDAO = new SociosDAO();
        ArrayList<Socio> listaSocios = sociosDAO.obtenerListaSocios();
        sociosDAO.mostrarListaSocios(listaSocios);
    }

    public static void mostrarSociosPorTipo() {
        String tipoSocio = Teclado.pedirString("Escribe el tipo de Socio: Estandar, Federado o Infantil: ");
        SociosDAO sociosDAO = new SociosDAO();
        ArrayList<Socio> listaSocios = sociosDAO.obtenerListaSociosPorTipo(tipoSocio);
        sociosDAO.mostrarListaSociosPorTipo(listaSocios);

    }


    //Funcion para mostrar el Importe total de la Factura segun el Socio y las excursiones que tiene asignadas
    public static void mostrarFacturaTotal() {
        int idSocio = Teclado.pedirInt("Ingrese el ID del socio para mostrar su factura: ");
        SociosDAO sDao = new SociosDAO();
        Socio socioFactura = sDao.buscarSocioPorId(idSocio);
        System.out.println("\nId del Socio: " + socioFactura.getIdSocio());
        System.out.println("Nombre del Socio: " + socioFactura.getNombre());
        System.out.println("Factura mensual del socio numero: " + socioFactura.getIdSocio());
        mostrarFactura(socioFactura);
        System.out.println(mostrarFactura(socioFactura) + "\n");

    }
    public static double mostrarFactura (Socio socio){
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        ArrayList<Inscripcion> ListaInscripciones = inscripcionDAO.obtenerListaInscripciones();
        ArrayList<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
        double coste = 0;
        double costeExcursiones = 0;
        for (Inscripcion inscripcion : ListaInscripciones) {
            if (inscripcion.getIdSocio() == socio.getIdSocio()) {
                inscripciones.add(inscripcion);
            }
        }
        ExcursionDAO excursionDAO = new ExcursionDAO();
        ArrayList<Excursion> listaExcursiones = excursionDAO.obtenerListaExcursiones();
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
    public static void crearInscripcion() {
        Inscripcion i = new Inscripcion();
        SociosDAO sDAO = new SociosDAO();
        int idSocio = 0;
        Socio socioEncontrado = null;

        // bucle para verificar si la ID del socio existe
        while (socioEncontrado == null) {
            idSocio = Teclado.pedirInt("Indica la ID del Socio que quieres Inscribir: ");
            socioEncontrado = sDAO.buscarSocioPorId(idSocio);
            if (socioEncontrado != null) {
                System.out.println("Socio encontrado: " + socioEncontrado.getNombre());
            } else {
                System.out.println("No se encontró un Socio con la ID proporcionada. Intentalo Nuevamente");
            }
        }
        ExcursionDAO eDAO = new ExcursionDAO();
        int idExcursion = 0;
        Excursion excursionEncontrada = null;
        // bucle para verificar si la ID de la excursion existe
        while (excursionEncontrada == null) {
            ArrayList<Excursion> listaExcursion = eDAO.obtenerListaExcursiones();
            eDAO.mostrarListaExcursiones(listaExcursion);
            idExcursion = Teclado.pedirInt("Elige la ID de la Excursion para Inscribir al Socio: ");
            excursionEncontrada = eDAO.buscarExcursionPorId(idExcursion);

            if (excursionEncontrada != null){
                System.out.println("Excursion encontrada: " + excursionEncontrada.getDescripcion());
            } else {
                System.out.println("No se encontro la Excursion con la ID indicada. Intentalo Nuevamente");
            }

        }
        i.setIdSocio(idSocio);
        i.setIdExcursion(idExcursion);
        i.setFechaInscripcion(Generador.generarFechaActual());
        InscripcionDAO incripcionDAO = new InscripcionDAO();
        incripcionDAO.agregarInscripcion(i);
    }
    public static void eliminarInscripcion() {
        Inscripcion iaborrar = null;
        InscripcionDAO iDAO = new InscripcionDAO();
        int idInscripcionAEliminar = 0;
        while (iaborrar == null){
            idInscripcionAEliminar = Teclado.pedirInt("Inserta el ID de la Inscripcion que quieres eliminar: ");
            iaborrar = iDAO.buscarInscripcionPorID(idInscripcionAEliminar);
            if (iaborrar != null){
                iDAO.eliminarInscripcion(idInscripcionAEliminar);
            }else {
                System.out.println("No se ha encontrado la Inscripcion por el ID que has indicado. Prueba otra vez");
            }
        }
    }
    public static void mostrarTodasLasInscripciones() {
        InscripcionDAO iDAO = new InscripcionDAO();
        ArrayList<Inscripcion> listaInscripcion = iDAO.obtenerListaInscripciones();
        iDAO.mostrarListaInscripciones(listaInscripcion);
    }

    //Mostrarporsocio
    public static void mostrarInscripcionPorSocio() {
        int idSocioInscripciones = Teclado.pedirInt("Ingrese el ID del socio: ");
        InscripcionDAO iDAO = new InscripcionDAO();
        SociosDAO sDAO = new SociosDAO();
        ArrayList<Socio> listaSocio = sDAO.obtenerListaSocios();
        ArrayList<Inscripcion> listaInscripcion = iDAO.obtenerInscripcionesporSocio(idSocioInscripciones);

        if (listaInscripcion.isEmpty()) {
            System.out.println("\n----------------------------------------------------");
            System.out.println("     No hay inscripciones agregadas al socio " + idSocioInscripciones);
            System.out.println("----------------------------------------------------\n");

        } else {
        for (Inscripcion inscripcion : listaInscripcion) {
            if (idSocioInscripciones == inscripcion.getIdSocio()) {
                System.out.println("\nNúmero de socio: " + inscripcion.getIdSocio());

                // Buscar el nombre del socio correspondiente
                Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocio);
                if (socio != null) {
                    System.out.println("Nombre del socio: " + socio.getNombre());
                }

                // Buscar la excursión correspondiente a la inscripción
                ExcursionDAO eDAO = new ExcursionDAO();
                Excursion excursion = eDAO.buscarExcursionPorId(inscripcion.getIdExcursion());

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
    public static void mostrarInscripcionPorFecha() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Solicitamos al usuario las fechas de inicio y fin para aplicar el filtro
        Date fechaInicio = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de inicio (dd/MM/yyyy): "));
        Date fechaFin = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de fin (dd/MM/yyyy): "));
        if (fechaInicio.after(fechaFin)) {
            System.out.println("\n--------------------------------------------------------------------");
            System.out.println("     La fecha de inicio no puede ser posterior a la fecha final");
            System.out.println("--------------------------------------------------------------------\n");
            return;
        }
        // Convertimos java.util.Date a java.sql.Date para usar la funcion DAO
        java.sql.Date fechaInicioSQL = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date fechaFinSQL = new java.sql.Date(fechaFin.getTime());
        InscripcionDAO iDAO = new InscripcionDAO();
        SociosDAO sDAO = new SociosDAO();
        ArrayList<Socio> listaSocio = sDAO.obtenerListaSocios();
        ArrayList<Inscripcion> listaInscripciones = iDAO.obtenerInscripcionesFiltroFechas(fechaInicioSQL, fechaFinSQL);
        boolean inscripcionesEncontradas = false;
            for (Inscripcion inscripcion : listaInscripciones) {
                Date fechaInscripcion = inscripcion.getFechaInscripcion();
                if ((fechaInscripcion.after(fechaInicio) && fechaInscripcion.before(fechaFin)) || (fechaInscripcion.equals(fechaInicio) && fechaInscripcion.before(fechaFin)) || (fechaInscripcion.after(fechaInicio) && fechaInscripcion.equals(fechaFin)))  {
                    System.out.println("Número de socio: " + inscripcion.getIdSocio());

                    // Buscar el nombre del socio correspondiente
                    Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocio);
                    if (socio != null) {
                        System.out.println("Nombre del socio: " + socio.getNombre());
                    } else {
                        System.out.println("Nombre del socio: No encontrado");
                    }
                    // Buscar la excursión correspondiente a la inscripción
                    ExcursionDAO eDAO = new ExcursionDAO();
                    Excursion excursion = eDAO.buscarExcursionPorId(inscripcion.getIdExcursion());
                    if (excursion != null) {
                        // Mostrar fecha de la excursión y descripción
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                        System.out.println("Fecha de la excursión: " + fechaTransformada);
                        System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                        ;
                        // Calcular e imprimir el importe con los cargos o descuentos aplicados
                        double importeTotal = calcularImporteTotal(excursion, socio);
                        System.out.println("Importe total: " + importeTotal + " euros.\n");
                    } else {
                        System.out.println("No se encontró información de la excursión para esta inscripción.");
                    }
                    inscripcionesEncontradas = true;
                }
            }
            if (!inscripcionesEncontradas) {
                System.out.println(" No hay inscripciones realizadas entre el día " + dateFormat.format(fechaInicio) + " y el día " + dateFormat.format(fechaFin) + "\n");
            }
    }

    public static void mostrarInscripcionPorSocioYFecha() throws ParseException {

        int idSocioInscripciones = Teclado.pedirInt("Ingrese el ID del socio: ");
        InscripcionDAO iDAO = new InscripcionDAO();
        SociosDAO sDAO = new SociosDAO();
        ArrayList<Socio> listaSocio = sDAO.obtenerListaSocios();
        ArrayList<Inscripcion> listaInscripcion = iDAO.obtenerInscripcionesporSocio(idSocioInscripciones);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Solicitamos al usuario las fechas de inicio y fin para aplicar el filtro
        Date fechaInicio = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de inicio (dd/MM/yyyy): "));
        Date fechaFin = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de fin (dd/MM/yyyy): "));
        if (fechaInicio.after(fechaFin)) {
            System.out.println("\n--------------------------------------------------------------------");
            System.out.println("     La fecha de inicio no puede ser posterior a la fecha final");
            System.out.println("--------------------------------------------------------------------\n");
            return;
        }
        // Convertimos java.util.Date a java.sql.Date para usar la funcion DAO
        java.sql.Date fechaInicioSQL = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date fechaFinSQL = new java.sql.Date(fechaFin.getTime());
        ArrayList<Inscripcion> listaInscripciones = iDAO.obtenerInscripcionesFiltroFechas(fechaInicioSQL, fechaFinSQL);

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
                    Socio socio = obtenerSocioPorId(inscripcion.getIdSocio(), listaSocio);
                    if (socio != null) {
                        System.out.println("\nNombre del socio: " + socio.getNombre());
                    } else {
                        System.out.println("Nombre del socio: No encontrado");
                    }

                    // Buscar la excursión correspondiente a la inscripción
                    ExcursionDAO eDAO = new ExcursionDAO();
                    Excursion excursion = eDAO.buscarExcursionPorId(inscripcion.getIdExcursion());
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
