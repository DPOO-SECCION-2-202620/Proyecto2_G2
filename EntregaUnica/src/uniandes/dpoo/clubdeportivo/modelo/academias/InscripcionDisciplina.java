package uniandes.dpoo.clubdeportivo.modelo.academias;

import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

public class InscripcionDisciplina {

	private Socio socio;
	private Disciplina disciplina;
	private String nivel;
	private int puntajeRanking;

	public InscripcionDisciplina(Socio socio, Disciplina disciplina, String nivel) {
		this.socio = socio;
		this.disciplina = disciplina;
		this.nivel = nivel;
		this.puntajeRanking = 1000;
	}

	public Socio getSocio() {
		return socio;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public String getNivel() {
		return nivel;
	}

	public int getPuntajeRanking() {
		return puntajeRanking;
	}

	public void setPuntajeRanking(int puntajeRanking) {
		this.puntajeRanking = puntajeRanking;
	}
}
