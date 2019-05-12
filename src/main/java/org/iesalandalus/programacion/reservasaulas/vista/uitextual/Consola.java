package org.iesalandalus.programacion.reservasaulas.vista.uitextual;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.utilidades.Entrada;

public class Consola {

	private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final String ERROR = "ERROR: ";
	private static final String ER_TELEFONO = "[69][0-9]{8}";
	//private static final String ER_CORREO = "\\w[.\\w]*@[a-zA-Z]+\\.[a-zA-Z]{2,5}";

	private Consola() {
	}

	public static void mostrarMenu() {
		mostrarCabecera("\nGestión de clientes");
		for (Opcion opcion : Opcion.values()) {
			System.out.println(opcion);
		}
	}

	public static void mostrarCabecera(String mensaje) {
		System.out.printf("%n%s%n", mensaje);
		String cadena = "%0" + mensaje.length() + "d%n";
		System.out.println(String.format(cadena, 0).replace("0", "-"));
	}

	public static int elegirOpcion() {
		int ordinalOpcion = 0;
		do {
			System.out.print("\nElige una opción: ");
			ordinalOpcion = Entrada.entero();
		} while (!Opcion.esOrdinalValido(ordinalOpcion));
		return ordinalOpcion;
	}

	public static Aula leerAula() {
		String nombreAula = leerNombreAula();
		System.out.print("Introduce el numero de puestos del Aula: ");
		int puestos = Entrada.entero();
		Aula aula = new Aula(nombreAula, puestos);
		return aula;
	}

	public static String leerNombreAula() {
		System.out.print("Introduce el nombre da la aula: ");
		String nombreAula = Entrada.cadena();
		return nombreAula;
	}

	public static Profesor leerProfesor() {

		Profesor profesor = null;
		String nombreProfesor = leerNombreProfesor();

		System.out.print("Introduce el correo valido: ");
		String correoProfesor = Entrada.cadena();

		System.out.print("Introduce el telefono valido: ");
		String telefonoProfesor = Entrada.cadena();

		if (telefonoProfesor.matches(ER_TELEFONO))
			profesor = new Profesor(nombreProfesor, correoProfesor, telefonoProfesor);
		else
			profesor = new Profesor(nombreProfesor, correoProfesor);

		return profesor;
	}

	public static String leerNombreProfesor() {
		System.out.print("Introduce el nombre del profesor: ");
		String nombreProfesor = Entrada.cadena();
		return nombreProfesor;
	}

	public static Tramo leerTramo() {

		Tramo tramo = null;

		do {
			System.out.print("Introduce el tramo (M/T): ");
			char ch = Entrada.caracter();
			if (ch == 'M' || ch == 'm') {
				tramo = Tramo.MANANA;
			} else if (ch == 'T' || ch == 't') {
				tramo = Tramo.TARDE;
			}
		} while (tramo == null);

		return tramo;
	}

	public static String leerDia() {
		LocalDate data = null;
		LocalDate curDate = LocalDate.now();

		do {

			int anno = 0;
			do {
				System.out.print("Introduce el anno (yyyy): ");
				anno = Entrada.entero();
				// Comprobamos si el año introducido es mayour o igual al corriente
				if (anno < curDate.getYear()) {
					anno = 0;
					System.out.println("el año tiene que ser mayor o igual al actual");
				}

			} while (anno <= 0);

			int mes = 0;
			do {
				System.out.print("Introduce el mes (mm): ");
				mes = Entrada.entero();
				// validamos el numero del mes entre 1 y 12
				if (mes < 1 || mes > 12) {
					mes = 0;
					System.out.println("incorrecto valor del mes, tiene que ser del 1 a 12");
					// no pasamo con el mes si es anterior al actual
				} else if (anno == curDate.getYear() && mes < curDate.getMonthValue()) {
					mes = 0;
					System.out.println("El mes no tiene que ser pasado");
				}

			} while (mes <= 0);

			int dia = 0;
			do {
				System.out.print("Introduce el día (dd): ");
				dia = Entrada.entero();
				// comprobamos si el dia tecleado esta entre dia minimo y dia maximo por lo alto
				if (dia < 1 || dia > 31) {
					dia = 0;
					System.out.println("incorrecto valor del día");
					// no pasamos el dia si es anterior al actual
				} else if (mes == curDate.getMonthValue() && dia < curDate.getDayOfMonth()) {
					dia = 0;
					System.out.println("El dia no tiene que ser pasado");
				} else {
					// sacamos el numero de los dias que tiene el mes en el año tecleado
					YearMonth ym = YearMonth.of(anno, mes);
					int maxDay = ym.lengthOfMonth();
					// comprobamos si el dia tecledo no sobrepasa el maximo de los dias que tiene el
					// mes
					if (dia > maxDay) {
						dia = 0;
						System.out.println(
								"mes " + Month.of(mes) + " del año " + anno + " tiene solo " + maxDay + " dias");
					}
				}

			} while (dia <= 0);

			try {
				data = LocalDate.of(anno, mes, dia);
			} catch (Exception e) {
				System.out.println(ERROR + e.getMessage());
			}

		} while (data == null);

		return data.format(FORMATO_DIA).toString();
	}

	public static String leerHora() {
		System.out.print("Introduce la hora de 08:00 hasta 22:00: ");
		String hora = Entrada.cadena();
		return hora;
	}

	public static Permanencia leerPermanencia() {
		Permanencia permanencia = null;
		int tipoPermanencia = elegirPermanencia();

		if (tipoPermanencia == 1)
			permanencia = new PermanenciaPorHora(leerDia(), leerHora());
		else
			permanencia = new PermanenciaPorTramo(leerDia(), leerTramo());
		return permanencia;
	}

	public static int elegirPermanencia() {
		int tipoPermanencia = -11;

		do {
			System.out.println("Elije permanencia:");
			System.out.println("1. por Hora(H)");
			System.out.println("2. por Tramo(T)");
			char ch = Entrada.caracter();
			if (ch == 'H' || ch == 'h') {
				tipoPermanencia = 1;
			} else if (ch == 'T' || ch == 't') {
				tipoPermanencia = 2;
			}
		} while (tipoPermanencia == -1);

		return tipoPermanencia;
	}
}