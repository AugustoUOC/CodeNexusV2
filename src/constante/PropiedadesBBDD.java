package constante;

public class PropiedadesBBDD {

    private static final String USUARIO = "admin"; // Usuario administrador de la BBDD

    private static final String PASSWORD = "123456"; // Cualquiera, lo suyo seria mejorarla por seguridad


    public static final String URL_BBDD = "jdbc:mysql://localhost:3306/poo_bbdd?user=" + USUARIO + "&password=" + PASSWORD;

}
