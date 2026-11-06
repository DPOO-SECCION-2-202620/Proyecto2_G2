package uniandes.dpoo.clubdeportivo.modelo.usuarios;

public abstract class Empleado extends Usuario{
	private String codigoDescuento;

	public String getCodigoDescuento() {
		return codigoDescuento;
	}

	public void setCodigoDescuento(String codigoDescuento) {
		this.codigoDescuento = codigoDescuento;
	}

	public Empleado(String login, String password, String codigoDescuento) {
		super(login, password);
		this.codigoDescuento = codigoDescuento;
	}
	
}
