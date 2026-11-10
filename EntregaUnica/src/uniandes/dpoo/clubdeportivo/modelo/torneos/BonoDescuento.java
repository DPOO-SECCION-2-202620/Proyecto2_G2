package uniandes.dpoo.clubdeportivo.modelo.torneos;

public class BonoDescuento {

	private String codigo;
	private String nombreTorneo;
	private String identificacionGanador;
	private double valor;
	private boolean usado;

	public BonoDescuento(String codigo, String nombreTorneo, String identificacionGanador, double valor) {
		this.codigo = codigo;
		this.nombreTorneo = nombreTorneo;
		this.identificacionGanador = identificacionGanador;
		this.valor = valor;
		this.usado = false;
	}

	public boolean estaDisponible() {
		return usado == false;
	}

	public boolean consumir() {
		if (usado) {
			return false;
		}
		usado = true;
		return true;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNombreTorneo() {
		return nombreTorneo;
	}

	public String getIdentificacionGanador() {
		return identificacionGanador;
	}

	public double getValor() {
		return valor;
	}

	@Override
	public String toString() {
		return codigo + " por " + valor + (usado ? " (usado)" : " (disponible)");
	}
}