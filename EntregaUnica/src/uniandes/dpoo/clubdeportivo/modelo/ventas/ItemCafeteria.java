package uniandes.dpoo.clubdeportivo.modelo.ventas;

public class ItemCafeteria {

	private ProductoCafeteria producto;
	private int cantidad;
	private double precioUnitario;

	public ItemCafeteria(ProductoCafeteria producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = producto.getPrecio();
	}

	public double getSubtotal() {
		return cantidad * precioUnitario;
	}

	public ProductoCafeteria getProducto() {
		return producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}
}
