package uniandes.dpoo.clubdeportivo.pruebas.integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.administracion.EstadoSolicitud;
import uniandes.dpoo.clubdeportivo.modelo.administracion.SolicitudCambioTurno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Sugerencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.Asistencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.EstadoAsistencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.SesionEntrenamiento;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaFutbol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class EmpleadoTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void rfe02PlanearSesionDeEntrenamiento() {
        // Dado: hay una instalacion libre para que el entrenador haga una sesion
        SesionEntrenamiento sesion = new SesionEntrenamiento("2026-11-25", 15,
                datos.entrenador, datos.canchaTenis1);
        int cantidadAntes = datos.club.getSesionesEntrenamiento().size();

        // Cuando: guardamos la sesion y convocamos a un socio
        boolean agregada = datos.club.agregarSesionEntrenamiento(sesion);
        boolean convocado = sesion.convocar(datos.socioPrueba);

        // Entonces: deben quedar guardados la sesion y el convocado
        assertTrue(agregada);
        assertTrue(convocado);
        assertEquals(cantidadAntes + 1, datos.club.getSesionesEntrenamiento().size());
        assertTrue(sesion.getJugadoresConvocados().contains(datos.socioPrueba));
    }

    @Test
    void rfe02RechazarSesionEnInstalacionOcupada() {
        // Dado: la cancha ya esta reservada el 20 de noviembre a las diez
        SesionEntrenamiento sesion = new SesionEntrenamiento("2026-11-20", 10,
                datos.entrenador, datos.canchaTenis1);
        int cantidadAntes = datos.club.getSesionesEntrenamiento().size();

        // Cuando: intentamos programar otra sesion ahi
        boolean resultado = datos.club.agregarSesionEntrenamiento(sesion);

        // Entonces: la rechaza y no cambia la agenda
        assertFalse(resultado);
        assertEquals(cantidadAntes, datos.club.getSesionesEntrenamiento().size());
    }

    @Test
    void rfe03RegistrarAsistencia() {
        // Dado: Santiago esta convocado a la sesion
        assertTrue(datos.sesionTenis.convocar(datos.santiago));
        int cantidadAntes = datos.sesionTenis.getAsistencias().size();

        // Cuando: el entrenador marca que si fue
        boolean resultado = datos.club.registrarAsistencia(datos.sesionTenis,
                datos.santiago, EstadoAsistencia.ASISTIO);

        // Entonces: aparece una asistencia nueva con ese estado
        assertTrue(resultado);
        assertEquals(cantidadAntes + 1, datos.sesionTenis.getAsistencias().size());
        Asistencia asistencia = datos.sesionTenis.getAsistencias().get(cantidadAntes);
        assertSame(datos.santiago, asistencia.getSocio());
        assertEquals(EstadoAsistencia.ASISTIO, asistencia.getEstado());
    }

    @Test
    void rfe03RechazarAsistenciaDeNoConvocado() {
        // Dado: Mariana no fue convocada a la sesion
        int cantidadAntes = datos.sesionTenis.getAsistencias().size();

        // Cuando: tratamos de marcar su asistencia
        boolean resultado = datos.club.registrarAsistencia(datos.sesionTenis,
                datos.mariana, EstadoAsistencia.ASISTIO);

        // Entonces: no debe dejarla registrar
        assertFalse(resultado);
        assertEquals(cantidadAntes, datos.sesionTenis.getAsistencias().size());
    }

    @Test
    void rfe04RegistrarEvaluacion() {
        // Dado: Camila tiene una asistencia registrada pero todavia no tiene nota
        Asistencia asistenciaCamila = buscarAsistencia(datos.camila.getIdentificacion());
        assertNotNull(asistenciaCamila);
        assertNull(asistenciaCamila.getNota());

        // Cuando: el entrenador pone una nota y un comentario validos
        boolean resultado = datos.sesionTenis.registrarEvaluacion(datos.camila, 4,
                "Mejoro el control de la pelota");

        // Entonces: la evaluacion queda en su asistencia
        assertTrue(resultado);
        assertEquals(4, asistenciaCamila.getNota());
        assertEquals("Mejoro el control de la pelota", asistenciaCamila.getComentario());
    }

    @Test
    void rfe04RechazarNotaFueraDeRango() {
        // Dado: Camila sigue sin evaluacion
        Asistencia asistenciaCamila = buscarAsistencia(datos.camila.getIdentificacion());
        assertNotNull(asistenciaCamila);

        // Cuando: se intenta poner una nota de seis
        boolean resultado = datos.sesionTenis.registrarEvaluacion(datos.camila, 6,
                "Nota fuera del rango");

        // Entonces: no la acepta y la nota sigue vacia
        assertFalse(resultado);
        assertNull(asistenciaCamila.getNota());
    }

    @Test
    void rfe05RegistrarEstadisticasDePartido() {
        // Dado: Valentina juega de local y todavia no tiene estadistica
        JugadorEquipo jugadora = DatosPrueba.jugador(datos.futbolMayoresA, datos.valentina);
        EstadisticaFutbol estadistica = new EstadisticaFutbol(jugadora, 1, 1, 0, 0);
        int cantidadAntes = datos.partidoInterno.getEstadisticas().size();

        // Cuando: el entrenador guarda su rendimiento
        datos.partidoInterno.registrarEstadistica(estadistica);

        // Entonces: la estadistica aparece al final de la lista
        assertEquals(cantidadAntes + 1, datos.partidoInterno.getEstadisticas().size());
        assertSame(estadistica, datos.partidoInterno.getEstadisticas().get(cantidadAntes));
    }

    @Test
    void rfe05DosAmarillasSuspendenAlJugador() {
        // Dado: Mariana ya trae una amarilla del partido anterior
        JugadorEquipo jugadora = DatosPrueba.jugador(datos.futbolMayoresA, datos.mariana);
        assertEquals(1, jugadora.getAmarillasAcumuladas());

        // Cuando: le registramos otra amarilla
        datos.partidoInterno.registrarEstadistica(new EstadisticaFutbol(jugadora, 0, 0, 1, 0));

        // Entonces: llega a dos y queda suspendida para el siguiente partido
        assertEquals(2, jugadora.getAmarillasAcumuladas());
        assertTrue(jugadora.estaSuspendido());
    }

    @Test
    void rfe06ComprarConCodigoDeDescuentoPropio() {
        // Dado: el entrenador compra pelotas usando su codigo
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 12), "entrenador", false);
        venta.setCodigoDescuentoUsado("ENT-001");
        assertTrue(venta.agregarArticulo(datos.pelotasTenis, 1));
        int inventarioAntes = datos.pelotasTenis.getCantidadDisponible(datos.tienda);

        // Cuando: registramos la compra
        boolean resultado = datos.administracion.registrarVentaTienda(venta, 0);

        // Entonces: recibe quince por ciento y sale una unidad del inventario
        assertTrue(resultado);
        assertEquals(3750.0, venta.getDescuento(), 0.01);
        assertEquals(inventarioAntes - 1, datos.pelotasTenis.getCantidadDisponible(datos.tienda));
    }

    @Test
    void rfe07SolicitarCambioDeTurno() {
        // Dado: el nuevo horario de Lina no se cruza con el turno de la tarde
        Turno propuesto = new Turno("lrestrepo", Turno.ENTRENADOR, "LUNES",
                LocalTime.of(9, 0), LocalTime.of(13, 0));
        int cantidadAntes = datos.administracion.getSolicitudesCambio().size();

        // Cuando: Lina manda la solicitud
        SolicitudCambioTurno solicitud = datos.administracion.solicitarCambioTurno(
                LocalDate.of(2026, 11, 9), datos.turnoExtraLina, propuesto);

        // Entonces: debe quedar pendiente
        assertNotNull(solicitud);
        assertEquals(cantidadAntes + 1, datos.administracion.getSolicitudesCambio().size());
        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
    }

    @Test
    void rfe07RechazarSolicitudQueCruzaOtroTurno() {
        // Dado: esta propuesta si se cruza con el turno de la tarde
        Turno propuesto = new Turno("lrestrepo", Turno.ENTRENADOR, "LUNES",
                LocalTime.of(13, 0), LocalTime.of(16, 0));
        int cantidadAntes = datos.administracion.getSolicitudesCambio().size();

        // Cuando: Lina trata de pedir el cambio
        SolicitudCambioTurno solicitud = datos.administracion.solicitarCambioTurno(
                LocalDate.of(2026, 11, 9), datos.turnoExtraLina, propuesto);

        // Entonces: no se crea otra solicitud
        assertNull(solicitud);
        assertEquals(cantidadAntes, datos.administracion.getSolicitudesCambio().size());
    }

    @Test
    void rfe08ProponerNuevoProducto() {
        // Dado: el entrenador tiene una propuesta para la tienda
        Sugerencia sugerencia = new Sugerencia("Botella reutilizable",
                "Botella liviana con el escudo del club", "Articulo", "entrenador");
        int cantidadAntes = datos.administracion.getSugerencias().size();

        // Cuando: la presenta al club
        boolean resultado = datos.administracion.agregarSugerencia(sugerencia);

        // Entonces: queda guardada como pendiente
        assertTrue(resultado);
        assertEquals(cantidadAntes + 1, datos.administracion.getSugerencias().size());
        assertEquals(EstadoSolicitud.PENDIENTE, sugerencia.getEstado());
    }

    @Test
    void rfe09ConsultarTurnosAsignados() {
        // Dado: hay turnos de toda la semana para varios empleados
        int turnosEntrenador = 0;
        int turnosLina = 0;
        boolean encontroExtra = false;

        // Cuando: filtramos los turnos por la identificacion del empleado
        for (Turno turno : datos.administracion.getTurnos()) {
            if (turno.getIdentificacionEmpleado().equals("entrenador")) {
                turnosEntrenador++;
            }
            if (turno.getIdentificacionEmpleado().equals("lrestrepo")) {
                turnosLina++;
                if (turno.getDiaSemana().equals("LUNES")
                        && turno.getHoraInicio().equals(LocalTime.of(10, 0))
                        && turno.getHoraFin().equals(LocalTime.of(14, 0))) {
                    encontroExtra = true;
                }
            }
        }

        // Entonces: cada uno ve sus turnos y Lina tambien tiene el extra del lunes
        assertEquals(7, turnosEntrenador);
        assertEquals(8, turnosLina);
        assertTrue(encontroExtra);
    }

    @Test
    void huEnt02ConsultarHabilitadosPorEquipo() {
        // Dado: el equipo B tiene justo siete habilitados
        assertTrue(datos.club.equipoCumpleMinimo(datos.futbolMayoresB));
        JugadorEquipo jugador = DatosPrueba.jugador(datos.futbolMayoresB, datos.sebastian);

        // Cuando: suspendemos al portero
        jugador.setSuspendidoProximoPartido(true);

        // Entonces: el equipo queda por debajo del minimo y el jugador sale como no habilitado
        assertFalse(datos.club.equipoCumpleMinimo(datos.futbolMayoresB));
        assertEquals(1, datos.club.jugadoresNoHabilitados(datos.futbolMayoresB).size());
        assertSame(jugador, datos.club.jugadoresNoHabilitados(datos.futbolMayoresB).get(0));
    }

    @Test
    void huEnt02Ca2BaloncestoConCuatroHabilitados() {
        // Dado: el equipo de baloncesto tiene seis jugadores y se suspenden dos
        JugadorEquipo primera = DatosPrueba.jugador(datos.baloncestoMayores, datos.valentina);
        JugadorEquipo segunda = DatosPrueba.jugador(datos.baloncestoMayores, datos.camila);
        primera.setSuspendidoProximoPartido(true);
        segunda.setSuspendidoProximoPartido(true);

        // Cuando: el entrenador consulta si alcanza el minimo y quienes faltan
        boolean cumple = datos.club.equipoCumpleMinimo(datos.baloncestoMayores);
        List<JugadorEquipo> noDisponibles = datos.club.jugadoresNoHabilitados(datos.baloncestoMayores);

        // Entonces: con cuatro no puede jugar y la causa de los dos es suspension
        assertFalse(cumple);
        assertEquals(4, datos.baloncestoMayores.contarJugadoresHabilitados());
        assertEquals(2, noDisponibles.size());
        assertTrue(noDisponibles.contains(primera));
        assertTrue(noDisponibles.contains(segunda));
        assertTrue(primera.estaSuspendido());
        assertTrue(segunda.estaSuspendido());
    }

    private Asistencia buscarAsistencia(String identificacion) {
        for (Asistencia asistencia : datos.sesionTenis.getAsistencias()) {
            if (asistencia.getSocio().getIdentificacion().equals(identificacion)) {
                return asistencia;
            }
        }
        return null;
    }
}
