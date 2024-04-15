package constante;

public class PropiedadesBBDD {

    private static final String USUARIO = "Antoni"; // Usuario administrador de la BBDD

    private static final String PASSWORD = "123456"; // Cualquiera, lo suyo seria mejorarla por seguridad

    // HAY QUE CAMBIAR LA DIRECCION DE LA BASE DE DATOS DONDE "TEST" ES EL NOMBRE QUE LE PONGA A LA MISMA
    public static final String URL_BBDD = "jdbc:mysql://localhost/test?" + "user=" + USUARIO + "password=" + PASSWORD;


}
