package uniandes.dpoo.clubdeportivo.pruebas.integracion;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.usuarios.Administrador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Fisioterapeuta;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

/**
 * RFS01, RFE01 y RFA01: iniciar sesion con login y contrasena.
 *
 * Cada prueba arranca con un club nuevo construido por DatosPrueba, ejecuta una
 * accion y verifica el resultado. Los comentarios Dado / Cuando / Entonces siguen
 * los criterios de aceptacion de la historia de usuario correspondiente.
 */
class AccesoTest {

	private DatosPrueba datos;

	@BeforeEach
	void prepararDatos() {
		datos = new DatosPrueba();
	}

	@Test
	void socioConCredencialesCorrectasIniciaSesion() {
		// Dado: el socio con login "vrojas" y contrasena "vrojas123"
		// Cuando: inicia sesion con esas credenciales
		Usuario usuario = datos.club.autenticar("vrojas", "vrojas123");

		// Entonces: el sistema lo reconoce como ese socio
		assertSame(datos.valentina, usuario);
	}

	@Test
	void contrasenaIncorrectaNoIniciaSesion() {
		// Dado: el socio con login "vrojas"
		// Cuando: intenta iniciar sesion con una contrasena equivocada
		Usuario usuario = datos.club.autenticar("vrojas", "clave-equivocada");

		// Entonces: el sistema no lo deja entrar
		assertNull(usuario);
	}

	@Test
	void cadaTipoDeEmpleadoIniciaSesionConSuCuenta() {
		// Dado: un entrenador, una fisioterapeuta y el administrador registrados
		// Cuando: cada uno inicia sesion con sus credenciales
		Usuario entrenador = datos.club.autenticar("entrenador", "ent123");
		Usuario fisioterapeuta = datos.club.autenticar("fisioterapeuta", "fis123");
		Usuario administrador = datos.club.autenticar("admin", "admin123");

		// Entonces: el sistema reconoce el tipo de cada usuario
		assertTrue(entrenador instanceof Entrenador);
		assertTrue(fisioterapeuta instanceof Fisioterapeuta);
		assertTrue(administrador instanceof Administrador);
	}
}
