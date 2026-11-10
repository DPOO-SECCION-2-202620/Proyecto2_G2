package uniandes.dpoo.clubdeportivo.modelo.torneos;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteTorneo {

	private String identificacion;
	private String nombre;
	private TipoParticipante tipo;
	private Vinculacion vinculacion;
	private List<String> integrantes;
	private double puntajeDesempeno;
	private double montoPagado;

	public ParticipanteTorneo(String identificacion, String nombre, TipoParticipante tipo, Vinculacion vinculacion,
			List<String> integrantes, double puntajeDesempeno) {
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.tipo = tipo;
		this.vinculacion = vinculacion;
		this.integrantes = new ArrayList<String>();
		if (integrantes != null) {
			this.integrantes.addAll(integrantes);
		}
		this.puntajeDesempeno = puntajeDesempeno;
		this.montoPagado = 0;
	}

	public boolean estaCompleto() {
		if (tipo == TipoParticipante.JUGADOR) {
			return integrantes.size() == 1;
		}
		if (tipo == TipoParticipante.PAREJA) {
			return integrantes.size() == 2;
		}
		return integrantes.isEmpty() == false;
	}

	public boolean esEmpleado() {
		return vinculacion == Vinculacion.EMPLEADO;
	}

	public void registrarPago(double monto) {
		montoPagado = monto;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public TipoParticipante getTipo() {
		return tipo;
	}

	public Vinculacion getVinculacion() {
		return vinculacion;
	}

	public List<String> getIntegrantes() {
		return new ArrayList<String>(integrantes);
	}

	public double getPuntajeDesempeno() {
		return puntajeDesempeno;
	}

	public double getMontoPagado() {
		return montoPagado;
	}

	@Override
	public String toString() {
		return nombre + " (" + tipo + ", " + vinculacion + ")";
	}
}