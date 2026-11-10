package uniandes.dpoo.clubdeportivo.modelo.torneos;

import java.time.LocalDate;

public class EncuentroTorneo {

	private String idLocal;
	private String nombreLocal;
	private String idVisitante;
	private String nombreVisitante;
	private String escenario;
	private LocalDate fecha;
	private int marcadorLocal;
	private int marcadorVisitante;
	private boolean jugado;

	public EncuentroTorneo(ParticipanteTorneo local, ParticipanteTorneo visitante, String escenario,
			LocalDate fecha) {
		this.idLocal = local.getIdentificacion();
		this.nombreLocal = local.getNombre();
		this.idVisitante = visitante.getIdentificacion();
		this.nombreVisitante = visitante.getNombre();
		this.escenario = escenario;
		this.fecha = fecha;
		this.marcadorLocal = 0;
		this.marcadorVisitante = 0;
		this.jugado = false;
	}

	public boolean registrarResultado(int marcadorLocal, int marcadorVisitante) {
		if (marcadorLocal < 0 || marcadorVisitante < 0) {
			return false;
		}
		this.marcadorLocal = marcadorLocal;
		this.marcadorVisitante = marcadorVisitante;
		this.jugado = true;
		return true;
	}

	public boolean involucra(String identificacion) {
		return idLocal.equals(identificacion) || idVisitante.equals(identificacion);
	}

	public String getIdLocal() {
		return idLocal;
	}

	public String getIdVisitante() {
		return idVisitante;
	}

	public String getEscenario() {
		return escenario;
	}

	public LocalDate getFecha() {
		return fecha;
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

	@Override
	public String toString() {
		String marcador = jugado ? marcadorLocal + " - " + marcadorVisitante : "pendiente";
		return fecha + " | " + escenario + " | " + nombreLocal + " vs " + nombreVisitante + " | " + marcador;
	}
}