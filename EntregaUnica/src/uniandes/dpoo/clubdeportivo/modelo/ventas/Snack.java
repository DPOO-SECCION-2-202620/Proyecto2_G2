package uniandes.dpoo.clubdeportivo.modelo.ventas;

import java.util.ArrayList;
import java.util.List;

public class Snack extends ProductoCafeteria {

	private List<String> alergenos;

	public Snack(String nombre, double precio, int cantidadDisponible, List<String> alergenos) {
		super(nombre, precio, cantidadDisponible);
		this.alergenos = new ArrayList<String>();
		if (alergenos != null) {
			this.alergenos.addAll(alergenos);
		}
	}

	public List<String> getAlergenos() {
		return new ArrayList<String>(alergenos);
	}

	public void setAlergenos(List<String> alergenos) {
		this.alergenos = new ArrayList<String>();
		if (alergenos != null) {
			this.alergenos.addAll(alergenos);
		}
	}
}
