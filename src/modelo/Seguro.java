package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSeguro;

    private String tipo;
    private double precio;

    // Constructor vacío
    public Seguro() {
    }

    public Seguro(String tipo, double precio) {
        this.tipo = tipo;
        this.precio = precio;
    }

    public int getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(int idSeguro) {
        this.idSeguro = idSeguro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seguro seguro = (Seguro) o;
        return idSeguro == seguro.idSeguro && Double.compare(seguro.precio, precio) == 0 && Objects.equals(tipo, seguro.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSeguro, tipo, precio);
    }

    @Override
    public String toString() {
        return "Seguro{" +
                "idSeguro=" + idSeguro +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }
}
