package uniandes.dpoo.clubdeportivo.pruebas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.academias.Disciplina;
import uniandes.dpoo.clubdeportivo.modelo.academias.Instalacion;
import uniandes.dpoo.clubdeportivo.modelo.academias.PartidoPractica;
import uniandes.dpoo.clubdeportivo.modelo.academias.Reserva;
import uniandes.dpoo.clubdeportivo.modelo.administracion.AdministracionComercial;
import uniandes.dpoo.clubdeportivo.modelo.administracion.SolicitudCambioTurno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Sugerencia;
import uniandes.dpoo.clubdeportivo.modelo.administracion.Turno;
import uniandes.dpoo.clubdeportivo.modelo.administracion.UbicacionInventario;
import uniandes.dpoo.clubdeportivo.modelo.competencias.CompetenciaLiga;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Participante;
import uniandes.dpoo.clubdeportivo.modelo.competencias.PartidoCompetencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.EstadoAsistencia;
import uniandes.dpoo.clubdeportivo.modelo.entrenamiento.SesionEntrenamiento;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Categoria;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Deporte;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Equipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaFutbol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.JugadorEquipo;
import uniandes.dpoo.clubdeportivo.modelo.equipos.Partido;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Administrador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Fisioterapeuta;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ArticuloTienda;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Bebida;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Snack;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;

/**
 * Construye un club completo con datos de ejemplo para las pruebas y para el
 * archivo de datos inicial de las consolas.
 *
 * Todos los datos se registran con los metodos del modelo, de modo que cumplen
 * las reglas de dominio. Si algun metodo rechaza un dato, la construccion falla
 * con una excepcion: asi las pruebas nunca arrancan con datos a medias.
 *
 * Cada prueba debe crear su propia instancia (por ejemplo en un metodo
 * anotado con @BeforeEach) para no depender de lo que hizo otra prueba.
 */
public class DatosPrueba {

	/** Fecha de referencia de los datos: las consultas de reportes pueden usar este mes. */
	public static final LocalDate HOY = LocalDate.of(2026, 11, 8);

	public static final String[] DIAS = { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO",
			"DOMINGO" };

	public Club club;
	public AdministracionComercial administracion;

	// ---------------------------------------------------------------- usuarios
	/** Socios mayores de edad (categoria Mayores). */
	public Socio socioPrueba, valentina, andres, camila, santiago, mariana, juanPablo, daniela, sebastian, laura,
			nicolas, isabella, mateo, sara, felipe;
	/** Socios de 16 y 17 anos (categoria Sub-17). */
	public Socio emilio, gabriela;
	/** Socios de 14 y 15 anos (categoria Sub-15). */
	public Socio samuel, lucia;
	/** Socio de 12 anos (categoria Sub-13), sin equipo. */
	public Socio martin;

	public Entrenador entrenador, carlos, lina, diego;
	public Fisioterapeuta fisioterapeuta, paula;
	public Administrador administrador;

	// ---------------------------------------------------------------- deportes de conjunto
	public Equipo futbolMayoresA;
	/** Tiene exactamente el minimo de 7 jugadores: una suspension lo deja sin poder jugar. */
	public Equipo futbolMayoresB;
	public Equipo baloncestoMayores;
	public Equipo voleibolMayores;
	/** Solo tiene 2 jugadores: no alcanza el minimo de voleibol. */
	public Equipo voleibolSub17;
	/** Tiene el cupo maximo (2) ya lleno. */
	public Equipo futbolSub15;
	public Partido partidoInterno;

	// ---------------------------------------------------------------- competencias
	public CompetenciaLiga torneoDeLosCerros;
	public Participante participanteClub, rivalNacional, rivalJaveriana, rivalRosario;
	public PartidoCompetencia partidoOficialJugado;

