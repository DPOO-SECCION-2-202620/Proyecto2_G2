package uniandes.dpoo.clubdeportivo.pruebas.unitarias;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.competencias.CompetenciaLiga;
import uniandes.dpoo.clubdeportivo.modelo.competencias.FilaTabla;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Participante;
import uniandes.dpoo.clubdeportivo.modelo.competencias.PartidoCompetencia;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class CompetenciaLigaTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void puntosDeFutbolSonTresUnoCero() {
        // Dado: una fila nueva en una liga de futbol
        FilaTabla fila = new FilaTabla(datos.participanteClub);

        // Cuando: le sumamos una victoria, un empate y una derrota
        fila.acumularResultado(2, 1, datos.torneoDeLosCerros.getPuntosPorVictoria(),
                datos.torneoDeLosCerros.getPuntosPorEmpate());
        fila.acumularResultado(1, 1, datos.torneoDeLosCerros.getPuntosPorVictoria(),
                datos.torneoDeLosCerros.getPuntosPorEmpate());
        fila.acumularResultado(0, 1, datos.torneoDeLosCerros.getPuntosPorVictoria(),
                datos.torneoDeLosCerros.getPuntosPorEmpate());

        // Entonces: debe terminar con cuatro puntos en total
        assertEquals(3, datos.torneoDeLosCerros.getPuntosPorVictoria());
        assertEquals(1, datos.torneoDeLosCerros.getPuntosPorEmpate());
        assertEquals(4, fila.getPuntos());
        assertEquals(1, fila.getGanados());
        assertEquals(1, fila.getEmpatados());
        assertEquals(1, fila.getPerdidos());
    }

    @Test
    void baloncestoNoDaPuntosPorEmpate() {
        // Dado: creamos una liga de baloncesto
        CompetenciaLiga liga = new CompetenciaLiga("Liga de baloncesto", "Club Seneca", 2026, 5,
                DatosPrueba.baloncesto(), DatosPrueba.mayores());

        // Cuando: revisamos los puntos definidos para ese deporte
        int porVictoria = liga.getPuntosPorVictoria();
        int porEmpate = liga.getPuntosPorEmpate();

        // Entonces: ganar da dos y empatar no da nada
        assertEquals(2, porVictoria);
        assertEquals(0, porEmpate);
    }

    @Test
    void procesarPartidoActualizaLaTabla() {
        // Dado: dos participantes inscritos que todavia no han jugado
        PartidoCompetencia partido = new PartidoCompetencia(LocalDate.of(2026, 11, 10),
                "Cancha del Rosario", datos.rivalJaveriana, datos.rivalRosario);
        partido.registrarResultado(2, 2);

        // Cuando: la liga procesa el empate
        datos.torneoDeLosCerros.procesarPartido(partido);

        // Entonces: las dos filas deben quedar con un punto
        FilaTabla local = datos.torneoDeLosCerros.getTabla().buscarFila(datos.rivalJaveriana);
        FilaTabla visitante = datos.torneoDeLosCerros.getTabla().buscarFila(datos.rivalRosario);
        assertEquals(1, local.getPartidosJugados());
        assertEquals(1, visitante.getPartidosJugados());
        assertEquals(1, local.getEmpatados());
        assertEquals(1, visitante.getEmpatados());
        assertEquals(1, local.getPuntos());
        assertEquals(1, visitante.getPuntos());
    }

    @Test
    void rechazarEquipoDeOtraCategoria() {
        // Dado: el torneo es de Mayores y el equipo es Sub-15
        int participantesAntes = datos.torneoDeLosCerros.getParticipantes().size();

        // Cuando: tratamos de inscribir ese equipo
        Participante resultado = datos.torneoDeLosCerros.inscribirEquipo(datos.futbolSub15,
                datos.futbolSub15.getJugadores());

        // Entonces: lo rechaza y no cambia los participantes
        assertNull(resultado);
        assertEquals(participantesAntes, datos.torneoDeLosCerros.getParticipantes().size());
    }

    @Test
    void rechazarNominaInsuficiente() {
        // Dado: hay siete inscritos, pero uno esta suspendido
        JugadorEquipo jugador = DatosPrueba.jugador(datos.futbolMayoresB, datos.sebastian);
        jugador.setSuspendidoProximoPartido(true);
        int participantesAntes = datos.torneoDeLosCerros.getParticipantes().size();

        // Cuando: mandamos la nomina al torneo que pide siete habilitados
        Participante resultado = datos.torneoDeLosCerros.inscribirEquipo(datos.futbolMayoresB,
                datos.futbolMayoresB.getJugadores());

        // Entonces: los seis que quedan no alcanzan
        assertNull(resultado);
        assertEquals(6, datos.futbolMayoresB.contarJugadoresHabilitados());
        assertEquals(participantesAntes, datos.torneoDeLosCerros.getParticipantes().size());
    }
}
