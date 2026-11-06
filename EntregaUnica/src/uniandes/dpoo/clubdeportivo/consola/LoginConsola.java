package uniandes.dpoo.clubdeportivo.consola;

import java.util.Scanner;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Administrador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Empleado;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;
import uniandes.dpoo.clubdeportivo.persistencia.PersistenciaClub;

public class LoginConsola {

	public static void iniciar(String tipo, Scanner entrada) {
		PersistenciaClub persistencia = new PersistenciaClub();
		Club club = persistencia.cargarClub();

		System.out.println("Inicio de sesion - " + tipo);
		String login = ConsolaUtil.leerTexto(entrada, "Login: ");
		String password = ConsolaUtil.leerTexto(entrada, "Password: ");
		if (login == null || password == null) return;

		Usuario usuario = club.autenticar(login, password);
		if (usuario == null || perteneceAlTipo(usuario, tipo) == false) {
			System.out.println("Login o tipo de usuario incorrecto.");
			return;
		}
		try {
			if (usuario instanceof Socio) {
				new ConsolaSocio(club, (Socio) usuario, entrada).iniciar();
			} else if (usuario instanceof Empleado) {
				new ConsolaEmpleado(club, (Empleado) usuario, entrada).iniciar();
			} else if (usuario instanceof Administrador) {
				new ConsolaAdministrador(club, entrada).iniciar();
			}
		} finally {
			persistencia.guardarClub(club);
		}
	}

	private static boolean perteneceAlTipo(Usuario usuario, String tipo) {
		if (tipo.equals("socio")) {
			return usuario instanceof Socio;
		} else if (tipo.equals("empleado")) {
			return usuario instanceof Empleado;
		} else if (tipo.equals("administrador")) {
			return usuario instanceof Administrador;
		}
		return false;
	}
}