	// ---------------------------------------------------------------- disciplinas individuales
	public Disciplina tenis, padel, tenisDeMesa;
	public Instalacion canchaTenis1, canchaTenis2, canchaPadel1, mesa1, mesa2;
	/** Reserva futura de la cancha de tenis 1 con un partido de practica pendiente. */
	public Reserva reservaTenisFutura;
	/** Reserva pasada de la cancha de tenis 2, con un partido de practica ya finalizado. */
	public Reserva reservaTenisPasada;
	/** Reserva futura de la cancha de padel, sin partido. */
	public Reserva reservaPadelFutura;
	public PartidoPractica partidoPracticaPendiente;
	public PartidoPractica partidoPracticaFinalizado;

	// ---------------------------------------------------------------- entrenamiento
	public SesionEntrenamiento sesionTenis;

	// ---------------------------------------------------------------- tienda y cafeteria
	public UbicacionInventario tienda, bodega;
	public ArticuloTienda balonFutbol, raquetaTenis, pelotasTenis, camisetaClub, mallaVoleibol, paletaPadel;
	public Bebida cafe, chocolate, agua, bebidaHidratante;
	public Snack mani, galletas, sandwich, fruta;
	public VentaTienda ventaTiendaValentina;
	public VentaCafeteria ventaCafeteriaAndres;

	// ---------------------------------------------------------------- administracion
	/** Turno adicional de Lina (lunes de 10 a 14) que se puede mover sin romper la cobertura. */
	public Turno turnoExtraLina;
	public SolicitudCambioTurno solicitudPendiente;
	public Sugerencia sugerenciaPendiente;
	public Sugerencia sugerenciaAprobada;

	public DatosPrueba() {
		club = new Club();
		administracion = club.getAdministracion();
		crearUsuarios();
		crearEquiposYPartido();
		crearCompetencia();
		crearDisciplinasYReservas();
		crearEntrenamiento();
		crearTiendaYCafeteria();
		crearAdministracion();
	}

	private void crearUsuarios() {
		socioPrueba = socio("S-001", "socio", "socio123", "Socio de prueba", 20);
		valentina = socio("S-002", "vrojas", "vrojas123", "Valentina Rojas", 21);
		andres = socio("S-003", "agomez", "agomez123", "Andres Gomez", 23);
		camila = socio("S-004", "cherrera", "cherrera123", "Camila Herrera", 19);
		santiago = socio("S-005", "spardo", "spardo123", "Santiago Pardo", 22);
		mariana = socio("S-006", "mlopez", "mlopez123", "Mariana Lopez", 20);
		juanPablo = socio("S-007", "jdiaz", "jdiaz123", "Juan Pablo Diaz", 24);
		daniela = socio("S-008", "dcastro", "dcastro123", "Daniela Castro", 18);
		sebastian = socio("S-009", "sruiz", "sruiz123", "Sebastian Ruiz", 25);
		laura = socio("S-010", "lmoreno", "lmoreno123", "Laura Moreno", 22);
		nicolas = socio("S-011", "nvargas", "nvargas123", "Nicolas Vargas", 19);
		isabella = socio("S-012", "itorres", "itorres123", "Isabella Torres", 21);
		mateo = socio("S-013", "mrincon", "mrincon123", "Mateo Rincon", 23);
		sara = socio("S-014", "smejia", "smejia123", "Sara Mejia", 20);
		felipe = socio("S-015", "fospina", "fospina123", "Felipe Ospina", 26);
		emilio = socio("S-016", "esuarez", "esuarez123", "Emilio Suarez", 16);
		gabriela = socio("S-017", "gnino", "gnino123", "Gabriela Nino", 17);
		samuel = socio("S-018", "spineda", "spineda123", "Samuel Pineda", 14);
		lucia = socio("S-019", "lcardenas", "lcardenas123", "Lucia Cardenas", 15);
		martin = socio("S-020", "mortiz", "mortiz123", "Martin Ortiz", 12);

		entrenador = new Entrenador("entrenador", "ent123", "ENT-001");
		carlos = new Entrenador("cmendoza", "cmendoza123", "ENT-002");
		lina = new Entrenador("lrestrepo", "lrestrepo123", "ENT-003");
		diego = new Entrenador("dsalazar", "dsalazar123", "ENT-004");
		fisioterapeuta = new Fisioterapeuta("fisioterapeuta", "fis123", "FIS-001");
		paula = new Fisioterapeuta("pcorrea", "pcorrea123", "FIS-002");
		administrador = new Administrador("admin", "admin123");
		club.agregarUsuario(entrenador);
		club.agregarUsuario(carlos);
		club.agregarUsuario(lina);
		club.agregarUsuario(diego);
		club.agregarUsuario(fisioterapeuta);
		club.agregarUsuario(paula);
		club.agregarUsuario(administrador);

		// Los codigos de descuento se asocian al login del empleado, como en las consolas.
		administracion.registrarCodigoDescuento("ENT-001", "entrenador");
		administracion.registrarCodigoDescuento("ENT-002", "cmendoza");
		administracion.registrarCodigoDescuento("ENT-003", "lrestrepo");
		administracion.registrarCodigoDescuento("ENT-004", "dsalazar");
		administracion.registrarCodigoDescuento("FIS-001", "fisioterapeuta");
		administracion.registrarCodigoDescuento("FIS-002", "pcorrea");
	}

