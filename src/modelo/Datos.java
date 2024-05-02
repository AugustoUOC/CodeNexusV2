package modelo;

import modelo.dao.ExcursionDAO;
import modelo.dao.InscripcionDAO;
import modelo.dao.SociosDAO;
import utilidad.Generador;
import utilidad.Teclado;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Datos {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("AntoniPersistenceUnit");


    //Métodos para excursiones
    public static void crearExcursion() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AntoniPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Excursion excursion = new Excursion();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Descripción de la Excursión: ");
        excursion.setDescripcion(scanner.nextLine());

        Date fechaExcursion = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while (fechaExcursion == null) {
            try {
                System.out.print("Ingrese la fecha de la excursión (formato: dd/MM/yyyy): ");
                fechaExcursion = dateFormat.parse(scanner.nextLine());
                excursion.setFechaExcursion(fechaExcursion);
            } catch (ParseException e) {
                System.out.println("Formato de fecha incorrecto. Intente nuevamente.");
            }
        }

        System.out.print("Ingrese la duración en días de la excursión: ");
        excursion.setDuracionDias(scanner.nextInt());

        System.out.print("Ingrese el precio de inscripción: ");
        excursion.setPrecioInscripcion(scanner.nextDouble());

        em.persist(excursion);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }



    public static void borrarExcursion() {
        int idExcursionAEliminar = Teclado.pedirInt("Inserta el ID de la Excursión que quieres eliminar: ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Excursion excursion = em.find(Excursion.class, idExcursionAEliminar);
        if (excursion != null) {
            em.remove(excursion);
            em.getTransaction().commit();
            System.out.println("La excursión ha sido eliminada exitosamente.");
        } else {
            System.out.println("No se encontró la excursión con el ID proporcionado.");
        }

        em.close();
        emf.close();
    }

    public static void mostrarExcursionesPorFechas() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de inicio (dd/MM/yyyy): "));
        Date fechaFin = dateFormat.parse(Teclado.pedirString("Ingrese la fecha de fin (dd/MM/yyyy): "));

        if (fechaInicio.after(fechaFin)) {
            System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();

        TypedQuery<Excursion> query = em.createQuery("SELECT e FROM Excursion e WHERE e.fechaExcursion BETWEEN :fechaInicio AND :fechaFin", Excursion.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        List<Excursion> listaExcursiones = query.getResultList();

        em.close();
        emf.close();

        if (listaExcursiones.isEmpty()) {
            System.out.println("No se encontraron excursiones en el rango de fechas especificado.");
        } else {
            for (Excursion excursion : listaExcursiones) {
                System.out.println(excursion);
            }
        }
    }


    public static void borrarSocio() {
        int idSocioABorrar = Teclado.pedirInt("Inserta el ID del Socio que quieres eliminar: ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Socio socio = em.find(Socio.class, idSocioABorrar);
        if (socio != null) {
            em.remove(socio);
            em.getTransaction().commit();
            System.out.println("El Socio ha sido borrado correctamente.");
        } else {
            System.out.println("No se encontró el Socio con el ID proporcionado.");
        }

        em.close();
        emf.close();
    }

    public static void crearSocio() {
        Socio nuevoSocio = null;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

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
                seguroElegido = em.find(Seguro.class, opcionSeguro == 1 ? 1 : 2); // Asumiendo que los ID de seguros son 1 y 2 en la DB
                nuevoSocio = new Estandar(nombre, nif, seguroElegido);
                break;
            case 2:
                nif = Teclado.pedirString("Ingrese el NIF del socio: ");
                String nombreFederacion = Teclado.pedirString("Ingrese el nombre de la federación: ");
                Federacion federacion = em.createQuery("SELECT f FROM Federacion f WHERE f.nombre = :nombre", Federacion.class)
                        .setParameter("nombre", nombreFederacion)
                        .getSingleResult();
                nuevoSocio = new Federado(nombre, federacion, nif);
                break;
            case 3:
                int idTutor = Teclado.pedirInt("Elige el ID del tutor: ");
                Socio tutor = em.find(Socio.class, idTutor);
                if (tutor != null && Teclado.pedirInt("El tutor seleccionado es: " + tutor.getNombre() + " (ID: " + tutor.getId() + ")\n1. Confirmar tutor\n2. Cancelar\nIngrese la opción deseada: ") == 1) {
                    nuevoSocio = new Infantil(nombre, tutor);
                } else {
                    System.out.println("Creación de socio infantil cancelada o no se encontró un tutor con el ID proporcionado.");
                }
                break;
            default:
                System.out.println("Opción no válida. Por favor, reintente.");
                return;
        }

        if (nuevoSocio != null) {
            em.persist(nuevoSocio);
            em.getTransaction().commit();
            System.out.println("Socio creado exitosamente.");
        } else {
            System.out.println("No se ha podido agregar el Socio");
        }

        em.close();
        emf.close();
    }



    public static void modificarSeguro(int idSocio) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Socio socio = em.find(Socio.class, idSocio);
        if (socio instanceof Estandar) {
            Estandar estandar = (Estandar) socio;
            Seguro seguroActual = estandar.getSeguroContratado();
            System.out.println(seguroActual);
            if (seguroActual != null) {
                System.out.println("El socio es del tipo Estandar con seguro actual: " + seguroActual.getTipo());
                // Determinamos el nuevo seguro basándonos en el actual que posee el socio indicado
                int nuevoIdSeguro = seguroActual.getId() == 1 ? 2 : 1;
                String nuevoNombreSeguro = nuevoIdSeguro == 1 ? "Básico" : "Completo";
                double nuevoPrecio = nuevoIdSeguro == 1 ? 10 : 20;
                if (Teclado.confirmarAccion("¿Desea cambiar al seguro " + nuevoNombreSeguro + " que vale $" + nuevoPrecio + "?")) {
                    Seguro nuevoSeguro = em.find(Seguro.class, nuevoIdSeguro);
                    estandar.setSeguroContratado(nuevoSeguro);
                    em.merge(estandar);
                    em.getTransaction().commit();
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

        em.close();
        emf.close();
    }


    public static void mostrarSocios() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Socio> query = em.createQuery("SELECT s FROM Socio s", Socio.class);
        List<Socio> listaSocios = query.getResultList();
        for (Socio socio : listaSocios) {
            System.out.println(socio);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public static void mostrarSociosPorTipo() {
        String tipoSocio = Teclado.pedirString("Escribe el tipo de Socio: Estandar, Federado o Infantil: ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Socio> query = em.createQuery("SELECT s FROM Socio s WHERE TYPE(s) = :tipo", Socio.class);
        query.setParameter("tipo", tipoSocio);
        List<Socio> listaSocios = query.getResultList();
        for (Socio socio : listaSocios) {
            System.out.println(socio);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
    }



    //Funcion para mostrar el Importe total de la Factura segun el Socio y las excursiones que tiene asignadas
    public static void mostrarFacturaTotal() {
        int idSocio = Teclado.pedirInt("Ingrese el ID del socio para mostrar su factura: ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Socio socioFactura = em.find(Socio.class, idSocio);
        if (socioFactura != null) {
            System.out.println("\nId del Socio: " + socioFactura.getIdSocio());
            System.out.println("Nombre del Socio: " + socioFactura.getNombre());
            System.out.println("Factura mensual del socio número: " + socioFactura.getIdSocio());
            double facturaTotal = mostrarFactura(socioFactura);
            System.out.println("Factura Total: " + facturaTotal + "\n");
        } else {
            System.out.println("No se encontró ningún socio con el ID proporcionado.");
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public static double mostrarFactura(Socio socio) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Query query = em.createQuery("SELECT i FROM Inscripcion i WHERE i.socio.id = :idSocio");
        query.setParameter("idSocio", socio.getIdSocio());
        List<Inscripcion> inscripciones = query.getResultList();

        double coste = 0;
        double costeExcursiones = 0;

        for (Inscripcion inscripcion : inscripciones) {
            Excursion excursion = inscripcion.getExcursion();
            if (excursion != null) {
                costeExcursiones += calcularCosteExcursion(socio, excursion);
            }
        }

        coste = calcularCuota(socio) + costeExcursiones;

        em.getTransaction().commit();
        em.close();
        emf.close();

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
            precio = excursion.getPrecioInscripcion();
        }
        return precio;
    }

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
        ExcursionDAO eDAO = new ExcursionDAO();

        int idSocio;
        Socio socioEncontrado;
        do {
            idSocio = Teclado.pedirInt("Indica la ID del Socio que quieres Inscribir: ");
            socioEncontrado = sDAO.buscarSocioPorId(idSocio);
            if (socioEncontrado == null) {
                System.out.println("No se encontró un Socio con la ID proporcionada. Inténtalo nuevamente.");
            }
        } while (socioEncontrado == null);

        int idExcursion;
        Excursion excursionEncontrada;
        do {
            ArrayList<Excursion> listaExcursion = eDAO.obtenerListaExcursiones();
            eDAO.mostrarListaExcursiones(listaExcursion);
            idExcursion = Teclado.pedirInt("Elige la ID de la Excursión para Inscribir al Socio: ");
            excursionEncontrada = eDAO.buscarExcursionPorId(idExcursion);
            if (excursionEncontrada == null) {
                System.out.println("No se encontró la Excursión con la ID indicada. Inténtalo nuevamente.");
            }
        } while (excursionEncontrada == null);

        i.setSocio(socioEncontrado);
        i.setExcursion(excursionEncontrada);
        i.setFechaInscripcion(Generador.generarFechaActual());

        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        inscripcionDAO.agregarInscripcion(i);
    }

    public static void eliminarInscripcion() {
        InscripcionDAO iDAO = new InscripcionDAO();

        int idInscripcionAEliminar;
        Inscripcion inscripcionAEliminar;
        do {
            idInscripcionAEliminar = Teclado.pedirInt("Inserta el ID de la Inscripción que quieres eliminar: ");
            inscripcionAEliminar = iDAO.buscarInscripcionPorID(idInscripcionAEliminar);
            if (inscripcionAEliminar == null) {
                System.out.println("No se ha encontrado la Inscripción por el ID que has indicado. Prueba otra vez.");
            }
        } while (inscripcionAEliminar == null);

        iDAO.eliminarInscripcion(inscripcionAEliminar);
    }

    public static void mostrarTodasLasInscripciones() {
        InscripcionDAO iDAO = new InscripcionDAO();
        ArrayList<Inscripcion> listaInscripcion = iDAO.obtenerListaInscripciones();
        iDAO.mostrarListaInscripciones(listaInscripcion);
    }

    public static void mostrarInscripcionPorSocio() {
        int idSocioInscripciones = Teclado.pedirInt("Ingrese el ID del socio: ");
        InscripcionDAO iDAO = new InscripcionDAO();
        SociosDAO sDAO = new SociosDAO();

        Socio socio = sDAO.buscarSocioPorId(idSocioInscripciones);
        if (socio == null) {
            System.out.println("No se encontró un socio con el ID proporcionado.");
            return;
        }

        ArrayList<Inscripcion> listaInscripcion = iDAO.obtenerInscripcionesPorSocio(idSocioInscripciones);

        if (listaInscripcion.isEmpty()) {
            System.out.println("\n----------------------------------------------------");
            System.out.println("     No hay inscripciones agregadas al socio " + idSocioInscripciones);
            System.out.println("----------------------------------------------------\n");
        } else {
            for (Inscripcion inscripcion : listaInscripcion) {
                System.out.println("\nNúmero de socio: " + inscripcion.getSocio().getIdSocio());
                System.out.println("Nombre del socio: " + inscripcion.getSocio().getNombre());
                System.out.println("Fecha de la inscripción: " + inscripcion.getFechaInscripcion());

                Excursion excursion = inscripcion.getExcursion();
                if (excursion != null) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                    System.out.println("Fecha de la excursión: " + fechaTransformada);
                    System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                    double importeTotal = calcularImporteTotal(excursion, socio);
                    System.out.println("Importe total: " + importeTotal + " euros.");
                } else {
                    System.out.println("No se encontró información de la excursión para esta inscripción.");
                }

                System.out.println(); // Separador entre cada inscripción
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
        // Convertimos java.util.Date a java.sql.Date para usar la función DAO
        java.sql.Date fechaInicioSQL = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date fechaFinSQL = new java.sql.Date(fechaFin.getTime());

        InscripcionDAO iDAO = new InscripcionDAO();
        ArrayList<Inscripcion> listaInscripciones = iDAO.obtenerInscripcionesFiltroFechas(fechaInicioSQL, fechaFinSQL);
        boolean inscripcionesEncontradas = false;

        for (Inscripcion inscripcion : listaInscripciones) {
            Date fechaInscripcion = inscripcion.getFechaInscripcion();
            if ((fechaInscripcion.after(fechaInicio) && fechaInscripcion.before(fechaFin)) ||
                    (fechaInscripcion.equals(fechaInicio) && fechaInscripcion.before(fechaFin)) ||
                    (fechaInscripcion.after(fechaInicio) && fechaInscripcion.equals(fechaFin)))  {

                System.out.println("Número de socio: " + inscripcion.getSocio().getIdSocio());
                System.out.println("Nombre del socio: " + inscripcion.getSocio().getNombre());

                // Buscar la excursión correspondiente a la inscripción
                Excursion excursion = inscripcion.getExcursion();
                if (excursion != null) {
                    // Mostrar fecha de la excursión y descripción
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaTransformada = formatoFecha.format(excursion.getFechaExcursion());

                    System.out.println("Fecha de la excursión: " + fechaTransformada);
                    System.out.println("Descripción de la excursión: " + excursion.getDescripcion());

                    // Calcular e imprimir el importe con los cargos o descuentos aplicados
                    double importeTotal = calcularImporteTotal(excursion, inscripcion.getSocio());
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

        // Convertimos java.util.Date a java.sql.Date para usar la función DAO
        java.sql.Date fechaInicioSQL = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date fechaFinSQL = new java.sql.Date(fechaFin.getTime());

        InscripcionDAO iDAO = new InscripcionDAO();
        SociosDAO sDAO = new SociosDAO();

        Socio socio = sDAO.buscarSocioPorId(idSocioInscripciones);
        if (socio == null) {
            System.out.println("No se encontró un socio con el ID proporcionado.");
            return;
        }

        List<Inscripcion> listaInscripciones = iDAO.obtenerInscripcionesPorSocioYFechas(idSocioInscripciones, fechaInicioSQL, fechaFinSQL);

        System.out.println("\n----------------------------------------------------------------------------------------------");
        System.out.println("     Inscripciones realizadas por el socio " + socio.getIdSocio() + " (" + socio.getNombre() + ") entre el día " + dateFormat.format(fechaInicio) + " y el día " + dateFormat.format(fechaFin));
        System.out.println("----------------------------------------------------------------------------------------------\n");

        boolean inscripcionesEncontradas = false;
        for (Inscripcion inscripcion : listaInscripciones) {
            Excursion excursion = inscripcion.getExcursion();
            if (excursion != null) {
                System.out.println("Fecha de la inscripción: " + dateFormat.format(inscripcion.getFechaInscripcion()));
                System.out.println("Descripción de la excursión: " + excursion.getDescripcion());
                System.out.println("Fecha de la excursión: " + dateFormat.format(excursion.getFechaExcursion()));

                // Calcular e imprimir el importe con los cargos o descuentos aplicados
                double importeTotal = calcularImporteTotal(excursion, socio);
                System.out.println("Importe total: " + importeTotal + " euros.\n");

                inscripcionesEncontradas = true;
            }
        }

        if (!inscripcionesEncontradas) {
            System.out.println("No hay inscripciones realizadas para el socio " + socio.getIdSocio() + " entre el día " + dateFormat.format(fechaInicio) + " y el día " + dateFormat.format(fechaFin) + "\n");
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
