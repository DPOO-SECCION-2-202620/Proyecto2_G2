package uniandes.dpoo.clubdeportivo.consola;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.administracion.AdministracionComercial;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Sugerencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.Asistencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.EstadoAsistencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.SesionEntrenamiento;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Empleado;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;

class ConsolaEmpleado {

	private Club club;
	private Empleado empleado;
	private Scanner entrada;

	ConsolaEmpleado(Club club, Empleado empleado, Scanner entrada) {
		this.club = club;
		this.empleado = empleado;
		this.entrada = entrada;
	}

	void iniciar() {
		boolean salir = false;
		while (salir == false && entrada.hasNextLine()) {
			System.out.println("\nMenu de empleado - " + empleado.getLogin());
			System.out.println("1. Revisar jugadores habilitados por equipo");
			if (empleado instanceof Entrenador) System.out.println("2. Programar entrenamiento y registrar asistencia");
			System.out.println("3. Consultar entrenamientos");
			System.out.println("4. Solicitar cambio de turno");
			System.out.println("5. Enviar sugerencia");
			System.out.println("6. Comprar en tienda o cafeteria");
			System.out.println("0. Guardar y salir");
			int opcion = ConsolaUtil.leerEntero(entrada, "Opcion: ", 0, 6);
			if (opcion == 0 || opcion == -1) salir = true;
			else if (opcion == 1) consultarJugadoresHabilitados();
			else if (opcion == 2 && empleado instanceof Entrenador) programarEntrenamiento();
			else if (opcion == 3) consultarEntrenamientos();
			else if (opcion == 4) solicitarCambioTurno();
			else if (opcion == 5) enviarSugerencia();
			else if (opcion == 6) ConsolaCompra.mostrarMenu(club, empleado, entrada);
			else System.out.println("Opcion no disponible para este tipo de empleado.");
		}
	}

	private void consultarJugadoresHabilitados() {
		if (club.getEquipos().isEmpty()) {
			System.out.println("No hay equipos creados.");
			return;
		}
		for (Equipo equipo : club.getEquipos()) {
			int habilitados = equipo.contarJugadoresHabilitados();
			int minimo = equipo.getDeporte().getMinimoJugadoresHabilitados();
			System.out.println(equipo.getNombre() + " - " + equipo.getDeporte().getNombre() + ": " + habilitados
					+ " habilitados de " + minimo + " requeridos - "
					+ (club.equipoCumpleMinimo(equipo) ? "CUMPLE" : "NO CUMPLE"));
			for (JugadorEquipo jugador : club.jugadoresNoHabilitados(equipo)) {
				System.out.println("  Suspendido: " + jugador.getJugador().getNombre());
			}
		}
	}

	private void programarEntrenamiento() {
		List<Equipo> equipos = club.getEquipos();
		for (int i = 0; i < equipos.size(); i++) {
			System.out.println((i + 1) + ". " + equipos.get(i).getNombre() + " - "
					+ equipos.get(i).getDeporte().getNombre());
		}
		int indiceEquipo = ConsolaUtil.seleccionar(entrada, "Equipos", equipos.size());
		if (indiceEquipo < 0) return;
		Equipo equipo = equipos.get(indiceEquipo);
		if (equipo.getJugadores().isEmpty()) {
			System.out.println("El equipo no tiene jugadores.");
			return;
		}
		List<Instalacion> instalaciones = club.getInstalaciones();
		int cantidad = 0;
		for (Instalacion instalacion : instalaciones) {
			if (instalacion.getDeporte().equalsIgnoreCase(equipo.getDeporte().getNombre())) {
				System.out.println((cantidad + 1) + ". " + instalacion.getNombreCancha());
				cantidad++;
			}
		}
		if (cantidad == 0) {
			System.out.println("No hay instalaciones para este deporte.");
			return;
		}
		int seleccion = ConsolaUtil.leerEntero(entrada, "Instalacion: ", 1, cantidad);
		if (seleccion < 0) return;
		Instalacion instalacion = null;
		int actual = 0;
		for (Instalacion opcion : instalaciones) {
			if (opcion.getDeporte().equalsIgnoreCase(equipo.getDeporte().getNombre())) {
				actual++;
				if (actual == seleccion) instalacion = opcion;
			}
		}
		String fecha = ConsolaUtil.leerTexto(entrada, "Fecha (AAAA-MM-DD): ");
		int hora = ConsolaUtil.leerEntero(entrada, "Hora de inicio (0-23): ", 0, 23);
		if (fecha == null || hora < 0) return;
		SesionEntrenamiento sesion = new SesionEntrenamiento(fecha, hora, (Entrenador) empleado, instalacion);
		if (club.agregarSesionEntrenamiento(sesion) == false) {
			System.out.println("No se pudo programar. Revise fecha, instalacion y horario.");
			return;
		}
		int convocados = Math.min(4, equipo.getJugadores().size());
		for (int i = 0; i < convocados; i++) sesion.convocar(equipo.getJugadores().get(i).getJugador());
		for (Socio socio : sesion.getJugadoresConvocados()) registrarAsistencia(sesion, socio);
		System.out.println("Entrenamiento guardado.");
	}