	private Socio socio(String identificacion, String login, String password, String nombre, int edad) {
		Socio socio = new Socio(identificacion, login, password, nombre, edad);
		club.agregarUsuario(socio);
		return socio;
	}

	private void crearEquiposYPartido() {
		futbolMayoresA = equipo("Seneca Futbol Mayores A", 18, futbol(), mayores());
		inscribir(futbolMayoresA, socioPrueba, "Portero", 1);
		inscribir(futbolMayoresA, valentina, "Defensa", 2);
		inscribir(futbolMayoresA, andres, "Defensa", 3);
		inscribir(futbolMayoresA, camila, "Defensa", 4);
		inscribir(futbolMayoresA, santiago, "Mediocampista", 8);
		inscribir(futbolMayoresA, mariana, "Mediocampista", 10);
		inscribir(futbolMayoresA, juanPablo, "Delantero", 9);
		inscribir(futbolMayoresA, daniela, "Delantero", 11);

		futbolMayoresB = equipo("Seneca Futbol Mayores B", 18, futbol(), mayores());
		inscribir(futbolMayoresB, sebastian, "Portero", 1);
		inscribir(futbolMayoresB, laura, "Defensa", 2);
		inscribir(futbolMayoresB, nicolas, "Defensa", 3);
		inscribir(futbolMayoresB, isabella, "Mediocampista", 6);
		inscribir(futbolMayoresB, mateo, "Mediocampista", 8);
		inscribir(futbolMayoresB, sara, "Delantero", 9);
		inscribir(futbolMayoresB, felipe, "Delantero", 7);

		baloncestoMayores = equipo("Seneca Baloncesto Mayores", 12, baloncesto(), mayores());
		inscribir(baloncestoMayores, valentina, "Base", 4);
		inscribir(baloncestoMayores, camila, "Escolta", 5);
		inscribir(baloncestoMayores, mariana, "Alero", 6);
		inscribir(baloncestoMayores, laura, "Ala-pivot", 7);
		inscribir(baloncestoMayores, isabella, "Pivot", 8);
		inscribir(baloncestoMayores, sara, "Alero", 9);

		voleibolMayores = equipo("Seneca Voleibol Mayores", 12, voleibol(), mayores());
		inscribir(voleibolMayores, andres, "Armador", 1);
		inscribir(voleibolMayores, santiago, "Opuesto", 2);
		inscribir(voleibolMayores, juanPablo, "Central", 3);
		inscribir(voleibolMayores, sebastian, "Central", 4);
		inscribir(voleibolMayores, nicolas, "Punta", 5);
		inscribir(voleibolMayores, mateo, "Libero", 6);

		voleibolSub17 = equipo("Seneca Voleibol Sub-17", 12, voleibol(), sub17());
		inscribir(voleibolSub17, emilio, "Punta", 10);
		inscribir(voleibolSub17, gabriela, "Armador", 11);

		futbolSub15 = equipo("Seneca Futbol Sub-15", 2, futbol(), sub15());
		inscribir(futbolSub15, samuel, "Delantero", 9);
		inscribir(futbolSub15, lucia, "Mediocampista", 10);

		partidoInterno = new Partido(LocalDate.of(2026, 10, 25), futbolMayoresA, futbolMayoresB);
		exigir(partidoInterno.puedeJugarse(), "habilitar el partido interno");
		partidoInterno.setMarcador("2-1");
		partidoInterno.registrarEstadistica(new EstadisticaFutbol(jugador(futbolMayoresA, juanPablo), 2, 0, 0, 0));
		partidoInterno.registrarEstadistica(new EstadisticaFutbol(jugador(futbolMayoresA, mariana), 0, 2, 1, 0));
		partidoInterno.registrarEstadistica(new EstadisticaFutbol(jugador(futbolMayoresB, sara), 1, 0, 0, 0));
		exigir(partidoInterno.getEstadisticas().size() == 3, "registrar las estadisticas del partido interno");
		club.getPartidos().add(partidoInterno);
	}

