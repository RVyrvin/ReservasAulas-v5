package org.iesalandalus.programacion.reservasaulas.modelo.dao;

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

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;

public class Aulas {

	private static final String NOMBRE_FICHERO_AULAS = "res/ficheros/aulas.dat";
	private List<Aula> coleccionAulas;

	public Aulas() {
		coleccionAulas = new ArrayList<>();
	}

	public Aulas(Aulas aulas) {
		setAulas(aulas);
	}

	private void setAulas(Aulas aulas) {
		if (aulas == null)
			throw new IllegalArgumentException("No se pueden copiar aulas nulas.");
		else {
			this.coleccionAulas = copiaProfundaAulas(aulas.coleccionAulas);
		}
	}

	private List<Aula> copiaProfundaAulas(List<Aula> aulas) {
		List<Aula> cpyAlua = new ArrayList<>();
		for (Aula aula : aulas) {
			cpyAlua.add(new Aula(aula));
		}
		return cpyAlua;
	}

	public List<Aula> getAulas() {
		return copiaProfundaAulas(coleccionAulas);
	}

	public int getNumAulas() {
		return this.coleccionAulas.size();
	}

	public void insertar(Aula aula) throws OperationNotSupportedException, IllegalArgumentException {
		if (aula == null)
			throw new IllegalArgumentException("No se puede insertar un aula nula.");
		else {
			if (coleccionAulas.contains(aula)) {
				throw new OperationNotSupportedException("El aula ya existe.");
			} else {
				coleccionAulas.add(new Aula(aula));
			}
		}

	}

	public Aula buscar(Aula aula) {
		int index = coleccionAulas.indexOf(aula);
		if (index != -1)
			return new Aula(this.coleccionAulas.get(index));
		else
			return null;
	}

	public void borrar(Aula aula) throws OperationNotSupportedException, IllegalArgumentException {
		if (aula == null) {
			throw new IllegalArgumentException("No se puede borrar un aula nula.");
		} else {
			if (!coleccionAulas.remove(aula)) {
				throw new OperationNotSupportedException("El aula a borrar no existe.");
			}
		}
	}

	public List<String> representar() {
		List<String> str = new ArrayList<>();
		for (Aula aula : coleccionAulas) {
			str.add(aula.toString());
		}
		return str;
	}

	public void leer() {
		
		File ficheroAulas = new File(NOMBRE_FICHERO_AULAS);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroAulas))) {
			Aula aula = null;
			do {
				aula = (Aula) entrada.readObject();
				insertar(aula);
			} while (aula != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fihero de aulas.");
		} catch (EOFException e) {
			System.out.println("Fichero aulas leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}

	public void escribir() {
		
		File ficheroAulas = new File(NOMBRE_FICHERO_AULAS);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroAulas))) {
			for (Aula aula : coleccionAulas)
				salida.writeObject(aula);
			System.out.println("\nFichero clientes escrito satisfactoriamente.");
			System.out.println(ficheroAulas.getAbsolutePath());
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de clientes");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}
}
