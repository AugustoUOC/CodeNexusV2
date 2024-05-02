package modelo;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Estandar extends Socio {
    private String nif;

    @OneToOne
    private Seguro seguroContratado;

    // Constructor vacío
    // public Estandar() {}

    // Constructor con todos los atributos
    public Estandar(String nombre, String nif, Seguro seguroContratado) {
        super(nombre, "Estandar");
        this.nif = nif;
        this.seguroContratado = seguroContratado;
    }

    // Getter y setter para el NIF
    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Seguro getSeguroContratado() {
        return seguroContratado;
    }

    public void setSeguroContratado(Seguro seguroContratado) {
        this.seguroContratado = seguroContratado;
    }

    @Override
    public String toString() {
        return "Estandar{" +
                "nif='" + nif + '\'' +
                ", seguroContratado=" + seguroContratado +
                ", tipoSocio='" + tipoSocio + '\'' +
                '}';
    }
}
