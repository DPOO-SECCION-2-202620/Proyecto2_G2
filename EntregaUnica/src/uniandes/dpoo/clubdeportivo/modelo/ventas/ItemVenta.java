package uniandes.dpoo.clubdeportivo.modelo.ventas;

public class ItemVenta {

	private ArticuloTienda articulo;
	private int cantidad;
	private double precioUnitario;

	public ItemVenta(ArticuloTienda articulo, int cantidad) {
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.precioUnitario = articulo.getPrecio();
	}

	public double getSubtotal() {
		return cantidad * precioUnitario;
	}

	public ArticuloTienda getArticulo() {
		return articulo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}
}
