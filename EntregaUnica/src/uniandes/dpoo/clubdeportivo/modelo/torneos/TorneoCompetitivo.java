package uniandes.dpoo.clubdeportivo.modelo.torneos;

import java.time.LocalDate;

public class TorneoCompetitivo extends Torneo {

	public static final double PORCENTAJE_PREMIO = 0.70;

	private double tarifa;
	private String responsablePago;
	private boolean premioPagado;

	public TorneoCompetitivo(String nombre, String deporteODisciplina, String categoriaONivel,
			TipoParticipante modalidad, int cupos, LocalDate fechaInicio, LocalDate fechaFin,
			LocalDate fechaLimitePreferencial, double tarifa) {
		super(nombre, deporteODisciplina, categoriaONivel, modalidad, cupos, fechaInicio, fechaFin,
				fechaLimitePreferencial);
		this.tarifa = tarifa;
		this.responsablePago = null;
		this.premioPagado = false;
	}

	@Override
	public boolean esCompetitivo() {
		return true;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && tarifa > 0;
	}

	@Override
	public double calcularCobro(ParticipanteTorneo participante) {
		if (participante.esEmpleado()) {
			return 0;
		}
		return tarifa;
	}

	public double getTotalRecaudado() {
		double total = 0;
		for (ParticipanteTorneo participante : getInscritos()) {
			total += participante.getMontoPagado();
		}
		return total;
	}

	public double calcularPremio() {
		ParticipanteTorneo ganador = getGanador();
		if (ganador == null || ganador.esEmpleado()) {
			return 0;
		}
		return getTotalRecaudado() * PORCENTAJE_PREMIO;
	}

	public boolean registrarPagoPremio(String identificacionResponsable, LocalDate fecha) {
		ParticipanteTorneo ganador = getGanador();
		if (getEstado(fecha) != EstadoTorneo.FINALIZADO || ganador == null || premioPagado) {
			return false;
		}
		if (ganador.esEmpleado()) {
			registrarEvento(fecha, "Premio en efectivo rechazado: el ganador es empleado");
			return false;
		}
		if (identificacionResponsable == null || identificacionResponsable.isBlank()) {
			return false;
		}
		if (identificacionResponsable.equals(ganador.getIdentificacion())
				|| ganador.getIntegrantes().contains(identificacionResponsable)) {
			return false;
		}
		responsablePago = identificacionResponsable;
		premioPagado = true;
		registrarEvento(fecha, "Premio de " + calcularPremio() + " pagado a " + ganador.getNombre()
				+ ", registrado por " + identificacionResponsable);
		return true;
	}

	public double getTarifa() {
		return tarifa;
	}

	public String getResponsablePago() {
		return responsablePago;
	}

	public boolean isPremioPagado() {
		return premioPagado;
	}
}