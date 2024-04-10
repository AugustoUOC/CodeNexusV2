package modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Excursion {
    private int idExcursion;
    private String descripcion;
    private Date fechaExcursion;
    private int duracionDias;
    private double precioInscripcion;



    // Constructor vacío
    public Excursion() {
    }

    // Constructor con todos los atributos
    public Excursion(int idExcursion, String descripcion, Date fechaExcursion, int duracionDias, double precioInscripcion) {
        this.idExcursion = idExcursion;
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
        //Transformar la fecha al formato deseado para que lo imporima
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaTransformada = formatoFecha.format(fechaExcursion);
        return "Excursión con id : " + idExcursion + ".\n" +
                "Descripción = " + descripcion + ".\n" +
                "Fecha de la excursión = " + fechaTransformada + ", con una duración de " + duracionDias +" días.\n" +
                "El precio de inscripción es de " + precioInscripcion +" Euros.\n";
    }

}