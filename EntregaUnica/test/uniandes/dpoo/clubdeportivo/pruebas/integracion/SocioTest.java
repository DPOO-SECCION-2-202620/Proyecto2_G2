package uniandes.dpoo.clubdeportivo.pruebas.integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.academias.PartidoPractica;
import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class SocioTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void rfs02InscribirseEnDisciplina() {
        // Dado: Martin todavia no esta inscrito en tenis de mesa
        int cantidadAntes = datos.club.getInscripcionesDisciplina().size();

        // Cuando: se inscribe como principiante
        boolean resultado = datos.club.inscribirEnDisciplina(datos.martin,
                datos.tenisDeMesa, "Principiante");

        // Entonces: debe aparecer una inscripcion nueva
        assertTrue(resultado);
        assertEquals(cantidadAntes + 1, datos.club.getInscripcionesDisciplina().size());
    }

    @Test
    void rfs02NoSePuedeInscribirDosVecesEnLaMismaDisciplina() {
        // Dado: Valentina ya aparece inscrita en tenis
        int cantidadAntes = datos.club.getInscripcionesDisciplina().size();

        // Cuando: trata de inscribirse de nuevo
        boolean resultado = datos.club.inscribirEnDisciplina(datos.valentina,
                datos.tenis, "Avanzado");

        // Entonces: no debe duplicar la inscripcion
        assertFalse(resultado);
        assertEquals(cantidadAntes, datos.club.getInscripcionesDisciplina().size());
    }

    @Test
    void rfs03ProgramarPartidoDePractica() {
        // Dado: hay una reserva nueva y dos jugadores intermedios
        Reserva reserva = new Reserva("2026-11-25", 12, 1, 2,
                datos.canchaTenis1, datos.andres);
        assertTrue(datos.club.reservarInstalacion(reserva));
        int cantidadAntes = datos.club.getPartidosPractica().size();

        // Cuando: programamos el partido de Andres y Santiago
        PartidoPractica partido = datos.club.programarPartidoPractica(reserva,
                DatosPrueba.socios(datos.andres, datos.santiago));

        // Entonces: queda pendiente y con los dos confirmados
        assertNotNull(partido);
        assertEquals(cantidadAntes + 1, datos.club.getPartidosPractica().size());
        assertEquals("Pendiente", partido.getResultadoFinal());
        assertEquals(2, partido.getJugadoresConfirmados().size());
    }

    @Test
    void rfs03RechazarNivelesNoAdyacentes() {
        // Dado: Valentina es avanzada y Camila es principiante
        Reserva reserva = new Reserva("2026-11-25", 14, 1, 2,
                datos.canchaTenis2, datos.valentina);
        assertTrue(datos.club.reservarInstalacion(reserva));
        int cantidadAntes = datos.club.getPartidosPractica().size();

        // Cuando: tratamos de programar el partido entre ellas
        PartidoPractica partido = datos.club.programarPartidoPractica(reserva,
                DatosPrueba.socios(datos.valentina, datos.camila));

        // Entonces: no lo deja crear porque los niveles estan muy separados
        assertNull(partido);
        assertEquals(cantidadAntes, datos.club.getPartidosPractica().size());
    }

    @Test
    void rfs04ReservarInstalacion() {
        // Dado: la cancha esta libre en un horario futuro
        Reserva reserva = new Reserva("2026-11-25", 18, 2, 2,
                datos.canchaTenis1, datos.socioPrueba);
        int cantidadAntes = datos.club.getReservas().size();

        // Cuando: el socio hace la reserva
        boolean resultado = datos.club.reservarInstalacion(reserva);

        // Entonces: debe aparecer en el club y en sus reservas
        assertTrue(resultado);
        assertEquals(cantidadAntes + 1, datos.club.getReservas().size());
        assertTrue(datos.socioPrueba.getMisReservas().contains(reserva));
    }

    @Test
    void rfs04RechazarReservaConCupoDeDisciplinaLleno() {
        // Dado: tenis permite dos canchas al tiempo y una ya esta ocupada
        Instalacion canchaTres = new Instalacion("Cancha de tenis 3", "Tenis");
        assertTrue(datos.club.agregarInstalacion(canchaTres));
        Reserva segunda = new Reserva("2026-11-20", 10, 2, 2,
                datos.canchaTenis2, datos.andres);
        assertTrue(datos.club.reservarInstalacion(segunda));
        Reserva tercera = new Reserva("2026-11-20", 10, 2, 2,
                canchaTres, datos.santiago);
        int cantidadAntes = datos.club.getReservas().size();

        // Cuando: intentamos reservar una tercera cancha a la misma hora
        boolean resultado = datos.club.reservarInstalacion(tercera);

        // Entonces: la rechaza porque ya se completo el limite
        assertFalse(resultado);
        assertEquals(cantidadAntes, datos.club.getReservas().size());
    }

    @Test
    void rfs05ComprarEnTienda() {
        // Dado: hay una camiseta disponible para comprar
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 15),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));
        int inventarioAntes = datos.camisetaClub.getCantidadDisponible(datos.tienda);

        // Cuando: el socio paga la compra
        boolean resultado = datos.administracion.registrarVentaTienda(venta, 0);

        // Entonces: se registra y sale una camiseta del inventario
        assertTrue(resultado);
        assertTrue(venta.estaRegistrada());
        assertEquals(inventarioAntes - 1, datos.camisetaClub.getCantidadDisponible(datos.tienda));
        assertEquals(1200, venta.getPuntosGenerados());
    }

    @Test
    void rfs05ComprarEnCafeteria() {
        // Dado: el socio pide una botella de agua
        VentaCafeteria venta = new VentaCafeteria(LocalDate.of(2026, 11, 15),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarProducto(datos.agua, 1, false, false));
        int inventarioAntes = datos.agua.getCantidadDisponible();

        // Cuando: se registra la compra
        boolean resultado = datos.administracion.registrarVentaCafeteria(venta, 0);

        // Entonces: cobra el total y descuenta la botella
        assertTrue(resultado);
        assertEquals(inventarioAntes - 1, datos.agua.getCantidadDisponible());
        assertEquals(3540.0, venta.getTotal(), 0.01);
    }

    @Test
    void rfs05RechazarCompraSinInventario() {
        // Dado: el socio pide tres mallas y solamente quedan dos
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 15),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarArticulo(datos.mallaVoleibol, 3));
        int inventarioAntes = datos.mallaVoleibol.getCantidadDisponible(datos.tienda);

        // Cuando: tratamos de registrar la compra
        boolean resultado = datos.administracion.registrarVentaTienda(venta, 0);

        // Entonces: se rechaza y no toca el inventario
        assertFalse(resultado);
        assertFalse(venta.estaRegistrada());
        assertEquals(inventarioAntes, datos.mallaVoleibol.getCantidadDisponible(datos.tienda));
    }

    @Test
    void rfs06ModificarPropina() {
        // Dado: la compra trae una propina sugerida de diez por ciento
        VentaCafeteria venta = new VentaCafeteria(LocalDate.of(2026, 11, 15),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarProducto(datos.agua, 1, false, false));

        // Cuando: el socio la sube a veinte por ciento y paga
        assertTrue(venta.setPropina(0.20));
        assertTrue(datos.administracion.registrarVentaCafeteria(venta, 0));

        // Entonces: la propina y el total usan el nuevo porcentaje
        assertEquals(600.0, venta.getValorPropina(), 0.01);
        assertEquals(3840.0, venta.getTotal(), 0.01);
    }

    @Test
    void rfs07RedimirPuntos() {
        // Dado: el socio tiene cinco mil puntos y compra una camiseta
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 15),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));

        // Cuando: usa mil puntos para pagar
        boolean resultado = datos.administracion.registrarVentaTienda(venta, 1000);

        // Entonces: se descuentan y luego se suman los puntos de la compra
        assertTrue(resultado);
        assertEquals(1000, venta.getPuntosRedimidos());
        assertEquals(70210.0, venta.getTotal(), 0.01);
        assertEquals(5180, datos.administracion.getPuntosFidelidad(datos.socioPrueba.getIdentificacion()));
    }

    @Test
    void rfs07RechazarRedimirMasPuntosDeLosQueTiene() {
        // Dado: el socio tiene cinco mil puntos e intenta gastar cinco mil uno
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 15),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));
        int inventarioAntes = datos.camisetaClub.getCantidadDisponible(datos.tienda);

        // Cuando: trata de registrar la compra
        boolean resultado = datos.administracion.registrarVentaTienda(venta, 5001);

        // Entonces: no se registra y deja igual el saldo y el inventario
        assertFalse(resultado);
        assertFalse(venta.estaRegistrada());
        assertEquals(5000, datos.administracion.getPuntosFidelidad(datos.socioPrueba.getIdentificacion()));
        assertEquals(inventarioAntes, datos.camisetaClub.getCantidadDisponible(datos.tienda));
    }

    @Test
    void inscribirseEnEquipo() {
        // Dado: Martin tiene doce anos y hay un equipo Sub-13 nuevo
        Equipo equipo = new Equipo("Seneca Futbol Sub-13", 15,
                DatosPrueba.futbol(), DatosPrueba.sub13());
        assertTrue(datos.club.agregarEquipo(equipo));

        // Cuando: se inscribe con una posicion y camiseta libres
        boolean resultado = datos.club.inscribirJugador(equipo, datos.martin, "Delantero", 10);

        // Entonces: debe quedar en la nomina
        assertTrue(resultado);
        assertEquals(1, equipo.getJugadores().size());
        assertSame(datos.martin, equipo.getJugadores().get(0).getJugador());
    }

    @Test
    void rechazarSegundoEquipoDelMismoDeporte() {
        // Dado: Valentina ya juega en un equipo de futbol
        Equipo equipo = new Equipo("Seneca Futbol Mayores C", 18,
                DatosPrueba.futbol(), DatosPrueba.mayores());
        assertTrue(datos.club.agregarEquipo(equipo));

        // Cuando: trata de entrar a otro equipo de futbol
        boolean resultado = datos.club.inscribirJugador(equipo, datos.valentina, "Defensa", 12);

        // Entonces: se rechaza y el otro equipo queda vacio
        assertFalse(resultado);
        assertEquals(0, equipo.getJugadores().size());
    }
}
