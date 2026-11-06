package uniandes.dpoo.clubdeportivo.modelo.administracion;

public class Sugerencia {

	private String nombre;
	private String descripcion;
	private String tipo;
	private String identificacionEmpleado;
	private EstadoSolicitud estado;

	public Sugerencia(String nombre, String descripcion, String tipo, String identificacionEmpleado) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.identificacionEmpleado = identificacionEmpleado;
		this.estado = EstadoSolicitud.PENDIENTE;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public String getIdentificacionEmpleado() {
		return identificacionEmpleado;
	}

	public EstadoSolicitud getEstado() {
		return estado;
	}

	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return nombre + " (" + tipo + ") - " + estado;
	}
}
