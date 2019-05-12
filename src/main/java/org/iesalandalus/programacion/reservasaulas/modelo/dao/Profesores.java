package org.iesalandalus.programacion.reservasaulas.modelo.dao;

import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Profesores {

	private static final String NOMBRE_FICHERO_PROFESORES = "res/ficheros/profesores.dat";
	private List<Profesor> coleccionProfesores;

	public Profesores() {
		this.coleccionProfesores = new ArrayList<>();
	}

	public Profesores(Profesores profesores) {
		setProfesores(profesores);
	}

	private void setProfesores(Profesores profesores) {
		if (profesores == null) {
			throw new IllegalArgumentException("No se pueden copiar profesores nulos.");
		} else {
			this.coleccionProfesores = copiaProfundaProfesores(profesores.coleccionProfesores);
		}
	}

	private List<Profesor> copiaProfundaProfesores(List<Profesor> profesores) {
		List<Profesor> cpyProfesor = new ArrayList<>();
		for (Profesor profesor : profesores) {
			cpyProfesor.add(new Profesor(profesor));
		}
		return cpyProfesor;
	}

	public List<Profesor> getProfesores() {
		return copiaProfundaProfesores(this.coleccionProfesores);
	}

	public int getNumProfesores() {
		return this.coleccionProfesores.size();
	}

	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("No se puede insertar un profesor nulo.");
		} else {
			if (coleccionProfesores.contains(profesor)) {
				throw new OperationNotSupportedException("El profesor ya existe.");
			} else {
				coleccionProfesores.add(new Profesor(profesor));
			}
		}
	}

	public Profesor buscar(Profesor profesor) {
		int index = coleccionProfesores.indexOf(profesor);
		if (index != -1) {
			return new Profesor(coleccionProfesores.get(index));
		} else {
			return null;
		}
	}

	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("No se puede borrar un profesor nulo.");
		} else {
			if (!coleccionProfesores.remove(profesor)) {
				throw new OperationNotSupportedException("El profesor a borrar no existe.");
			}
		}
	}

	public List<String> representar() {
		List<String> str = new ArrayList<>();
		for (Profesor profesor : coleccionProfesores) {
			str.add(profesor.toString());
		}
		return str;
	}
	
	public void leer() {
		File ficheroProfesores = new File(NOMBRE_FICHERO_PROFESORES);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroProfesores))) {
			Profesor profesor = null;
			do {
				profesor = (Profesor) entrada.readObject();
				insertar(profesor);
			} while (profesor != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fihero de profesores.");
		} catch (EOFException e) {
			System.out.println("Fichero profesores leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void escribir() {
		File ficheroProfesores = new File(NOMBRE_FICHERO_PROFESORES);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroProfesores))){
			for (Profesor profesor : coleccionProfesores)
				salida.writeObject(profesor);
			System.out.println("\nFichero profesores escrito satisfactoriamente.");
			System.out.println(ficheroProfesores.getAbsolutePath());
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de profesores");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}
}
