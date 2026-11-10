package uniandes.dpoo.clubdeportivo.modelo.torneos;

public class ParticipanteTorneo {

	private String identificacion;
	private String nombre;
	private TipoParticipante tipo;
	private boolean empleado;

	public ParticipanteTorneo(String identificacion, String nombre, TipoParticipante tipo, boolean empleado) {
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.tipo = tipo;
		this.empleado = empleado;
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

	public boolean esEmpleado() {
		return empleado;
	}

	@Override
	public String toString() {
		return nombre + " (" + tipo + ")";
	}
}