package uniandes.dpoo.clubdeportivo.modelo.equipos;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

/**
 * Equipo del club. Pertenece a un unico deporte y a una unica categoria, y
 * tiene un cupo maximo de jugadores inscritos (regla RD02).
 */
public class Equipo {

	private String nombre;
	private int cupoMaximo;
	private Deporte deporte;
	private Categoria categoria;
	private List<JugadorEquipo> jugadores;

	public Equipo(String nombre, int cupoMaximo, Deporte deporte, Categoria categoria) {
		this.nombre = nombre;
		this.cupoMaximo = cupoMaximo;
		this.deporte = deporte;
		this.categoria = categoria;
		this.jugadores = new ArrayList<JugadorEquipo>();
	}

	/**
	 * Indica si todavia queda espacio en el equipo.
	 *
	 * REGLA RD02: cada equipo tiene un cupo maximo de jugadores inscritos.
	 */
	public boolean tieneCupo() {
		if (jugadores.size() < cupoMaximo) {
			return true;
		}
		return false;
	}

	/**
	 * Indica si el numero de camiseta esta libre en este equipo.
	 *
	 * REGLA RD03: el numero de camiseta es unico dentro del equipo.
	 */
	public boolean camisetaDisponible(int numeroCamiseta) {
		for(int i= 0;i<jugadores.size();i++) {
			
			JugadorEquipo jugador =jugadores.get(i);
			if(jugador.getNumeroCamiseta()==numeroCamiseta) {
			return false;
				
			}
		}
		return true;
	}

	/**
	 * Inscribe un socio en el equipo, si cumple todas las condiciones.
	 *
	 * Antes de aceptarlo hay que verificar, segun RD02 y RD03:
	 * 1. que el equipo tenga cupo
	 * 2. que la edad del socio corresponda a la categoria del equipo
	 * 3. que el numero de camiseta no este tomado
	 * 4. que el socio no pertenezca ya a otro equipo de este mismo deporte
	 *
	 * La condicion 4 no se valida aqui porque un equipo no conoce a los demas
	 * equipos del club; queda a cargo de quien tenga la lista completa.
	 *
	 * Devuelve el JugadorEquipo creado, o null si la inscripcion se rechaza.
	 */
	public JugadorEquipo inscribirJugador(Socio socio, String posicion, int numeroCamiseta) {
		if (socio == null || posicion == null || posicion.trim().isEmpty() || numeroCamiseta <= 0
				|| deporte == null || categoria == null || tieneCupo() == false) {
			return null;
		}
		for (JugadorEquipo jugador : jugadores) {
			if (jugador.getJugador() != null && socio.getIdentificacion() != null
					&& socio.getIdentificacion().equals(jugador.getJugador().getIdentificacion())) return null;
		}
		if (categoria.admiteEdad(socio.getEdad()) == false) {
			return null;
		}
		if (camisetaDisponible(numeroCamiseta) == false) {
			return null;
		}

		JugadorEquipo jugador = new JugadorEquipo(socio, this, posicion, numeroCamiseta);
		jugadores.add(jugador);
		return jugador;
	}

	/**
	 * Cuenta los jugadores que estan en condiciones de jugar el proximo partido,
	 * es decir, los que no estan suspendidos (RD04 y RD06).
	 */
	public int contarJugadoresHabilitados() {
		int cantidadHabilitados = 0;
		for (JugadorEquipo jugador : jugadores) {
			if (jugador.estaSuspendido() == false) {
				cantidadHabilitados++;
			}
		}
		return cantidadHabilitados;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCupoMaximo() {
		return cupoMaximo;
	}

	public void setCupoMaximo(int cupoMaximo) {
		this.cupoMaximo = cupoMaximo;
	}

	public Deporte getDeporte() {
		return deporte;
	}

	public void setDeporte(Deporte deporte) {
		this.deporte = deporte;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<JugadorEquipo> getJugadores() {
		if (jugadores == null) jugadores = new ArrayList<JugadorEquipo>();
		return jugadores;
	}

	public void setJugadores(List<JugadorEquipo> jugadores) {
		this.jugadores = jugadores;
	}

	@Override
	public String toString() {
		return nombre + " (" + deporte + ", " + categoria + ")";
	}
}
