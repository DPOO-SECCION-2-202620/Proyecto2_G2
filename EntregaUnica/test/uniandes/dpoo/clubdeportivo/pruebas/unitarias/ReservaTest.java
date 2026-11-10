package uniandes.dpoo.clubdeportivo.pruebas.unitarias;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class ReservaTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void padelExigeCuatroJugadores() {
        // Dado: hacemos dos reservas de padel con cantidades distintas
        Reserva individual = new Reserva("2026-11-25", 10, 1, 2, datos.canchaPadel1, datos.mariana);
        Reserva dobles = new Reserva("2026-11-25", 11, 1, 4, datos.canchaPadel1, datos.mariana);

        // Cuando: se revisa si las dos reservas son validas
        boolean individualValida = individual.esReservaValida();
        boolean doblesValida = dobles.esReservaValida();

        // Entonces: solo la de cuatro jugadores debe servir
        assertFalse(individualValida);
        assertTrue(doblesValida);
    }

    @Test
    void tenisAdmiteDosOCuatroJugadores() {
        // Dado: tenemos una reserva de dos y otra de cuatro en tenis
        Reserva dosJugadores = new Reserva("2026-11-25", 12, 1, 2, datos.canchaTenis1, datos.andres);
        Reserva cuatroJugadores = new Reserva("2026-11-25", 14, 1, 4, datos.canchaTenis1, datos.andres);

        // Cuando: validamos las dos cantidades
        boolean dosEsValida = dosJugadores.esReservaValida();
        boolean cuatroEsValida = cuatroJugadores.esReservaValida();

        // Entonces: ambas estan permitidas en tenis
        assertTrue(dosEsValida);
        assertTrue(cuatroEsValida);
    }

    @Test
    void rechazarFechaInvalida() {
        // Dado: la fecha viene escrita en un formato que no sirve
        Reserva reserva = new Reserva("25/11/2026", 10, 1, 2, datos.canchaTenis1, datos.valentina);

        // Cuando: se valida la reserva
        boolean esValida = reserva.esReservaValida();

        // Entonces: el formato se rechaza
        assertFalse(esValida);
    }

    @Test
    void instalacionNoDisponibleSiSeCruzaElHorario() {
        // Dado: la cancha uno ya esta ocupada el 20 de noviembre de 10 a 12

        // Cuando: consultamos una hora cruzada y otra despues de terminar
        boolean libreALasOnce = datos.canchaTenis1.verificarDisponibilidad("2026-11-20", 11, 1);
        boolean libreALasDoce = datos.canchaTenis1.verificarDisponibilidad("2026-11-20", 12, 1);

        // Entonces: a las 11 se cruza, pero justo a las 12 ya debe estar libre
        assertFalse(libreALasOnce);
        assertTrue(libreALasDoce);
    }
}
