package uniandes.dpoo.clubdeportivo.modelo.equipos;

/** Estadistica de un jugador en un partido de futbol (regla RD05). */
public class EstadisticaFutbol extends EstadisticaPartido {

	private int goles;
	private int asistencias;
	private int tarjetasAmarillas;
	private int tarjetasRojas;

	public EstadisticaFutbol(JugadorEquipo jugador, int goles, int asistencias, int tarjetasAmarillas,
			int tarjetasRojas) {
		super(jugador);
		this.goles = goles;
		this.asistencias = asistencias;
		this.tarjetasAmarillas = tarjetasAmarillas;
		this.tarjetasRojas = tarjetasRojas;
	}

	/**
	 * Indica si lo ocurrido en este partido deja al jugador suspendido para el
	 * siguiente partido de su equipo.
	 *
	 * REGLA RD06: dos tarjetas amarillas o una roja.
	 */
	public boolean generaSuspension() {
		if (tarjetasAmarillas >= 2 || tarjetasRojas >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public String getResumen() {
		return goles + " goles, " + asistencias + " asistencias, " + tarjetasAmarillas + " amarillas, "
				+ tarjetasRojas + " rojas";
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}

	public int getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}

	public int getTarjetasAmarillas() {
		return tarjetasAmarillas;
	}

	public void setTarjetasAmarillas(int tarjetasAmarillas) {
		this.tarjetasAmarillas = tarjetasAmarillas;
	}

	public int getTarjetasRojas() {
		return tarjetasRojas;
	}

	public void setTarjetasRojas(int tarjetasRojas) {
		this.tarjetasRojas = tarjetasRojas;
	}
}
