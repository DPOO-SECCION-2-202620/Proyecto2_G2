package uniandes.dpoo.clubdeportivo.modelo.usuarios;

import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import java.util.ArrayList;

public class Socio extends Usuario{

	private String identificacion;
	private String nombre;
	private int edad;
	private transient ArrayList<Reserva> misReservas;


	public Socio(String identificacion, String login, String password, String nombre, int edad) {
        super(login, password);
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.edad = edad;
        this.misReservas = new ArrayList<>();
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
}
