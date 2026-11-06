package uniandes.dpoo.clubdeportivo.modelo.equipos;

/** Estadistica de un jugador en un partido de voleibol (regla RD05). */
public class EstadisticaVoleibol extends EstadisticaPartido {

	private int aces;
	private int bloqueos;

	public EstadisticaVoleibol(JugadorEquipo jugador, int aces, int bloqueos) {
		super(jugador);
		this.aces = aces;
		this.bloqueos = bloqueos;
	}

	@Override
	public String getResumen() {
		return aces + " aces, " + bloqueos + " bloqueos";
	}

	public int getAces() {
		return aces;
	}

	public void setAces(int aces) {
		this.aces = aces;
	}

	public int getBloqueos() {
		return bloqueos;
	}

	public void setBloqueos(int bloqueos) {
		this.bloqueos = bloqueos;
	}
}
