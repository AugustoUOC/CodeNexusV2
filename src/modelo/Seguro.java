package modelo;

public class Seguro {
    public boolean tipo;
    public double precio;

    // Constructor vacío
    public Seguro() {
    }

    // Constructor con todos los atributos
    public Seguro(boolean tipo, double precio) {
        this.tipo = tipo;
        this.precio = precio;
    }

    // Getters y setters
    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // Método toString para imprimir los detalles del seguro
    @Override
    public String toString() {
        return "El seguro es " + (tipo ? "Completo" : "Básico") +
                "y tiene un precio de " + precio +
                " euros.";
    }
}
