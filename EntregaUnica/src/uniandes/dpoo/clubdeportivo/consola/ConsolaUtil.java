package uniandes.dpoo.clubdeportivo.consola;

import java.util.Scanner;

class ConsolaUtil {

	static String leerTexto(Scanner entrada, String mensaje) {
		System.out.print(mensaje);
		if (entrada.hasNextLine() == false) return null;
		return entrada.nextLine().trim();
	}

	static int leerEntero(Scanner entrada, String mensaje, int minimo, int maximo) {
		while (entrada.hasNextLine()) {
			System.out.print(mensaje);
			String texto = entrada.nextLine().trim();
			try {
				int valor = Integer.parseInt(texto);
				if (valor >= minimo && valor <= maximo) return valor;
			} catch (NumberFormatException e) {
			}
			System.out.println("Escriba un numero entre " + minimo + " y " + maximo + ".");
		}
		return -1;
	}

	static double leerDecimal(Scanner entrada, String mensaje, double minimo) {
		while (entrada.hasNextLine()) {
			System.out.print(mensaje);
			String texto = entrada.nextLine().trim();
			try {
				double valor = Double.parseDouble(texto);
				if (valor >= minimo) return valor;
			} catch (NumberFormatException e) {
			}
			System.out.println("Escriba un numero mayor o igual a " + minimo + ".");
		}
		return -1;
	}

	static int seleccionar(Scanner entrada, String titulo, int cantidad) {
		if (cantidad == 0) {
			System.out.println("No hay opciones registradas.");
			return -1;
		}
		System.out.println(titulo);
		return leerEntero(entrada, "Numero: ", 1, cantidad) - 1;
	}

	static boolean confirmar(Scanner entrada, String mensaje) {
		String respuesta = leerTexto(entrada, mensaje + " (s/n): ");
		return respuesta != null && respuesta.equalsIgnoreCase("s");
	}
}
