package modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Inscripcion {
    private int idInscripcion;
    private int idSocio;
    private int idExcursion;
    private Date fechaInscripcion;



    // Constructor vacío
    public Inscripcion() {
    }

    // Constructor con todos los atributos
    public Inscripcion(int idInscripcion, int idSocio, int idExcursion, Date fechaInscripcion) {
        this.idInscripcion = idInscripcion;
        this.idSocio = idSocio;
        this.idExcursion = idExcursion;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y setters
    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public int getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(int idExcursion) {
        this.idExcursion = idExcursion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    // Método toString para imprimir los detalles de la inscripción
    @Override
    public String toString() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaTransformada = formatoFecha.format(fechaInscripcion);
        return "Inscripcion con id número " + idInscripcion + ", realizada el " + fechaTransformada +
                ".\nAsociada a la excursión número " + idExcursion +" y al socio número " + idSocio + ".";
    }
}

