package uniandes.dpoo.clubdeportivo.pruebas.unitarias;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class TurnosTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void datosInicialesMantienenCobertura() {
        // Dado: usamos la programacion semanal que viene en DatosPrueba

        // Cuando: se revisa la cobertura de los turnos
        boolean completa = datos.administracion.mantieneCobertura();

        // Entonces: la cobertura debe estar completa
        assertTrue(completa);
    }

    @Test
    void quitarUnEntrenadorRompeLaCobertura() {
        // Dado: buscamos el turno del entrenador del lunes en la manana
        Turno turnoBase = null;
        for (Turno turno : datos.administracion.getTurnos()) {
            if (turno.getIdentificacionEmpleado().equals("entrenador")
                    && turno.getDiaSemana().equals("LUNES")
                    && turno.getHoraInicio().equals(LocalTime.of(6, 0))) {
                turnoBase = turno;
                break;
            }
        }
        assertNotNull(turnoBase);

        // Cuando: quitamos ese turno
        datos.administracion.getTurnos().remove(turnoBase);

        // Entonces: durante una parte de la manana ya no hay cobertura
        assertFalse(datos.administracion.mantieneCobertura());
    }

    @Test
    void turnoNoCubreSuHoraDeFin() {
        // Dado: hay un turno de seis de la manana a dos de la tarde
        Turno turno = new Turno("entrenador", Turno.ENTRENADOR, "LUNES",
                LocalTime.of(6, 0), LocalTime.of(14, 0));

        // Cuando: preguntamos por la primera hora y por la hora final
        boolean cubreInicio = turno.cubre("LUNES", LocalTime.of(6, 0));
        boolean cubreFin = turno.cubre("LUNES", LocalTime.of(14, 0));

        // Entonces: cubre las seis, pero no las dos que es cuando termina
        assertTrue(cubreInicio);
        assertFalse(cubreFin);
    }

    @Test
    void turnoConDiaInvalidoNoEsValido() {
        // Dado: se arma un turno con un dia que no existe
        Turno turno = new Turno("entrenador", Turno.ENTRENADOR, "FESTIVO",
                LocalTime.of(6, 0), LocalTime.of(14, 0));

        // Cuando: se valida el turno
        boolean esValido = turno.esValido();

        // Entonces: el dia invalido hace que de falso
        assertFalse(esValido);
    }
}
