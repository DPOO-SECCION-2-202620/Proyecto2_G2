package uniandes.dpoo.clubdeportivo.pruebas.integracion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.clubdeportivo.pruebas.DatosPrueba;

/**
 * Casos que quedan preparados para los torneos internos del Proyecto 2.
 * Por ahora estan deshabilitados porque todavia no existen las clases y operaciones
 * necesarias para ejecutarlos sin inventar una interfaz diferente.
 */
class TorneosTest {

    private DatosPrueba datos;

    @BeforeEach
    void prepararDatos() {
        datos = new DatosPrueba();
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void crearTorneoRecreativo() {
        // Dado: tenemos nombre, deporte, categoria, fechas y cupos
        // Cuando: el administrador crea un torneo recreativo
        // Entonces: queda abierto para inscripciones y no cobra tarifa
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void crearTorneoCompetitivo() {
        // Dado: los datos estan completos y la tarifa es mayor que cero
        // Cuando: se crea como torneo competitivo
        // Entonces: guarda la tarifa y calcula el premio con el recaudo
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void inscribirEquipoEnTorneoDeConjunto() {
        // Dado: hay cupo y el equipo cumple con deporte y categoria
        // Cuando: se manda la nomina completa de jugadores habilitados
        // Entonces: ocupa un cupo y aparece como participante
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void inscribirJugadorOParejaEnTorneoIndividual() {
        // Dado: hay cupo y los participantes tienen cuenta
        // Cuando: se inscribe una persona o una pareja para dobles
        // Entonces: la inscripcion queda confirmada una sola vez
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void rechazarInscripcionSinCupos() {
        // Dado: el torneo ya lleno todos sus cupos
        // Cuando: alguien mas intenta inscribirse
        // Entonces: lo rechaza sin cobrarle ni cambiar la lista
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void salirseAntesDeIniciarLiberaElCupo() {
        // Dado: el torneo todavia no empieza y el participante esta inscrito
        // Cuando: cancela su inscripcion
        // Entonces: sale de la lista y vuelve a quedar un cupo libre
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void rechazarSalirseDespuesDeIniciado() {
        // Dado: el torneo ya comenzo
        // Cuando: un participante intenta retirarse
        // Entonces: no lo deja y conserva su registro en el historial
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void reservarVeintePorCientoDeCuposPreferenciales() {
        // Dado: ya estan definidos los cupos y el orden por desempeno
        // Cuando: empieza la inscripcion preferencial
        // Entonces: reserva el veinte por ciento redondeando hacia arriba
        // En individual usa el ranking y en equipos usa el promedio de la nomina
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void liberarCuposPreferencialesNoUsados() {
        // Dado: quedaron cupos preferenciales sin usar
        // Cuando: comienza la inscripcion normal
        // Entonces: esos cupos vuelven a estar disponibles para todos
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void rechazarEmpleadoDeTurnoElDiaDelTorneo() {
        // Dado: el empleado tiene turno durante el dia del torneo individual
        // Cuando: trata de inscribirse
        // Entonces: lo rechaza y no gasta ningun cupo
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void empleadoNoPagaTorneoCompetitivoNiRecibePremio() {
        // Dado: el empleado no esta de turno ese dia y entra a un torneo individual
        // Cuando: se inscribe y despues gana el torneo competitivo
        // Entonces: no paga la entrada y tampoco recibe dinero
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void inscribirEstudianteOExterno() {
        // Dado: un estudiante o externo tiene su cuenta completa
        // Cuando: entra con la cuenta y pide un cupo
        // Entonces: queda inscrito con su tipo de vinculacion
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void consultarEstadoEHistorialDelTorneo() {
        // Dado: el torneo ya tiene participantes y resultados
        // Cuando: alguien consulta sus detalles
        // Entonces: ve el estado y todo el historial disponible
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void ganadorRecreativoRecibeBono() {
        // Dado: el torneo recreativo termino con un ganador valido
        // Cuando: el administrador guarda el resultado final
        // Entonces: el ganador recibe un bono que no se puede acumular
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void usarBonoEnCompraFutura() {
        // Dado: el ganador tiene un bono y hace otra compra despues
        // Cuando: usa el bono sin mezclarlo con otro descuento
        // Entonces: baja el valor y el bono queda usado
        // Si tambien usa codigo o puntos, la compra se debe rechazar
    }

    @Test
    @Disabled("Pendiente de implementar el modelo de torneos")
    void premioCompetitivoSegunRecaudo() {
        // Dado: el torneo termino y conocemos lo que se cobro en inscripciones
        // Cuando: calculamos el premio de un ganador que no es empleado
        // Entonces: recibe el setenta por ciento y el tercero registra el pago
    }
}
