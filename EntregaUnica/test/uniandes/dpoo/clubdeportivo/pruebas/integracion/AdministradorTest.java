package uniandes.dpoo.clubdeportivo.pruebas.integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.administracion.EstadoSolicitud;
import uniandes.dpoo.clubdeportivo.modelo.administracion.SolicitudCambioTurno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.competencias.CompetenciaLiga;
import uniandes.dpoo.clubdeportivo.modelo.competencias.FilaTabla;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Participante;
import uniandes.dpoo.clubdeportivo.modelo.competencias.PartidoCompetencia;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class AdministradorTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void rfa02ConsultarInventario() {
        // Dado: hay balones guardados en la tienda y en la bodega

        // Cuando: miramos cuantos hay en cada lugar
        int enTienda = datos.balonFutbol.getCantidadDisponible(datos.tienda);
        int enBodega = datos.balonFutbol.getCantidadDisponible(datos.bodega);

        // Entonces: las cantidades y el total deben coincidir
        assertEquals(12, enTienda);
        assertEquals(30, enBodega);
        assertEquals(42, datos.balonFutbol.getCantidadDisponible());
    }

    @Test
    void rfa03ConsultarHistorial() {
        // Dado: el club ya trae varias operaciones de prueba

        // Cuando: consultamos las listas que forman el historial
        int reservas = datos.club.getReservas().size();
        int partidos = datos.club.getPartidos().size();
        int sesiones = datos.club.getSesionesEntrenamiento().size();
        int partidosPractica = datos.club.getPartidosPractica().size();

        // Entonces: debe conservar reservas, partidos y sesiones
        assertEquals(3, reservas);
        assertEquals(1, partidos);
        assertEquals(1, sesiones);
        assertEquals(2, partidosPractica);
    }

    @Test
    void rfa04CrearEquipo() {
        // Dado: armamos un equipo nuevo con datos validos
        Equipo nuevo = new Equipo("Seneca Futbol Sub-13", 15,
                DatosPrueba.futbol(), DatosPrueba.sub13());
        int cantidadAntes = datos.club.getEquipos().size();

        // Cuando: el administrador lo agrega
        boolean resultado = datos.club.agregarEquipo(nuevo);

        // Entonces: debe aparecer una sola vez en la lista
        assertTrue(resultado);
        assertEquals(cantidadAntes + 1, datos.club.getEquipos().size());
        assertTrue(datos.club.getEquipos().contains(nuevo));
    }

    @Test
    void rfa04RechazarEquipoConNombreRepetido() {
        // Dado: ya existe ese nombre aunque este escrito con otras mayusculas
        Equipo repetido = new Equipo("seneca futbol mayores a", 18,
                DatosPrueba.futbol(), DatosPrueba.mayores());
        int cantidadAntes = datos.club.getEquipos().size();

        // Cuando: tratamos de agregarlo
        boolean resultado = datos.club.agregarEquipo(repetido);

        // Entonces: el club lo rechaza y no cambia la lista
        assertFalse(resultado);
        assertEquals(cantidadAntes, datos.club.getEquipos().size());
    }

    @Test
    void rfa05TrasladarInventario() {
        // Dado: hay cuatro paletas en tienda y ninguna en bodega
        assertEquals(4, datos.paletaPadel.getCantidadDisponible(datos.tienda));
        assertEquals(0, datos.paletaPadel.getCantidadDisponible(datos.bodega));

        // Cuando: movemos dos a la bodega
        boolean resultado = datos.administracion.moverArticulo(datos.paletaPadel,
                datos.tienda, datos.bodega, 2);

        // Entonces: cambia donde estan, pero el total sigue siendo cuatro
        assertTrue(resultado);
        assertEquals(2, datos.paletaPadel.getCantidadDisponible(datos.tienda));
        assertEquals(2, datos.paletaPadel.getCantidadDisponible(datos.bodega));
        assertEquals(4, datos.paletaPadel.getCantidadDisponible());
    }

    @Test
    void rfa05RechazarTrasladoSinExistencias() {
        // Dado: la tienda solo tiene cuatro paletas
        int tiendaAntes = datos.paletaPadel.getCantidadDisponible(datos.tienda);

        // Cuando: intentamos mover cinco
        boolean resultado = datos.administracion.moverArticulo(datos.paletaPadel,
                datos.tienda, datos.bodega, 5);

        // Entonces: no se mueve nada
        assertFalse(resultado);
        assertEquals(tiendaAntes, datos.paletaPadel.getCantidadDisponible(datos.tienda));
        assertEquals(0, datos.paletaPadel.getCantidadDisponible(datos.bodega));
    }

    @Test
    void huAdm01Ca1TrasladarTodoElProducto() {
        // Dado: las cuatro paletas estan en la tienda y no en la bodega
        assertEquals(4, datos.paletaPadel.getCantidadDisponible(datos.tienda));
        assertEquals(0, datos.paletaPadel.getCantidadDisponible(datos.bodega));

        // Cuando: se mueve todo el inventario de ese producto
        boolean resultado = datos.administracion.moverArticulo(datos.paletaPadel,
                datos.tienda, datos.bodega, 4);

        // Entonces: deja de venderse en el origen y queda completo en el destino
        assertTrue(resultado);
        assertEquals(0, datos.paletaPadel.getCantidadDisponible(datos.tienda));
        assertEquals(4, datos.paletaPadel.getCantidadDisponible(datos.bodega));
        assertEquals(4, datos.paletaPadel.getCantidadDisponible());
    }

    @Test
    void huAdm01Ca2RechazarProductoQueNoEstaEnElOrigen() {
        // Dado: no hay paletas en la bodega
        int tiendaAntes = datos.paletaPadel.getCantidadDisponible(datos.tienda);
        int bodegaAntes = datos.paletaPadel.getCantidadDisponible(datos.bodega);

        // Cuando: intentamos trasladar una desde la bodega hacia la tienda
        boolean resultado = datos.administracion.moverArticulo(datos.paletaPadel,
                datos.bodega, datos.tienda, 1);

        // Entonces: se rechaza y los dos inventarios quedan igual
        assertFalse(resultado);
        assertEquals(tiendaAntes, datos.paletaPadel.getCantidadDisponible(datos.tienda));
        assertEquals(bodegaAntes, datos.paletaPadel.getCantidadDisponible(datos.bodega));
    }

    @Test
    void huAdm01Ca3RechazarProductoQueYaEstaEnElDestino() {
        // Dado: el balon ya tiene unidades tanto en tienda como en bodega
        int tiendaAntes = datos.balonFutbol.getCantidadDisponible(datos.tienda);
        int bodegaAntes = datos.balonFutbol.getCantidadDisponible(datos.bodega);

        // Cuando: se intenta llevar otra unidad al destino que ya lo maneja
        boolean resultado = datos.administracion.moverArticulo(datos.balonFutbol,
                datos.tienda, datos.bodega, 1);

        // Entonces: el traslado no se hace y las cantidades siguen iguales
        assertFalse(resultado);
        assertEquals(tiendaAntes, datos.balonFutbol.getCantidadDisponible(datos.tienda));
        assertEquals(bodegaAntes, datos.balonFutbol.getCantidadDisponible(datos.bodega));
    }

    @Test
    void rfa05Reabastecer() {
        // Dado: la tienda comienza con doce balones
        int totalAntes = datos.balonFutbol.getCantidadDisponible();

        // Cuando: llegan tres balones mas
        boolean resultado = datos.administracion.reabastecerInventario(datos.balonFutbol,
                datos.tienda, 3);

        // Entonces: sube la cantidad de la tienda y tambien el total
        assertTrue(resultado);
        assertEquals(15, datos.balonFutbol.getCantidadDisponible(datos.tienda));
        assertEquals(totalAntes + 3, datos.balonFutbol.getCantidadDisponible());
    }

    @Test
    void rfa06AprobarSugerencia() {
        // Dado: el entrenador dejo una sugerencia pendiente
        assertEquals(EstadoSolicitud.PENDIENTE, datos.sugerenciaPendiente.getEstado());

        // Cuando: el administrador la aprueba
        boolean resultado = datos.administracion.gestionarSugerencia(datos.sugerenciaPendiente, true);

        // Entonces: el estado debe cambiar a aprobada
        assertTrue(resultado);
        assertEquals(EstadoSolicitud.APROBADA, datos.sugerenciaPendiente.getEstado());
    }

    @Test
    void rfa07AprobarCambioQueMantieneCobertura() {
        // Dado: Lina tiene una solicitud pendiente para mover su turno extra
        Turno anterior = datos.solicitudPendiente.getTurnoActual();
        Turno propuesto = datos.solicitudPendiente.getTurnoPropuesto();

        // Cuando: el administrador acepta el cambio
        boolean resultado = datos.administracion.aprobarCambioTurno(datos.solicitudPendiente);

        // Entonces: cambia el turno sin dejar huecos en la cobertura
        assertTrue(resultado);
        assertEquals(EstadoSolicitud.APROBADA, datos.solicitudPendiente.getEstado());
        assertFalse(datos.administracion.getTurnos().contains(anterior));
        assertTrue(datos.administracion.getTurnos().contains(propuesto));
        assertTrue(datos.administracion.mantieneCobertura());
    }

    @Test
    void rfa07RechazarCambioQueRompeCobertura() {
        // Dado: la propuesta deja dos horas sin uno de los entrenadores
        Turno turnoBase = null;
        for (Turno turno : datos.administracion.getTurnos()) {
            if (turno.getIdentificacionEmpleado().equals("entrenador")
                    && turno.getDiaSemana().equals("LUNES")
                    && turno.getHoraInicio().equals(LocalTime.of(6, 0))) {
                turnoBase = turno;
                break;
            }
        }
        assertNotNull(turnoBase);
        Turno propuesto = new Turno("entrenador", Turno.ENTRENADOR, "LUNES",
                LocalTime.of(8, 0), LocalTime.of(14, 0));
        SolicitudCambioTurno solicitud = datos.administracion.solicitarCambioTurno(
                LocalDate.of(2026, 11, 9), turnoBase, propuesto);
        assertNotNull(solicitud);

        // Cuando: el administrador trata de aprobarla
        boolean resultado = datos.administracion.aprobarCambioTurno(solicitud);

        // Entonces: se rechaza y se conserva el turno original
        assertFalse(resultado);
        assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstado());
        assertTrue(datos.administracion.getTurnos().contains(turnoBase));
        assertFalse(datos.administracion.getTurnos().contains(propuesto));
    }

    @Test
    void rfa08GenerarReporteDeVentas() {
        // Dado: tenemos ventas y mensualidades de octubre y noviembre
        String esperado = "Reporte de ventas del 2026-10-01 al 2026-11-30\n"
                + "Tienda: $59500.0\n"
                + "Cafeteria: $8260.0\n"
                + "Mensualidades: $3750000.0\n"
                + "Total: $3817760.0";

        // Cuando: pedimos el reporte de esos dos meses
        String reporte = datos.administracion.generarReporteVentas(
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 30));

        // Entonces: separa cada ingreso y muestra el total
        assertEquals(esperado, reporte);
    }

    @Test
    void rfa08RechazarPeriodoInvalido() {
        // Dado: la fecha inicial queda despues de la final

        // Cuando: se pide el reporte
        String reporte = datos.administracion.generarReporteVentas(
                LocalDate.of(2026, 11, 30), LocalDate.of(2026, 11, 1));

        // Entonces: debe avisar que el periodo no sirve
        assertEquals("El periodo del reporte no es valido.", reporte);
    }

    @Test
    void rfa10CrearCompetencia() {
        // Dado: el club tiene una competencia y se preparan los datos de otra liga
        int cantidadAntes = datos.club.getCompetencias().size();
        CompetenciaLiga liga = new CompetenciaLiga("Liga interclubes", "IDRD", 2027, 5,
                DatosPrueba.baloncesto(), DatosPrueba.mayores());

        // Cuando: el administrador la registra en el club
        datos.club.getCompetencias().add(liga);

        // Entonces: la competencia queda guardada con sus datos principales
        assertEquals(cantidadAntes + 1, datos.club.getCompetencias().size());
        assertSame(liga, datos.club.getCompetencias().get(cantidadAntes));
        assertEquals("Liga interclubes", liga.getNombre());
        assertEquals("liga de todos contra todos", liga.getFormato());
        assertEquals(5, liga.getNominaMinima());
    }

    @Test
    void rfa11InscribirEquipoEnCompetencia() {
        // Dado: Futbol B tiene los siete habilitados que pide el torneo
        int participantesAntes = datos.torneoDeLosCerros.getParticipantes().size();
        assertEquals(7, datos.futbolMayoresB.contarJugadoresHabilitados());

        // Cuando: el administrador envia el equipo y su nomina
        Participante participante = datos.torneoDeLosCerros.inscribirEquipo(datos.futbolMayoresB,
                datos.futbolMayoresB.getJugadores());

        // Entonces: queda inscrito una vez y con el nombre del equipo
        assertNotNull(participante);
        assertEquals(participantesAntes + 1, datos.torneoDeLosCerros.getParticipantes().size());
        assertTrue(datos.torneoDeLosCerros.getParticipantes().contains(participante));
        assertEquals(datos.futbolMayoresB.getNombre(), participante.getNombre());
    }

    @Test
    void rfa12RegistrarPartidoDeCompetencia() {
        // Dado: el segundo partido esta pendiente y las filas conservan el primer resultado
        PartidoCompetencia partido = datos.torneoDeLosCerros.getPartidos().get(1);
        FilaTabla filaClub = datos.torneoDeLosCerros.getTabla().buscarFila(datos.participanteClub);
        FilaTabla filaJaveriana = datos.torneoDeLosCerros.getTabla().buscarFila(datos.rivalJaveriana);
        assertEquals(3, filaClub.getPuntos());
        assertEquals(1, filaClub.getPartidosJugados());
        assertEquals(0, filaJaveriana.getPartidosJugados());

        // Cuando: se guarda un triunfo del club y se procesa en la tabla
        partido.registrarResultado(1, 2);
        datos.torneoDeLosCerros.procesarPartido(partido);

        // Entonces: el partido queda jugado y las dos filas acumulan el resultado
        assertTrue(partido.isJugado());
        assertSame(datos.participanteClub, partido.getGanador());
        assertEquals(6, filaClub.getPuntos());
        assertEquals(2, filaClub.getPartidosJugados());
        assertEquals(1, filaJaveriana.getPartidosJugados());
        assertEquals(1, filaJaveriana.getPerdidos());
    }

    @Test
    @Disabled("El modelo aun no ofrece un metodo para generar reportes deportivos")
    void rfa09ReporteDeportivo() {
        // Dado: el club tiene resultados deportivos guardados
        // Cuando: el administrador pida el reporte deportivo
        // Entonces: debe recibir resultados y estadisticas resumidas
    }
}
