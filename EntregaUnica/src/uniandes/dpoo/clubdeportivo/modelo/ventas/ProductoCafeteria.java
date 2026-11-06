package uniandes.dpoo.clubdeportivo.modelo.ventas;

public abstract class ProductoCafeteria {

	private String nombre;
	private double precio;
	private int cantidadDisponible;

	public ProductoCafeteria(String nombre, double precio, int cantidadDisponible) {
		this.nombre = nombre;
		this.precio = precio;
		this.cantidadDisponible = cantidadDisponible;
	}

	public boolean reabastecer(int cantidad) {
		if (cantidad <= 0) {
			return false;
		}
		cantidadDisponible += cantidad;
		return true;
	}

	public boolean descontar(int cantidad) {
		if (cantidad <= 0 || cantidad > cantidadDisponible) {
			return false;
		}
		cantidadDisponible -= cantidad;
		return true;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
