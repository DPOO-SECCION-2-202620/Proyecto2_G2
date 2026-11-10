package uniandes.dpoo.clubdeportivo.modelo.torneos;

import java.time.LocalDate;

public class TorneoRecreativo extends Torneo {

	private double valorBono;
	private BonoDescuento bonoEntregado;

	public TorneoRecreativo(String nombre, String deporteODisciplina, String categoriaONivel,
			TipoParticipante modalidad, int cupos, LocalDate fechaInicio, LocalDate fechaFin,
			LocalDate fechaLimitePreferencial, double valorBono) {
		super(nombre, deporteODisciplina, categoriaONivel, modalidad, cupos, fechaInicio, fechaFin,
				fechaLimitePreferencial);
		this.valorBono = valorBono;
		this.bonoEntregado = null;
	}

	@Override
	public boolean esCompetitivo() {
		return false;
	}

	@Override
	public double calcularCobro(ParticipanteTorneo participante) {
		return 0;
	}

	public BonoDescuento entregarBono(LocalDate fecha) {
		ParticipanteTorneo ganador = getGanador();
		if (getEstado(fecha) != EstadoTorneo.FINALIZADO || ganador == null || bonoEntregado != null) {
			return null;
		}
		String codigo = "BONO-" + getNombre() + "-" + ganador.getIdentificacion();
		bonoEntregado = new BonoDescuento(codigo, getNombre(), ganador.getIdentificacion(), valorBono);
		registrarEvento(fecha, "Bono de " + valorBono + " entregado a " + ganador.getNombre());
		return bonoEntregado;
	}

	public double getValorBono() {
		return valorBono;
	}

	public BonoDescuento getBonoEntregado() {
		return bonoEntregado;
	}
}