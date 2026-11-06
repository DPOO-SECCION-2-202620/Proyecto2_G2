package uniandes.dpoo.clubdeportivo.modelo;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.academias.Disciplina;
import uniandes.dpoo.clubdeportivo.modelo.academias.InscripcionDisciplina;
import uniandes.dpoo.clubdeportivo.modelo.academias.PartidoPractica;
import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import uniandes.dpoo.clubdeportivo.modelo.administracion.AdministracionComercial;
import uniandes.dpoo.clubdeportivo.modelo.administracion.UbicacionInventario;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Existencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.SolicitudCambioTurno;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Participante;
import uniandes.dpoo.clubdeportivo.modelo.competencias.PartidoCompetencia;
import uniandes.dpoo.clubdeportivo.modelo.competencias.FilaTabla;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Competencia;
import uniandes.dpoo.clubdeportivo.modelo.competencias.CompetenciaLiga;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.SesionEntrenamiento;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.EstadoAsistencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.Asistencia;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaPartido;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Partido;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Administrador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Empleado;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Fisioterapeuta;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ArticuloTienda;

public class Club {

	private int version;
	private ArrayList<Usuario> usuarios;
	private ArrayList<Equipo> equipos;
	private ArrayList<Partido> partidos;
	private ArrayList<Competencia> competencias;
	private ArrayList<Instalacion> instalaciones;
	private ArrayList<Disciplina> disciplinas;
	private ArrayList<InscripcionDisciplina> inscripcionesDisciplina;
	private ArrayList<Reserva> reservas;
	private ArrayList<PartidoPractica> partidosPractica;
	private ArrayList<SesionEntrenamiento> sesionesEntrenamiento;
	private AdministracionComercial administracion;

	public Club() {
		version = 1;
		usuarios = new ArrayList<Usuario>();
		equipos = new ArrayList<Equipo>();
		partidos = new ArrayList<Partido>();
		competencias = new ArrayList<Competencia>();
		instalaciones = new ArrayList<Instalacion>();
		disciplinas = new ArrayList<Disciplina>();
		inscripcionesDisciplina = new ArrayList<InscripcionDisciplina>();
		reservas = new ArrayList<Reserva>();
		partidosPractica = new ArrayList<PartidoPractica>();
		sesionesEntrenamiento = new ArrayList<SesionEntrenamiento>();
		administracion = new AdministracionComercial(new UbicacionInventario("Tienda"), LocalTime.of(6, 0), LocalTime.of(22, 0));
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios == null ? new ArrayList<Usuario>() : usuarios;
	}

	public void agregarUsuario(Usuario usuario) {
		if (usuario != null && usuario.getLogin() != null && buscarUsuario(usuario.getLogin()) == null) {
			usuarios.add(usuario);
		}
	}

	public Usuario autenticar(String login, String password) {
		if (login == null || password == null || usuarios == null) {
			return null;
		}
		for (Usuario usuario : usuarios) {
			if (login.equals(usuario.getLogin()) && password.equals(usuario.getPassword())) {
				return usuario;
			}
		}
		return null;
	}

	private Usuario buscarUsuario(String login) {
		for (Usuario usuario : usuarios) {
			if (login != null && login.equals(usuario.getLogin())) {
				return usuario;
			}
		}
		return null;
	}

	public boolean inscribirJugador(Equipo equipo, Socio socio, String posicion, int camiseta) {
		if (equipo == null || socio == null || socio.getIdentificacion() == null
				|| buscarSocio(socio.getIdentificacion()) == null || equipos.contains(equipo) == false) {
			return false;
		}
		for (Equipo equipoExistente : equipos) {
			if (equipoExistente.getDeporte() != null && equipo.getDeporte() != null
					&& equipoExistente.getDeporte().getNombre().equalsIgnoreCase(equipo.getDeporte().getNombre())) {
				for (JugadorEquipo jugador : equipoExistente.getJugadores()) {
					if (jugador.getJugador() != null && jugador.getJugador().getIdentificacion() != null
							&& jugador.getJugador().getIdentificacion().equals(socio.getIdentificacion())) {
						return false;
					}
				}
			}
		}
		return equipo.inscribirJugador(socio, posicion, camiseta) != null;
	}

