package uniandes.dpoo.clubdeportivo.pruebas.unitarias;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class EquipoTest {

	private DatosPrueba datos;

	@BeforeEach
	void prepararDatos() {
		datos = new DatosPrueba();
	}

	@Test
	void rechazarInscripcionConEquipoLleno() {
		// Dado: el equipo Sub-15 ya esta lleno
		int cantidadInicial = datos.futbolSub15.getJugadores().size();
		Socio nuevo = new Socio("S-021", "nuevo", "clave", "Nuevo jugador", 15);

		// Cuando: tratamos de agregar a otro jugador
		JugadorEquipo resultado = datos.futbolSub15.inscribirJugador(nuevo, "Defensa", 12);

        // Entonces: no lo inscribe y la lista queda igual
		assertNull(resultado);
		assertEquals(cantidadInicial, datos.futbolSub15.getJugadores().size());
	}

	@Test
	void rechazarCamisetaRepetida() {
		// Dado: el portero del equipo A ya tiene la camiseta 1
		int cantidadInicial = datos.futbolMayoresA.getJugadores().size();
		Socio nuevo = new Socio("S-022", "camiseta", "clave", "Jugador repetido", 20);

		// Cuando: alguien mas trata de usar ese mismo numero
		JugadorEquipo resultado = datos.futbolMayoresA.inscribirJugador(nuevo, "Defensa", 1);

		// Entonces: la inscripcion no se hace
		assertNull(resultado);
		assertEquals(cantidadInicial, datos.futbolMayoresA.getJugadores().size());
	}

	@Test
	void rechazarEdadFueraDeLaCategoria() {
		// Dado: tenemos un socio de 15 anos y un equipo de Mayores
		int cantidadInicial = datos.futbolMayoresA.getJugadores().size();
		Socio menor = new Socio("S-023", "menor", "clave", "Socio menor", 15);

		// Cuando: intentamos meterlo en ese equipo
		JugadorEquipo resultado = datos.futbolMayoresA.inscribirJugador(menor, "Volante", 15);

		// Entonces: no lo acepta y la nomina sigue igual
		assertNull(resultado);
		assertEquals(cantidadInicial, datos.futbolMayoresA.getJugadores().size());
	}

	@Test
	void contarHabilitadosExcluyeSuspendidos() {
		// Dado: Futbol B arranca con siete jugadores habilitados
		JugadorEquipo jugador = DatosPrueba.jugador(datos.futbolMayoresB, datos.sebastian);
		assertEquals(7, datos.futbolMayoresB.contarJugadoresHabilitados());

		// Cuando: suspendemos a uno para el siguiente partido
		jugador.setSuspendidoProximoPartido(true);

		// Entonces: sigue en la lista, pero ya no cuenta como habilitado
		assertTrue(jugador.estaSuspendido());
		assertEquals(7, datos.futbolMayoresB.getJugadores().size());
		assertEquals(6, datos.futbolMayoresB.contarJugadoresHabilitados());
	}
}
