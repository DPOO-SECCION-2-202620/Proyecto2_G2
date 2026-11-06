package uniandes.dpoo.clubdeportivo.modelo.competencias;

import java.time.LocalDate;

/**
 * Partido oficial de una competencia externa. A diferencia de un partido
 * interno, los rivales son participantes de otras instituciones y se registra
 * el escenario donde se jugo (regla RD09).
 */
public class PartidoCompetencia {

	private LocalDate fecha;
	private String escenario;
	private Participante local;
	private Participante visitante;
	private int marcadorLocal;
	private int marcadorVisitante;
	private boolean jugado;

	public PartidoCompetencia(LocalDate fecha, String escenario, Participante local, Participante visitante) {
		this.fecha = fecha;
		this.escenario = escenario;
		this.local = local;
		this.visitante = visitante;
		this.marcadorLocal = 0;
		this.marcadorVisitante = 0;
		this.jugado = false;
	}

	/** Registra el resultado final del partido. */
	public void registrarResultado(int marcadorLocal, int marcadorVisitante) {
		this.marcadorLocal = marcadorLocal;
		this.marcadorVisitante = marcadorVisitante;
		this.jugado = true;
	}

	/**
	 * Devuelve el participante que gano el partido, o null si quedo empatado.
	 *
	 * REGLA RD10: en baloncesto y voleibol no existen los empates.
	 */
	public Participante getGanador() {
		if (marcadorLocal == marcadorVisitante) {
			return null;
		}
		if (marcadorLocal > marcadorVisitante) {
			return local;
		}
		return visitante;
	}

	/** Indica si el partido termino en empate. */
	public boolean fueEmpate() {
		return marcadorLocal == marcadorVisitante;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getEscenario() {
		return escenario;
	}

	public void setEscenario(String escenario) {
		this.escenario = escenario;
	}

	public Participante getLocal() {
		return local;
	}

	public void setLocal(Participante local) {
		this.local = local;
	}

	public Participante getVisitante() {
		return visitante;
	}

	public void setVisitante(Participante visitante) {
		this.visitante = visitante;
	}

	public int getMarcadorLocal() {
		return marcadorLocal;
	}

	public int getMarcadorVisitante() {
		return marcadorVisitante;
	}

	public boolean isJugado() {
		return jugado;
	}

	public void setJugado(boolean jugado) {
		this.jugado = jugado;
	}

	@Override
	public String toString() {
		return fecha + "  " + local.getNombre() + " " + marcadorLocal + " - " + marcadorVisitante + " "
				+ visitante.getNombre() + "  (" + escenario + ")";
	}
}
