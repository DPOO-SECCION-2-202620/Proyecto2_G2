package uniandes.dpoo.clubdeportivo.modelo.administracion;

public class UbicacionInventario {

	private String nombre;

	public UbicacionInventario(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