	public boolean reservarInstalacion(Reserva reserva) {
		if (reserva == null || reserva.getInstalacionReservada() == null || reserva.esReservaValida() == false) {
			return false;
		}
		Instalacion instalacion = buscarInstalacion(reserva.getInstalacionReservada().getNombreCancha());
		if (instalacion == null || reserva.getSocioResponsable() == null
				|| buscarSocio(reserva.getSocioResponsable().getIdentificacion()) == null
				|| instalacion.verificarDisponibilidad(reserva.getFecha(), reserva.getHoraInicio(),
						reserva.getDuracionHoras()) == false) {
			return false;
		}
		Disciplina disciplina = buscarDisciplina(instalacion.getDeporte());
		if (disciplina == null || disciplina.getMaximoInstalacionesSimultaneas() <= 0
				|| contarReservasDeDisciplina(disciplina, reserva) >= disciplina.getMaximoInstalacionesSimultaneas()) {
			return false;
		}
		reserva.setInstalacionReservada(instalacion);
		instalacion.agregarReserva(reserva);
		reservas.add(reserva);
		if (reserva.getSocioResponsable() != null) {
			reserva.getSocioResponsable().agregarReservaSocio(reserva);
		}
		return true;
	}

	private int contarReservasDeDisciplina(Disciplina disciplina, Reserva nueva) {
		int cantidad = 0;
		for (Reserva reserva : reservas) {
			if (reserva.getFecha() != null && reserva.getFecha().equals(nueva.getFecha())
					&& reserva.getInstalacionReservada() != null
					&& reserva.getInstalacionReservada().getDeporte().equalsIgnoreCase(disciplina.getNombre())
					&& nueva.getHoraInicio() < reserva.getHoraInicio() + reserva.getDuracionHoras()
					&& reserva.getHoraInicio() < nueva.getHoraInicio() + nueva.getDuracionHoras()) {
				cantidad++;
			}
		}
		return cantidad;
	}

	private Disciplina buscarDisciplina(String nombre) {
		for (Disciplina disciplina : disciplinas) {
			if (disciplina != null && disciplina.getNombre() != null && nombre != null
					&& disciplina.getNombre().equalsIgnoreCase(nombre)) {
				return disciplina;
			}
		}
		return null;
	}

	public boolean inscribirEnDisciplina(Socio socio, Disciplina disciplina, String nivel) {
		if (socio == null || disciplina == null || nivel == null
				|| buscarSocio(socio.getIdentificacion()) == null
				|| buscarDisciplina(disciplina.getNombre()) == null
				|| (nivel.equalsIgnoreCase("Principiante") == false && nivel.equalsIgnoreCase("Intermedio") == false
						&& nivel.equalsIgnoreCase("Avanzado") == false)) {
			return false;
		}
		for (InscripcionDisciplina inscripcion : inscripcionesDisciplina) {
			if (inscripcion.getSocio() != null && inscripcion.getDisciplina() != null
					&& inscripcion.getSocio().getIdentificacion().equals(socio.getIdentificacion())
					&& inscripcion.getDisciplina().getNombre().equalsIgnoreCase(disciplina.getNombre())) {
				return false;
			}
		}
		inscripcionesDisciplina.add(new InscripcionDisciplina(socio, disciplina, nivel));
		return true;
	}

	public boolean registrarAsistencia(SesionEntrenamiento sesion, Socio socio, EstadoAsistencia estado) {
		return sesion != null && sesion.registrarAsistencia(socio, estado);
	}

