package uniandes.dpoo.clubdeportivo.modelo.administracion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.clubdeportivo.modelo.torneos.BonoDescuento;

import uniandes.dpoo.clubdeportivo.modelo.ventas.ArticuloTienda;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ItemCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ItemVenta;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ProductoCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Venta;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;

public class AdministracionComercial {

	private List<ArticuloTienda> articulos;
	private List<ProductoCafeteria> productosCafeteria;
	private List<Venta> ventas;
	private List<Turno> turnos;
	private List<SolicitudCambioTurno> solicitudesCambio;
	private List<Sugerencia> sugerencias;
	private Map<String, Integer> puntosFidelidad;
	private Map<String, String> codigosDescuento;
	private Map<LocalDate, Double> mensualidades;
	private UbicacionInventario ubicacionTienda;
	private LocalTime horaInicioAtencion;
	private LocalTime horaFinAtencion;

	public AdministracionComercial(UbicacionInventario ubicacionTienda, LocalTime horaInicioAtencion,
			LocalTime horaFinAtencion) {
		this.articulos = new ArrayList<ArticuloTienda>();
		this.productosCafeteria = new ArrayList<ProductoCafeteria>();
		this.ventas = new ArrayList<Venta>();
		this.turnos = new ArrayList<Turno>();
		this.solicitudesCambio = new ArrayList<SolicitudCambioTurno>();
		this.sugerencias = new ArrayList<Sugerencia>();
		this.puntosFidelidad = new HashMap<String, Integer>();
		this.codigosDescuento = new HashMap<String, String>();
		this.mensualidades = new HashMap<LocalDate, Double>();
		this.ubicacionTienda = ubicacionTienda;
		this.horaInicioAtencion = horaInicioAtencion;
		this.horaFinAtencion = horaFinAtencion;
	}

	public void prepararDatos() {
		if (articulos == null) articulos = new ArrayList<ArticuloTienda>();
		if (productosCafeteria == null) productosCafeteria = new ArrayList<ProductoCafeteria>();
		if (ventas == null) ventas = new ArrayList<Venta>();
		if (turnos == null) turnos = new ArrayList<Turno>();
		if (solicitudesCambio == null) solicitudesCambio = new ArrayList<SolicitudCambioTurno>();
		if (sugerencias == null) sugerencias = new ArrayList<Sugerencia>();
		if (puntosFidelidad == null) puntosFidelidad = new HashMap<String, Integer>();
		if (codigosDescuento == null) codigosDescuento = new HashMap<String, String>();
		if (mensualidades == null) mensualidades = new HashMap<LocalDate, Double>();
		if (ubicacionTienda == null) ubicacionTienda = new UbicacionInventario("Tienda");
		if (horaInicioAtencion == null) horaInicioAtencion = LocalTime.of(6, 0);
		if (horaFinAtencion == null) horaFinAtencion = LocalTime.of(22, 0);
	}

	public void agregarArticulo(ArticuloTienda articulo) {
		if (articulo != null && articulos.contains(articulo) == false) {
			articulos.add(articulo);
		}
	}

	public void agregarProductoCafeteria(ProductoCafeteria producto) {
		if (producto != null && productosCafeteria.contains(producto) == false) {
			productosCafeteria.add(producto);
		}
	}

	public void registrarCodigoDescuento(String codigo, String identificacionEmpleado) {
		if (codigo != null && codigo.length() > 0 && identificacionEmpleado != null) {
			codigosDescuento.put(codigo, identificacionEmpleado);
		}
	}

	public boolean reabastecerInventario(ArticuloTienda articulo, UbicacionInventario ubicacion, int cantidad) {
		if (articulos.contains(articulo) == false || ubicacion == null || cantidad <= 0) {
			return false;
		}
		Existencia existencia = articulo.getExistencia(ubicacion);
		if (existencia == null) {
			articulo.agregarExistencia(new Existencia(cantidad, ubicacion));
		} else {
			existencia.agregarCantidad(cantidad);
		}
		return true;
	}

	public boolean moverArticulo(ArticuloTienda articulo, UbicacionInventario origen,
			UbicacionInventario destino, int cantidad) {
		if (articulos.contains(articulo) == false || origen == null || destino == null
				|| origen.getNombre().equalsIgnoreCase(destino.getNombre())
				|| cantidad <= 0) {
			return false;
		}

		Existencia existenciaOrigen = articulo.getExistencia(origen);
		if (existenciaOrigen == null || existenciaOrigen.getCantidad() < cantidad) {
			return false;
		}

		Existencia existenciaDestino = articulo.getExistencia(destino);
		if (existenciaDestino != null) {
			return false;
		}
		existenciaOrigen.quitarCantidad(cantidad);
		articulo.agregarExistencia(new Existencia(cantidad, destino));
		return true;
	}

