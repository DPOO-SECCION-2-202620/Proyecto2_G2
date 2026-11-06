package uniandes.dpoo.clubdeportivo.modelo.academias;
import java.util.ArrayList;

public class Instalacion {
    private String nombreCancha; 
    private String deporte; 
    private boolean disponible;
    private transient ArrayList<Reserva> reservas;

    public Instalacion(String nombreCancha, String deporte) {
        this.nombreCancha = nombreCancha;
        this.deporte = deporte;
        this.disponible = true;
        this.reservas = new ArrayList<>();
    }

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public String getNombreCancha() {
		return nombreCancha;
	}

	public String getDeporte() {
		return deporte;
	}
	
	public boolean verificarDisponibilidad(String fecha, int horaInicio) {
        return verificarDisponibilidad(fecha, horaInicio, 1);
    }

    public boolean verificarDisponibilidad(String fecha, int horaInicio, int duracion) {
        if (disponible == false || reservas == null || fecha == null || horaInicio < 0 || duracion <= 0
				|| horaInicio + duracion > 24) return false;
        for (Reserva reservaExistente : this.reservas) {
            int finNuevo = horaInicio + duracion;
            if (reservaExistente.getFecha() != null && reservaExistente.getFecha().equals(fecha)
                    && horaInicio < reservaExistente.getHoraInicio() + reservaExistente.getDuracionHoras()
                    && reservaExistente.getHoraInicio() < finNuevo) {
                return false; 
            }
        }
        return true; 
    }

    public void agregarReserva(Reserva nuevaReserva) {
        if (reservas == null) reservas = new ArrayList<Reserva>();
        this.reservas.add(nuevaReserva);
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }
}
