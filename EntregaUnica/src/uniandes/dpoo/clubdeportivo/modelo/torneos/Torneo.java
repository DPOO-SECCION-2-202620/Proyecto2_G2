package uniandes.dpoo.clubdeportivo.modelo.torneos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Torneo {

	private String nombre;
	private String deporteODisciplina;
	private String categoriaONivel;
	private TipoParticipante modalidad;
	private int cupos;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private LocalDate fechaLimitePreferencial;
	private List<ParticipanteTorneo> inscritos;
	private List<String> elegiblesPreferenciales;
	private List<String> inscritosPreferenciales;
	private List<String> historial;
	private List<EncuentroTorneo> encuentros;
	private ParticipanteTorneo ganador;
	private boolean resultadosCerrados;

	public Torneo(String nombre, String deporteODisciplina, String categoriaONivel, TipoParticipante modalidad,
			int cupos, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaLimitePreferencial) {
		this.nombre = nombre;
		this.deporteODisciplina = deporteODisciplina;
		this.categoriaONivel = categoriaONivel;
		this.modalidad = modalidad;
		this.cupos = cupos;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fechaLimitePreferencial = fechaLimitePreferencial;
		this.inscritos = new ArrayList<ParticipanteTorneo>();
		this.elegiblesPreferenciales = new ArrayList<String>();
		this.inscritosPreferenciales = new ArrayList<String>();
		this.historial = new ArrayList<String>();
		this.encuentros = new ArrayList<EncuentroTorneo>();
		this.ganador = null;
		this.resultadosCerrados = false;
	}

	public abstract boolean esCompetitivo();

	public abstract double calcularCobro(ParticipanteTorneo participante);

	public boolean esValido() {
		if (nombre == null || nombre.isBlank() || deporteODisciplina == null || deporteODisciplina.isBlank()) {
			return false;
		}
		if (modalidad == null || cupos <= 0) {
			return false;
		}
		if (fechaInicio == null || fechaFin == null || fechaLimitePreferencial == null) {
			return false;
		}
		if (fechaFin.isBefore(fechaInicio)) {
			return false;
		}
		return fechaLimitePreferencial.isAfter(fechaInicio) == false;
	}

	public EstadoTorneo getEstado(LocalDate fecha) {
		if (resultadosCerrados) {
			return EstadoTorneo.FINALIZADO;
		}
		if (fecha.isBefore(fechaInicio)) {
			return EstadoTorneo.ABIERTO;
		}
		return EstadoTorneo.EN_CURSO;
	}

	public int getCuposPreferenciales() {
		return (int) Math.ceil(cupos * 20 / 100.0);
	}

	public int getCuposDisponibles() {
		return cupos - inscritos.size();
	}

	public boolean enEtapaPreferencial(LocalDate fecha) {
		return fecha.isAfter(fechaLimitePreferencial) == false;
	}

	public void definirElegibles(List<ParticipanteTorneo> candidatos) {
		List<ParticipanteTorneo> ordenados = new ArrayList<ParticipanteTorneo>(candidatos);
		ordenados.sort((a, b) -> Double.compare(b.getPuntajeDesempeno(), a.getPuntajeDesempeno()));
		elegiblesPreferenciales.clear();
		for (int i = 0; i < ordenados.size() && i < getCuposPreferenciales(); i++) {
			elegiblesPreferenciales.add(ordenados.get(i).getIdentificacion());
		}
	}

	public boolean esElegible(String identificacion) {
		return elegiblesPreferenciales.contains(identificacion);
	}

	public ParticipanteTorneo buscarInscrito(String identificacion) {
		for (ParticipanteTorneo participante : inscritos) {
			if (participante.getIdentificacion().equals(identificacion)) {
				return participante;
			}
		}
		return null;
	}

	public boolean estaInscritaPersona(String identificacionPersona) {
		for (ParticipanteTorneo participante : inscritos) {
			if (participante.getIntegrantes().contains(identificacionPersona)) {
				return true;
			}
		}
		return false;
	}

	public boolean inscribir(ParticipanteTorneo participante, LocalDate fecha) {
		if (getEstado(fecha) != EstadoTorneo.ABIERTO || participante == null) {
			return false;
		}
		if (participante.estaCompleto() == false || participante.getTipo() != modalidad) {
			return false;
		}
		if (participante.esEmpleado() && modalidad == TipoParticipante.EQUIPO) {
			return false;
		}
		if (buscarInscrito(participante.getIdentificacion()) != null) {
			return false;
		}
		for (String persona : participante.getIntegrantes()) {
			if (estaInscritaPersona(persona)) {
				return false;
			}
		}
		boolean usaCupoPreferencial = false;
		if (enEtapaPreferencial(fecha)) {
			int usadosPreferenciales = inscritosPreferenciales.size();
			int usadosRegulares = inscritos.size() - usadosPreferenciales;
			if (esElegible(participante.getIdentificacion()) && usadosPreferenciales < getCuposPreferenciales()) {
				usaCupoPreferencial = true;
			} else if (usadosRegulares >= cupos - getCuposPreferenciales()) {
				return false;
			}
		} else if (inscritos.size() >= cupos) {
			return false;
		}
		participante.registrarPago(calcularCobro(participante));
		inscritos.add(participante);
		if (usaCupoPreferencial) {
			inscritosPreferenciales.add(participante.getIdentificacion());
		}
		registrarEvento(fecha, "Inscripcion de " + participante.getNombre());
		return true;
	}

	public boolean retirar(String identificacion, LocalDate fecha) {
		ParticipanteTorneo participante = buscarInscrito(identificacion);
		if (participante == null) {
			return false;
		}
		if (getEstado(fecha) != EstadoTorneo.ABIERTO) {
			registrarEvento(fecha, "Retiro rechazado de " + participante.getNombre());
			return false;
		}
		inscritos.remove(participante);
		inscritosPreferenciales.remove(identificacion);
		registrarEvento(fecha, "Retiro de " + participante.getNombre());
		return true;
	}

	public boolean finalizar(String identificacionGanador, LocalDate fecha) {
		if (getEstado(fecha) != EstadoTorneo.EN_CURSO) {
			return false;
		}
		ParticipanteTorneo candidato = buscarInscrito(identificacionGanador);
		if (candidato == null) {
			return false;
		}
		ganador = candidato;
		resultadosCerrados = true;
		registrarEvento(fecha, "Torneo finalizado. Ganador: " + candidato.getNombre());
		return true;
	}

	public boolean programarEncuentro(String idLocal, String idVisitante, String escenario, LocalDate fecha) {
		ParticipanteTorneo local = buscarInscrito(idLocal);
		ParticipanteTorneo visitante = buscarInscrito(idVisitante);
		if (local == null || visitante == null || local == visitante) {
			return false;
		}
		if (escenario == null || escenario.isBlank() || fecha == null) {
			return false;
		}
		if (fecha.isBefore(fechaInicio) || fecha.isAfter(fechaFin)) {
			return false;
		}
		encuentros.add(new EncuentroTorneo(local, visitante, escenario, fecha));
		registrarEvento(fecha, "Encuentro programado: " + local.getNombre() + " vs " + visitante.getNombre()
				+ " en " + escenario);
		return true;
	}

	public boolean registrarResultado(int numeroEncuentro, int marcadorLocal, int marcadorVisitante,
			LocalDate fecha) {
		if (getEstado(fecha) != EstadoTorneo.EN_CURSO) {
			return false;
		}
		if (numeroEncuentro < 1 || numeroEncuentro > encuentros.size()) {
			return false;
		}
		EncuentroTorneo encuentro = encuentros.get(numeroEncuentro - 1);
		if (encuentro.registrarResultado(marcadorLocal, marcadorVisitante) == false) {
			return false;
		}
		registrarEvento(fecha, "Resultado: " + encuentro);
		return true;
	}

	public List<EncuentroTorneo> getEncuentros() {
		return new ArrayList<EncuentroTorneo>(encuentros);
	}

	public List<EncuentroTorneo> getEncuentrosDe(String identificacion) {
		List<EncuentroTorneo> resultado = new ArrayList<EncuentroTorneo>();
		for (EncuentroTorneo encuentro : encuentros) {
			if (encuentro.involucra(identificacion)) {
				resultado.add(encuentro);
			}
		}
		return resultado;
	}

	public List<String> getHistorialDelDia(LocalDate fecha) {
		List<String> resultado = new ArrayList<String>();
		for (String evento : historial) {
			if (evento.startsWith(fecha.toString())) {
				resultado.add(evento);
			}
		}
		return resultado;
	}
	
	public void prepararDatos() {
		if (inscritos == null) inscritos = new ArrayList<ParticipanteTorneo>();
		if (elegiblesPreferenciales == null) elegiblesPreferenciales = new ArrayList<String>();
		if (inscritosPreferenciales == null) inscritosPreferenciales = new ArrayList<String>();
		if (historial == null) historial = new ArrayList<String>();
		if (encuentros == null) encuentros = new ArrayList<EncuentroTorneo>();
		if (ganador != null) {
			ParticipanteTorneo inscrito = buscarInscrito(ganador.getIdentificacion());
			if (inscrito != null) ganador = inscrito;
		}
	}
	
	protected void registrarEvento(LocalDate fecha, String descripcion) {
		historial.add(fecha + " - " + descripcion);
	}

	public List<ParticipanteTorneo> getInscritos() {
		return new ArrayList<ParticipanteTorneo>(inscritos);
	}

	public List<String> getElegiblesPreferenciales() {
		return new ArrayList<String>(elegiblesPreferenciales);
	}

	public List<String> getHistorial() {
		return new ArrayList<String>(historial);
	}

	public String getNombre() {
		return nombre;
	}

	public String getDeporteODisciplina() {
		return deporteODisciplina;
	}

	public String getCategoriaONivel() {
		return categoriaONivel;
	}

	public TipoParticipante getModalidad() {
		return modalidad;
	}

	public int getCupos() {
		return cupos;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public LocalDate getFechaLimitePreferencial() {
		return fechaLimitePreferencial;
	}

	public ParticipanteTorneo getGanador() {
		return ganador;
	}
}