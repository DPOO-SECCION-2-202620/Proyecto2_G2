package uniandes.dpoo.clubdeportivo.consola;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.academias.Disciplina;
import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.administracion.AdministracionComercial;
import uniandes.dpoo.clubdeportivo.modelo.administracion.EstadoSolicitud;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Existencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.SolicitudCambioTurno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Sugerencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.UbicacionInventario;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Empleado;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Fisioterapeuta;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ArticuloTienda;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Bebida;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ProductoCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Snack;

class ConsolaAdministrador {

	private Club club;
	private Scanner entrada;

	ConsolaAdministrador(Club club, Scanner entrada) {
		this.club = club;
		this.entrada = entrada;
	}

	void iniciar() {
		boolean salir = false;
		while (salir == false && entrada.hasNextLine()) {
			System.out.println("\nMenu de administrador");
			System.out.println("1. Consultar equipos y jugadores habilitados");
			System.out.println("2. Crear equipo");
			System.out.println("3. Crear disciplina e instalacion");
			System.out.println("4. Agregar articulo o producto");
			System.out.println("5. Trasladar inventario");
			System.out.println("6. Registrar turno de empleado");
			System.out.println("7. Revisar solicitudes de cambio de turno");
			System.out.println("8. Revisar sugerencias");
			System.out.println("9. Generar reporte de ventas");
			System.out.println("0. Guardar y salir");
			int opcion = ConsolaUtil.leerEntero(entrada, "Opcion: ", 0, 9);
			if (opcion == 0 || opcion == -1) salir = true;
			else if (opcion == 1) consultarEquipos();
			else if (opcion == 2) crearEquipo();
			else if (opcion == 3) crearInstalacion();
			else if (opcion == 4) agregarProducto();
			else if (opcion == 5) trasladarInventario();
			else if (opcion == 6) agregarTurno();
			else if (opcion == 7) revisarSolicitudes();
			else if (opcion == 8) revisarSugerencias();
			else if (opcion == 9) generarReporte();
		}
	}

	private void consultarEquipos() {
		for (Equipo equipo : club.getEquipos()) {
			System.out.println(equipo.getNombre() + " - " + equipo.getDeporte().getNombre() + " - "
					+ equipo.contarJugadoresHabilitados() + "/"
					+ equipo.getDeporte().getMinimoJugadoresHabilitados() + " habilitados - "
					+ (club.equipoCumpleMinimo(equipo) ? "CUMPLE" : "NO CUMPLE"));
		}
		if (club.getEquipos().isEmpty()) System.out.println("No hay equipos creados.");
	}

	private void crearEquipo() {
		String nombre = ConsolaUtil.leerTexto(entrada, "Nombre del equipo: ");
		System.out.println("1. Futbol (7)  2. Baloncesto (5)  3. Voleibol (6)");
		int opcionDeporte = ConsolaUtil.leerEntero(entrada, "Deporte: ", 1, 3);
		System.out.println("1. Sub-13  2. Sub-15  3. Sub-17  4. Mayores");
		int opcionCategoria = ConsolaUtil.leerEntero(entrada, "Categoria: ", 1, 4);
		int cupo = ConsolaUtil.leerEntero(entrada, "Cupo maximo: ", 1, 40);
		if (nombre == null || nombre.isEmpty() || opcionDeporte < 0 || opcionCategoria < 0 || cupo < 0) return;
		String[] deportes = { "Futbol", "Baloncesto", "Voleibol" };
		int[] minimos = { 7, 5, 6 };
		Categoria[] categorias = { new Categoria("Sub-13", 0, 13), new Categoria("Sub-15", 14, 15),
				new Categoria("Sub-17", 16, 17), new Categoria("Mayores", 18, 99) };
		Equipo equipo = new Equipo(nombre, cupo,
				new Deporte(deportes[opcionDeporte - 1], minimos[opcionDeporte - 1]),
				categorias[opcionCategoria - 1]);
		System.out.println(club.agregarEquipo(equipo) ? "Equipo creado." : "No se pudo crear el equipo.");
	}

	private void crearInstalacion() {
		String nombreDisciplina = ConsolaUtil.leerTexto(entrada, "Disciplina (Tenis, Padel, Futbol, etc.): ");
		int maximo = ConsolaUtil.leerEntero(entrada, "Instalaciones simultaneas permitidas: ", 1, 20);
		String nombreInstalacion = ConsolaUtil.leerTexto(entrada, "Nombre de la instalacion: ");
		if (nombreDisciplina == null || nombreDisciplina.isEmpty() || maximo < 0
				|| nombreInstalacion == null || nombreInstalacion.isEmpty()) return;
		Disciplina disciplina = buscarDisciplina(nombreDisciplina);
		if (disciplina == null) {
			disciplina = new Disciplina(nombreDisciplina, maximo);
			if (club.agregarDisciplina(disciplina) == false) {
				System.out.println("No se pudo crear la disciplina.");
				return;
			}
		}
		if (club.agregarInstalacion(new Instalacion(nombreInstalacion, disciplina.getNombre()))) {
			System.out.println("Instalacion creada.");
		} else {
			System.out.println("No se pudo crear la instalacion.");
		}
	}