	public PartidoPractica programarPartidoPractica(Reserva reserva, List<Socio> jugadores) {
		if (reserva == null || jugadores == null || jugadores.size() != reserva.getCantidadJugadores()
				|| reservas.contains(reserva) == false || reserva.esReservaValida() == false) {
			return null;
		}
		String nombreDisciplina = reserva.getInstalacionReservada().getDeporte();
		String nivelBase = null;
		for (Socio socio : jugadores) {
			if (socio == null || buscarSocio(socio.getIdentificacion()) == null) {
				return null;
			}
			InscripcionDisciplina inscripcion = buscarInscripcion(socio, nombreDisciplina);
			if (inscripcion == null) {
				return null;
			}
			if (nivelBase == null) {
				nivelBase = inscripcion.getNivel();
			} else if (Math.abs(indiceNivel(nivelBase) - indiceNivel(inscripcion.getNivel())) > 1) {
				return null;
			}
		}
		PartidoPractica partido = new PartidoPractica(reserva);
		for (Socio socio : jugadores) {
			if (partido.agregarJugador(socio) == false) {
				return null;
			}
		}
		partidosPractica.add(partido);
		return partido;
	}

	public boolean finalizarPartidoPractica(PartidoPractica partido, String resultado, Socio ganador, Socio perdedor) {
		if (partido == null || partidosPractica.contains(partido) == false || resultado == null
				|| resultado.trim().isEmpty() || ganador == null || perdedor == null
				|| ganador.getIdentificacion().equals(perdedor.getIdentificacion())
				|| partido.getResultadoFinal().equals("Pendiente") == false
				|| buscarSocio(ganador.getIdentificacion()) == null || buscarSocio(perdedor.getIdentificacion()) == null
				|| buscarJugadorConfirmado(partido, ganador.getIdentificacion()) == false
				|| buscarJugadorConfirmado(partido, perdedor.getIdentificacion()) == false) {
			return false;
		}
		partido.finalizarPartido(resultado, ganador, perdedor);
		InscripcionDisciplina inscripcionGanador = buscarInscripcion(ganador,
				partido.getReservaAsociada().getInstalacionReservada().getDeporte());
		InscripcionDisciplina inscripcionPerdedor = buscarInscripcion(perdedor,
				partido.getReservaAsociada().getInstalacionReservada().getDeporte());
		if (inscripcionGanador != null && inscripcionPerdedor != null) {
			inscripcionGanador.setPuntajeRanking(inscripcionGanador.getPuntajeRanking() + 20);
			inscripcionPerdedor.setPuntajeRanking(Math.max(0, inscripcionPerdedor.getPuntajeRanking() - 20));
		}
		return true;
	}

	private boolean buscarJugadorConfirmado(PartidoPractica partido, String identificacion) {
		for (Socio socio : partido.getJugadoresConfirmados()) {
			if (socio != null && socio.getIdentificacion().equals(identificacion)) {
				return true;
			}
		}
		return false;
	}

	private InscripcionDisciplina buscarInscripcion(Socio socio, String nombreDisciplina) {
		if (socio == null) {
			return null;
		}
		for (InscripcionDisciplina inscripcion : inscripcionesDisciplina) {
			if (inscripcion.getSocio() != null && inscripcion.getDisciplina() != null
					&& inscripcion.getSocio().getIdentificacion().equals(socio.getIdentificacion())
					&& inscripcion.getDisciplina().getNombre().equalsIgnoreCase(nombreDisciplina)) {
				return inscripcion;
			}
		}
		return null;
	}

	private int indiceNivel(String nivel) {
		return Arrays.asList("principiante", "intermedio", "avanzado").indexOf(nivel.toLowerCase());
	}

	public boolean agregarEquipo(Equipo equipo) {
		if (equipo == null || equipo.getNombre() == null || equipo.getNombre().trim().isEmpty()
				|| equipo.getDeporte() == null || equipo.getCategoria() == null || equipo.getCupoMaximo() <= 0) {
			return false;
		}
		for (Equipo existente : equipos) {
			if (existente.getNombre().equalsIgnoreCase(equipo.getNombre())) {
				return false;
			}
		}
		equipos.add(equipo);
		return true;
	}

