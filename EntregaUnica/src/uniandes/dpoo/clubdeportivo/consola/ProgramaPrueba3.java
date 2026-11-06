package uniandes.dpoo.clubdeportivo.consola;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.clubdeportivo.modelo.administracion.AdministracionComercial;
import uniandes.dpoo.clubdeportivo.modelo.administracion.SolicitudCambioTurno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Sugerencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.UbicacionInventario;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ArticuloTienda;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Bebida;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Snack;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;

public class ProgramaPrueba3 {

	public static void main(String[] args) {
		LocalDate hoy = LocalDate.of(2026, 10, 19);
		UbicacionInventario tienda = new UbicacionInventario("Tienda");
		UbicacionInventario bodega = new UbicacionInventario("Bodega");
		AdministracionComercial administracion = new AdministracionComercial(tienda, LocalTime.of(6, 0),
				LocalTime.of(22, 0));

		titulo("PROGRAMA DE PRUEBA 3 - TIENDA, CAFETERIA Y ADMINISTRACION");

		seccion("1. INVENTARIO INICIAL");
		ArticuloTienda camiseta = new ArticuloTienda("Camiseta deportiva", "Ropa", 120000);
		administracion.agregarArticulo(camiseta);
		administracion.reabastecerInventario(camiseta, tienda, 8);
		administracion.reabastecerInventario(camiseta, bodega, 20);
		System.out.println("  Camisetas en tienda: " + camiseta.getCantidadDisponible(tienda));
		System.out.println("  Camisetas en bodega: " + camiseta.getCantidadDisponible(bodega));
		System.out.println("  Total en inventario: " + camiseta.getCantidadDisponible());

		seccion("2. COMPRA DE EMPLEADO CON SU DESCUENTO");
		String identificacionEmpleado = "E-01";
		administracion.registrarCodigoDescuento("COD-E01", identificacionEmpleado);
		ArticuloTienda balon = new ArticuloTienda("Balon", "Deportes", 50000);
		administracion.agregarArticulo(balon);
		administracion.reabastecerInventario(balon, tienda, 6);

		VentaTienda ventaEmpleado = new VentaTienda(hoy, identificacionEmpleado, false);
		ventaEmpleado.setCodigoDescuentoUsado("COD-E01");
		ventaEmpleado.agregarArticulo(balon, 1);
		if (administracion.registrarVentaTienda(ventaEmpleado, 0)) {
			mostrarVentaTienda(ventaEmpleado);
		} else {
			System.out.println("  La compra del empleado fue rechazada.");
		}
		System.out.println("  Puntos del empleado: "
				+ administracion.getPuntosFidelidad(identificacionEmpleado));

		seccion("3. COMPRA DE SOCIO CON CODIGO COMPARTIDO");
		String identificacionSocio = "S-01";
		VentaTienda ventaSocio = new VentaTienda(hoy, identificacionSocio, true);
		ventaSocio.setCodigoDescuentoUsado("COD-E01");
		ventaSocio.agregarArticulo(camiseta, 1);
		if (administracion.registrarVentaTienda(ventaSocio, 0)) {
			mostrarVentaTienda(ventaSocio);
		} else {
			System.out.println("  La compra del socio fue rechazada.");
		}
		System.out.println("  Puntos del socio: " + administracion.getPuntosFidelidad(identificacionSocio));

		seccion("4. PUNTOS Y DESCUENTOS");
		VentaTienda ventaCombinada = new VentaTienda(hoy, identificacionSocio, true);
		ventaCombinada.setCodigoDescuentoUsado("COD-E01");
		ventaCombinada.agregarArticulo(camiseta, 1);
		System.out.println("  Codigo de descuento y puntos en la misma compra: "
				+ (administracion.registrarVentaTienda(ventaCombinada, 1) ? "ACEPTADO" : "RECHAZADO"));

		VentaTienda ventaConPuntos = new VentaTienda(hoy, identificacionSocio, true);
		ventaConPuntos.agregarArticulo(camiseta, 1);
		if (administracion.registrarVentaTienda(ventaConPuntos, 500)) {
			System.out.println("  Compra con 500 puntos redimidos:");
			mostrarVentaTienda(ventaConPuntos);
		} else {
			System.out.println("  No fue posible redimir los puntos.");
		}
		System.out.println("  Puntos restantes del socio: "
				+ administracion.getPuntosFidelidad(identificacionSocio));

		seccion("5. COMPRA EN CAFETERIA");
		Bebida cafe = new Bebida("Cafe caliente", 8000, 12, true);
		Snack galleta = new Snack("Galleta", 6000, 15, crearAlergenos());
		administracion.agregarProductoCafeteria(cafe);
		administracion.agregarProductoCafeteria(galleta);

		VentaCafeteria ventaCafe = new VentaCafeteria(hoy, identificacionSocio, true);
		ventaCafe.setPropina(0.12);
		System.out.println("  Alergenos de la galleta: " + galleta.getAlergenos());
		System.out.println("  Snack sin informar alergenos: "
				+ (ventaCafe.agregarProducto(galleta, 1, false, false) ? "ACEPTADO" : "RECHAZADO"));
		System.out.println("  Bebida caliente en instalacion restringida: "
				+ (ventaCafe.agregarProducto(cafe, 1, true, true) ? "ACEPTADO" : "RECHAZADO"));
		ventaCafe.agregarProducto(galleta, 1, true, false);
		ventaCafe.agregarProducto(cafe, 1, true, false);

		if (administracion.registrarVentaCafeteria(ventaCafe, 100)) {
			System.out.println("  Venta registrada. Subtotal: $" + ventaCafe.getSubtotal() + ", impuesto: $"
					+ ventaCafe.getImpuestos() + ", propina (12%): $" + ventaCafe.getValorPropina() + ", total: $"
					+ ventaCafe.getTotal());
			System.out.println("  Puntos generados en cafeteria: " + ventaCafe.getPuntosGenerados());
		} else {
			System.out.println("  La compra de cafeteria fue rechazada.");
		}
		System.out.println("  Puede entrar con el cafe a una cancha restringida: "
				+ (administracion.permiteUsoInstalacion(ventaCafe, true) ? "SI" : "NO"));

		seccion("6. INVENTARIO Y REABASTECIMIENTO");
		System.out.println("  Camisetas en tienda despues de las ventas: " + camiseta.getCantidadDisponible(tienda));
		System.out.println("  Camisetas en bodega antes del movimiento: " + camiseta.getCantidadDisponible(bodega));
		ArticuloTienda raqueta = new ArticuloTienda("Raqueta", "Deportes", 90000);
		administracion.agregarArticulo(raqueta);
		administracion.reabastecerInventario(raqueta, bodega, 6);
		boolean movimiento = administracion.moverArticulo(raqueta, bodega, tienda, 4);
		System.out.println("  Trasladar raquetas desde bodega: " + (movimiento ? "ACEPTADO" : "RECHAZADO"));
		System.out.println("  Raquetas en tienda despues del movimiento: " + raqueta.getCantidadDisponible(tienda));
		System.out.println("  Rechazar camisetas porque ya existen en tienda: "
				+ (administracion.moverArticulo(camiseta, bodega, tienda, 4) ? "FALLO" : "CORRECTO"));
		System.out.println("  Reabastecimiento con cantidad negativa: "
				+ (administracion.reabastecerInventario(camiseta, tienda, -2) ? "ACEPTADO" : "RECHAZADO"));

		seccion("7. SUGERENCIAS DE EMPLEADOS");
		Sugerencia sugerenciaSnack = new Sugerencia("Barra de cereal", "Snack para despues del entrenamiento",
				"CAFETERIA", identificacionEmpleado);
		Sugerencia sugerenciaArticulo = new Sugerencia("Botella deportiva", "Articulo para la tienda", "TIENDA",
				identificacionEmpleado);
		administracion.agregarSugerencia(sugerenciaSnack);
		administracion.agregarSugerencia(sugerenciaArticulo);
		administracion.gestionarSugerencia(sugerenciaSnack, true);
		administracion.gestionarSugerencia(sugerenciaArticulo, false);
		System.out.println("  " + sugerenciaSnack);
		System.out.println("  " + sugerenciaArticulo);

		seccion("8. SOLICITUDES DE CAMBIO DE TURNO");
		crearTurnosSemana(administracion);
		Turno turnoEntrenadorTres = buscarTurno(administracion, "E-03", "LUNES");
		Turno nuevoTurnoTres = new Turno("E-03", Turno.ENTRENADOR, "LUNES", LocalTime.of(8, 0),
				LocalTime.of(22, 0));
		SolicitudCambioTurno solicitudAceptable = administracion.solicitarCambioTurno(hoy, turnoEntrenadorTres,
				nuevoTurnoTres);
		System.out.println("  Cambio de un entrenador adicional: "
				+ (administracion.aprobarCambioTurno(solicitudAceptable) ? "APROBADO" : "RECHAZADO"));

		Turno turnoEntrenadorUno = buscarTurno(administracion, "E-01", "LUNES");
		Turno nuevoTurnoUno = new Turno("E-01", Turno.ENTRENADOR, "LUNES", LocalTime.of(8, 0),
				LocalTime.of(22, 0));
		SolicitudCambioTurno solicitudNoAceptable = administracion.solicitarCambioTurno(hoy, turnoEntrenadorUno,
				nuevoTurnoUno);
		System.out.println("  Cambio que deja menos de dos entrenadores: "
				+ (administracion.aprobarCambioTurno(solicitudNoAceptable) ? "APROBADO" : "RECHAZADO"));
		System.out.println("  Cobertura semanal despues de las solicitudes: "
				+ (administracion.mantieneCobertura() ? "CUMPLE" : "NO CUMPLE"));

		seccion("9. REPORTE DEL PERIODO");
		administracion.registrarMensualidad(hoy, 85000);
		System.out.println(administracion.generarReporteVentas(LocalDate.of(2026, 10, 1),
				LocalDate.of(2026, 10, 31)));
		System.out.println();
		titulo("FIN DEL PROGRAMA DE PRUEBA 3");
	}

