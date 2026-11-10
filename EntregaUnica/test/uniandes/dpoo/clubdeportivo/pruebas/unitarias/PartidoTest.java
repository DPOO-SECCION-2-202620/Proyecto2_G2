package uniandes.dpoo.clubdeportivo.pruebas.unitarias;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaBaloncesto;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaFutbol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Partido;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class PartidoTest {

	private DatosPrueba datos;

	@BeforeEach
	void prepararDatos() {
		datos = new DatosPrueba();
	}

	@Test
	void noSeJuegaEntreDeportesOCategoriasDistintas() {
		// Dado: armamos partidos con equipos que no combinan
		Partido deportesDistintos = new Partido(LocalDate.of(2026, 11, 9), datos.futbolMayoresA,
				datos.baloncestoMayores);
		Partido categoriasDistintas = new Partido(LocalDate.of(2026, 11, 9), datos.voleibolMayores,
				datos.voleibolSub17);

		// Cuando: preguntamos si los dos partidos se pueden jugar
		boolean seJuegaConOtroDeporte = deportesDistintos.puedeJugarse();
		boolean seJuegaConOtraCategoria = categoriasDistintas.puedeJugarse();

		// Entonces: los dos deben dar falso
		assertFalse(seJuegaConOtroDeporte);
		assertFalse(seJuegaConOtraCategoria);
	}

	@Test
	void noSeJuegaSinElMinimoDeHabilitados() {
		// Dado: Futbol B queda con seis habilitados por una suspension
		JugadorEquipo jugador = DatosPrueba.jugador(datos.futbolMayoresB, datos.sebastian);
		jugador.setSuspendidoProximoPartido(true);
		Partido partido = new Partido(LocalDate.of(2026, 11, 9), datos.futbolMayoresA, datos.futbolMayoresB);

		// Cuando: revisamos si el partido puede hacerse
		boolean puedeJugarse = partido.puedeJugarse();

		// Entonces: no llega al minimo de siete
		assertFalse(puedeJugarse);
		assertEquals(6, datos.futbolMayoresB.contarJugadoresHabilitados());
	}

	@Test
	void ignorarEstadisticaDeOtroDeporte() {
		// Dado: ya hay varias estadisticas en un partido de futbol
		int cantidadInicial = datos.partidoInterno.getEstadisticas().size();
		JugadorEquipo jugador = DatosPrueba.jugador(datos.futbolMayoresA, datos.juanPablo);
		EstadisticaBaloncesto estadistica = new EstadisticaBaloncesto(jugador, 20, 6);

		// Cuando: tratamos de meter una estadistica de baloncesto
		datos.partidoInterno.registrarEstadistica(estadistica);

		// Entonces: el partido simplemente no la guarda
		assertEquals(cantidadInicial, datos.partidoInterno.getEstadisticas().size());
	}

	@Test
	void rojaSuspendeAlJugador() {
		// Dado: Mariana juega en el partido y todavia no esta suspendida
		JugadorEquipo jugador = DatosPrueba.jugador(datos.futbolMayoresA, datos.mariana);
		int cantidadInicial = datos.partidoInterno.getEstadisticas().size();
		assertFalse(jugador.estaSuspendido());

		// Cuando: le registramos una roja
		EstadisticaFutbol estadistica = new EstadisticaFutbol(jugador, 0, 0, 0, 1);
		datos.partidoInterno.registrarEstadistica(estadistica);

		// Entonces: se guarda la estadistica y ella queda suspendida
		assertEquals(cantidadInicial + 1, datos.partidoInterno.getEstadisticas().size());
		assertTrue(jugador.estaSuspendido());
	}
}
