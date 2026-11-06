package uniandes.dpoo.clubdeportivo.modelo.academias;

import java.time.LocalDate;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;

public class Reserva {
    private String fecha;
    private int horaInicio; 
    private int duracionHoras; 
    private int cantidadJugadores;
    private transient Instalacion instalacionReservada;
    private transient Socio socioResponsable;
    private String nombreInstalacion;
    private String identificacionSocio;

    public Reserva(String fecha, int horaInicio, int duracionHoras, int cantidadJugadores, Instalacion instalacion, Socio socio) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionHoras = duracionHoras;
        this.cantidadJugadores = cantidadJugadores;
        this.instalacionReservada = instalacion;
        this.socioResponsable = socio;
        this.nombreInstalacion = instalacion == null ? null : instalacion.getNombreCancha();
        this.identificacionSocio = socio == null ? null : socio.getIdentificacion();
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

	public int getDuracionHoras() {
		return duracionHoras;
	}

	public void setDuracionHoras(int duracionHoras) {
		this.duracionHoras = duracionHoras;
	}

	public int getCantidadJugadores() {
		return cantidadJugadores;
	}

	public void setCantidadJugadores(int cantidadJugadores) {
		this.cantidadJugadores = cantidadJugadores;
	}

	public Instalacion getInstalacionReservada() {
		return instalacionReservada;
	}
	
	public Socio getSocioResponsable() {
		return socioResponsable;
	}

	public void setInstalacionReservada(Instalacion instalacion) {
		this.instalacionReservada = instalacion;
		this.nombreInstalacion = instalacion == null ? null : instalacion.getNombreCancha();
	}

	public void setSocioResponsable(Socio socio) {
		this.socioResponsable = socio;
		this.identificacionSocio = socio == null ? null : socio.getIdentificacion();
	}

	public String getNombreInstalacion() { return nombreInstalacion; }
	public String getIdentificacionSocio() { return identificacionSocio; }
	
    public boolean esReservaValida() {
        if (instalacionReservada == null || fecha == null || horaInicio < 0 || horaInicio > 23
				|| duracionHoras <= 0 || horaInicio + duracionHoras > 24 || cantidadJugadores <= 0) {
            return false;
        }
        try {
            LocalDate.parse(fecha);
        } catch (Exception e) {
            return false;
        }
        String deporte = instalacionReservada.getDeporte();
        if (deporte == null) return false;

        if (deporte.equalsIgnoreCase("Padel") || deporte.equalsIgnoreCase("Pádel")) {
            return cantidadJugadores == 4;
        } 
        else if (deporte.equalsIgnoreCase("Tenis") || deporte.equalsIgnoreCase("Tenis de Mesa")) {
            return cantidadJugadores == 2 || cantidadJugadores == 4;
        }
        return false;
    }
}