	public boolean agregarDisciplina(Disciplina disciplina) {
		if (disciplina == null || disciplina.getNombre() == null || disciplina.getNombre().trim().isEmpty()
				|| disciplina.getMaximoInstalacionesSimultaneas() <= 0
				|| buscarDisciplina(disciplina.getNombre()) != null) {
			return false;
		}
		disciplinas.add(disciplina);
		return true;
	}

	public boolean agregarInstalacion(Instalacion instalacion) {
		if (instalacion == null || instalacion.getNombreCancha() == null
				|| buscarDisciplina(instalacion.getDeporte()) == null
				|| buscarInstalacion(instalacion.getNombreCancha()) != null) {
			return false;
		}
		instalaciones.add(instalacion);
		return true;
	}

	public boolean agregarSesionEntrenamiento(SesionEntrenamiento sesion) {
		if (sesion == null || sesion.getEntrenadorACargo() == null || sesion.getInstalacion() == null
				|| sesion.getFecha() == null || sesion.getHoraInicio() < 0
				|| instalaciones.contains(sesion.getInstalacion()) == false
				|| usuarios.contains(sesion.getEntrenadorACargo()) == false
				|| sesion.getInstalacion().verificarDisponibilidad(sesion.getFecha(), sesion.getHoraInicio()) == false) {
			return false;
		}
		try {
			LocalDate.parse(sesion.getFecha());
		} catch (Exception e) {
			return false;
		}
		for (SesionEntrenamiento existente : sesionesEntrenamiento) {
			if (existente.getFecha().equals(sesion.getFecha()) && existente.getHoraInicio() == sesion.getHoraInicio()
					&& existente.getInstalacion().getNombreCancha().equalsIgnoreCase(
							sesion.getInstalacion().getNombreCancha())) return false;
		}
		sesionesEntrenamiento.add(sesion);
		return true;
	}

	public boolean equipoCumpleMinimo(Equipo equipo) {
		return equipo != null && equipo.getDeporte() != null
				&& equipo.contarJugadoresHabilitados() >= equipo.getDeporte().getMinimoJugadoresHabilitados();
	}

	public List<JugadorEquipo> jugadoresNoHabilitados(Equipo equipo) {
		ArrayList<JugadorEquipo> resultado = new ArrayList<JugadorEquipo>();
		if (equipo != null) {
			for (JugadorEquipo jugador : equipo.getJugadores()) {
				if (jugador.estaSuspendido()) {
					resultado.add(jugador);
				}
			}
		}
		return resultado;
	}

	private Instalacion buscarInstalacion(String nombre) {
		for (Instalacion instalacion : instalaciones) {
			if (instalacion != null && instalacion.getNombreCancha() != null && nombre != null
					&& instalacion.getNombreCancha().equalsIgnoreCase(nombre)) {
				return instalacion;
			}
		}
		return null;
	}

