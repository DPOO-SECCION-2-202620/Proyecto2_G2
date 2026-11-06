package uniandes.dpoo.clubdeportivo.consola;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.clubdeportivo.modelo.competencias.CompetenciaLiga;
import uniandes.dpoo.clubdeportivo.modelo.competencias.FilaTabla;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Participante;
import uniandes.dpoo.clubdeportivo.modelo.competencias.PartidoCompetencia;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaFutbol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Partido;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

/**
 * Programa de prueba 2: competencias externas en formato de liga.
 *
 * Demuestra las reglas RD07 a RD10 del documento de analisis: creacion de una
 * competencia con su formato, inscripcion de equipos del club con sus
 * requisitos, participacion de rivales de otras instituciones, partidos
 * oficiales con escenario y construccion de la tabla de posiciones.
 */
public class ProgramaPrueba2 {

	public static void main(String[] args) {

		titulo("PROGRAMA DE PRUEBA 2 - COMPETENCIAS EXTERNAS");
		System.out.println("Reglas demostradas: RD07, RD08, RD09 y RD10");

		Deporte futbol = new Deporte("Futbol", 7);
		Deporte baloncesto = new Deporte("Baloncesto", 5);
		Categoria mayores = new Categoria("Mayores", 18, 99);
		Categoria sub17 = new Categoria("Sub-17", 15, 17);

		String[] posicionesFutbol = { "Arquero", "Defensa", "Defensa", "Volante", "Volante", "Delantero",
				"Delantero" };
		String[] posicionesBaloncesto = { "Base", "Escolta", "Alero", "Ala-pivot", "Pivot" };

		CompetenciaLiga torneo = new CompetenciaLiga("Torneo de los Cerros", "Universidad de los Andes", 2026, 5,
				futbol, mayores);

		// ------------------------------------------------------------------
		seccion("1. COMPETENCIA CREADA (RD07)");
		// ------------------------------------------------------------------
		System.out.println("  " + pad("Nombre", 22) + torneo.getNombre());
		System.out.println("  " + pad("Organiza", 22) + torneo.getEntidadOrganizadora());
		System.out.println("  " + pad("Temporada", 22) + torneo.getTemporada());
		System.out.println("  " + pad("Deporte", 22) + torneo.getDeporte().getNombre());
		System.out.println("  " + pad("Categoria habilitada", 22) + torneo.getCategoriaHabilitada().getNombre());
		System.out.println("  " + pad("Nomina minima", 22) + torneo.getNominaMinima() + " jugadores habilitados");
		System.out.println("  " + pad("Formato", 22) + torneo.getFormato());

		// ------------------------------------------------------------------
		seccion("2. INSCRIPCION ACEPTADA (RD08)");
		// ------------------------------------------------------------------
		Equipo halcones = new Equipo("Halcones", 7, futbol, mayores);
		for (int i = 0; i < posicionesFutbol.length; i++) {
			Socio socio = new Socio("H" + (i + 1), "h" + (i + 1), "clave", "Jugador Halcones " + (i + 1), 22);
			halcones.inscribirJugador(socio, posicionesFutbol[i], i + 1);
		}

		Participante participanteHalcones = torneo.inscribirEquipo(halcones, halcones.getJugadores());

		System.out.println("  Equipo: " + halcones.getNombre() + "  (" + halcones.getDeporte().getNombre() + ", "
				+ halcones.getCategoria().getNombre() + ")");
		System.out.println("     inscritos:       " + halcones.getJugadores().size());
		System.out.println("     habilitados:     " + halcones.contarJugadoresHabilitados());
		System.out.println("     minimo exigido:  " + torneo.getNominaMinima());
		if (participanteHalcones == null) {
			System.out.println("     resultado:       RECHAZADO");
		} else {
			System.out.println("     resultado:       ACEPTADO -> " + participanteHalcones);
		}

		// ------------------------------------------------------------------
		seccion("3. INSCRIPCIONES RECHAZADAS (RD08)");
		// ------------------------------------------------------------------

		Equipo juveniles = new Equipo("Juveniles", 7, futbol, sub17);
		for (int i = 0; i < 5; i++) {
			Socio socio = new Socio("J" + (i + 1), "j" + (i + 1), "clave", "Jugador Juveniles " + (i + 1), 16);
			juveniles.inscribirJugador(socio, posicionesFutbol[i], i + 11);
		}
		Participante inscripcionJuveniles = torneo.inscribirEquipo(juveniles, juveniles.getJugadores());
		System.out.println("\n  " + juveniles.getNombre() + "  (" + juveniles.getDeporte().getNombre() + ", "
				+ juveniles.getCategoria().getNombre() + ", " + juveniles.getJugadores().size() + " jugadores)");
		if (inscripcionJuveniles == null) {
			System.out.println("     RECHAZADO: la categoria del equipo no es la habilitada ("
					+ torneo.getCategoriaHabilitada().getNombre() + ")");
		} else {
			System.out.println("     ACEPTADO (inesperado)");
		}

		Equipo titanes = new Equipo("Titanes", 7, baloncesto, mayores);
		for (int i = 0; i < posicionesBaloncesto.length; i++) {
			Socio socio = new Socio("T" + (i + 1), "t" + (i + 1), "clave", "Jugador Titanes " + (i + 1), 23);
			titanes.inscribirJugador(socio, posicionesBaloncesto[i], i + 21);
		}
		Participante inscripcionTitanes = torneo.inscribirEquipo(titanes, titanes.getJugadores());
		System.out.println("\n  " + titanes.getNombre() + "  (" + titanes.getDeporte().getNombre() + ", "
				+ titanes.getCategoria().getNombre() + ", " + titanes.getJugadores().size() + " jugadores)");
		if (inscripcionTitanes == null) {
			System.out.println("     RECHAZADO: el deporte del equipo no coincide con el de la competencia ("
					+ torneo.getDeporte().getNombre() + ")");
		} else {
			System.out.println("     ACEPTADO (inesperado)");
		}

		Equipo incompletos = new Equipo("Incompletos", 7, futbol, mayores);
		for (int i = 0; i < 4; i++) {
			Socio socio = new Socio("I" + (i + 1), "i" + (i + 1), "clave", "Jugador Incompletos " + (i + 1), 22);
			incompletos.inscribirJugador(socio, posicionesFutbol[i], i + 31);
		}
		Participante inscripcionIncompletos = torneo.inscribirEquipo(incompletos, incompletos.getJugadores());
		System.out.println("\n  " + incompletos.getNombre() + "  (" + incompletos.getJugadores().size()
				+ " inscritos, " + incompletos.contarJugadoresHabilitados() + " habilitados)");
		if (inscripcionIncompletos == null) {
			System.out.println("     RECHAZADO: solo tiene " + incompletos.contarJugadoresHabilitados()
					+ " jugadores habilitados y la competencia exige " + torneo.getNominaMinima());
		} else {
			System.out.println("     ACEPTADO (inesperado)");
		}

		Equipo sancionados = new Equipo("Sancionados", 7, futbol, mayores);
		for (int i = 0; i < posicionesFutbol.length; i++) {
			Socio socio = new Socio("S" + (i + 1), "s" + (i + 1), "clave", "Jugador Sancionados " + (i + 1), 24);
			sancionados.inscribirJugador(socio, posicionesFutbol[i], i + 41);
		}

		Partido amistoso = new Partido(LocalDate.of(2026, 9, 30), sancionados, halcones);
		amistoso.registrarEstadistica(new EstadisticaFutbol(sancionados.getJugadores().get(0), 0, 0, 2, 0));
		amistoso.registrarEstadistica(new EstadisticaFutbol(sancionados.getJugadores().get(1), 0, 0, 0, 1));
		amistoso.registrarEstadistica(new EstadisticaFutbol(sancionados.getJugadores().get(2), 0, 0, 2, 0));

		int suspendidos = 0;
		for (JugadorEquipo jugador : sancionados.getJugadores()) {
			if (jugador.estaSuspendido() == true) {
				suspendidos++;
			}
		}

		Participante inscripcionSancionados = torneo.inscribirEquipo(sancionados, sancionados.getJugadores());
		System.out.println("\n  " + sancionados.getNombre() + "  (jugo un partido interno el "
				+ amistoso.getFecha() + " y acumulo tarjetas)");
		System.out.println("     inscritos:       " + sancionados.getJugadores().size());
		System.out.println("     suspendidos:     " + suspendidos);
		System.out.println("     habilitados:     " + sancionados.contarJugadoresHabilitados());
		if (inscripcionSancionados == null) {
			System.out.println("     RECHAZADO: las sanciones lo dejaron con "
					+ sancionados.contarJugadoresHabilitados() + " habilitados y se exigen "
					+ torneo.getNominaMinima());
		} else {
			System.out.println("     ACEPTADO (inesperado)");
		}

		// ------------------------------------------------------------------
		seccion("4. RIVALES EXTERNOS (RD09)");
		// ------------------------------------------------------------------
		Participante tigres = torneo.inscribirRival("Tigres", "Universidad Central");
		Participante condores = torneo.inscribirRival("Cóndores", "Universidad del Norte");

		System.out.println("  " + pad(tigres.getNombre(), 14) + tigres.getInstitucion());
		System.out.println("  " + pad(condores.getNombre(), 14) + condores.getInstitucion());

		torneo.getTabla().agregarParticipante(participanteHalcones);
		torneo.getTabla().agregarParticipante(tigres);
		torneo.getTabla().agregarParticipante(condores);
		System.out.println("\n  Participantes en la tabla: " + torneo.getTabla().getFilas().size());

		// ------------------------------------------------------------------
		seccion("5. PARTIDOS OFICIALES (RD09)");
		// ------------------------------------------------------------------
		List<PartidoCompetencia> oficiales = new ArrayList<PartidoCompetencia>();
		oficiales.add(new PartidoCompetencia(LocalDate.of(2026, 10, 10), "Cancha Norte", participanteHalcones,
				tigres));
		oficiales.add(new PartidoCompetencia(LocalDate.of(2026, 10, 17), "Estadio Central", condores,
				participanteHalcones));
		oficiales.add(new PartidoCompetencia(LocalDate.of(2026, 10, 24), "Cancha Sur", tigres, condores));

		int[][] marcadores = { { 2, 1 }, { 1, 1 }, { 0, 3 } };

		for (int i = 0; i < oficiales.size(); i++) {
			PartidoCompetencia partido = oficiales.get(i);
			torneo.programarPartido(partido);
			partido.registrarResultado(marcadores[i][0], marcadores[i][1]);
			torneo.procesarPartido(partido);

			System.out.println("\n  " + partido.getFecha() + "   " + partido.getEscenario());
			System.out.println("     " + pad(partido.getLocal().getNombre(), 12) + partido.getMarcadorLocal()
					+ " - " + partido.getMarcadorVisitante() + "   " + partido.getVisitante().getNombre());
		}

		// ------------------------------------------------------------------
		seccion("6. TABLA DE POSICIONES (RD10)");
		// ------------------------------------------------------------------
		System.out.println("  En " + torneo.getDeporte().getNombre() + " la victoria otorga "
				+ torneo.getPuntosPorVictoria() + " puntos y el empate " + torneo.getPuntosPorEmpate() + ".\n");

		System.out.println("  " + pad("PARTICIPANTE", 14) + pad("PJ", 4) + pad("G", 4) + pad("E", 4) + pad("P", 4)
				+ pad("DIF", 6) + "PTS");
		for (FilaTabla fila : torneo.getTabla().getFilasOrdenadas()) {
			System.out.println("  " + pad(fila.getParticipante().getNombre(), 14)
					+ pad(String.valueOf(fila.getPartidosJugados()), 4) + pad(String.valueOf(fila.getGanados()), 4)
					+ pad(String.valueOf(fila.getEmpatados()), 4) + pad(String.valueOf(fila.getPerdidos()), 4)
					+ pad(String.valueOf(fila.getDiferenciaMarcador()), 6) + fila.getPuntos());
		}

		// ------------------------------------------------------------------
		seccion("7. CIERRE");
		// ------------------------------------------------------------------
		FilaTabla lider = torneo.getTabla().getFilasOrdenadas().get(0);
		System.out.println("  Lider del " + torneo.getNombre() + ": " + lider.getParticipante().getNombre()
				+ " con " + lider.getPuntos() + " puntos y diferencia de " + lider.getDiferenciaMarcador() + ".");

		System.out.println("\n  Alcance no implementado en esta entrega:");
		System.out.println("   - el formato de ASCUN por grupos, fases y eliminacion directa");
		System.out.println("   - las estadisticas individuales acumuladas durante toda la competencia");
		System.out.println("   - las tarjetas y suspensiones acumuladas en los partidos oficiales");

		System.out.println();
		titulo("FIN DEL PROGRAMA DE PRUEBA 2");
	}

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
}
