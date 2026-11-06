package uniandes.dpoo.clubdeportivo.modelo.equipos;

/**
 * Estadistica individual de un jugador en un partido. Es abstracta porque cada
 * deporte registra datos distintos (decision de modelado de la Entrega 1).
 */
public abstract class EstadisticaPartido {

	private JugadorEquipo jugador;

	public EstadisticaPartido(JugadorEquipo jugador) {
		this.jugador = jugador;
	}

	/** Resumen legible de la estadistica, para mostrar en consola. */
	public abstract String getResumen();

	public JugadorEquipo getJugador() {
		return jugador;
	}

	public void setJugador(JugadorEquipo jugador) {
		this.jugador = jugador;
	}

	@Override
	public String toString() {
		return jugador + ": " + getResumen();
	}
}