	public void reconstruirRelaciones() {
		if (usuarios == null) usuarios = new ArrayList<Usuario>();
		if (equipos == null) equipos = new ArrayList<Equipo>();
		if (partidos == null) partidos = new ArrayList<Partido>();
		if (competencias == null) competencias = new ArrayList<Competencia>();
		if (instalaciones == null) instalaciones = new ArrayList<Instalacion>();
		if (disciplinas == null) disciplinas = new ArrayList<Disciplina>();
		if (inscripcionesDisciplina == null) inscripcionesDisciplina = new ArrayList<InscripcionDisciplina>();
		if (reservas == null) reservas = new ArrayList<Reserva>();
		if (partidosPractica == null) partidosPractica = new ArrayList<PartidoPractica>();
		if (sesionesEntrenamiento == null) sesionesEntrenamiento = new ArrayList<SesionEntrenamiento>();
		if (administracion == null) {
			administracion = new AdministracionComercial(new UbicacionInventario("Tienda"),
					LocalTime.of(6, 0), LocalTime.of(22, 0));
		}
		administracion.prepararDatos();

		for (Usuario usuario : usuarios) {
			if (usuario instanceof Socio) {
				((Socio) usuario).reiniciarReservas();
			}
		}
		for (Equipo equipo : equipos) {
			for (JugadorEquipo jugador : equipo.getJugadores()) {
				jugador.setEquipo(equipo);
				if (jugador.getJugador() != null) {
					Socio socio = buscarSocio(jugador.getJugador().getIdentificacion());
					if (socio != null) jugador.setJugador(socio);
				}
			}
		}
		for (Partido partido : partidos) {
			Equipo local = buscarEquipo(partido.getEquipoLocal() == null ? null : partido.getEquipoLocal().getNombre());
			Equipo visitante = buscarEquipo(partido.getEquipoVisitante() == null ? null : partido.getEquipoVisitante().getNombre());
			if (local != null) partido.setEquipoLocal(local);
			if (visitante != null) partido.setEquipoVisitante(visitante);
			for (EstadisticaPartido estadistica : partido.getEstadisticas()) {
				if (estadistica.getJugador() != null && estadistica.getJugador().getJugador() != null) {
					String id = estadistica.getJugador().getJugador().getIdentificacion();
					JugadorEquipo jugador = buscarJugador(local, id);
					if (jugador == null) jugador = buscarJugador(visitante, id);
					if (jugador != null) estadistica.setJugador(jugador);
				}
			}
		}
		for (Competencia competencia : competencias) {
			for (Participante participante : competencia.getParticipantes()) {
				if (participante.getEquipoClub() != null) {
					Equipo equipo = buscarEquipo(participante.getEquipoClub().getNombre());
					if (equipo != null) participante.setEquipoClub(equipo);
				}
			}
			for (PartidoCompetencia partido : competencia.getPartidos()) {
				partido.setLocal(buscarParticipante(competencia,
						partido.getLocal() == null ? null : partido.getLocal().getNombre()));
				partido.setVisitante(buscarParticipante(competencia,
						partido.getVisitante() == null ? null : partido.getVisitante().getNombre()));
			}
			if (competencia instanceof CompetenciaLiga) {
				for (FilaTabla fila : ((CompetenciaLiga) competencia).getTabla().getFilas()) {
					if (fila.getParticipante() != null) {
						fila.setParticipante(buscarParticipante(competencia, fila.getParticipante().getNombre()));
					}
				}
			}
		}
		for (Instalacion instalacion : instalaciones) {
			instalacion.setReservas(new ArrayList<Reserva>());
		}
		for (Reserva reserva : reservas) {
			Instalacion instalacion = buscarInstalacion(reserva.getNombreInstalacion());
			if (instalacion != null) {
				reserva.setInstalacionReservada(instalacion);
				instalacion.agregarReserva(reserva);
			}
			Socio socio = buscarSocio(reserva.getIdentificacionSocio());
			if (socio != null) {
				reserva.setSocioResponsable(socio);
				socio.agregarReservaSocio(reserva);
			}
		}
		for (InscripcionDisciplina inscripcion : inscripcionesDisciplina) {
			Socio socio = inscripcion.getSocio() == null ? null : buscarSocio(inscripcion.getSocio().getIdentificacion());
			Disciplina disciplina = inscripcion.getDisciplina() == null ? null
					: buscarDisciplina(inscripcion.getDisciplina().getNombre());
			if (socio != null) {
				inscripcion.setSocio(socio);
			}
			if (disciplina != null) {
				inscripcion.setDisciplina(disciplina);
			}
		}
		for (PartidoPractica partido : partidosPractica) {
			Reserva reserva = buscarReserva(partido.getReservaAsociada());
			if (reserva != null) partido.setReservaAsociada(reserva);
			ArrayList<Socio> jugadores = new ArrayList<Socio>();
			for (Socio socio : partido.getJugadoresConfirmados()) {
				Socio socioClub = buscarSocio(socio.getIdentificacion());
				if (socioClub != null) jugadores.add(socioClub);
			}
			partido.setJugadoresConfirmados(jugadores);
		}
		for (SesionEntrenamiento sesion : sesionesEntrenamiento) {
			Usuario entrenador = sesion.getEntrenadorACargo() == null ? null
					: buscarUsuario(sesion.getEntrenadorACargo().getLogin());
			if (entrenador instanceof Entrenador) sesion.setEntrenadorACargo((Entrenador) entrenador);
			Instalacion instalacion = buscarInstalacion(
					sesion.getInstalacion() == null ? null : sesion.getInstalacion().getNombreCancha());
			if (instalacion != null) sesion.setInstalacion(instalacion);
			ArrayList<Socio> convocados = new ArrayList<Socio>(sesion.getJugadoresConvocados());
			sesion.getJugadoresConvocados().clear();
			for (Socio socio : convocados) {
				Socio socioClub = buscarSocio(socio.getIdentificacion());
				if (socioClub != null) sesion.getJugadoresConvocados().add(socioClub);
			}
			for (Asistencia asistencia : sesion.getAsistencias()) {
				if (asistencia.getSocio() != null) {
					Socio socio = buscarSocio(asistencia.getSocio().getIdentificacion());
					if (socio != null) asistencia.setSocio(socio);
				}
			}
		}
		reconstruirAdministracion();
		for (Usuario usuario : usuarios) {
			if (usuario instanceof Empleado) {
				Empleado empleado = (Empleado) usuario;
				administracion.registrarCodigoDescuento(empleado.getCodigoDescuento(), empleado.getLogin());
			}
		}
	}

