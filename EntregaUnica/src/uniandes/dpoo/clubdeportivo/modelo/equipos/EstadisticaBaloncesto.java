package uniandes.dpoo.clubdeportivo.modelo.equipos;

/** Estadistica de un jugador en un partido de baloncesto (regla RD05). */
public class EstadisticaBaloncesto extends EstadisticaPartido {

	private int puntos;
	private int rebotes;

	public EstadisticaBaloncesto(JugadorEquipo jugador, int puntos, int rebotes) {
		super(jugador);
		this.puntos = puntos;
		this.rebotes = rebotes;
	}

	@Override
	public String getResumen() {
		return puntos + " puntos, " + rebotes + " rebotes";
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getRebotes() {
		return rebotes;
	}

	public void setRebotes(int rebotes) {
		this.rebotes = rebotes;
	}
}
