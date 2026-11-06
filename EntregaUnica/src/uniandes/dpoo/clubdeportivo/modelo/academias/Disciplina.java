package uniandes.dpoo.clubdeportivo.modelo.academias;

public class Disciplina {

	private String nombre;
	private int maximoInstalacionesSimultaneas;

	public Disciplina(String nombre, int maximoInstalacionesSimultaneas) {
		this.nombre = nombre;
		this.maximoInstalacionesSimultaneas = maximoInstalacionesSimultaneas;
	}

	public String getNombre() {
		return nombre;
	}

	public int getMaximoInstalacionesSimultaneas() {
		return maximoInstalacionesSimultaneas;
	}
}
