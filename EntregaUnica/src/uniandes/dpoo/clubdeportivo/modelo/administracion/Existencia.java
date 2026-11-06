package uniandes.dpoo.clubdeportivo.modelo.administracion;

public class Existencia {

	private int cantidad;
	private UbicacionInventario ubicacion;

	public Existencia(int cantidad, UbicacionInventario ubicacion) {
		this.cantidad = cantidad;
		this.ubicacion = ubicacion;
	}

	public boolean agregarCantidad(int cantidad) {
		if (cantidad <= 0) {
			return false;
		}
		this.cantidad += cantidad;
		return true;
	}

	public boolean quitarCantidad(int cantidad) {
		if (cantidad <= 0 || cantidad > this.cantidad) {
			return false;
		}
		this.cantidad -= cantidad;
		return true;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		if (cantidad >= 0) {
			this.cantidad = cantidad;
		}
	}

	public UbicacionInventario getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(UbicacionInventario ubicacion) {
		this.ubicacion = ubicacion;
	}
}
