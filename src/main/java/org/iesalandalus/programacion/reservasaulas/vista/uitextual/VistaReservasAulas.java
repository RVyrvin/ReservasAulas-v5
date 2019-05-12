package org.iesalandalus.programacion.reservasaulas.vista.uitextual;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.vista.IVistaReservasAulas;

public class VistaReservasAulas implements IVistaReservasAulas {

	private static final String ERROR = "ERROR: ";
	// private static final String NOMBRE_VALIDO = "\\w+";
	private static final String CORREO_VALIDO = "good@mail.com";

	private IControladorReservasAulas controlador;

	public VistaReservasAulas() {
		// modelo = new ModeloReservasAulas();
		Opcion.setVista(this);
	}

	@Override
	public void setControlador(IControladorReservasAulas controlador) {
		this.controlador = controlador;
	}

	@Override
	public void comenzar() {
		int ordinalOpcion;
		do {
			Consola.mostrarMenu();
			ordinalOpcion = Consola.elegirOpcion();
			Opcion opcion = Opcion.getOpcionSegunOrdinal(ordinalOpcion);
			opcion.ejecutar();
		} while (ordinalOpcion != Opcion.SALIR.ordinal());

	}

	
	public void salir() {
		controlador.salir();
		System.out.println("\nHasta luego ...!!!");
	}

	
	public void insertarAula() {

		Consola.mostrarCabecera("\nInsertar aula");

		try {
			Aula aula = Consola.leerAula();
			controlador.insertarAula(aula);
			System.out.println("Aula insertada correctamente");
		} catch (OperationNotSupportedException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void borrarAula() {

		Consola.mostrarCabecera("\nBorrar aula");

		try {
			Aula aula = Consola.leerAula();
			controlador.borrarAula(aula);
			System.out.println("Aula borrada correctamente");
		} catch (OperationNotSupportedException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void buscarAula() {

		Consola.mostrarCabecera("\nBuscar aula");
		Aula aula = null;

		try {
			aula = Consola.leerAula();
			aula = controlador.buscarAula(aula);
			if (aula != null) {
				System.out.println("El aula buscado es: " + aula);
			} else {
				System.out.println("No existe ningún aula con dicho nombre");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void listarAulas() {

		Consola.mostrarCabecera("\nListar aulas");

		List<String> aulas = controlador.representarAulas();

		if (aulas.size() > 0) {
			for (String aula : aulas) {
				System.out.println(aula);
			}
		} else {
			System.out.println("No hay aulas para listar");
		}
	}

	
	public void insertarProfesor() {

		Consola.mostrarCabecera("\nInsertar profesor");

		try {
			Profesor profesor = Consola.leerProfesor();
			controlador.insertarProfesor(profesor);
			System.out.println("Profesor insertado correctamente");
		} catch (OperationNotSupportedException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void borrarProfesor() {

		Consola.mostrarCabecera("\nBorrar prfesor");

		try {
			String nombreProfesor = Consola.leerNombreProfesor().trim();

			Profesor profesor = null;
			profesor = controlador.buscarProfesor(new Profesor(nombreProfesor, CORREO_VALIDO));

			if (profesor != null) {
				controlador.borrarProfesor(profesor);
				System.out.println("Profesor borrado correctamente");
			} else {
				System.out.println("Profesor que queres borrar todavia no se ha registrado...");
			}

		} catch (OperationNotSupportedException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void buscarProfesor() {

		Consola.mostrarCabecera("\nBuscar profesor");

		try {
			String nombreProfesor = Consola.leerNombreProfesor().trim();

			Profesor profesor = null;
			profesor = controlador.buscarProfesor(new Profesor(nombreProfesor, CORREO_VALIDO));

			if (profesor != null) {
				System.out.println("El profesor buscado es: " + profesor.toString());
			} else {
				System.out.println("No existe ningún profesor con dicho nombre");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void listarProfesor() {

		Consola.mostrarCabecera("\nListar profesores");
		List<String> profesores = controlador.representarProfesores();
		if (profesores.size() > 0) {
			for (String profesor : profesores) {
				System.out.println(profesor);
			}
		} else {
			System.out.println("No hay profesores para listar");
		}
	}

	
	public void realizarReserva() {

		Consola.mostrarCabecera("\nRealizar reserva");

		try {

			boolean yaExiste = false;
			String nombreProfesor = Consola.leerNombreProfesor().trim();
			Profesor profesor = controlador.buscarProfesor(new Profesor(nombreProfesor, CORREO_VALIDO));

			if (profesor != null) {

				Reserva reserva = leerReserva(profesor);

				List<Reserva> reservas = controlador.getReservasProfesor(profesor);

				for (Reserva res : reservas) {
					if (res.equals(reserva))
						yaExiste = true;
				}

				if (!yaExiste) {
					controlador.realizarReserva(reserva);
					System.out.println("La reserva fue realizada co exito!!!");
				} else
					System.out.println("La misma reserva ya existe.");

			} else {
				System.out.println("Profesor " + nombreProfesor + " no esta registrado en el sistema");
			}

		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	private Reserva leerReserva(Profesor profesor) {

		// Consola.mostrarCabecera("Leer reserva");

		Reserva reserva = null;

		try {

			Aula aula = Consola.leerAula();

			if (controlador.buscarAula(aula) != null) {

				// creamos la nueva reserva
				Permanencia permanencia = Consola.leerPermanencia();

				reserva = new Reserva(profesor, aula, permanencia);

			} else {
				System.out.println("Alua " + aula.getNombre() + " no eta registrada en el sistema");
			}

		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}

		return reserva;
	}

	
	public void anularReserva() {

		Consola.mostrarCabecera("\nAnular reserva");

		try {
			String nombreProfesor = Consola.leerNombreProfesor().trim();
			Profesor profesor = controlador.buscarProfesor(new Profesor(nombreProfesor, CORREO_VALIDO));

			if (profesor != null) {

				Reserva reserva = leerReserva(profesor);

				if (reserva != null) {
					controlador.anularReserva(reserva);
					System.out.println("La reserva fue borrada correctamente");
				}

			} else {
				System.out.println("Profesor " + nombreProfesor + " no esta registrado en el sistema");
			}
		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void listarReserva() {

		Consola.mostrarCabecera("\nListar reserva");

		List<String> reservasSTR = controlador.representarReservas();

		if (reservasSTR.size() > 0) {
			for (String reserva : reservasSTR) {
				System.out.println(reserva.toString());
			}
		} else {
			System.out.println("No hay reservas para listar ...");
		}
	}

	
	public void listarReservasAulas() {

		Consola.mostrarCabecera("\nlista reservas de aulas");

		try {
			Aula aula = Consola.leerAula();

			if (controlador.buscarAula(aula) != null) {

				List<Reserva> aulas = controlador.getReservasAula(aula);

				if (aulas.size() > 0) {
					for (Reserva reserva : aulas) {
						System.out.println(reserva.toString());
					}
				} else {
					System.out.println("No hay reservas para esta aula ...");
				}
			} else {
				System.out.println("Alua " + aula.getNombre() + " no eta registrada en el sistema");
			}
		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void listarReservasProfesores() {

		Consola.mostrarCabecera("\nlista reservas de profesores");

		try {

			String nombreProfesor = Consola.leerNombreProfesor();
			Profesor profesor = new Profesor(nombreProfesor, CORREO_VALIDO);

			if (controlador.buscarProfesor(profesor) != null) {

				List<Reserva> profesores = controlador.getReservasProfesor(profesor);

				if (profesores.size() > 0) {
					for (Reserva reserva : profesores) {
						System.out.println(reserva.toString());
					}
				} else {
					System.out.println("No hay reservas para este profesor ...");
				}

			} else {
				System.out.println("Profesor " + nombreProfesor + " no esta registrado en el sistema");
			}
		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void listarReservasPermanencia() {

		Consola.mostrarCabecera("\nlista reservas de permanencia");

		try {
			Permanencia permanencia = Consola.leerPermanencia();

			List<Reserva> permanencias = controlador.getReservasPermanencia(permanencia);

			if (permanencias.size() > 0) {
				boolean anyReserva = false;
				for (Reserva reserva : permanencias) {
					if (reserva != null) {
						System.out.println(reserva.toString());
						anyReserva = true;
					}
				}
				if (!anyReserva)
					System.out.println("No hay reservas para esta permanencia ...");
			} else {
				System.out.println("No hay reservas con esta permanencia");
			}
		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	
	public void consultarDisponibilidad() {

		Consola.mostrarCabecera("\nConsultar disponibilidad");

		boolean isDisponible = true;

		try {

			Aula aula = Consola.leerAula();

			if (controlador.buscarAula(aula) != null) {

				Permanencia permanencia = Consola.leerPermanencia();

				isDisponible = controlador.consultarDisponibilidad(aula, permanencia);

				if (isDisponible)
					System.out.println(
							"\naula " + aula.getNombre() + " esta disponible para la " + permanencia.toString());
				else
					System.out.println(
							"\naula " + aula.getNombre() + " no esta disponible para la " + permanencia.toString());

			} else {
				System.out.println("Alua " + aula.getNombre() + " no eta registrada en el sistema");
			}
		} catch (Exception e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

}
