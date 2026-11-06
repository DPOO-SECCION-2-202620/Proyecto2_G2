package uniandes.dpoo.clubdeportivo.consola;

import java.time.LocalDate;

import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaBaloncesto;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaFutbol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaPartido;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaVoleibol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Partido;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

/**
 * Programa de prueba 1: deportes de conjunto.
 *
 * Demuestra las reglas RD02 a RD06 del documento de analisis: creacion de
 * equipos por deporte y categoria, inscripcion de jugadores con sus
 * validaciones, condiciones para que un partido pueda disputarse, registro de
 * estadisticas por deporte y suspension por acumulacion de tarjetas.
 *
 * No solicita informacion al usuario: construye su propio escenario de datos y
 * muestra por consola el resultado de cada operacion y la razon de cada rechazo.
 */
public class ProgramaPrueba1 {

	public static void main(String[] args) {

		titulo("PROGRAMA DE PRUEBA 1 - DEPORTES DE CONJUNTO");
		System.out.println("Reglas demostradas: RD02, RD03, RD04, RD05 y RD06");

		// ------------------------------------------------------------------
		seccion("1. DEPORTES Y CATEGORIAS");
		// ------------------------------------------------------------------
		Deporte futbol = new Deporte("Futbol", 7);
		Deporte baloncesto = new Deporte("Baloncesto", 5);
		Deporte voleibol = new Deporte("Voleibol", 6);
		Categoria sub17 = new Categoria("Sub-17", 15, 17);
		Categoria mayores = new Categoria("Mayores", 18, 99);

		Deporte[] deportes = { futbol, baloncesto, voleibol };
		for (Deporte deporte : deportes) {
			System.out.println("  " + pad(deporte.getNombre(), 14) + "minimo de jugadores habilitados: "
					+ deporte.getMinimoJugadoresHabilitados());
		}
		Categoria[] categorias = { sub17, mayores };
		for (Categoria categoria : categorias) {
			System.out.println("  " + pad(categoria.getNombre(), 14) + "edades de " + categoria.getEdadMinima()
					+ " a " + categoria.getEdadMaxima() + " anios");
		}

		// ------------------------------------------------------------------
		seccion("2. EQUIPOS CREADOS");
		// ------------------------------------------------------------------
		Equipo futbolLocal = new Equipo("Halcones", 8, futbol, sub17);
		Equipo futbolVisitante = new Equipo("Leones", 8, futbol, sub17);
		Equipo baloncestoLocal = new Equipo("Titanes", 5, baloncesto, mayores);
		Equipo baloncestoVisitante = new Equipo("Centauros", 5, baloncesto, mayores);
		Equipo voleibolLocal = new Equipo("Delfines", 7, voleibol, mayores);
		Equipo voleibolVisitante = new Equipo("Panteras", 7, voleibol, mayores);

		Equipo[] equipos = { futbolLocal, futbolVisitante, baloncestoLocal, baloncestoVisitante, voleibolLocal,
				voleibolVisitante };
		for (Equipo equipo : equipos) {
			System.out.println("  " + pad(equipo.getNombre(), 14) + pad(equipo.getDeporte().getNombre(), 13)
					+ pad(equipo.getCategoria().getNombre(), 10) + "cupo maximo: " + equipo.getCupoMaximo());
		}

		// ------------------------------------------------------------------
		seccion("3. INSCRIPCION DE JUGADORES (RD02, RD03)");
		// ------------------------------------------------------------------
		String[] posicionesFutbol = { "Arquero", "Defensa", "Defensa", "Volante", "Volante", "Delantero",
				"Delantero" };
		String[] posicionesBaloncesto = { "Base", "Escolta", "Alero", "Ala-pivot", "Pivot" };
		String[] posicionesVoleibol = { "Armador", "Opuesto", "Central", "Central", "Punta", "Libero" };

		for (int i = 0; i < posicionesFutbol.length; i++) {
			Socio local = new Socio("FL" + (i + 1), "fl" + (i + 1), "clave", "Jugador Halcones " + (i + 1), 16);
			Socio visitante = new Socio("FV" + (i + 1), "fv" + (i + 1), "clave", "Jugador Leones " + (i + 1), 16);
			futbolLocal.inscribirJugador(local, posicionesFutbol[i], i + 1);
			futbolVisitante.inscribirJugador(visitante, posicionesFutbol[i], i + 11);
		}
		for (int i = 0; i < posicionesBaloncesto.length; i++) {
			Socio local = new Socio("BL" + (i + 1), "bl" + (i + 1), "clave", "Jugador Titanes " + (i + 1), 22);
			Socio visitante = new Socio("BV" + (i + 1), "bv" + (i + 1), "clave", "Jugador Centauros " + (i + 1), 24);
			baloncestoLocal.inscribirJugador(local, posicionesBaloncesto[i], i + 4);
			baloncestoVisitante.inscribirJugador(visitante, posicionesBaloncesto[i], i + 14);
		}
		for (int i = 0; i < posicionesVoleibol.length; i++) {
			Socio local = new Socio("VL" + (i + 1), "vl" + (i + 1), "clave", "Jugador Delfines " + (i + 1), 21);
			Socio visitante = new Socio("VV" + (i + 1), "vv" + (i + 1), "clave", "Jugador Panteras " + (i + 1), 23);
			voleibolLocal.inscribirJugador(local, posicionesVoleibol[i], i + 2);
			voleibolVisitante.inscribirJugador(visitante, posicionesVoleibol[i], i + 12);
		}

		for (Equipo equipo : equipos) {
			System.out.println("\n  " + equipo.getNombre() + " (" + equipo.getJugadores().size() + " de "
					+ equipo.getCupoMaximo() + " cupos usados)");
			for (JugadorEquipo jugador : equipo.getJugadores()) {
				System.out.println("     #" + pad(String.valueOf(jugador.getNumeroCamiseta()), 4)
						+ pad(jugador.getPosicion(), 12) + jugador.getJugador().getNombre());
			}
		}

		// ------------------------------------------------------------------
		seccion("4. INSCRIPCIONES QUE EL SISTEMA RECHAZA (RD02, RD03)");
		// ------------------------------------------------------------------
		Socio menor = new Socio("R1", "r1", "clave", "Socio menor", 12);
		mostrarInscripcion("Socio de 12 anios en Halcones (Sub-17)",
				futbolLocal.inscribirJugador(menor, "Volante", 30),
				"la edad no corresponde a la categoria Sub-17 (15 a 17)");

		Socio adicional = new Socio("R2", "r2", "clave", "Socio adicional", 25);
		mostrarInscripcion("Jugador numero 6 en Titanes (cupo 5)",
				baloncestoLocal.inscribirJugador(adicional, "Base", 20),
				"el equipo ya alcanzo su cupo maximo de 5 jugadores");

		Socio camisetaRepetida = new Socio("R3", "r3", "clave", "Socio camiseta repetida", 16);
		mostrarInscripcion("Jugador con camiseta #1 en Halcones",
				futbolLocal.inscribirJugador(camisetaRepetida, "Defensa", 1),
				"el numero de camiseta 1 ya pertenece a otro jugador del equipo");

		// ------------------------------------------------------------------
		seccion("5. PARTIDOS QUE SI PUEDEN DISPUTARSE (RD04)");
		// ------------------------------------------------------------------
		Partido partidoFutbol = new Partido(LocalDate.of(2026, 10, 20), futbolLocal, futbolVisitante);
		Partido partidoBaloncesto = new Partido(LocalDate.of(2026, 10, 21), baloncestoLocal, baloncestoVisitante);
		Partido partidoVoleibol = new Partido(LocalDate.of(2026, 10, 22), voleibolLocal, voleibolVisitante);

		mostrarPartido(partidoFutbol);
		mostrarPartido(partidoBaloncesto);
		mostrarPartido(partidoVoleibol);

		// ------------------------------------------------------------------
		seccion("6. PARTIDOS QUE EL SISTEMA RECHAZA (RD04)");
		// ------------------------------------------------------------------
		Equipo futbolMayores = new Equipo("Veteranos", 8, futbol, mayores);
		mostrarPartidoRechazado(new Partido(LocalDate.of(2026, 10, 23), futbolLocal, futbolMayores),
				"los equipos pertenecen a categorias distintas (Sub-17 y Mayores)");

		mostrarPartidoRechazado(new Partido(LocalDate.of(2026, 10, 24), futbolLocal, baloncestoLocal),
				"los equipos pertenecen a deportes distintos (Futbol y Baloncesto)");

		Equipo futbolIncompleto = new Equipo("Condors", 8, futbol, sub17);
		futbolIncompleto.inscribirJugador(new Socio("R4", "r4", "clave", "Unico jugador", 16), "Arquero", 1);
		mostrarPartidoRechazado(new Partido(LocalDate.of(2026, 10, 25), futbolLocal, futbolIncompleto),
				"Condors solo tiene 1 jugador habilitado y futbol exige 7");

		// ------------------------------------------------------------------
		seccion("7. REGISTRO DE ESTADISTICAS Y MARCADORES (RD05)");
		// ------------------------------------------------------------------
		partidoFutbol.setMarcador("2 - 1");
		partidoBaloncesto.setMarcador("78 - 71");
		partidoVoleibol.setMarcador("3 - 2");

		partidoFutbol.registrarEstadistica(new EstadisticaFutbol(futbolLocal.getJugadores().get(0), 1, 0, 2, 0));
		partidoFutbol.registrarEstadistica(new EstadisticaFutbol(futbolVisitante.getJugadores().get(5), 1, 1, 0, 0));
		partidoBaloncesto.registrarEstadistica(new EstadisticaBaloncesto(baloncestoLocal.getJugadores().get(1), 24, 7));
		partidoVoleibol.registrarEstadistica(new EstadisticaVoleibol(voleibolLocal.getJugadores().get(2), 5, 4));

		Partido[] partidos = { partidoFutbol, partidoBaloncesto, partidoVoleibol };
		for (Partido partido : partidos) {
			System.out.println("\n  " + partido.getEquipoLocal().getNombre() + " vs "
					+ partido.getEquipoVisitante().getNombre() + "   marcador final: " + partido.getMarcador());
			for (EstadisticaPartido estadistica : partido.getEstadisticas()) {
				System.out.println("     " + estadistica.getJugador().getJugador().getNombre() + " -> "
						+ estadistica.getResumen());
			}
		}

		// ------------------------------------------------------------------
		seccion("8. SANCIONES POR ACUMULACION DE TARJETAS (RD06)");
		// ------------------------------------------------------------------
		boolean huboSanciones = false;
		for (Equipo equipo : equipos) {
			for (JugadorEquipo jugador : equipo.getJugadores()) {
				if (jugador.estaSuspendido()) {
					System.out.println("  SUSPENDIDO  " + jugador.getJugador().getNombre() + " (#"
							+ jugador.getNumeroCamiseta() + ", " + equipo.getNombre() + ") acumulo "
							+ jugador.getAmarillasAcumuladas()
							+ " amarillas y no puede jugar el proximo partido de su equipo");
					huboSanciones = true;
				}
			}
		}
		if (huboSanciones == false) {
			System.out.println("  No hay jugadores suspendidos.");
		}

		// ------------------------------------------------------------------
		seccion("9. ESTADO FINAL DE LAS NOMINAS");
		// ------------------------------------------------------------------
		System.out.println("  " + pad("EQUIPO", 14) + pad("INSCRITOS", 11) + pad("HABILITADOS", 14) + "PUEDE JUGAR");
		for (Equipo equipo : equipos) {
			int habilitados = equipo.contarJugadoresHabilitados();
			int minimo = equipo.getDeporte().getMinimoJugadoresHabilitados();
			System.out.println("  " + pad(equipo.getNombre(), 14)
					+ pad(String.valueOf(equipo.getJugadores().size()), 11) + pad(habilitados + " de " + minimo, 14)
					+ (habilitados >= minimo ? "si" : "NO"));
		}

		System.out.println();
		titulo("FIN DEL PROGRAMA DE PRUEBA 1");
	}