	public boolean reabastecerProductoCafeteria(ProductoCafeteria producto, int cantidad) {
		if (productosCafeteria.contains(producto) == false) {
			return false;
		}
		return producto.reabastecer(cantidad);
	}

	public boolean registrarVentaTienda(VentaTienda venta, int puntosRedimidos) {
		return registrarVentaTienda(venta, puntosRedimidos, null);
	}

	public boolean registrarVentaTienda(VentaTienda venta, int puntosRedimidos, BonoDescuento bono) {
		if (venta == null || venta.estaRegistrada() || venta.getItems().isEmpty() || ubicacionTienda == null
				|| venta.getIdentificacionComprador() == null) {
			return false;
		}

		double tasaDescuento = buscarTasaDescuento(venta);
		if (tasaDescuento < 0) {
			return false;
		}
		if (tasaDescuento > 0 && puntosRedimidos > 0) {
			return false;
		}
		if (bonoNoPermitido(bono, tasaDescuento, puntosRedimidos)) {
			return false;
		}
		if (puntosRedimidos < 0 || puntosRedimidos > getPuntosFidelidad(venta.getIdentificacionComprador())) {
			return false;
		}

		for (ItemVenta item : venta.getItems()) {
			if (articulos.contains(item.getArticulo()) == false) {
				return false;
			}
			int cantidadTotal = 0;
			for (ItemVenta otroItem : venta.getItems()) {
				if (otroItem.getArticulo() == item.getArticulo()) {
					cantidadTotal += otroItem.getCantidad();
				}
			}
			if (item.getArticulo().getCantidadDisponible(ubicacionTienda) < cantidadTotal) {
				return false;
			}
		}

		double descuento = redondear(venta.getValorProductos() * tasaDescuento) + valorBono(bono, venta);
		if (puntosRedimidos + descuento > venta.getValorProductos()) {
			return false;
		}
		if (venta.calcularTotales(descuento, puntosRedimidos) == false) {
			return false;
		}

		for (ItemVenta item : venta.getItems()) {
			Existencia existencia = item.getArticulo().getExistencia(ubicacionTienda);
			existencia.quitarCantidad(item.getCantidad());
		}
		if (bono != null) {
			bono.consumir();
		}
		registrarVenta(venta, puntosRedimidos);
		return true;
	}

	public boolean registrarVentaCafeteria(VentaCafeteria venta, int puntosRedimidos) {
		return registrarVentaCafeteria(venta, puntosRedimidos, null);
	}

	public boolean registrarVentaCafeteria(VentaCafeteria venta, int puntosRedimidos, BonoDescuento bono) {
		if (venta == null || venta.estaRegistrada() || venta.getItems().isEmpty()
				|| venta.getIdentificacionComprador() == null) {
			return false;
		}
		if (puntosRedimidos < 0 || puntosRedimidos > getPuntosFidelidad(venta.getIdentificacionComprador())) {
			return false;
		}
		if (bonoNoPermitido(bono, 0, puntosRedimidos)) {
			return false;
		}

		for (ItemCafeteria item : venta.getItems()) {
			if (productosCafeteria.contains(item.getProducto()) == false) {
				return false;
			}
			int cantidadTotal = 0;
			for (ItemCafeteria otroItem : venta.getItems()) {
				if (otroItem.getProducto() == item.getProducto()) {
					cantidadTotal += otroItem.getCantidad();
				}
			}
			if (item.getProducto().getCantidadDisponible() < cantidadTotal) {
				return false;
			}
		}
		if (puntosRedimidos > venta.getValorProductos()) {
			return false;
		}
		if (venta.calcularTotales(valorBono(bono, venta), puntosRedimidos) == false) {
			return false;
		}

		for (ItemCafeteria item : venta.getItems()) {
			item.getProducto().descontar(item.getCantidad());
		}
		if (bono != null) {
			bono.consumir();
		}
		registrarVenta(venta, puntosRedimidos);
		return true;
	}

	private boolean bonoNoPermitido(BonoDescuento bono, double tasaDescuento, int puntosRedimidos) {
		if (bono == null) {
			return false;
		}
		return bono.estaDisponible() == false || tasaDescuento > 0 || puntosRedimidos > 0;
	}

