package utilidad;

import modelo.Datos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Generador {

    public static Date generarFechaActual() {
        try {
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fechaString = formato.format(fechaActual);
            return formato.parse(fechaString);
        }catch (ParseException pe){
            return null;
        }
    }

}
