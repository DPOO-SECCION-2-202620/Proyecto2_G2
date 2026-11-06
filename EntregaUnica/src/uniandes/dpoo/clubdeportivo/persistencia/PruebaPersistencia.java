package uniandes.dpoo.clubdeportivo.persistencia;

import java.util.ArrayList;

import uniandes.dpoo.clubdeportivo.modelo.usuarios.Administrador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;

public class PruebaPersistencia {

    public static void main(String[] args) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        Socio socio1 = new Socio("10203040", "valen_padel", "clave123", "Valentina", 25);
        Administrador admin1 = new Administrador("admin_carlos", "admin123");
        Entrenador profe1 = new Entrenador("profe_luis", "luis123", "DESC-LUIS");
        
        usuarios.add(socio1);
        usuarios.add(admin1);
        usuarios.add(profe1);
        
        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
        System.out.println("Guardando datos...");
        persistencia.guardarUsuarios(usuarios);
    }
}
