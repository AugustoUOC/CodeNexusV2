package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Federacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFederacion;
    private String nombre;

    // Constructor vacío
    public Federacion() {
    }

    // Constructor con todos los atributos
    public Federacion(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
    public int getIdFederacion() {
        return idFederacion;
    }

    public void setIdFederacion(int idFederacion) {
        this.idFederacion = idFederacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método toString para imprimir los detalles de la federación
    @Override
    public String toString() {
        return "La federación se llama " + nombre +
                " y su ID es el número " + idFederacion + ".";
    }
}
