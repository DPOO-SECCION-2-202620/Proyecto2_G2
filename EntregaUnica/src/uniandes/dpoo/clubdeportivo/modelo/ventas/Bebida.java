package uniandes.dpoo.clubdeportivo.modelo.ventas;

public class Bebida extends ProductoCafeteria {

	private boolean caliente;

	public Bebida(String nombre, double precio, int cantidadDisponible, boolean caliente) {
		super(nombre, precio, cantidadDisponible);
		this.caliente = caliente;
	}

	public boolean esCaliente() {
		return caliente;
	}

	public void setCaliente(boolean caliente) {
		this.caliente = caliente;
	}
}
