package uniandes.dpoo.clubdeportivo.modelo.ventas;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.clubdeportivo.modelo.administracion.Existencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.UbicacionInventario;

public class ArticuloTienda {

	private String nombre;
	private String categoria;
	private double precio;
	private List<Existencia> existencias;

	public ArticuloTienda(String nombre, String categoria, double precio) {
		this.nombre = nombre;
		this.categoria = categoria;
		this.precio = precio;
		this.existencias = new ArrayList<Existencia>();
	}

	public void agregarExistencia(Existencia existencia) {
		if (existencia != null) {
			existencias.add(existencia);
		}
	}

	public Existencia getExistencia(UbicacionInventario ubicacion) {
		if (ubicacion == null || ubicacion.getNombre() == null) return null;
		if (existencias == null) existencias = new ArrayList<Existencia>();
		for (Existencia existencia : existencias) {
			if (existencia.getUbicacion() != null && existencia.getUbicacion().getNombre() != null
					&& existencia.getUbicacion().getNombre().equalsIgnoreCase(ubicacion.getNombre())) {
				return existencia;
			}
		}
		return null;
	}

	public int getCantidadDisponible() {
		// El total se calcula sumando las existencias de cada ubicacion.
		int cantidad = 0;
		for (Existencia existencia : existencias) {
			cantidad += existencia.getCantidad();
		}
		return cantidad;
	}

	public int getCantidadDisponible(UbicacionInventario ubicacion) {
		Existencia existencia = getExistencia(ubicacion);
		if (existencia == null) {
			return 0;
		}
		return existencia.getCantidad();
	}

	public List<Existencia> getExistencias() {
		return existencias;
	}

	public void setExistencias(List<Existencia> existencias) {
		this.existencias = existencias == null ? new ArrayList<Existencia>() : existencias;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return nombre + " (" + categoria + ")";
	}
}