	// ----------------------------------------------------------------------
	// Metodos auxiliares de presentacion
	// ----------------------------------------------------------------------

	/** Imprime un titulo enmarcado. */
	private static void titulo(String texto) {
		System.out.println("======================================================================");
		System.out.println("  " + texto);
		System.out.println("======================================================================");
	}

	/** Imprime el encabezado de una seccion. */
	private static void seccion(String texto) {
		System.out.println("\n----------------------------------------------------------------------");
		System.out.println(texto);
		System.out.println("----------------------------------------------------------------------");
	}

	/** Completa un texto con espacios para que las columnas queden alineadas. */
	private static String pad(String texto, int ancho) {
		StringBuilder resultado = new StringBuilder(texto);
		while (resultado.length() < ancho) {
			resultado.append(" ");
		}
		return resultado.toString();
	}

	/** Muestra si una inscripcion fue aceptada o rechazada, con su razon. */
	private static void mostrarInscripcion(String intento, JugadorEquipo resultado, String razonEsperada) {
		if (resultado == null) {
			System.out.println("  RECHAZADO   " + intento);
			System.out.println("              razon: " + razonEsperada);
		} else {
			System.out.println("  ACEPTADO    " + intento + " -> " + resultado);
		}
	}

	/** Muestra un partido valido y por que cumple el minimo de jugadores. */
	private static void mostrarPartido(Partido partido) {
		Equipo local = partido.getEquipoLocal();
		Equipo visitante = partido.getEquipoVisitante();
		int minimo = local.getDeporte().getMinimoJugadoresHabilitados();
		System.out.println("\n  " + partido.getFecha() + "  " + local.getNombre() + " vs " + visitante.getNombre()
				+ "  (" + local.getDeporte().getNombre() + ", " + local.getCategoria().getNombre() + ")");
		System.out.println("     habilitados: " + local.getNombre() + " " + local.contarJugadoresHabilitados() + ", "
				+ visitante.getNombre() + " " + visitante.contarJugadoresHabilitados() + "   minimo exigido: " + minimo);
		System.out.println("     puede jugarse: " + (partido.puedeJugarse() ? "SI" : "NO"));
	}

	/** Muestra un partido que el sistema rechaza, con la razon. */
	private static void mostrarPartidoRechazado(Partido partido, String razon) {
		System.out.println("\n  " + (partido.puedeJugarse() ? "ACEPTADO (inesperado)  " : "RECHAZADO   ")
				+ partido.getEquipoLocal().getNombre() + " vs " + partido.getEquipoVisitante().getNombre());
		System.out.println("              razon: " + razon);
	}
}
