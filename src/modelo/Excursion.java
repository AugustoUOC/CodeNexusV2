package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity // Indica que esta clase es una entidad persistente
public class Excursion {
    @Id // Indica que este atributo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el valor se generará automáticamente
    private int idExcursion;
    private String descripcion;
    private Date fechaExcursion;
    private int duracionDias;
    private double precioInscripcion;

    // Constructor vacío
    public Excursion() {
    }

    // Constructor con todos los atributos
    public Excursion(String descripcion, Date fechaExcursion, int duracionDias, double precioInscripcion) {
        this.descripcion = descripcion;
        this.fechaExcursion = fechaExcursion;
        this.duracionDias = duracionDias;
        this.precioInscripcion = precioInscripcion;
    }

    // Getters y setters
    public int getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(int idExcursion) {
        this.idExcursion = idExcursion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaExcursion() {
        return fechaExcursion;
    }

    public void setFechaExcursion(Date fechaExcursion) {
        this.fechaExcursion = fechaExcursion;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public double getPrecioInscripcion() {
        return precioInscripcion;
    }

    public void setPrecioInscripcion(double precioInscripcion) {
        this.precioInscripcion = precioInscripcion;
    }

    // Método toString para imprimir los detalles de la excursión
    @Override
    public String toString() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaTransformada = formatoFecha.format(fechaExcursion);
        return "Excursión con ID: " + idExcursion + ".\n" +
                "Descripción: " + descripcion + ".\n" +
                "Fecha de la excursión: " + fechaTransformada + ", duración (días): " + duracionDias + ".\n" +
                "Precio de inscripción: " + precioInscripcion + " Euros.\n";
    }
}
