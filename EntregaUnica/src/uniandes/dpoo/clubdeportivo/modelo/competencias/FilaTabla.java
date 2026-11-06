package uniandes.dpoo.clubdeportivo.modelo.competencias;

/**
 * Fila de la tabla de posiciones de una competencia con formato de liga:
 * acumula lo que ha hecho un participante a lo largo del torneo.
 */
public class FilaTabla {

	private Participante participante;
	private int partidosJugados;
	private int ganados;
	private int empatados;
	private int perdidos;
	private int puntos;
	private int diferenciaMarcador;

	public FilaTabla(Participante participante) {
		this.participante = participante;
		this.partidosJugados = 0;
		this.ganados = 0;
		this.empatados = 0;
		this.perdidos = 0;
		this.puntos = 0;
		this.diferenciaMarcador = 0;
	}

	/**
	 * Suma a esta fila lo que ocurrio en un partido.
	 *
	 * REGLA RD10: los puntos dependen del deporte. En futbol la victoria otorga
	 * 3 puntos, el empate 1 y la derrota 0. En baloncesto y voleibol no hay
	 * empates, siempre se juega hasta definir un ganador.
	 *
	 * Parametros: cuantos goles o puntos hizo este participante (aFavor) y
	 * cuantos recibio (enContra), y cuantos puntos vale una victoria y un
	 * empate en este deporte.
	 */
	public void acumularResultado(int aFavor, int enContra, int puntosPorVictoria, int puntosPorEmpate) {
		partidosJugados++;

		if (aFavor > enContra) {
			ganados++;
			puntos += puntosPorVictoria;
		} else if (aFavor == enContra) {
			empatados++;
			puntos += puntosPorEmpate;
		} else {
			perdidos++;
		}

		diferenciaMarcador += (aFavor - enContra);
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public int getPartidosJugados() {
		return partidosJugados;
	}

	public void setPartidosJugados(int partidosJugados) {
		this.partidosJugados = partidosJugados;
	}

	public int getGanados() {
		return ganados;
	}

	public void setGanados(int ganados) {
		this.ganados = ganados;
	}

	public int getEmpatados() {
		return empatados;
	}

	public void setEmpatados(int empatados) {
		this.empatados = empatados;
	}

	public int getPerdidos() {
		return perdidos;
	}

	public void setPerdidos(int perdidos) {
		this.perdidos = perdidos;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getDiferenciaMarcador() {
		return diferenciaMarcador;
	}

	public void setDiferenciaMarcador(int diferenciaMarcador) {
		this.diferenciaMarcador = diferenciaMarcador;
	}
}