	private void reconstruirAdministracion() {
		Map<String, UbicacionInventario> ubicaciones = new HashMap<String, UbicacionInventario>();
		UbicacionInventario tienda = administracion.getUbicacionTienda();
		if (tienda.getNombre() != null) ubicaciones.put(tienda.getNombre().toUpperCase(), tienda);
		for (ArticuloTienda articulo : administracion.getArticulos()) {
			if (articulo.getExistencias() == null) articulo.setExistencias(null);
			for (Existencia existencia : articulo.getExistencias()) {
				if (existencia.getUbicacion() == null || existencia.getUbicacion().getNombre() == null) continue;
				String nombre = existencia.getUbicacion().getNombre();
				UbicacionInventario ubicacion = ubicaciones.get(nombre.toUpperCase());
				if (ubicacion == null) {
					ubicacion = existencia.getUbicacion();
					ubicaciones.put(nombre.toUpperCase(), ubicacion);
				}
				existencia.setUbicacion(ubicacion);
			}
		}
		for (SolicitudCambioTurno solicitud : administracion.getSolicitudesCambio()) {
			Turno original = solicitud.getTurnoActual() == null ? null : buscarTurno(solicitud.getTurnoActual());
			if (original != null) solicitud.setTurnoActual(original);
		}
	}

	private Turno buscarTurno(Turno turnoBuscado) {
		for (Turno turno : administracion.getTurnos()) {
			if (turno != null && turnoBuscado != null
					&& turno.getIdentificacionEmpleado().equals(turnoBuscado.getIdentificacionEmpleado())
					&& turno.getTipoEmpleado().equals(turnoBuscado.getTipoEmpleado())
					&& turno.getDiaSemana().equals(turnoBuscado.getDiaSemana())
					&& turno.getHoraInicio().equals(turnoBuscado.getHoraInicio())
					&& turno.getHoraFin().equals(turnoBuscado.getHoraFin())) {
				return turno;
			}
		}
		return null;
	}

