package uniandes.dpoo.clubdeportivo.modelo.competencias;

import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;

/**
 * Equipo que participa en una competencia externa. Puede ser un equipo del
 * propio club o un equipo de otra institucion, en cuyo caso no tiene equipo
 * asociado.
 */
public class Participante {

	private String nombre;
	private String institucion;
	private Equipo equipoClub;

	public Participante(String nombre, String institucion, Equipo equipoClub) {
		this.nombre = nombre;
		this.institucion = institucion;
		this.equipoClub = equipoClub;
	}

	/** Indica si este participante es un equipo del Club Deportivo Seneca. */
	public boolean esDelClub() {
		return equipoClub != null;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public Equipo getEquipoClub() {
		return equipoClub;
	}

	public void setEquipoClub(Equipo equipoClub) {
		this.equipoClub = equipoClub;
	}

	@Override
	public String toString() {
		return nombre + " (" + institucion + ")";
	}
}
