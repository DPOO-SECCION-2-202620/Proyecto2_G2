package uniandes.dpoo.clubdeportivo.modelo.usuarios;

import uniandes.dpoo.clubdeportivo.modelo.torneos.Vinculacion;

public class UsuarioExterno extends Usuario {

	private String identificacion;
	private String nombre;
	private Vinculacion vinculacion;

	public UsuarioExterno(String identificacion, String login, String password, String nombre,
			Vinculacion vinculacion) {
		super(login, password);
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.vinculacion = vinculacion;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public Vinculacion getVinculacion() {
		return vinculacion;
	}

	@Override
	public String toString() {
		return nombre;
	}
}