	private void registrarAsistencia(SesionEntrenamiento sesion, Socio socio) {
		System.out.println("Asistencia de " + socio.getNombre() + ": 1. Asistio  2. Falto  3. Llego tarde");
		int opcion = ConsolaUtil.leerEntero(entrada, "Estado: ", 1, 3);
		if (opcion < 0) return;
		EstadoAsistencia[] estados = { EstadoAsistencia.ASISTIO, EstadoAsistencia.FALTO, EstadoAsistencia.LLEGO_TARDE };
		if (club.registrarAsistencia(sesion, socio, estados[opcion - 1])
				&& estados[opcion - 1] != EstadoAsistencia.FALTO
				&& ConsolaUtil.confirmar(entrada, "Registrar evaluacion")) {
			int nota = ConsolaUtil.leerEntero(entrada, "Nota de 1 a 5: ", 1, 5);
			String comentario = ConsolaUtil.leerTexto(entrada, "Comentario: ");
			if (nota > 0 && comentario != null) sesion.registrarEvaluacion(socio, nota, comentario);
		}
	}

	private void consultarEntrenamientos() {
		if (club.getSesionesEntrenamiento().isEmpty()) {
			System.out.println("No hay entrenamientos registrados.");
			return;
		}
		for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {
			System.out.println(sesion.getFecha() + " " + sesion.getHoraInicio() + ":00 - "
					+ sesion.getInstalacion().getNombreCancha() + " - entrenador: "
					+ sesion.getEntrenadorACargo().getLogin());
			for (Asistencia asistencia : sesion.getAsistencias()) {
				System.out.println("  " + asistencia.getSocio().getNombre() + " - " + asistencia.getEstado()
						+ (asistencia.getNota() == null ? "" : " - nota: " + asistencia.getNota()));
			}
		}
	}

	private void solicitarCambioTurno() {
		AdministracionComercial administracion = club.getAdministracion();
		ArrayList<Turno> turnos = new ArrayList<Turno>();
		for (Turno turno : administracion.getTurnos()) {
			if (turno.getIdentificacionEmpleado().equals(empleado.getLogin())) turnos.add(turno);
		}
		for (int i = 0; i < turnos.size(); i++) System.out.println((i + 1) + ". " + turnos.get(i));
		int indice = ConsolaUtil.seleccionar(entrada, "Sus turnos", turnos.size());
		if (indice < 0) return;
		Turno actual = turnos.get(indice);
		System.out.println("1. Lunes 2. Martes 3. Miercoles 4. Jueves 5. Viernes 6. Sabado 7. Domingo");
		int dia = ConsolaUtil.leerEntero(entrada, "Nuevo dia: ", 1, 7);
		int inicio = ConsolaUtil.leerEntero(entrada, "Hora de inicio (0-23): ", 0, 23);
		int fin = ConsolaUtil.leerEntero(entrada, "Hora de fin (1-23): ", 1, 23);
		if (dia < 0 || inicio < 0 || fin < 0) return;
		String[] dias = { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" };
		Turno propuesto = new Turno(empleado.getLogin(), actual.getTipoEmpleado(), dias[dia - 1],
				LocalTime.of(inicio, 0), LocalTime.of(fin, 0));
		if (administracion.solicitarCambioTurno(LocalDate.now(), actual, propuesto) == null) {
			System.out.println("La solicitud no es valida.");
		} else {
			System.out.println("Solicitud enviada al administrador.");
		}
	}

	private void enviarSugerencia() {
		String nombre = ConsolaUtil.leerTexto(entrada, "Nombre: ");
		String descripcion = ConsolaUtil.leerTexto(entrada, "Descripcion: ");
		System.out.println("1. Tienda  2. Cafeteria");
		int tipo = ConsolaUtil.leerEntero(entrada, "Tipo: ", 1, 2);
		if (nombre == null || nombre.isEmpty() || descripcion == null || tipo < 0) return;
		String categoria = tipo == 1 ? "TIENDA" : "CAFETERIA";
		if (club.getAdministracion().agregarSugerencia(
				new Sugerencia(nombre, descripcion, categoria, empleado.getLogin()))) {
			System.out.println("Sugerencia enviada.");
		}
	}
}