	private Reserva buscarReserva(Reserva reservaBuscada) {
		if (reservaBuscada == null) return null;
		for (Reserva reserva : reservas) {
			if (reserva.getNombreInstalacion() != null && reserva.getFecha() != null
					&& reserva.getIdentificacionSocio() != null
					&& reserva.getNombreInstalacion().equals(reservaBuscada.getNombreInstalacion())
					&& reserva.getFecha().equals(reservaBuscada.getFecha())
					&& reserva.getHoraInicio() == reservaBuscada.getHoraInicio()
					&& reserva.getIdentificacionSocio().equals(reservaBuscada.getIdentificacionSocio())) {
				return reserva;
			}
		}
		return null;
	}

	private Participante buscarParticipante(Competencia competencia, String nombre) {
		for (Participante participante : competencia.getParticipantes()) {
			if (nombre != null && nombre.equals(participante.getNombre())) return participante;
		}
		return null;
	}

	private Equipo buscarEquipo(String nombre) {
		for (Equipo equipo : equipos) {
			if (nombre != null && nombre.equals(equipo.getNombre())) return equipo;
		}
		return null;
	}

	private JugadorEquipo buscarJugador(Equipo equipo, String identificacion) {
		if (equipo == null) return null;
		for (JugadorEquipo jugador : equipo.getJugadores()) {
			if (jugador.getJugador() != null && identificacion != null
					&& identificacion.equals(jugador.getJugador().getIdentificacion())) return jugador;
		}
		return null;
	}

	private Socio buscarSocio(String identificacion) {
		for (Usuario usuario : usuarios) {
			if (usuario instanceof Socio && ((Socio) usuario).getIdentificacion() != null
					&& ((Socio) usuario).getIdentificacion().equals(identificacion)) {
				return (Socio) usuario;
			}
		}
		return null;
	}

	public List<Equipo> getEquipos() {
		return equipos;
	}

	public void setEquipos(ArrayList<Equipo> equipos) {
		this.equipos = equipos == null ? new ArrayList<Equipo>() : equipos;
	}

	public List<Partido> getPartidos() {
		return partidos;
	}

	public void setPartidos(ArrayList<Partido> partidos) {
		this.partidos = partidos == null ? new ArrayList<Partido>() : partidos;
	}

	public List<Competencia> getCompetencias() {
		return competencias;
	}

	public void setCompetencias(ArrayList<Competencia> competencias) {
		this.competencias = competencias == null ? new ArrayList<Competencia>() : competencias;
	}

	public List<Instalacion> getInstalaciones() {
		return instalaciones;
	}

	public void setInstalaciones(ArrayList<Instalacion> instalaciones) {
		this.instalaciones = instalaciones == null ? new ArrayList<Instalacion>() : instalaciones;
	}

	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(ArrayList<Disciplina> disciplinas) {
		this.disciplinas = disciplinas == null ? new ArrayList<Disciplina>() : disciplinas;
	}

	public List<InscripcionDisciplina> getInscripcionesDisciplina() {
		return inscripcionesDisciplina;
	}

	public void setInscripcionesDisciplina(ArrayList<InscripcionDisciplina> inscripciones) {
		this.inscripcionesDisciplina = inscripciones == null ? new ArrayList<InscripcionDisciplina>() : inscripciones;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas == null ? new ArrayList<Reserva>() : reservas;
	}

	public List<PartidoPractica> getPartidosPractica() {
		return partidosPractica;
	}

	public void setPartidosPractica(ArrayList<PartidoPractica> partidosPractica) {
		this.partidosPractica = partidosPractica == null ? new ArrayList<PartidoPractica>() : partidosPractica;
	}

	public List<SesionEntrenamiento> getSesionesEntrenamiento() {
		return sesionesEntrenamiento;
	}

	public void setSesionesEntrenamiento(ArrayList<SesionEntrenamiento> sesiones) {
		this.sesionesEntrenamiento = sesiones == null ? new ArrayList<SesionEntrenamiento>() : sesiones;
	}

	public AdministracionComercial getAdministracion() {
		return administracion;
	}

	public void setAdministracion(AdministracionComercial administracion) {
		this.administracion = administracion;
	}

	public int getVersion() {
		return version;
	}
}
