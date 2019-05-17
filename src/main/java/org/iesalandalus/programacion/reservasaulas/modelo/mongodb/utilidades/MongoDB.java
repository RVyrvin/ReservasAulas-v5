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

	public static final String AULA = "aulas";
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
	public static final String PERMANENCIA_POR_HORA = "permanenciaporhora";
	public static final String PERMANENCIA_POR_TRAMO = "permanenciaportramo";
	public static final String TRAMO = "tramo";

	////

	public static final String DATOS_CONTACTO = "datosContacto";

	public static final String TELEFONO = "telefono";
	public static final String CORREO = "correo";

	public static final String DIRECCION_POSTAL = "direccionPostal";

	public static final String DIRECCION = "direccion";
	public static final String LOCALIDAD = "localidad";
	public static final String CODIGO_POSTAL = "codigoPostal";

	public static final String DATOS_PERSONALES = "datosPersonales";

	public static final String NOMBRE = "nombre";
	public static final String APELLIDOS = "apellidos";
	public static final String DNI = "dni";
	public static final String FECHA_NACIMIENTO = "fechaNacimiento";

	public static final String DATOS_PERSONALES_DNI = DATOS_PERSONALES + "." + DNI;
	////

	public static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
			dProfesor = new Document().append(PROFESOR_NOMBRE, profesor.getNombre()).append(PROFESOR_CORREO, profesor.getCorreo());
		} else {
			dProfesor = new Document().append(PROFESOR_NOMBRE, profesor.getNombre()).append(PROFESOR_CORREO, profesor.getCorreo()).append(PROFESOR_TELEFONO, profesor.getTelefono());
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

}
