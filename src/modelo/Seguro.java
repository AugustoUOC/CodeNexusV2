package modelo;

import java.util.Objects;

public class Seguro {

    private int idSeguro;
    public String tipo;
    public double precio;

    // Constructor vacío
    public Seguro() {

    }

    public Seguro(int idSeguro, String tipo, double precio) {
        this.idSeguro = idSeguro;
        this.tipo = tipo;
        this.precio = precio;
    }

    public int getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(int idSeguro) {
        this.idSeguro = idSeguro;
    }

    public Seguro(String tipo, double precio) {
        this.tipo = tipo;
        this.precio = precio;
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
    public String toString() {
        return "Seguro{" +
                "idSeguro=" + idSeguro +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }
}
