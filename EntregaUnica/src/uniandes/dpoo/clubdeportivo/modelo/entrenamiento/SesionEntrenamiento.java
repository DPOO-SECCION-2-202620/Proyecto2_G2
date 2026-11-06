package uniandes.dpoo.clubdeportivo.modelo.entrenamiento;

import java.util.ArrayList;

import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

public class SesionEntrenamiento {
    private String fecha;
    private int horaInicio;
    private Entrenador entrenadorACargo;
    private Instalacion instalacion;
    private ArrayList<Socio> jugadoresConvocados;
    private ArrayList<Asistencia> asistencias;

    public SesionEntrenamiento(String fecha, int horaInicio, Entrenador entrenador, Instalacion instalacion) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.entrenadorACargo = entrenador;
        this.instalacion = instalacion;
        this.jugadoresConvocados = new ArrayList<>();
        this.asistencias = new ArrayList<Asistencia>();
    }

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public int getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(int horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Entrenador getEntrenadorACargo() {
		return entrenadorACargo;
	}

	public void setEntrenadorACargo(Entrenador entrenadorACargo) {
		this.entrenadorACargo = entrenadorACargo;
	}

	public Instalacion getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(Instalacion instalacion) {
		this.instalacion = instalacion;
	}

	public ArrayList<Socio> getJugadoresConvocados() {
		if (jugadoresConvocados == null) jugadoresConvocados = new ArrayList<Socio>();
		return jugadoresConvocados;
	}
	
    public void registrarAsistenciaJugador(Socio socio) {
        if (socio != null && jugadoresConvocados.size() < 4 && jugadoresConvocados.contains(socio) == false) {
            jugadoresConvocados.add(socio);
            System.out.println("El jugador " + socio.getLogin() + " ha sido registrado en el entrenamiento.");
        } else {
            System.out.println("Error: Capacidad máxima del entrenamiento alcanzada (4 jugadores).");
        }
    }

	public boolean convocar(Socio socio) {
		if (socio == null || jugadoresConvocados.size() >= 4 || jugadoresConvocados.contains(socio)) {
			return false;
		}
		jugadoresConvocados.add(socio);
		return true;
	}

	public boolean registrarAsistencia(Socio socio, EstadoAsistencia estado) {
		if (socio == null || estado == null || jugadoresConvocados.contains(socio) == false) {
			return false;
		}
		for (Asistencia asistencia : asistencias) {
			if (asistencia.getSocio().getIdentificacion().equals(socio.getIdentificacion())) {
				return false;
			}
		}
		asistencias.add(new Asistencia(socio, estado));
		return true;
	}

	public boolean registrarEvaluacion(Socio socio, int nota, String comentario) {
		if (socio == null) return false;
		for (Asistencia asistencia : asistencias) {
			if (asistencia.getSocio().getIdentificacion().equals(socio.getIdentificacion())) {
				return asistencia.evaluar(nota, comentario);
			}
		}
		return false;
	}

	public ArrayList<Asistencia> getAsistencias() {
		if (asistencias == null) {
			asistencias = new ArrayList<Asistencia>();
		}
		return asistencias;
	}
}