	private void crearCompetencia() {
		torneoDeLosCerros = new CompetenciaLiga("Torneo de los Cerros", "Universidades de Bogota", 2026, 7,
				futbol(), mayores());
		participanteClub = torneoDeLosCerros.inscribirEquipo(futbolMayoresA, futbolMayoresA.getJugadores());
		exigir(participanteClub != null, "inscribir el equipo del club en el Torneo de los Cerros");
		rivalNacional = torneoDeLosCerros.inscribirRival("Nacional FC", "Universidad Nacional de Colombia");
		rivalJaveriana = torneoDeLosCerros.inscribirRival("Javeriana FC", "Pontificia Universidad Javeriana");
		rivalRosario = torneoDeLosCerros.inscribirRival("Rosario FC", "Universidad del Rosario");
		torneoDeLosCerros.getTabla().agregarParticipante(participanteClub);
		torneoDeLosCerros.getTabla().agregarParticipante(rivalNacional);
		torneoDeLosCerros.getTabla().agregarParticipante(rivalJaveriana);
		torneoDeLosCerros.getTabla().agregarParticipante(rivalRosario);

		partidoOficialJugado = torneoDeLosCerros.programarPartido(
				new PartidoCompetencia(LocalDate.of(2026, 10, 18), "Cancha principal del club", participanteClub,
						rivalNacional));
		partidoOficialJugado.registrarResultado(3, 1);
		torneoDeLosCerros.procesarPartido(partidoOficialJugado);
		torneoDeLosCerros.programarPartido(new PartidoCompetencia(LocalDate.of(2026, 11, 22),
				"Cancha de la Universidad Javeriana", rivalJaveriana, participanteClub));
		club.getCompetencias().add(torneoDeLosCerros);
	}

