package uniandes.dpoo.clubdeportivo.modelo.ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaCafeteria extends Venta {

	private double impuestoConsumo;
	private double propina;
	private List<ItemCafeteria> items;

	public VentaCafeteria(LocalDate fecha, String identificacionComprador, boolean compradorSocio) {
		super(fecha, identificacionComprador, compradorSocio);
		this.impuestoConsumo = 0.08;
		this.propina = 0.10;
		this.items = new ArrayList<ItemCafeteria>();
	}

	public boolean agregarProducto(ProductoCafeteria producto, int cantidad, boolean alergenosInformados,
			boolean instalacionRestringida) {
		if (estaRegistrada() || producto == null || cantidad <= 0) {
			return false;
		}
		if (producto instanceof Snack && alergenosInformados == false) {
			return false;
		}
		if (producto instanceof Bebida && esCompradorSocio() && instalacionRestringida) {
			Bebida bebida = (Bebida) producto;
			if (bebida.esCaliente()) {
				return false;
			}
		}

		int cantidadSolicitada = cantidad;
		for (ItemCafeteria item : items) {
			if (item.getProducto() == producto) {
				cantidadSolicitada += item.getCantidad();
			}
		}
		if (cantidadSolicitada > producto.getCantidadDisponible()) {
			return false;
		}

		items.add(new ItemCafeteria(producto, cantidad));
		return true;
	}

	public boolean puedeUsarInstalacion(boolean instalacionRestringida) {
		if (instalacionRestringida == false) {
			return true;
		}
		for (ItemCafeteria item : items) {
			if (item.getProducto() instanceof Bebida) {
				Bebida bebida = (Bebida) item.getProducto();
				if (bebida.esCaliente()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public double getValorProductos() {
		double subtotal = 0;
		for (ItemCafeteria item : items) {
			subtotal += item.getSubtotal();
		}
		return subtotal;
	}

	@Override
	protected double calcularImpuestos(double base) {
		return base * impuestoConsumo;
	}

	@Override
	protected double calcularPropina(double base) {
		return base * propina;
	}

	public double getImpuestoConsumo() {
		return impuestoConsumo;
	}

	public double getPropina() {
		return propina;
	}

	public boolean setPropina(double propina) {
		if (propina < 0 || propina > 1) {
			return false;
		}
		this.propina = propina;
		return true;
	}

	public double getValorPropina() {
		double base = getSubtotal() - getDescuento() - getPuntosRedimidos();
		return Math.round(base * propina * 100.0) / 100.0;
	}

	public List<ItemCafeteria> getItems() {
		return items;
	}
}
