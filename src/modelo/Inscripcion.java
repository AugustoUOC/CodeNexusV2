package modelo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInscripcion")
    private int idInscripcion;

    @Column(name = "idSocio")
    private int idSocio;

    @Column(name = "idExcursion")
    private int idExcursion;

    @Column(name = "fechaInscripcion")
    private Date fechaInscripcion;

    // Constructor vacío
    public Inscripcion() {
    }

    // Constructor con todos los atributos
    public Inscripcion(int idSocio, int idExcursion, Date fechaInscripcion) {
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
        return "Inscripción con ID: " + idInscripcion + ", realizada el " + fechaInscripcion +
                ", asociada a la excursión con ID: " + idExcursion + " y al socio con ID: " + idSocio + ".";
    }
}