	private void crearDisciplinasYReservas() {
		tenis = new Disciplina("Tenis", 2);
		padel = new Disciplina("Padel", 1);
		tenisDeMesa = new Disciplina("Tenis de Mesa", 2);
		exigir(club.agregarDisciplina(tenis), "agregar Tenis");
		exigir(club.agregarDisciplina(padel), "agregar Padel");
		exigir(club.agregarDisciplina(tenisDeMesa), "agregar Tenis de Mesa");

		canchaTenis1 = instalacion("Cancha de tenis 1", "Tenis");
		canchaTenis2 = instalacion("Cancha de tenis 2", "Tenis");
		canchaPadel1 = instalacion("Cancha de padel 1", "Padel");
		mesa1 = instalacion("Mesa de tenis de mesa 1", "Tenis de Mesa");
		mesa2 = instalacion("Mesa de tenis de mesa 2", "Tenis de Mesa");

		inscribirDisciplina(valentina, tenis, "Avanzado");
		inscribirDisciplina(andres, tenis, "Intermedio");
		inscribirDisciplina(camila, tenis, "Principiante");
		inscribirDisciplina(santiago, tenis, "Intermedio");
		inscribirDisciplina(mariana, padel, "Intermedio");
		inscribirDisciplina(juanPablo, padel, "Intermedio");
		inscribirDisciplina(daniela, padel, "Avanzado");
		inscribirDisciplina(laura, padel, "Principiante");
		inscribirDisciplina(socioPrueba, tenisDeMesa, "Principiante");
		inscribirDisciplina(nicolas, tenisDeMesa, "Intermedio");

		reservaTenisPasada = reservar("2026-11-01", 8, 2, 2, canchaTenis2, andres);
		partidoPracticaFinalizado = club.programarPartidoPractica(reservaTenisPasada, Arrays.asList(andres, santiago));
		exigir(partidoPracticaFinalizado != null, "programar el partido de practica pasado");
		exigir(club.finalizarPartidoPractica(partidoPracticaFinalizado, "6-4, 6-3", andres, santiago),
				"finalizar el partido de practica pasado");

		reservaTenisFutura = reservar("2026-11-20", 10, 2, 2, canchaTenis1, valentina);
		partidoPracticaPendiente = club.programarPartidoPractica(reservaTenisFutura, Arrays.asList(valentina, andres));
		exigir(partidoPracticaPendiente != null, "programar el partido de practica futuro");

		reservaPadelFutura = reservar("2026-11-20", 16, 1, 4, canchaPadel1, mariana);
	}

	private void crearEntrenamiento() {
		sesionTenis = new SesionEntrenamiento("2026-11-05", 7, entrenador, canchaTenis2);
		exigir(club.agregarSesionEntrenamiento(sesionTenis), "agregar la sesion de entrenamiento de tenis");
		exigir(sesionTenis.convocar(valentina), "convocar a Valentina");
		exigir(sesionTenis.convocar(andres), "convocar a Andres");
		exigir(sesionTenis.convocar(camila), "convocar a Camila");
		exigir(club.registrarAsistencia(sesionTenis, valentina, EstadoAsistencia.ASISTIO), "asistencia de Valentina");
		exigir(club.registrarAsistencia(sesionTenis, andres, EstadoAsistencia.LLEGO_TARDE), "asistencia de Andres");
		exigir(club.registrarAsistencia(sesionTenis, camila, EstadoAsistencia.FALTO), "asistencia de Camila");
		exigir(sesionTenis.registrarEvaluacion(valentina, 5, "Muy buen saque y desplazamiento"),
				"evaluacion de Valentina");
		exigir(sesionTenis.registrarEvaluacion(andres, 3, "Debe trabajar el reves"), "evaluacion de Andres");
	}

