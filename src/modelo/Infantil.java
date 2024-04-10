package modelo;

import modelo.Socio;

public class Infantil extends Socio {
    private int idTutor;
    // Constructor vacío
    public Infantil() {
    }
    // Constructor con todos los atributos
    public Infantil(int idSocio, String nombre, int idTutor) {
        super(idSocio, nombre, "Infantil");
        this.idTutor = idTutor;

    }

    // Getter y setter para el tutor asociado
    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    // Método toString para imprimir los detalles del socio infantil
    @Override
    public String toString() {
        return "Socio Infantil con id número: " + getIdSocio() + ", llamado: " +  getNombre() + ".\n" +
                "Tiene un tutor asociado con el id número " + idTutor +".";

    }
}