	private double valorBono(BonoDescuento bono, Venta venta) {
		if (bono == null) {
			return 0;
		}
		return redondear(Math.min(bono.getValor(), venta.getValorProductos()));
	}

	private void registrarVenta(Venta venta, int puntosRedimidos) {
		venta.setRegistrada(true);
		ventas.add(venta);
		int saldo = getPuntosFidelidad(venta.getIdentificacionComprador());
		puntosFidelidad.put(venta.getIdentificacionComprador(), saldo - puntosRedimidos + venta.getPuntosGenerados());
	}

	private double buscarTasaDescuento(VentaTienda venta) {
		String codigo = venta.getCodigoDescuentoUsado();
		if (codigo == null || codigo.length() == 0) {
			return 0;
		}
		String identificacionEmpleado = codigosDescuento.get(codigo);
		if (identificacionEmpleado == null) {
			return -1;
		}
		if (identificacionEmpleado.equals(venta.getIdentificacionComprador())) {
			return 0.15;
		}
		if (venta.esCompradorSocio()) {
			return 0.08;
		}
		return -1;
	}

	public int getPuntosFidelidad(String identificacionComprador) {
		// El saldo se identifica por documento para no depender de Usuario y Empleado, que aun estan en construccion.
		Integer puntos = puntosFidelidad.get(identificacionComprador);
		if (puntos == null) {
			return 0;
		}
		return puntos;
	}

	public void setPuntosFidelidad(String identificacionComprador, int puntos) {
		if (identificacionComprador != null && puntos >= 0) {
			puntosFidelidad.put(identificacionComprador, puntos);
		}
	}

	public void registrarMensualidad(LocalDate fecha, double valor) {
		if (fecha == null || valor < 0) {
			return;
		}
		Double valorAnterior = mensualidades.get(fecha);
		if (valorAnterior == null) {
			valorAnterior = 0.0;
		}
		mensualidades.put(fecha, valorAnterior + valor);
	}

