package org.iesalandalus.programacion.reservasaulas.modelo.mongodb.utilidades;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.bson.Document;

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.*;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.*;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

	// credenciale
	private static final String SERVIDOR = "35.180.192.242";
	private static final int PUERTO = 27017;
	private static final String BD = "reservasaulas-roman-vyrvin";
	private static final String USUARIO = "roman-vyrvin";
	private static final String CONTRASENA = "roman-vyrvin-5620";

	public static final String AULA = "aula";
	public static final String PROFESOR = "profesor";
	public static final String RESERVA = "reserva";

	// AULA
	public static final String AULA_NOMBRE = "nombre";
	public static final String AULA_PUESTOS = "puestos";

	// PROFESOR
	public static final String PROFESOR_NOMBRE = "nombre";
	public static final String PROFESOR_CORREO = "correo";
	public static final String PROFESOR_TELEFONO = "telefono";

	// RESERVA
	public static final String PERMANENCIA = "permanencia";
	public static final String PERMANENCIA_DIA = "dia";
	public static final String PERMANENCIA_POR_HORA = "hora";
	public static final String PERMANENCIA_POR_TRAMO = "tramo";
	public static final String TRAMO = "tramo";

	public static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

	private static MongoClient cliente = null;

	private MongoDB() {
		// Evitamos que se cree el constructor por defecto
	}

	public static MongoClient establecerConexion() {
		if (cliente == null) {
			MongoCredential credenciales = MongoCredential.createScramSha1Credential(USUARIO, BD,
					CONTRASENA.toCharArray());
			cliente = MongoClients.create(MongoClientSettings.builder()
					.applyToClusterSettings(
							builder -> builder.hosts(Arrays.asList(new ServerAddress(SERVIDOR, PUERTO))))
					.credential(credenciales).build());
			System.out.println("Conexión a MongoDB realizada correctamente.");
		}
		return cliente;
	}

	public static MongoClient getCliente() {
		return cliente;
	}

	public static MongoDatabase getBD() {
		if (cliente == null) {
			establecerConexion();
		}
		return cliente.getDatabase(BD);
	}

	public static void cerrarCliente() {
		if (cliente != null) {
			cliente.close();
			System.out.println("Conexión a MongoDB cerrada.");
		}
	}

	// aulas
	public static Document obtenerDocumentoDesdeAula(Aula aula) {

		if (aula == null)
			return null;

		Document dAula = new Document().append(AULA_NOMBRE, aula.getNombre()).append(AULA_PUESTOS, aula.getPuestos());

		return dAula;

	}

	public static Aula obtenerAulaDesdaDocumento(Document documentoAula) {

		if (documentoAula == null)
			return null;

		Document dAula = (Document) documentoAula;
		String nombre = dAula.getString(AULA_NOMBRE);
		int puestos = dAula.getInteger(AULA_PUESTOS);

		return new Aula(nombre, puestos);
	}

	// profesores

	public static Document obtenerDocumentoDesdeProfesor(Profesor profesor) {

		if (profesor == null)
			return null;

		Document dProfesor;

		if (profesor.getTelefono() == null) {
			dProfesor = new Document().append(PROFESOR_NOMBRE, profesor.getNombre()).append(PROFESOR_CORREO,
					profesor.getCorreo());
		} else {
			dProfesor = new Document().append(PROFESOR_NOMBRE, profesor.getNombre())
					.append(PROFESOR_CORREO, profesor.getCorreo()).append(PROFESOR_TELEFONO, profesor.getTelefono());
		}

		return dProfesor;
	}

	public static Profesor obtenerProfesorDesdeDocumento(Document documentoProfesor) {

		if (documentoProfesor == null)
			return null;

		Document dProfesor = (Document) documentoProfesor;
		String nombre = dProfesor.getString(PROFESOR_NOMBRE);
		String correo = dProfesor.getString(PROFESOR_CORREO);
		String telefono = dProfesor.getString(PROFESOR_TELEFONO);

		if (telefono == null) {
			return new Profesor(nombre, correo);
		} else {
			return new Profesor(nombre, correo, telefono);
		}
	}

	public static Reserva obtenerReservaDesdeDocumento(Document documentoReserva) {
		if (documentoReserva == null)
			return null;

		Document rAula = (Document) documentoReserva.get(AULA);
		String aulaNombre = rAula.getString(AULA_NOMBRE);
		int aulaPuestos = rAula.getInteger(AULA_PUESTOS);
		Aula aula = new Aula(aulaNombre, aulaPuestos);

		Document rProfesor = (Document) documentoReserva.get(PROFESOR);
		String profesorNombre = rProfesor.getString(PROFESOR_NOMBRE);
		String profesorCorreo = rProfesor.getString(PROFESOR_CORREO);
		String profesorTelefono = rProfesor.getString(PROFESOR_TELEFONO);

		Profesor profesor;
		if (profesorTelefono == null) {
			profesor = new Profesor(profesorNombre, profesorCorreo);
		} else {
			profesor = new Profesor(profesorNombre, profesorCorreo, profesorTelefono);
		}

		Document rPermanencia = (Document) documentoReserva.get(PERMANENCIA);
		String dia = rPermanencia.getString(PERMANENCIA_DIA);
		String pHora = rPermanencia.getString(PERMANENCIA_POR_HORA);
		String pTramo = rPermanencia.getString(PERMANENCIA_POR_TRAMO);

		Permanencia permanencia;

		if (pHora == null)
			permanencia = new PermanenciaPorTramo(dia, pTramo);
		else if (pTramo == null)
			permanencia = new PermanenciaPorHora(dia, pHora);
		else
			throw new IllegalArgumentException("Ambos propiedades de permanencia (hora y tramo) no pueden ser nulos");

		return new Reserva(profesor, aula, permanencia);
	}

	public static Document obtenerDocumentoDesdeReserva(Reserva reserva) {

		if (reserva == null)
			return null;

		Aula aula = reserva.getAula();
		Document dAula = new Document().append(AULA_NOMBRE, aula.getNombre()).append(AULA_PUESTOS, aula.getPuestos());

		Profesor profesor = reserva.getProfesor();
		Document dProfesor = new Document()
				.append(PROFESOR_NOMBRE, profesor.getNombre())
				.append(PROFESOR_CORREO,
				profesor.getCorreo());
		
		if (profesor.getTelefono() != null) {
			dProfesor.append(PROFESOR_TELEFONO, profesor.getTelefono());
		}
		
		Permanencia permanencia = reserva.getPermanencia();
		
		Document dPermanencia = new Document().append(PERMANENCIA_DIA, permanencia.getDia().format(FORMATO_DIA));
		
		if (permanencia instanceof PermanenciaPorHora)
			dPermanencia.append(PERMANENCIA_POR_HORA, ((PermanenciaPorHora) permanencia).getHora().format(FORMATO_HORA));
		else if (permanencia instanceof PermanenciaPorTramo)
			dPermanencia.append(PERMANENCIA_POR_TRAMO, ((PermanenciaPorTramo) permanencia).getTramo().toString());
		else
			throw new IllegalArgumentException("Debe ser o por horas o por tramos, no pueden ser nulos");
		
		return new Document().append(PROFESOR, dProfesor).append(AULA, dAula).append(PERMANENCIA, dPermanencia);		
	}

}
