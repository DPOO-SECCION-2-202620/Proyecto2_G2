package uniandes.dpoo.clubdeportivo.modelo.administracion;

import java.time.LocalDate;

public class SolicitudCambioTurno {

	private LocalDate fecha;
	private EstadoSolicitud estado;
	private Turno turnoActual;
	private Turno turnoPropuesto;

	public SolicitudCambioTurno(LocalDate fecha, Turno turnoActual, Turno turnoPropuesto) {
		this.fecha = fecha;
		this.turnoActual = turnoActual;
		this.turnoPropuesto = turnoPropuesto;
		this.estado = EstadoSolicitud.PENDIENTE;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public EstadoSolicitud getEstado() {
		return estado;
	}

	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}

	public Turno getTurnoActual() {
		return turnoActual;
	}

	public void setTurnoActual(Turno turnoActual) {
		this.turnoActual = turnoActual;
	}

	public Turno getTurnoPropuesto() {
		return turnoPropuesto;
	}

	@Override
	public String toString() {
		return "Cambio de " + turnoActual + " por " + turnoPropuesto + " (" + estado + ")";
	}
}
