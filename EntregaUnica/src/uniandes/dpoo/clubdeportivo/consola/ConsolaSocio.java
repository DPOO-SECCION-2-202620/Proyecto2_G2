package uniandes.dpoo.clubdeportivo.consola;

import java.util.List;
import java.util.Scanner;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.academias.Disciplina;
import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

class ConsolaSocio {

	private Club club;
	private Socio socio;
	private Scanner entrada;

	ConsolaSocio(Club club, Socio socio, Scanner entrada) {
		this.club = club;
		this.socio = socio;
		this.entrada = entrada;
	}

	void iniciar() {
		boolean salir = false;
		while (salir == false && entrada.hasNextLine()) {
			System.out.println("\nMenu de socio - " + socio.getNombre());
			System.out.println("1. Consultar equipos y cupos");
			System.out.println("2. Inscribirme a un equipo");
			System.out.println("3. Inscribirme a una disciplina");
			System.out.println("4. Reservar una instalacion");
			System.out.println("5. Consultar mis reservas");
			System.out.println("6. Comprar en tienda o cafeteria");
			System.out.println("0. Guardar y salir");
			int opcion = ConsolaUtil.leerEntero(entrada, "Opcion: ", 0, 6);
			if (opcion == 0 || opcion == -1) salir = true;
			else if (opcion == 1) consultarEquipos();
			else if (opcion == 2) inscribirseEquipo();
			else if (opcion == 3) inscribirseDisciplina();
			else if (opcion == 4) reservarInstalacion();
			else if (opcion == 5) consultarReservas();
			else if (opcion == 6) ConsolaCompra.mostrarMenu(club, socio, entrada);
		}
	}

	private void consultarEquipos() {
		List<Equipo> equipos = club.getEquipos();
		if (equipos.isEmpty()) {
			System.out.println("No hay equipos creados.");
			return;
		}
		for (Equipo equipo : equipos) {
			System.out.println(equipo.getNombre() + " - " + equipo.getDeporte().getNombre() + " - "
					+ equipo.getCategoria().getNombre() + " - jugadores: " + equipo.getJugadores().size()
					+ "/" + equipo.getCupoMaximo());
		}
	}

	private void inscribirseEquipo() {
		List<Equipo> equipos = club.getEquipos();
		for (int i = 0; i < equipos.size(); i++) {
			Equipo equipo = equipos.get(i);
			System.out.println((i + 1) + ". " + equipo.getNombre() + " - " + equipo.getDeporte().getNombre()
					+ " - " + equipo.getCategoria().getNombre());
		}
		int indice = ConsolaUtil.seleccionar(entrada, "Equipos disponibles", equipos.size());
		if (indice < 0) return;
		String posicion = ConsolaUtil.leerTexto(entrada, "Posicion: ");
		if (posicion == null || posicion.isEmpty()) return;
		int camiseta = ConsolaUtil.leerEntero(entrada, "Numero de camiseta: ", 1, 99);
		if (camiseta < 0) return;
		if (club.inscribirJugador(equipos.get(indice), socio, posicion, camiseta)) {
			System.out.println("Inscripcion realizada.");
		} else {
			System.out.println("No fue posible inscribirlo. Revise edad, cupo, camiseta y equipos actuales.");
		}
	}

	private void inscribirseDisciplina() {
		List<Disciplina> disciplinas = club.getDisciplinas();
		for (int i = 0; i < disciplinas.size(); i++) {
			System.out.println((i + 1) + ". " + disciplinas.get(i).getNombre());
		}
		int indice = ConsolaUtil.seleccionar(entrada, "Disciplinas", disciplinas.size());
		if (indice < 0) return;
		System.out.println("1. Principiante  2. Intermedio  3. Avanzado");
		int nivel = ConsolaUtil.leerEntero(entrada, "Nivel: ", 1, 3);
		if (nivel < 0) return;
		String[] niveles = { "Principiante", "Intermedio", "Avanzado" };
		if (club.inscribirEnDisciplina(socio, disciplinas.get(indice), niveles[nivel - 1])) {
			System.out.println("Inscripcion realizada.");
		} else {
			System.out.println("Ya existe una inscripcion o los datos no son validos.");
		}
	}

	private void reservarInstalacion() {
		List<Instalacion> instalaciones = club.getInstalaciones();
		for (int i = 0; i < instalaciones.size(); i++) {
			System.out.println((i + 1) + ". " + instalaciones.get(i).getNombreCancha() + " - "
					+ instalaciones.get(i).getDeporte());
		}
		int indice = ConsolaUtil.seleccionar(entrada, "Instalaciones", instalaciones.size());
		if (indice < 0) return;
		String fecha = ConsolaUtil.leerTexto(entrada, "Fecha (AAAA-MM-DD): ");
		int hora = ConsolaUtil.leerEntero(entrada, "Hora de inicio (0-23): ", 0, 23);
		int duracion = ConsolaUtil.leerEntero(entrada, "Duracion en horas: ", 1, 24);
		int cantidad = ConsolaUtil.leerEntero(entrada, "Cantidad de jugadores: ", 1, 4);
		if (fecha == null || hora < 0 || duracion < 0 || cantidad < 0) return;
		Reserva reserva = new Reserva(fecha, hora, duracion, cantidad, instalaciones.get(indice), socio);
		if (club.reservarInstalacion(reserva)) {
			System.out.println("Reserva realizada.");
		} else {
			System.out.println("La reserva no cumple las reglas o el horario ya esta ocupado.");
		}
	}

	private void consultarReservas() {
		if (socio.getMisReservas().isEmpty()) {
			System.out.println("No tiene reservas.");
			return;
		}
		for (Reserva reserva : socio.getMisReservas()) {
			System.out.println(reserva.getFecha() + " " + reserva.getHoraInicio() + ":00 por "
					+ reserva.getDuracionHoras() + " h - " + reserva.getNombreInstalacion());
		}
	}
}
