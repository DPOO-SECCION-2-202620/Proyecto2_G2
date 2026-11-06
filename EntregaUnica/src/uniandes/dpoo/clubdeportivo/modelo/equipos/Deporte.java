package uniandes.dpoo.clubdeportivo.modelo.equipos;

/**
 * Deporte de conjunto que ofrece el club: futbol, baloncesto o voleibol.
 * Cada deporte define cuantos jugadores habilitados necesita un equipo para
 * poder disputar un partido (regla RD04).
 */
public class Deporte {

	private String nombre;
	private int minimoJugadoresHabilitados;

	public Deporte(String nombre, int minimoJugadoresHabilitados) {
		this.nombre = nombre;
		this.minimoJugadoresHabilitados = minimoJugadoresHabilitados;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getMinimoJugadoresHabilitados() {
		return minimoJugadoresHabilitados;
	}

	public void setMinimoJugadoresHabilitados(int minimoJugadoresHabilitados) {
		this.minimoJugadoresHabilitados = minimoJugadoresHabilitados;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
