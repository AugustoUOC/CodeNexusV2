package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


class DatosTest {

    private Socio socioEstandar;
    private Socio socioFederado;
    private Socio socioInfantil;
    private Excursion excursion;

    @BeforeEach
    public void setUp() {
        socioEstandar = new Estandar(1, "Pepe", "2815458W", new Seguro(false, 20));
        socioFederado = new Federado (2, "Manu", new Federacion(1, "Betica"),"12345678W");
        socioInfantil = new Infantil(3, "Luquitas", 1);
        Inscripcion inscripcion = new Inscripcion(1, 1, 1, new Date());
        excursion = new Excursion(1, "test", new Date(), 1, 18);

        Datos.listaInscripciones = List.of(inscripcion);
        Datos.listaExcursiones = List.of(excursion);
        Datos.listaSocios = List.of(socioEstandar);
    }

    @Test
    void mostrarFactura() {
        if (Datos.mostrarFactura(socioEstandar) != 48) throw new AssertionError();
    }

    @Test
    void calcularImporteTotal() {

        Socio socioEstandarTest = socioEstandar;
        Socio socioFederadoTest = socioFederado;
        Socio socioInfantilTest = socioInfantil;
        Excursion excursiontest = excursion;

        double costeSocioEstandar = Datos.calcularImporteTotal(excursion, socioEstandar);
        double costeSocioFederado = Datos.calcularImporteTotal(excursion, socioFederado);
        double costeSocioInfantil = Datos.calcularImporteTotal(excursion, socioInfantil);

        assert costeSocioEstandar == 38 && costeSocioFederado == 16.2 && costeSocioInfantil == 18;

    }

}