package uniandes.dpoo.clubdeportivo.consola;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.administracion.AdministracionComercial;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Empleado;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ArticuloTienda;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ProductoCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;

class ConsolaCompra {

	static void mostrarMenu(Club club, Usuario usuario, Scanner entrada) {
		System.out.println("1. Comprar en tienda");
		System.out.println("2. Comprar en cafeteria");
		int opcion = ConsolaUtil.leerEntero(entrada, "Opcion: ", 1, 2);
		if (opcion == 1) comprarEnTienda(club, usuario, entrada);
		if (opcion == 2) comprarEnCafeteria(club, usuario, entrada);
	}

	private static void comprarEnTienda(Club club, Usuario usuario, Scanner entrada) {
		AdministracionComercial administracion = club.getAdministracion();
		List<ArticuloTienda> articulos = administracion.getArticulos();
		if (articulos.isEmpty()) {
			System.out.println("No hay articulos registrados.");
			return;
		}
		for (int i = 0; i < articulos.size(); i++) {
			ArticuloTienda articulo = articulos.get(i);
			System.out.println((i + 1) + ". " + articulo.getNombre() + " - $" + articulo.getPrecio() + " - unidades en tienda: "
					+ articulo.getCantidadDisponible(administracion.getUbicacionTienda()));
		}
		int indice = ConsolaUtil.leerEntero(entrada, "Articulo: ", 1, articulos.size()) - 1;
		if (indice < 0) return;
		ArticuloTienda articulo = articulos.get(indice);
		int disponible = articulo.getCantidadDisponible(administracion.getUbicacionTienda());
		if (disponible == 0) {
			System.out.println("No hay unidades de ese articulo en la tienda.");
			return;
		}
		int cantidad = ConsolaUtil.leerEntero(entrada, "Cantidad: ", 1,
				disponible);
		if (cantidad < 0) return;

		boolean esSocio = usuario instanceof Socio;
		String identificacion = esSocio ? ((Socio) usuario).getIdentificacion() : usuario.getLogin();
		VentaTienda venta = new VentaTienda(LocalDate.now(), identificacion, esSocio);
		if (usuario instanceof Empleado && ConsolaUtil.confirmar(entrada, "Usar el descuento de empleado")) {
			venta.setCodigoDescuentoUsado(((Empleado) usuario).getCodigoDescuento());
		} else if (esSocio) {
			String codigo = ConsolaUtil.leerTexto(entrada, "Codigo de empleado (Enter para omitir): ");
			if (codigo != null) venta.setCodigoDescuentoUsado(codigo);
		}
		if (venta.agregarArticulo(articulo, cantidad) == false) {
			System.out.println("No se pudo agregar el articulo a la compra.");
			return;
		}
		int puntos = ConsolaUtil.leerEntero(entrada, "Puntos para redimir (saldo "
				+ administracion.getPuntosFidelidad(identificacion) + "): ", 0,
				administracion.getPuntosFidelidad(identificacion));
		if (puntos < 0) return;
		if (administracion.registrarVentaTienda(venta, puntos)) {
			System.out.println("Compra registrada. Total: $" + venta.getTotal() + ". Puntos: " + venta.getPuntosGenerados());
		} else {
			System.out.println("La compra no fue aceptada. Revise inventario, codigo o puntos.");
		}
	}

	private static void comprarEnCafeteria(Club club, Usuario usuario, Scanner entrada) {
		AdministracionComercial administracion = club.getAdministracion();
		List<ProductoCafeteria> productos = administracion.getProductosCafeteria();
		if (productos.isEmpty()) {
			System.out.println("No hay productos registrados.");
			return;
		}
		for (int i = 0; i < productos.size(); i++) {
			ProductoCafeteria producto = productos.get(i);
			System.out.println((i + 1) + ". " + producto.getNombre() + " - $" + producto.getPrecio()
					+ " - unidades: " + producto.getCantidadDisponible());
		}
		int indice = ConsolaUtil.leerEntero(entrada, "Producto: ", 1, productos.size()) - 1;
		if (indice < 0) return;
		ProductoCafeteria producto = productos.get(indice);
		if (producto.getCantidadDisponible() == 0) {
			System.out.println("No hay unidades de ese producto.");
			return;
		}
		int cantidad = ConsolaUtil.leerEntero(entrada, "Cantidad: ", 1,
				producto.getCantidadDisponible());
		if (cantidad < 0) return;
		boolean esSocio = usuario instanceof Socio;
		String identificacion = esSocio ? ((Socio) usuario).getIdentificacion() : usuario.getLogin();
		VentaCafeteria venta = new VentaCafeteria(LocalDate.now(), identificacion, esSocio);
		boolean alergenosInformados = ConsolaUtil.confirmar(entrada, "Confirmar que reviso los alergenos");
		boolean zonaRestringida = ConsolaUtil.confirmar(entrada, "Ingresar a una instalacion restringida");
		if (venta.agregarProducto(producto, cantidad, alergenosInformados, zonaRestringida) == false) {
			System.out.println("No se pudo agregar el producto. Revise alergenos, restricciones e inventario.");
			return;
		}
		int puntos = ConsolaUtil.leerEntero(entrada, "Puntos para redimir (saldo "
				+ administracion.getPuntosFidelidad(identificacion) + "): ", 0,
				administracion.getPuntosFidelidad(identificacion));
		if (puntos < 0) return;
		if (administracion.registrarVentaCafeteria(venta, puntos)) {
			System.out.println("Compra registrada. Total: $" + venta.getTotal() + ". Puntos: " + venta.getPuntosGenerados());
		} else {
			System.out.println("La compra no fue aceptada. Revise inventario o puntos.");
		}
	}
}
