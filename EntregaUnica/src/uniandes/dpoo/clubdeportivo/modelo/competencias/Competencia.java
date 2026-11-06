package uniandes.dpoo.clubdeportivo.modelo.competencias;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;

/**
 * Competencia externa en la que el club inscribe equipos (regla RD07). Es
 * abstracta porque el formato de juego cambia entre circuitos: el Torneo de los
 * Cerros se juega como liga y ASCUN por fases.
 */
public abstract class Competencia {

	private String nombre;
	private String entidadOrganizadora;
	private int temporada;
	private int nominaMinima;
	private Deporte deporte;
	private Categoria categoriaHabilitada;
	private List<Participante> participantes;
	private List<PartidoCompetencia> partidos;

	public Competencia(String nombre, String entidadOrganizadora, int temporada, int nominaMinima, Deporte deporte,
			Categoria categoriaHabilitada) {
		this.nombre = nombre;
		this.entidadOrganizadora = entidadOrganizadora;
		this.temporada = temporada;
		this.nominaMinima = nominaMinima;
		this.deporte = deporte;
		this.categoriaHabilitada = categoriaHabilitada;
		this.participantes = new ArrayList<Participante>();
		this.partidos = new ArrayList<PartidoCompetencia>();
	}

	/** Nombre del formato, para mostrarlo por consola. */
	public abstract String getFormato();

	/** Procesa un partido ya jugado segun el formato de la competencia. */
	public abstract void procesarPartido(PartidoCompetencia partido);

	/**
	 * Inscribe un equipo del club en la competencia.
	 *
	 * REGLA RD08: para inscribirse, el equipo debe cumplir los requisitos del
	 * circuito. Hay que verificar dos cosas antes de aceptarlo:
	 * 1. que la categoria del equipo sea la categoria habilitada de la competencia
	 * 2. que la nomina tenga al menos nominaMinima jugadores habilitados
	 *
	 * La nomina que se recibe es la lista de jugadores que el club inscribe;
	 * solo esos podran disputar los partidos oficiales. Recuerda que un jugador
	 * suspendido no cuenta como habilitado.
	 *
	 * Devuelve el Participante creado, o null si la inscripcion se rechaza.
	 */
	public Participante inscribirEquipo(Equipo equipo,List<JugadorEquipo> nomina)
	{
		if(equipo.getCategoria().getNombre().equals(categoriaHabilitada.getNombre()) == false) {
			return null;
		}


		if (equipo.getDeporte().getNombre().equals(deporte.getNombre()) == false)
		{
			return null;
		}
		int jugadoresHabilitados=0;

		for(JugadorEquipo jugador : nomina) {
			if (jugador.estaSuspendido() == false)
				jugadoresHabilitados++;
		}


		if(jugadoresHabilitados < nominaMinima) {
			return null;
		}

		Participante nuevo=new Participante(equipo.getNombre(),
				"Club Deportivo Seneca", equipo);
		participantes.add(nuevo);

		return nuevo;
	}

	/** Agrega un rival de otra institucion a la competencia. */
	public Participante inscribirRival(String nombre, String institucion) {
		Participante rival = new Participante(nombre, institucion, null);
		participantes.add(rival);
		return rival;
	}

	/** Programa un partido entre dos participantes ya inscritos. */
	public PartidoCompetencia programarPartido(PartidoCompetencia partido) {
		partidos.add(partido);
		return partido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEntidadOrganizadora() {
		return entidadOrganizadora;
	}

	public void setEntidadOrganizadora(String entidadOrganizadora) {
		this.entidadOrganizadora = entidadOrganizadora;
	}

	public int getTemporada() {
		return temporada;
	}

	public void setTemporada(int temporada) {
		this.temporada = temporada;
	}

	public int getNominaMinima() {
		return nominaMinima;
	}

	public void setNominaMinima(int nominaMinima) {
		this.nominaMinima = nominaMinima;
	}

	public Deporte getDeporte() {
		return deporte;
	}

	public void setDeporte(Deporte deporte) {
		this.deporte = deporte;
	}

	public Categoria getCategoriaHabilitada() {
		return categoriaHabilitada;
	}

	public void setCategoriaHabilitada(Categoria categoriaHabilitada) {
		this.categoriaHabilitada = categoriaHabilitada;
	}

	public List<Participante> getParticipantes() {
		return participantes;
	}

	public List<PartidoCompetencia> getPartidos() {
		return partidos;
	}

	@Override
	public String toString() {
		return nombre + " " + temporada + " (" + entidadOrganizadora + ", " + deporte.getNombre() + ", "
				+ categoriaHabilitada.getNombre() + ", " + getFormato() + ")";
	}
}