	private void crearTiendaYCafeteria() {
		tienda = administracion.getUbicacionTienda();
		bodega = new UbicacionInventario("Bodega");

		balonFutbol = articulo("Balon de futbol", "Balones", 85000, 12, 30);
		raquetaTenis = articulo("Raqueta de tenis", "Raquetas", 320000, 5, 10);
		pelotasTenis = articulo("Tubo de pelotas de tenis", "Pelotas", 25000, 20, 40);
		camisetaClub = articulo("Camiseta oficial del club", "Uniformes", 60000, 25, 50);
		mallaVoleibol = articulo("Malla de voleibol", "Mallas", 150000, 2, 4);
		paletaPadel = articulo("Paleta de padel", "Raquetas", 280000, 4, 0);

		cafe = new Bebida("Cafe", 4000, 50, true);
		chocolate = new Bebida("Chocolate caliente", 5000, 30, true);
		agua = new Bebida("Agua", 3000, 80, false);
		bebidaHidratante = new Bebida("Bebida hidratante", 6000, 40, false);
		mani = new Snack("Mani salado", 3500, 40, new ArrayList<String>(Arrays.asList("mani")));
		galletas = new Snack("Galletas de avena", 4000, 35, new ArrayList<String>(Arrays.asList("gluten", "lacteos")));
		sandwich = new Snack("Sandwich de pollo", 9000, 20, new ArrayList<String>(Arrays.asList("gluten", "lacteos")));
		fruta = new Snack("Fruta picada", 5000, 15, new ArrayList<String>());
		administracion.agregarProductoCafeteria(cafe);
		administracion.agregarProductoCafeteria(chocolate);
		administracion.agregarProductoCafeteria(agua);
		administracion.agregarProductoCafeteria(bebidaHidratante);
		administracion.agregarProductoCafeteria(mani);
		administracion.agregarProductoCafeteria(galletas);
		administracion.agregarProductoCafeteria(sandwich);
		administracion.agregarProductoCafeteria(fruta);

		ventaTiendaValentina = new VentaTienda(LocalDate.of(2026, 11, 2), valentina.getIdentificacion(), true);
		exigir(ventaTiendaValentina.agregarArticulo(pelotasTenis, 2), "agregar pelotas a la venta de tienda");
		exigir(administracion.registrarVentaTienda(ventaTiendaValentina, 0), "registrar la venta de tienda");

		ventaCafeteriaAndres = new VentaCafeteria(LocalDate.of(2026, 11, 3), andres.getIdentificacion(), true);
		exigir(ventaCafeteriaAndres.agregarProducto(agua, 1, false, false), "agregar agua a la venta de cafeteria");
		exigir(ventaCafeteriaAndres.agregarProducto(galletas, 1, true, false),
				"agregar galletas a la venta de cafeteria");
		exigir(administracion.registrarVentaCafeteria(ventaCafeteriaAndres, 0), "registrar la venta de cafeteria");

		administracion.setPuntosFidelidad(socioPrueba.getIdentificacion(), 5000);
		administracion.registrarMensualidad(LocalDate.of(2026, 10, 5), 1800000);
		administracion.registrarMensualidad(LocalDate.of(2026, 11, 5), 1950000);
	}

	private void crearAdministracion() {
		// Cobertura base: dos entrenadores y un fisioterapeuta en la manana y en la tarde, todos los dias.
		for (String dia : DIAS) {
			turno("entrenador", Turno.ENTRENADOR, dia, 6, 14);
			turno("cmendoza", Turno.ENTRENADOR, dia, 6, 14);
			turno("lrestrepo", Turno.ENTRENADOR, dia, 14, 22);
			turno("dsalazar", Turno.ENTRENADOR, dia, 14, 22);
			turno("fisioterapeuta", Turno.FISIOTERAPEUTA, dia, 6, 14);
			turno("pcorrea", Turno.FISIOTERAPEUTA, dia, 14, 22);
		}
		exigir(administracion.mantieneCobertura(), "cumplir la cobertura minima de personal");

		turnoExtraLina = turno("lrestrepo", Turno.ENTRENADOR, "LUNES", 10, 14);
		solicitudPendiente = administracion.solicitarCambioTurno(HOY, turnoExtraLina,
				new Turno("lrestrepo", Turno.ENTRENADOR, "LUNES", LocalTime.of(8, 0), LocalTime.of(12, 0)));
		exigir(solicitudPendiente != null, "crear la solicitud de cambio de turno pendiente");

		sugerenciaPendiente = new Sugerencia("Termo deportivo", "Termo de un litro con el escudo del club", "Articulo",
				"cmendoza");
		sugerenciaAprobada = new Sugerencia("Barra de proteina", "Snack para despues de entrenar", "Snack",
				"fisioterapeuta");
		exigir(administracion.agregarSugerencia(sugerenciaPendiente), "agregar la sugerencia pendiente");
		exigir(administracion.agregarSugerencia(sugerenciaAprobada), "agregar la sugerencia aprobada");
		exigir(administracion.gestionarSugerencia(sugerenciaAprobada, true), "aprobar la sugerencia");
	}

