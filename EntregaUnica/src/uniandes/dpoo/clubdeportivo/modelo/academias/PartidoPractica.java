package uniandes.dpoo.clubdeportivo.modelo.academias;

import java.util.ArrayList;

import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

public class PartidoPractica {
    private Reserva reservaAsociada;
    private ArrayList<Socio> jugadoresConfirmados;
    private String resultadoFinal; 

    public PartidoPractica(Reserva reservaAsociada) {
        this.reservaAsociada = reservaAsociada;
        this.jugadoresConfirmados = new ArrayList<>();
        this.resultadoFinal = "Pendiente";
    }
    
    public String getResultadoFinal() {
		return resultadoFinal;
	}

	public void setResultadoFinal(String resultadoFinal) {
		this.resultadoFinal = resultadoFinal;
	}

	public void setReservaAsociada(Reserva reservaAsociada) {
		this.reservaAsociada = reservaAsociada;
	}

	public void setJugadoresConfirmados(ArrayList<Socio> jugadoresConfirmados) {
		this.jugadoresConfirmados = jugadoresConfirmados == null ? new ArrayList<Socio>() : jugadoresConfirmados;
	}

	public Reserva getReservaAsociada() {
		return reservaAsociada;
	}

	public ArrayList<Socio> getJugadoresConfirmados() {
		return jugadoresConfirmados;
	}

	public boolean agregarJugador(Socio nuevoJugador) {
        int LIMITE_JUGADORES = 4; 

		if (nuevoJugador != null && resultadoFinal.equals("Pendiente")
				&& this.jugadoresConfirmados.size() < LIMITE_JUGADORES
				&& this.jugadoresConfirmados.contains(nuevoJugador) == false) {
            this.jugadoresConfirmados.add(nuevoJugador);
            return true; 
        } else {
            return false; 
        }
    }
	
    public void finalizarPartido(String resultado, Socio ganador, Socio perdedor) {
        if (resultado == null || resultado.trim().isEmpty() || ganador == null || perdedor == null
				|| ganador.getIdentificacion().equals(perdedor.getIdentificacion())
				|| resultadoFinal.equals("Pendiente") == false) return;
        this.setResultadoFinal(resultado);
    }
}
