package uniandes.dpoo.clubdeportivo.modelo.competencias;

import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;

/**
 * Competencia que se juega como liga de todos contra todos, como el Torneo de
 * los Cerros: cada resultado otorga puntos y se construye una tabla de
 * posiciones (regla RD09).
 */
public class CompetenciaLiga extends Competencia {

	private TablaPosiciones tabla;

	public CompetenciaLiga(String nombre, String entidadOrganizadora, int temporada, int nominaMinima,
			Deporte deporte, Categoria categoriaHabilitada) {
		super(nombre, entidadOrganizadora, temporada, nominaMinima, deporte, categoriaHabilitada);
		this.tabla = new TablaPosiciones();
	}

	@Override
	public String getFormato() {
		return "liga de todos contra todos";
	}

	/**
	 * Cuantos puntos otorga una victoria en esta competencia.
	 *
	 * REGLA RD10: en futbol la victoria otorga 3 puntos. En baloncesto y
	 * voleibol no hay empates y siempre se define un ganador.
	 */
	public int getPuntosPorVictoria() {
		String nombreDeporte = getDeporte().getNombre();
		if (nombreDeporte.equals("Futbol") == true || nombreDeporte.equals("Fútbol") == true) {
			return 3;
		}
		return 2;
	}

	/** Cuantos puntos otorga un empate en esta competencia (RD10). */
	public int getPuntosPorEmpate() {
		String nombreDeporte = getDeporte().getNombre();
		if (nombreDeporte.equals("Futbol") == true || nombreDeporte.equals("Fútbol") == true) {
			return 1;
		}
		return 0;
	}

	/**
	 * Suma el resultado de un partido ya jugado a la tabla de posiciones.
	 *
	 * Hay que acumular el resultado para los dos participantes: para el local,
	 * su marcador va "a favor" y el del visitante "en contra"; para el
	 * visitante es al reves. Las filas se consiguen con tabla.buscarFila(...)
	 * y se actualizan con acumularResultado(...).
	 */
	@Override
	public void procesarPartido(PartidoCompetencia partido)
	{
		FilaTabla filaLocal= tabla.buscarFila(partido.getLocal());
		FilaTabla filaVisitante =tabla.buscarFila(partido.getVisitante());


		if(filaLocal == null || filaVisitante == null) {
			return;
		}

		int puntosPorVictoria=getPuntosPorVictoria();
		int puntosPorEmpate = getPuntosPorEmpate();


		filaLocal.acumularResultado(partido.getMarcadorLocal(),
				partido.getMarcadorVisitante(), puntosPorVictoria,puntosPorEmpate);

		filaVisitante.acumularResultado(partido.getMarcadorVisitante(),partido.getMarcadorLocal(),
					puntosPorVictoria, puntosPorEmpate);
	}

	public TablaPosiciones getTabla() {
		return tabla;
	}

	public void setTabla(TablaPosiciones tabla) {
		this.tabla = tabla;
	}
}