	public String generarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin) {
		double ventasTienda = 0;
		double ventasCafeteria = 0;
		double mensualidadesPeriodo = 0;
		if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
			return "El periodo del reporte no es valido.";
		}

		for (Venta venta : ventas) {
			if (estaEnPeriodo(venta.getFecha(), fechaInicio, fechaFin)) {
				if (venta instanceof VentaTienda) {
					ventasTienda += venta.getTotal();
				} else if (venta instanceof VentaCafeteria) {
					ventasCafeteria += venta.getTotal();
				}
			}
		}
		for (LocalDate fecha : mensualidades.keySet()) {
			if (estaEnPeriodo(fecha, fechaInicio, fechaFin)) {
				mensualidadesPeriodo += mensualidades.get(fecha);
			}
		}

		StringBuilder reporte = new StringBuilder();
		reporte.append("Reporte de ventas del ").append(fechaInicio).append(" al ").append(fechaFin).append("\n");
		reporte.append("Tienda: $").append(redondear(ventasTienda)).append("\n");
		reporte.append("Cafeteria: $").append(redondear(ventasCafeteria)).append("\n");
		reporte.append("Mensualidades: $").append(redondear(mensualidadesPeriodo)).append("\n");
		reporte.append("Total: $").append(redondear(ventasTienda + ventasCafeteria + mensualidadesPeriodo));
		return reporte.toString();
	}

	private boolean estaEnPeriodo(LocalDate fecha, LocalDate fechaInicio, LocalDate fechaFin) {
		return fecha != null && (fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio))
				&& (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin));
	}

	public boolean agregarSugerencia(Sugerencia sugerencia) {
		if (sugerencia == null) {
			return false;
		}
		sugerencias.add(sugerencia);
		return true;
	}

	public boolean gestionarSugerencia(Sugerencia sugerencia, boolean aprobar) {
		if (sugerencias.contains(sugerencia) == false || sugerencia.getEstado() != EstadoSolicitud.PENDIENTE) {
			return false;
		}
		if (aprobar) {
			sugerencia.setEstado(EstadoSolicitud.APROBADA);
		} else {
			sugerencia.setEstado(EstadoSolicitud.RECHAZADA);
		}
		return true;
	}

	public boolean agregarTurno(Turno turno) {
		if (turno == null || turno.esValido() == false) {
			return false;
		}
		for (Turno existente : turnos) {
			if (mismosEmpleadoDia(existente, turno) && seCruzan(existente, turno)) return false;
		}
		turnos.add(turno);
		return true;
	}

	public SolicitudCambioTurno solicitarCambioTurno(LocalDate fecha, Turno turnoActual, Turno turnoPropuesto) {
		if (fecha == null || turnoActual == null || turnoPropuesto == null || turnos.contains(turnoActual) == false
				|| turnoPropuesto.esValido() == false
				|| turnoActual.getIdentificacionEmpleado().equals(turnoPropuesto.getIdentificacionEmpleado()) == false
				|| turnoActual.getTipoEmpleado().equals(turnoPropuesto.getTipoEmpleado()) == false) {
			return null;
		}
		for (Turno existente : turnos) {
			if (existente != turnoActual && mismosEmpleadoDia(existente, turnoPropuesto)
					&& seCruzan(existente, turnoPropuesto)) return null;
		}
		SolicitudCambioTurno solicitud = new SolicitudCambioTurno(fecha, turnoActual, turnoPropuesto);
		solicitudesCambio.add(solicitud);
		return solicitud;
	}

	private boolean mismosEmpleadoDia(Turno uno, Turno otro) {
		return uno.getIdentificacionEmpleado().equals(otro.getIdentificacionEmpleado())
				&& uno.getDiaSemana().equals(otro.getDiaSemana());
	}

	private boolean seCruzan(Turno uno, Turno otro) {
		return uno.getHoraInicio().isBefore(otro.getHoraFin())
				&& otro.getHoraInicio().isBefore(uno.getHoraFin());
	}

	public boolean aprobarCambioTurno(SolicitudCambioTurno solicitud) {
		if (solicitudesCambio.contains(solicitud) == false
				|| solicitud.getEstado() != EstadoSolicitud.PENDIENTE
				|| turnos.contains(solicitud.getTurnoActual()) == false) {
			return false;
		}

		List<Turno> turnosPropuestos = new ArrayList<Turno>(turnos);
		turnosPropuestos.remove(solicitud.getTurnoActual());
		turnosPropuestos.add(solicitud.getTurnoPropuesto());
		if (mantieneCobertura(turnosPropuestos) == false) {
			solicitud.setEstado(EstadoSolicitud.RECHAZADA);
			return false;
		}

		turnos = turnosPropuestos;
		solicitud.setEstado(EstadoSolicitud.APROBADA);
		return true;
	}

	public boolean rechazarCambioTurno(SolicitudCambioTurno solicitud) {
		if (solicitudesCambio.contains(solicitud) == false
				|| solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
			return false;
		}
		solicitud.setEstado(EstadoSolicitud.RECHAZADA);
		return true;
	}

	public boolean mantieneCobertura() {
		return mantieneCobertura(turnos);
	}

	private boolean mantieneCobertura(List<Turno> turnosRevisados) {
		if (horaInicioAtencion == null || horaFinAtencion == null || horaInicioAtencion.isBefore(horaFinAtencion) == false) {
			return false;
		}

		String[] dias = { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" };
		// Se revisa cada minuto para detectar cualquier espacio sin personal suficiente.
		for (String dia : dias) {
			for (LocalTime hora = horaInicioAtencion; hora.isBefore(horaFinAtencion); hora = hora.plusMinutes(1)) {
				int entrenadores = 0;
				int fisioterapeutas = 0;
				for (Turno turno : turnosRevisados) {
					if (turno.cubre(dia, hora)) {
						if (Turno.ENTRENADOR.equals(turno.getTipoEmpleado())) {
							entrenadores++;
						} else if (Turno.FISIOTERAPEUTA.equals(turno.getTipoEmpleado())) {
							fisioterapeutas++;
						}
					}
				}
				if (entrenadores < 2 || fisioterapeutas < 1) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean permiteUsoInstalacion(VentaCafeteria venta, boolean instalacionRestringida) {
		return venta != null && venta.puedeUsarInstalacion(instalacionRestringida);
	}

	private double redondear(double valor) {
		return Math.round(valor * 100.0) / 100.0;
	}

	public List<ArticuloTienda> getArticulos() {
		return articulos;
	}

	public List<ProductoCafeteria> getProductosCafeteria() {
		return productosCafeteria;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public List<Turno> getTurnos() {
		return turnos;
	}

	public List<SolicitudCambioTurno> getSolicitudesCambio() {
		return solicitudesCambio;
	}

	public List<Sugerencia> getSugerencias() {
		return sugerencias;
	}

	public UbicacionInventario getUbicacionTienda() {
		return ubicacionTienda;
	}
}