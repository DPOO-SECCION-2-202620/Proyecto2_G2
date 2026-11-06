package uniandes.dpoo.clubdeportivo.modelo.ventas;

import java.time.LocalDate;

public abstract class Venta {

	private LocalDate fecha;
	private String identificacionComprador;
	private boolean compradorSocio;
	private double subtotal;
	private double descuento;
	private double impuestos;
	private double total;
	private int puntosGenerados;
	private int puntosRedimidos;
	private boolean registrada;

	public Venta(LocalDate fecha, String identificacionComprador, boolean compradorSocio) {
		this.fecha = fecha;
		this.identificacionComprador = identificacionComprador;
		this.compradorSocio = compradorSocio;
		this.subtotal = 0;
		this.descuento = 0;
		this.impuestos = 0;
		this.total = 0;
		this.puntosGenerados = 0;
		this.puntosRedimidos = 0;
		this.registrada = false;
	}

	public abstract double getValorProductos();

	protected abstract double calcularImpuestos(double base);

	protected abstract double calcularPropina(double base);

	public boolean calcularTotales(double descuento, int puntosRedimidos) {
		// Los puntos se calculan sobre el valor de los productos despues de descuentos.
		double subtotalCalculado = redondear(getValorProductos());
		if (subtotalCalculado <= 0 || descuento < 0 || puntosRedimidos < 0) {
			return false;
		}
		if (descuento + puntosRedimidos > subtotalCalculado) {
			return false;
		}

		double base = redondear(subtotalCalculado - descuento - puntosRedimidos);
		this.subtotal = subtotalCalculado;
		this.descuento = redondear(descuento);
		this.puntosRedimidos = puntosRedimidos;
		this.impuestos = redondear(calcularImpuestos(base));
		this.total = redondear(base + impuestos + calcularPropina(base));
		this.puntosGenerados = (int) Math.floor(base * 0.02);
		return true;
	}

	protected double redondear(double valor) {
		return Math.round(valor * 100.0) / 100.0;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public String getIdentificacionComprador() {
		return identificacionComprador;
	}

	public boolean esCompradorSocio() {
		return compradorSocio;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public double getDescuento() {
		return descuento;
	}

	public double getImpuestos() {
		return impuestos;
	}

	public double getTotal() {
		return total;
	}

	public int getPuntosGenerados() {
		return puntosGenerados;
	}

	public int getPuntosRedimidos() {
		return puntosRedimidos;
	}

	public boolean estaRegistrada() {
		return registrada;
	}

	public void setRegistrada(boolean registrada) {
		this.registrada = registrada;
	}
}