	private Disciplina buscarDisciplina(String nombre) {
		for (Disciplina disciplina : club.getDisciplinas()) {
			if (disciplina.getNombre().equalsIgnoreCase(nombre)) return disciplina;
		}
		return null;
	}

	private void agregarProducto() {
		System.out.println("1. Articulo de tienda  2. Bebida  3. Snack");
		int tipo = ConsolaUtil.leerEntero(entrada, "Tipo: ", 1, 3);
		String nombre = ConsolaUtil.leerTexto(entrada, "Nombre: ");
		double precio = ConsolaUtil.leerDecimal(entrada, "Precio: $", 1);
		int cantidad = ConsolaUtil.leerEntero(entrada, "Cantidad inicial: ", 1, 10000);
		if (tipo < 0 || nombre == null || nombre.isEmpty() || precio < 0 || cantidad < 0) return;
		AdministracionComercial administracion = club.getAdministracion();
		if (tipo == 1) {
			String categoria = ConsolaUtil.leerTexto(entrada, "Categoria: ");
			String ubicacionTexto = ConsolaUtil.leerTexto(entrada, "Ubicacion inicial (Tienda o Bodega): ");
			if (categoria == null || ubicacionTexto == null || ubicacionTexto.isEmpty()
					|| articuloExiste(nombre)) return;
			ArticuloTienda articulo = new ArticuloTienda(nombre, categoria, precio);
			administracion.agregarArticulo(articulo);
			UbicacionInventario ubicacion = ubicacionTexto.equalsIgnoreCase("Tienda")
					? administracion.getUbicacionTienda() : new UbicacionInventario(ubicacionTexto);
			if (administracion.reabastecerInventario(articulo, ubicacion, cantidad)) {
				System.out.println("Articulo e inventario registrados.");
			}
		} else {
			for (ProductoCafeteria producto : administracion.getProductosCafeteria()) {
				if (producto.getNombre().equalsIgnoreCase(nombre)) {
					System.out.println("Ya existe un producto con ese nombre.");
					return;
				}
			}
			ProductoCafeteria producto;
			if (tipo == 2) {
				boolean caliente = ConsolaUtil.confirmar(entrada, "Es una bebida caliente");
				producto = new Bebida(nombre, precio, cantidad, caliente);
			} else {
				String alergenosTexto = ConsolaUtil.leerTexto(entrada, "Alergenos separados por coma: ");
				ArrayList<String> alergenos = new ArrayList<String>();
				if (alergenosTexto != null && alergenosTexto.isEmpty() == false) {
					for (String alergeno : alergenosTexto.split(",")) alergenos.add(alergeno.trim());
				}
				producto = new Snack(nombre, precio, cantidad, alergenos);
			}
			administracion.agregarProductoCafeteria(producto);
			System.out.println("Producto de cafeteria registrado.");
		}
	}

	private boolean articuloExiste(String nombre) {
		for (ArticuloTienda articulo : club.getAdministracion().getArticulos()) {
			if (articulo.getNombre().equalsIgnoreCase(nombre)) {
				System.out.println("Ya existe un articulo con ese nombre.");
				return true;
			}
		}
		return false;
	}

	private void trasladarInventario() {
		List<ArticuloTienda> articulos = club.getAdministracion().getArticulos();
		for (int i = 0; i < articulos.size(); i++) {
			System.out.println((i + 1) + ". " + articulos.get(i).getNombre() + " - unidades: "
					+ articulos.get(i).getCantidadDisponible());
		}
		int indice = ConsolaUtil.seleccionar(entrada, "Articulos", articulos.size());
		if (indice < 0) return;
		ArticuloTienda articulo = articulos.get(indice);
		if (articulo.getExistencias().isEmpty()) {
			System.out.println("El articulo no tiene inventario en ninguna ubicacion.");
			return;
		}
		for (int i = 0; i < articulo.getExistencias().size(); i++) {
			Existencia existencia = articulo.getExistencias().get(i);
			System.out.println((i + 1) + ". " + existencia.getUbicacion().getNombre() + " - " + existencia.getCantidad());
		}
		int origenIndice = ConsolaUtil.leerEntero(entrada, "Ubicacion de origen: ", 1,
				articulo.getExistencias().size()) - 1;
		if (origenIndice < 0) return;
		UbicacionInventario origen = articulo.getExistencias().get(origenIndice).getUbicacion();
		String destinoNombre = ConsolaUtil.leerTexto(entrada, "Ubicacion de destino: ");
		int cantidad = ConsolaUtil.leerEntero(entrada, "Cantidad a trasladar: ", 1,
				articulo.getExistencias().get(origenIndice).getCantidad());
		if (destinoNombre == null || destinoNombre.isEmpty() || cantidad < 0) return;
		UbicacionInventario destino = new UbicacionInventario(destinoNombre);
		boolean resultado = club.getAdministracion().moverArticulo(articulo, origen, destino, cantidad);
		System.out.println(resultado ? "Traslado realizado." : "Traslado rechazado. Revise origen, destino y cantidad.");
	}

