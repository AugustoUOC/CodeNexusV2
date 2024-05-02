package modelo;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idSocio;
    private String nombre;
    protected String tipoSocio;

    // Constructor con todos los atributos
    public Socio(int idSocio, String nombre, String tipoSocio) {
        this.idSocio = idSocio;
        this.nombre = nombre;
        this.tipoSocio = tipoSocio;
    }

    // Constructor con todos los atributos
    public Socio(String nombre, String tipoSocio) {
        this.nombre = nombre;
        this.tipoSocio = tipoSocio;
    }

    // Getters y setters
    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoSocio() {
        return tipoSocio;
    }

    public void setTipoSocio(String tipoSocio) {
        this.tipoSocio = tipoSocio;
    }

    // Método toString para imprimir los detalles del socio
    @Override
    public String toString() {
        return "Socio " + tipoSocio + " creado.\n" +
                "Su id es el número " + idSocio +
                " y se llama " + nombre + ".";
    }
}
