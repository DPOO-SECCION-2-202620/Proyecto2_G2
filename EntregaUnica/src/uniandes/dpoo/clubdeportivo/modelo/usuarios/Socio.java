package uniandes.dpoo.clubdeportivo.modelo.usuarios;

import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import java.util.ArrayList;
import uniandes.dpoo.clubdeportivo.modelo.torneos.BonoDescuento;

public class Socio extends Usuario{

	private String identificacion;
	private String nombre;
	private int edad;
	private transient ArrayList<Reserva> misReservas;
	private ArrayList<BonoDescuento> bonos;


	public Socio(String identificacion, String login, String password, String nombre, int edad) {
        super(login, password);
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.edad = edad;
        this.misReservas = new ArrayList<>();
        this.bonos = new ArrayList<BonoDescuento>();
    }

	public String getIdentificacion() {
		return identificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public int getEdad() {
		return edad;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
	public void agregarReservaSocio(Reserva nuevaReserva) {
		if (misReservas == null) misReservas = new ArrayList<Reserva>();
		if (nuevaReserva != null && misReservas.contains(nuevaReserva) == false) misReservas.add(nuevaReserva);
    }

	public void reiniciarReservas() {
		misReservas = new ArrayList<Reserva>();
	}

	public ArrayList<Reserva> getMisReservas() {
		if (misReservas == null) {
			misReservas = new ArrayList<Reserva>();
		}
		return misReservas;
	}

	public void agregarBono(BonoDescuento bono) {
		if (bonos == null) {
			bonos = new ArrayList<BonoDescuento>();
		}
		if (bono != null) {
			bonos.add(bono);
		}
	}

	public BonoDescuento buscarBono(String codigo) {
		if (bonos == null || codigo == null) {
			return null;
		}
		for (BonoDescuento bono : bonos) {
			if (codigo.equals(bono.getCodigo())) {
				return bono;
			}
		}
		return null;
	}

	public ArrayList<BonoDescuento> getBonosDisponibles() {
		ArrayList<BonoDescuento> disponibles = new ArrayList<BonoDescuento>();
		if (bonos != null) {
			for (BonoDescuento bono : bonos) {
				if (bono.estaDisponible()) {
					disponibles.add(bono);
				}
			}
		}
		return disponibles;
	}

	public ArrayList<BonoDescuento> getBonos() {
		if (bonos == null) {
			bonos = new ArrayList<BonoDescuento>();
		}
		return bonos;
	}
}