	private void agregarTurno() {
		ArrayList<Empleado> empleados = new ArrayList<Empleado>();
		for (Usuario usuario : club.getUsuarios()) {
			if (usuario instanceof Empleado) empleados.add((Empleado) usuario);
		}
		for (int i = 0; i < empleados.size(); i++) System.out.println((i + 1) + ". " + empleados.get(i).getLogin());
		int indice = ConsolaUtil.seleccionar(entrada, "Empleados", empleados.size());
		if (indice < 0) return;
		Empleado empleado = empleados.get(indice);
		String tipo = empleado instanceof Entrenador ? Turno.ENTRENADOR : Turno.FISIOTERAPEUTA;
		System.out.println("1. Lunes 2. Martes 3. Miercoles 4. Jueves 5. Viernes 6. Sabado 7. Domingo");
		int dia = ConsolaUtil.leerEntero(entrada, "Dia: ", 1, 7);
		int inicio = ConsolaUtil.leerEntero(entrada, "Hora de inicio (0-22): ", 0, 22);
		int fin = ConsolaUtil.leerEntero(entrada, "Hora de fin (1-23): ", 1, 23);
		if (dia < 0 || inicio < 0 || fin < 0) return;
		String[] dias = { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" };
		Turno turno = new Turno(empleado.getLogin(), tipo, dias[dia - 1], LocalTime.of(inicio, 0),
				LocalTime.of(fin, 0));
		if (club.getAdministracion().agregarTurno(turno)) {
			System.out.println("Turno registrado. Cobertura semanal: "
					+ (club.getAdministracion().mantieneCobertura() ? "CUMPLE" : "AUN INCOMPLETA"));
		} else {
			System.out.println("Turno no valido.");
		}
	}

	private void revisarSolicitudes() {
		AdministracionComercial administracion = club.getAdministracion();
		ArrayList<SolicitudCambioTurno> pendientes = new ArrayList<SolicitudCambioTurno>();
		for (SolicitudCambioTurno solicitud : administracion.getSolicitudesCambio()) {
			if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE) pendientes.add(solicitud);
		}
		for (int i = 0; i < pendientes.size(); i++) System.out.println((i + 1) + ". " + pendientes.get(i));
		int indice = ConsolaUtil.seleccionar(entrada, "Solicitudes pendientes", pendientes.size());
		if (indice < 0) return;
		System.out.println("1. Aprobar  2. Rechazar");
		int accion = ConsolaUtil.leerEntero(entrada, "Accion: ", 1, 2);
		if (accion < 0) return;
		SolicitudCambioTurno solicitud = pendientes.get(indice);
		if (accion == 1) {
			if (administracion.aprobarCambioTurno(solicitud)) {
				System.out.println("Solicitud aprobada.");
			} else {
				System.out.println("Solicitud rechazada porque no conserva la cobertura minima.");
			}
		} else {
			System.out.println(administracion.rechazarCambioTurno(solicitud)
					? "Solicitud rechazada." : "No se pudo rechazar la solicitud.");
		}
	}

	private void revisarSugerencias() {
		AdministracionComercial administracion = club.getAdministracion();
		ArrayList<Sugerencia> pendientes = new ArrayList<Sugerencia>();
		for (Sugerencia sugerencia : administracion.getSugerencias()) {
			if (sugerencia.getEstado() == EstadoSolicitud.PENDIENTE) pendientes.add(sugerencia);
		}
		for (int i = 0; i < pendientes.size(); i++) {
			Sugerencia sugerencia = pendientes.get(i);
			System.out.println((i + 1) + ". " + sugerencia + " - " + sugerencia.getDescripcion());
		}
		int indice = ConsolaUtil.seleccionar(entrada, "Sugerencias pendientes", pendientes.size());
		if (indice < 0) return;
		boolean aprobar = ConsolaUtil.confirmar(entrada, "Aprobar sugerencia");
		System.out.println(administracion.gestionarSugerencia(pendientes.get(indice), aprobar)
				? "Sugerencia gestionada." : "No se pudo gestionar.");
	}

	private void generarReporte() {
		String inicio = ConsolaUtil.leerTexto(entrada, "Fecha inicial (AAAA-MM-DD): ");
		String fin = ConsolaUtil.leerTexto(entrada, "Fecha final (AAAA-MM-DD): ");
		try {
			System.out.println(club.getAdministracion().generarReporteVentas(LocalDate.parse(inicio), LocalDate.parse(fin)));
		} catch (Exception e) {
			System.out.println("Las fechas no tienen el formato esperado.");
		}
	}
}
