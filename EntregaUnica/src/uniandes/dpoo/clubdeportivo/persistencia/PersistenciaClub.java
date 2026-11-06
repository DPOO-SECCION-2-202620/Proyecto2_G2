package uniandes.dpoo.clubdeportivo.persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import uniandes.dpoo.clubdeportivo.modelo.Club;
import uniandes.dpoo.clubdeportivo.modelo.competencias.Competencia;
import uniandes.dpoo.clubdeportivo.modelo.competencias.CompetenciaLiga;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaBaloncesto;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaFutbol;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaPartido;
import uniandes.dpoo.clubdeportivo.modelo.equipos.EstadisticaVoleibol;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Administrador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Entrenador;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Fisioterapeuta;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Socio;
import uniandes.dpoo.clubdeportivo.modelo.usuarios.Usuario;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Bebida;
import uniandes.dpoo.clubdeportivo.modelo.ventas.ProductoCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Snack;
import uniandes.dpoo.clubdeportivo.modelo.ventas.Venta;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaCafeteria;
import uniandes.dpoo.clubdeportivo.modelo.ventas.VentaTienda;

public class PersistenciaClub {

	private Path ruta;
	private Gson gson;

	public PersistenciaClub() {
		this(Paths.get("datos", "club.json"));
	}

	public PersistenciaClub(Path ruta) {
		this.ruta = ruta;
		GsonBuilder constructor = new GsonBuilder().setPrettyPrinting();
		constructor.registerTypeAdapter(LocalDate.class, new AdaptadorLocalDate());
		constructor.registerTypeAdapter(LocalTime.class, new AdaptadorLocalTime());
		constructor.registerTypeAdapter(Usuario.class, adaptador(Usuario.class, tiposUsuarios()));
		constructor.registerTypeAdapter(Competencia.class, adaptador(Competencia.class, tiposCompetencia()));
		constructor.registerTypeAdapter(EstadisticaPartido.class, adaptador(EstadisticaPartido.class, tiposEstadistica()));
		constructor.registerTypeAdapter(ProductoCafeteria.class, adaptador(ProductoCafeteria.class, tiposProductos()));
		constructor.registerTypeAdapter(Venta.class, adaptador(Venta.class, tiposVentas()));
		this.gson = constructor.create();
	}

	public Club cargarClub() {
		if (Files.exists(ruta) == false) {
			return new Club();
		}
		try (FileReader lector = new FileReader(ruta.toFile())) {
			Club club = gson.fromJson(lector, Club.class);
			if (club == null) {
				club = new Club();
			}
			club.reconstruirRelaciones();
			return club;
		} catch (Exception e) {
			throw new IllegalStateException("No fue posible cargar los datos del club: " + e.getMessage(), e);
		}
	}

	public void guardarClub(Club club) {
		try {
			Path carpeta = ruta.getParent();
			if (carpeta != null) {
				Files.createDirectories(carpeta);
			}
			try (FileWriter escritor = new FileWriter(ruta.toFile())) {
				gson.toJson(club, escritor);
			}
		} catch (Exception e) {
			throw new IllegalStateException("No fue posible guardar los datos del club: " + e.getMessage(), e);
		}
	}

	private <T> AdaptadorPolimorfico<T> adaptador(Class<T> base, Map<String, Class<? extends T>> tipos) {
		return new AdaptadorPolimorfico<T>(base, tipos);
	}

	private Map<String, Class<? extends Usuario>> tiposUsuarios() {
		Map<String, Class<? extends Usuario>> tipos = new HashMap<String, Class<? extends Usuario>>();
		tipos.put("socio", Socio.class);
		tipos.put("entrenador", Entrenador.class);
		tipos.put("fisioterapeuta", Fisioterapeuta.class);
		tipos.put("administrador", Administrador.class);
		return tipos;
	}

	private Map<String, Class<? extends Competencia>> tiposCompetencia() {
		Map<String, Class<? extends Competencia>> tipos = new HashMap<String, Class<? extends Competencia>>();
		tipos.put("liga", CompetenciaLiga.class);
		return tipos;
	}

	private Map<String, Class<? extends EstadisticaPartido>> tiposEstadistica() {
		Map<String, Class<? extends EstadisticaPartido>> tipos = new HashMap<String, Class<? extends EstadisticaPartido>>();
		tipos.put("futbol", EstadisticaFutbol.class);
		tipos.put("baloncesto", EstadisticaBaloncesto.class);
		tipos.put("voleibol", EstadisticaVoleibol.class);
		return tipos;
	}

	private Map<String, Class<? extends ProductoCafeteria>> tiposProductos() {
		Map<String, Class<? extends ProductoCafeteria>> tipos = new HashMap<String, Class<? extends ProductoCafeteria>>();
		tipos.put("bebida", Bebida.class);
		tipos.put("snack", Snack.class);
		return tipos;
	}

	private Map<String, Class<? extends Venta>> tiposVentas() {
		Map<String, Class<? extends Venta>> tipos = new HashMap<String, Class<? extends Venta>>();
		tipos.put("tienda", VentaTienda.class);
		tipos.put("cafeteria", VentaCafeteria.class);
		return tipos;
	}

	private class AdaptadorPolimorfico<T> implements JsonSerializer<T>, JsonDeserializer<T> {
		private Class<T> base;
		private Map<String, Class<? extends T>> tipos;
		private Map<Class<? extends T>, String> nombres;

		private AdaptadorPolimorfico(Class<T> base, Map<String, Class<? extends T>> tipos) {
			this.base = base;
			this.tipos = tipos;
			nombres = new HashMap<Class<? extends T>, String>();
			for (String nombre : tipos.keySet()) {
				nombres.put(tipos.get(nombre), nombre);
			}
		}

		@Override
		public JsonElement serialize(T objeto, Type tipo, JsonSerializationContext contexto) {
			JsonObject json = contexto.serialize(objeto, objeto.getClass()).getAsJsonObject();
			json.addProperty("tipo", nombres.get(objeto.getClass()));
			return json;
		}

		@Override
		public T deserialize(JsonElement elemento, Type tipo, JsonDeserializationContext contexto)
				throws JsonParseException {
			JsonObject json = elemento.getAsJsonObject();
			if (json.has("tipo") == false) {
				throw new JsonParseException("Falta el tipo de " + base.getSimpleName());
			}
			Class<? extends T> clase = tipos.get(json.get("tipo").getAsString());
			if (clase == null) {
				throw new JsonParseException("Tipo desconocido para " + base.getSimpleName());
			}
			return contexto.deserialize(json, clase);
		}
	}

	private class AdaptadorLocalDate implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
		@Override
		public JsonElement serialize(LocalDate fecha, Type tipo, JsonSerializationContext contexto) {
			return contexto.serialize(fecha.toString());
		}
		@Override
		public LocalDate deserialize(JsonElement elemento, Type tipo, JsonDeserializationContext contexto) {
			return LocalDate.parse(elemento.getAsString());
		}
	}

	private class AdaptadorLocalTime implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
		@Override
		public JsonElement serialize(LocalTime hora, Type tipo, JsonSerializationContext contexto) {
			return contexto.serialize(hora.toString());
		}
		@Override
		public LocalTime deserialize(JsonElement elemento, Type tipo, JsonDeserializationContext contexto) {
			return LocalTime.parse(elemento.getAsString());
		}
	}
}
