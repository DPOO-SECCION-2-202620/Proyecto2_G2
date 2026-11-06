package uniandes.dpoo.clubdeportivo.modelo.entrenamiento;

import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

public class Asistencia {

	private Socio socio;
	private EstadoAsistencia estado;
	private Integer nota;
	private String comentario;

	public Asistencia(Socio socio, EstadoAsistencia estado) {
		this.socio = socio;
		this.estado = estado;
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public EstadoAsistencia getEstado() {
		return estado;
	}

	public Integer getNota() {
		return nota;
	}

	public String getComentario() {
		return comentario;
	}

	public boolean evaluar(int nota, String comentario) {
		if (nota < 1 || nota > 5 || comentario == null) {
			return false;
		}
		this.nota = nota;
		this.comentario = comentario;
		return true;
	}
}
