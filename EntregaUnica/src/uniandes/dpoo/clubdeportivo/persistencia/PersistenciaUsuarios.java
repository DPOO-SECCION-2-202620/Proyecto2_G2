package uniandes.dpoo.clubdeportivo.persistencia;

import java.util.ArrayList;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;

public class PersistenciaUsuarios {

    public void guardarUsuarios(ArrayList<Usuario> listaUsuarios) {
        PersistenciaClub persistencia = new PersistenciaClub();
        Club club = persistencia.cargarClub();
        club.setUsuarios(listaUsuarios);
        persistencia.guardarClub(club);
    }

    public ArrayList<Usuario> cargarUsuarios() {
        return new ArrayList<Usuario>(new PersistenciaClub().cargarClub().getUsuarios());
    }
}
