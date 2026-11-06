package uniandes.dpoo.clubdeportivo.modelo.competencias;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabla de posiciones de una competencia con formato de liga. Mantiene una fila
 * por participante y las ordena por puntos.
 */
public class TablaPosiciones {

	private List<FilaTabla> filas;

	public TablaPosiciones() {
		this.filas = new ArrayList<FilaTabla>();
	}

	/** Agrega una fila para un participante que todavia no esta en la tabla. */
	public void agregarParticipante(Participante participante) {
		if (buscarFila(participante) == null) {
			filas.add(new FilaTabla(participante));
		}
	}

	/** Devuelve la fila de un participante, o null si no esta en la tabla. */
	public FilaTabla buscarFila(Participante participante) {
		for (FilaTabla fila : filas) {
			if (fila.getParticipante() == participante) {
				return fila;
			}
		}
		return null;
	}

	/**
	 * Devuelve las filas ordenadas de mejor a peor.
	 *
	 * El criterio principal son los puntos. Cuando dos participantes tienen los
	 * mismos puntos, decide tu el criterio de desempate (la diferencia de
	 * marcador es el mas habitual) y dejalo anotado como decision de diseno.
	 */
	public List<FilaTabla> getFilasOrdenadas()
	{
		List<FilaTabla> copia=new ArrayList<FilaTabla>(filas);


		for(int i=0;i < copia.size();i++) {
			for (int j=i+1; j<copia.size(); j++)
			{
				FilaTabla filaActual=copia.get(i);
				FilaTabla filaSiguiente = copia.get(j);

				boolean tieneMasPuntos=filaSiguiente.getPuntos() > filaActual.getPuntos();
				boolean mismosPuntos = filaSiguiente.getPuntos()==filaActual.getPuntos();

				boolean mejorDiferencia=filaSiguiente.getDiferenciaMarcador()
						> filaActual.getDiferenciaMarcador();
				if (tieneMasPuntos == true ||
						(mismosPuntos == true && mejorDiferencia == true))
				{
					FilaTabla temporal = copia.get(i);
					copia.set(i,copia.get(j));
					copia.set(j, temporal);
				}
			}

		}
		return copia;
	}

	public List<FilaTabla> getFilas() {
		return filas;
	}

	public void setFilas(List<FilaTabla> filas) {
		this.filas = filas;
	}
}