	private static List<String> crearAlergenos() {
		List<String> alergenos = new ArrayList<String>();
		alergenos.add("gluten");
		alergenos.add("leche");
		return alergenos;
	}

	private static void crearTurnosSemana(AdministracionComercial administracion) {
		String[] dias = { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" };
		for (String dia : dias) {
			administracion.agregarTurno(new Turno("E-01", Turno.ENTRENADOR, dia, LocalTime.of(6, 0),
					LocalTime.of(22, 0)));
			administracion.agregarTurno(new Turno("E-02", Turno.ENTRENADOR, dia, LocalTime.of(6, 0),
					LocalTime.of(22, 0)));
			administracion.agregarTurno(new Turno("E-03", Turno.ENTRENADOR, dia, LocalTime.of(6, 0),
					LocalTime.of(22, 0)));
			administracion.agregarTurno(new Turno("F-01", Turno.FISIOTERAPEUTA, dia, LocalTime.of(6, 0),
					LocalTime.of(22, 0)));
		}
	}

	private static Turno buscarTurno(AdministracionComercial administracion, String empleado, String dia) {
		for (Turno turno : administracion.getTurnos()) {
			if (turno.getIdentificacionEmpleado().equals(empleado) && turno.getDiaSemana().equals(dia)) {
				return turno;
			}
		}
		return null;
	}

	private static void mostrarVentaTienda(VentaTienda venta) {
		System.out.println("  Subtotal: $" + venta.getSubtotal() + ", descuento: $" + venta.getDescuento()
				+ ", IVA: $" + venta.getImpuestos() + ", total: $" + venta.getTotal());
		System.out.println("  Puntos generados: " + venta.getPuntosGenerados());
	}

	private static void titulo(String texto) {
		System.out.println("======================================================================");
		System.out.println("  " + texto);
		System.out.println("======================================================================");
	}

	private static void seccion(String texto) {
		System.out.println("\n----------------------------------------------------------------------");
		System.out.println(texto);
		System.out.println("----------------------------------------------------------------------");
	}
}
