package uniandes.dpoo.clubdeportivo.modelo.ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaTienda extends Venta {

	private double iva;
	private String codigoDescuentoUsado;
	private List<ItemVenta> items;

	public VentaTienda(LocalDate fecha, String identificacionComprador, boolean compradorSocio) {
		super(fecha, identificacionComprador, compradorSocio);
		this.iva = 0.19;
		this.codigoDescuentoUsado = "";
		this.items = new ArrayList<ItemVenta>();
	}

	public boolean agregarArticulo(ArticuloTienda articulo, int cantidad) {
		if (estaRegistrada() || articulo == null || cantidad <= 0) {
			return false;
		}

		int cantidadSolicitada = cantidad;
		for (ItemVenta item : items) {
			if (item.getArticulo() == articulo) {
				cantidadSolicitada += item.getCantidad();
			}
		}
		if (cantidadSolicitada > articulo.getCantidadDisponible()) {
			return false;
		}

		items.add(new ItemVenta(articulo, cantidad));
		return true;
	}

	@Override
	public double getValorProductos() {
		double subtotal = 0;
		for (ItemVenta item : items) {
			subtotal += item.getSubtotal();
		}
		return subtotal;
	}

	@Override
	protected double calcularImpuestos(double base) {
		return base * iva;
	}

	@Override
	protected double calcularPropina(double base) {
		return 0;
	}

	public double getIva() {
		return iva;
	}

	public String getCodigoDescuentoUsado() {
		return codigoDescuentoUsado;
	}

	public void setCodigoDescuentoUsado(String codigoDescuentoUsado) {
		if (codigoDescuentoUsado == null) {
			this.codigoDescuentoUsado = "";
		} else {
			this.codigoDescuentoUsado = codigoDescuentoUsado;
		}
	}

	public List<ItemVenta> getItems() {
		return items;
	}
}
