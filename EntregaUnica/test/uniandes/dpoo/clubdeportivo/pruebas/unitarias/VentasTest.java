package uniandes.dpoo.clubdeportivo.pruebas.unitarias;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;
import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

class VentasTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    void ventaDeTiendaCobraIva() {
        // Dado: se compra una camiseta sin descuentos
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 10),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));

        // Cuando: registramos la compra
        assertTrue(datos.administracion.registrarVentaTienda(venta, 0));

        // Entonces: el total debe incluir el IVA del 19 por ciento
        assertEquals(60000.0, venta.getSubtotal(), 0.01);
        assertEquals(11400.0, venta.getImpuestos(), 0.01);
        assertEquals(71400.0, venta.getTotal(), 0.01);
    }

    @Test
    void codigoDeEmpleadoDaQuincePorCientoAlEmpleado() {
        // Dado: el entrenador usa su propio codigo para comprar
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 10), "entrenador", false);
        venta.setCodigoDescuentoUsado("ENT-001");
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));

        // Cuando: se registra la venta
        assertTrue(datos.administracion.registrarVentaTienda(venta, 0));

        // Entonces: le aplica el quince por ciento
        assertEquals(9000.0, venta.getDescuento(), 0.01);
        assertEquals(60690.0, venta.getTotal(), 0.01);
    }

    @Test
    void codigoCompartidoDaOchoPorCientoAlSocio() {
        // Dado: una socia usa el codigo que compartio un empleado
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 10),
                datos.valentina.getIdentificacion(), true);
        venta.setCodigoDescuentoUsado("ENT-001");
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));

        // Cuando: guardamos la compra
        assertTrue(datos.administracion.registrarVentaTienda(venta, 0));

        // Entonces: recibe ocho por ciento y paga sobre lo que queda
        assertEquals(4800.0, venta.getDescuento(), 0.01);
        assertEquals(65688.0, venta.getTotal(), 0.01);
    }

    @Test
    void descuentoNoSeAcumulaConPuntos() {
        // Dado: un socio quiere usar codigo y puntos al mismo tiempo
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 10),
                datos.socioPrueba.getIdentificacion(), true);
        venta.setCodigoDescuentoUsado("ENT-001");
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));
        int inventarioAntes = datos.camisetaClub.getCantidadDisponible(datos.tienda);

        // Cuando: intenta pagar redimiendo mil puntos
        boolean resultado = datos.administracion.registrarVentaTienda(venta, 1000);

        // Entonces: se rechaza y el inventario queda como estaba
        assertFalse(resultado);
        assertFalse(venta.estaRegistrada());
        assertEquals(inventarioAntes, datos.camisetaClub.getCantidadDisponible(datos.tienda));
    }

    @Test
    void ventaGeneraPuntosDelDosPorCiento() {
        // Dado: un socio con cinco mil puntos compra una camiseta
        VentaTienda venta = new VentaTienda(LocalDate.of(2026, 11, 10),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarArticulo(datos.camisetaClub, 1));

        // Cuando: paga sin redimir puntos
        assertTrue(datos.administracion.registrarVentaTienda(venta, 0));

        // Entonces: gana el dos por ciento y eso se suma a su saldo
        assertEquals(1200, venta.getPuntosGenerados());
        assertEquals(6200, datos.administracion.getPuntosFidelidad(datos.socioPrueba.getIdentificacion()));
    }

    @Test
    void ventaDeCafeteriaCobraImpuestoYPropina() {
        // Dado: se compra una botella de agua
        VentaCafeteria venta = new VentaCafeteria(LocalDate.of(2026, 11, 10),
                datos.socioPrueba.getIdentificacion(), true);
        assertTrue(venta.agregarProducto(datos.agua, 1, false, false));

        // Cuando: se paga usando la propina sugerida
        assertTrue(datos.administracion.registrarVentaCafeteria(venta, 0));

        // Entonces: cobra ocho de impuesto y diez de propina
        assertEquals(240.0, venta.getImpuestos(), 0.01);
        assertEquals(300.0, venta.getValorPropina(), 0.01);
        assertEquals(3540.0, venta.getTotal(), 0.01);
    }

    @Test
    void rechazarBebidaCalienteEnInstalacionRestringida() {
        // Dado: alguien quiere llevar cafe caliente a una zona restringida
        VentaCafeteria venta = new VentaCafeteria(LocalDate.of(2026, 11, 10),
                datos.socioPrueba.getIdentificacion(), true);

        // Cuando: trata de agregarlo a la compra
        boolean resultado = venta.agregarProducto(datos.cafe, 1, false, true);

        // Entonces: el producto no se agrega
        assertFalse(resultado);
        assertEquals(0, venta.getItems().size());
    }

    @Test
    void rechazarSnackSinInformarAlergenos() {
        // Dado: las galletas tienen alergenos que no se informaron
        VentaCafeteria venta = new VentaCafeteria(LocalDate.of(2026, 11, 10),
                datos.socioPrueba.getIdentificacion(), true);

        // Cuando: tratamos de agregar el snack
        boolean resultado = venta.agregarProducto(datos.galletas, 1, false, false);

        // Entonces: no debe quedar en la compra
        assertFalse(resultado);
        assertEquals(0, venta.getItems().size());
    }
}
