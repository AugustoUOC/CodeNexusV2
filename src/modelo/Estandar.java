package modelo;

public class Estandar extends Socio {
    private String nif;
    public Seguro seguroContratado;

    // Constructor vacío
    public Estandar() {
    }

    // Constructor con todos los atributos

    public Estandar(int idSocio, String nombre, String nif, Seguro seguroContratado) {
        super(idSocio, nombre , "Estandar");
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

    // Getter y setter para el seguro contratado
    public Seguro getSeguroContratado() {
        return seguroContratado;
    }

    public void setSeguroContratado(Seguro seguroContratado) {
        this.seguroContratado = seguroContratado;
    }

    // Método para modificar el seguro contratado
    public void crearSeguro(Seguro nuevoSeguro) {
        this.seguroContratado = nuevoSeguro;
    }

    // Método toString para imprimir los detalles del socio estándar
    @Override
    public String toString() {
        String tipoSeguro = "básico";
        if (seguroContratado.tipo) {
            tipoSeguro = "completo";
        }
        return "Socio Estandar con id número: " +getIdSocio() + ", llamado: " +  getNombre() + ", con NIF: " + nif + ".\n" +
                "Tiene un seguro " +  tipoSeguro + " que vale " + seguroContratado.precio +" euros.";
    }
}
