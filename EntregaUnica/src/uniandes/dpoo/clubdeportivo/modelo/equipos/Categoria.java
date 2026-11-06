package uniandes.dpoo.clubdeportivo.modelo.equipos;

/**
 * Categoria de edad de los equipos del club: Sub-13, Sub-15, Sub-17 y Mayores.
 */
public class Categoria {

	private String nombre;
	private int edadMinima;
	private int edadMaxima;

	public Categoria(String nombre, int edadMinima, int edadMaxima) {
		this.nombre = nombre;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
	}

	/**
	 * Indica si un jugador de la edad dada puede pertenecer a un equipo de esta
	 * categoria.
	 *
	 * REGLA RD03: un jugador solo puede inscribirse en un equipo si su edad
	 * corresponde a la categoria de ese equipo.
	 */
	public boolean admiteEdad(int edad) {
		
		if (edad >= edadMinima && edad <= edadMaxima) {
			return true;
		}
		return false;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdadMinima() {
		return edadMinima;
	}

	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}

	public int getEdadMaxima() {
		return edadMaxima;
	}

	public void setEdadMaxima(int edadMaxima) {
		this.edadMaxima = edadMaxima;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
