import modelo.*;
import modelo.Datos;
import vista.Menu;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;


public class Main {
    public static void main(String[] args) throws ParseException, SQLException {
        Menu menu = new Menu();
        menu.menuPrincipal();
    }
}