	// ---------------------------------------------------------------- utilidades

	public static Deporte futbol() {
		return new Deporte("Futbol", 7);
	}

	public static Deporte baloncesto() {
		return new Deporte("Baloncesto", 5);
	}

	public static Deporte voleibol() {
		return new Deporte("Voleibol", 6);
	}

	/** Mismos rangos de edad que usa la consola del administrador al crear equipos. */
	public static Categoria sub13() {
		return new Categoria("Sub-13", 0, 13);
	}

	public static Categoria sub15() {
		return new Categoria("Sub-15", 14, 15);
	}

	public static Categoria sub17() {
		return new Categoria("Sub-17", 16, 17);
	}

	public static Categoria mayores() {
		return new Categoria("Mayores", 18, 99);
	}

	/** Busca la inscripcion de un socio en un equipo. */
	public static JugadorEquipo jugador(Equipo equipo, Socio socio) {
		for (JugadorEquipo jugador : equipo.getJugadores()) {
			if (jugador.getJugador().getIdentificacion().equals(socio.getIdentificacion())) {
				return jugador;
			}
		}
		return null;
	}

	/** Lista de socios con una sola llamada, util para programar partidos de practica. */
	public static List<Socio> socios(Socio... socios) {
		return new ArrayList<Socio>(Arrays.asList(socios));
	}

	private Equipo equipo(String nombre, int cupo, Deporte deporte, Categoria categoria) {
		Equipo equipo = new Equipo(nombre, cupo, deporte, categoria);
		exigir(club.agregarEquipo(equipo), "crear el equipo " + nombre);
		return equipo;
	}

	private void inscribir(Equipo equipo, Socio socio, String posicion, int camiseta) {
		exigir(club.inscribirJugador(equipo, socio, posicion, camiseta),
				"inscribir a " + socio.getNombre() + " en " + equipo.getNombre());
	}

	private Instalacion instalacion(String nombre, String disciplina) {
		Instalacion instalacion = new Instalacion(nombre, disciplina);
		exigir(club.agregarInstalacion(instalacion), "agregar la instalacion " + nombre);
		return instalacion;
	}

	private void inscribirDisciplina(Socio socio, Disciplina disciplina, String nivel) {
		exigir(club.inscribirEnDisciplina(socio, disciplina, nivel),
				"inscribir a " + socio.getNombre() + " en " + disciplina.getNombre());
	}

	private Reserva reservar(String fecha, int hora, int duracion, int jugadores, Instalacion instalacion,
			Socio socio) {
		Reserva reserva = new Reserva(fecha, hora, duracion, jugadores, instalacion, socio);
		exigir(club.reservarInstalacion(reserva), "reservar " + instalacion.getNombreCancha() + " el " + fecha);
		return reserva;
	}

	private ArticuloTienda articulo(String nombre, String categoria, double precio, int enTienda, int enBodega) {
		ArticuloTienda articulo = new ArticuloTienda(nombre, categoria, precio);
		administracion.agregarArticulo(articulo);
		exigir(administracion.reabastecerInventario(articulo, tienda, enTienda), "existencias en tienda de " + nombre);
		if (enBodega > 0) {
			exigir(administracion.reabastecerInventario(articulo, bodega, enBodega),
					"existencias en bodega de " + nombre);
		}
		return articulo;
	}

	private Turno turno(String login, String tipo, String dia, int inicio, int fin) {
		Turno turno = new Turno(login, tipo, dia, LocalTime.of(inicio, 0), LocalTime.of(fin, 0));
		exigir(administracion.agregarTurno(turno), "agregar el turno de " + login + " el " + dia);
		return turno;
	}

	private static void exigir(boolean condicion, String descripcion) {
		if (condicion == false) {
			throw new IllegalStateException("Los datos de prueba no son validos: no se pudo " + descripcion);
		}
	}
}
