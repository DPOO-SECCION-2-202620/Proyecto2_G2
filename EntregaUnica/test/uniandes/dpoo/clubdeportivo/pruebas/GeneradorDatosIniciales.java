package uniandes.dpoo.clubdeportivo.pruebas;

import uniandes.dpoo.clubdeportivo.persistencia.PersistenciaClub;

/**
 * Regenera el archivo datos/club.json con los datos de ejemplo de DatosPrueba.
 *
 * Se ejecuta desde la carpeta del proyecto (EntregaUnica), que es la carpeta de
 * trabajo por defecto de Eclipse. Sobrescribe el archivo actual, asi que solo se
 * debe correr cuando se quiera volver a los datos iniciales.
 */
public class GeneradorDatosIniciales {

	public static void main(String[] args) {
		DatosPrueba datos = new DatosPrueba();
		new PersistenciaClub().guardarClub(datos.club);
		System.out.println("Datos iniciales guardados en datos/club.json: " + datos.club.getUsuarios().size()
				+ " usuarios, " + datos.club.getEquipos().size() + " equipos y "
				+ datos.administracion.getArticulos().size() + " articulos de tienda.");
	}
}
