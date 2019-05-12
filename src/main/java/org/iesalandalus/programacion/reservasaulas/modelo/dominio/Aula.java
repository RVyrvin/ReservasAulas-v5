package org.iesalandalus.programacion.reservasaulas.modelo.dominio;

import java.io.Serializable;

public class Aula implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final float PUNTOS_POR_PUESTO = 0.5f;
	private static final int MIN_PUESTOS = 10;
	private static final int MAX_PUESTOS = 100;
	private String nombre;
	private int puestos;

	public Aula(String nombre, int puestos) {
		this.setNombre(nombre);
		this.setPuestos(puestos);
	}

	public Aula(Aula aula) {
		if (aula == null)
			throw new IllegalArgumentException("No se puede copiar un aula nula.");
		else {
			this.setNombre(aula.nombre);
			this.setPuestos(aula.puestos);
		}
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(String nombre) {
		if (nombre == null)
			throw new IllegalArgumentException("El nombre del aula no puede ser nulo.");
		else if (nombre.trim().equals(""))
			throw new IllegalArgumentException("El nombre del aula no puede estar vacío.");
		else
			this.nombre = nombre;
	}

	public int getPuestos() {
		return puestos;
	}

	private void setPuestos(int puestos) {
		if (puestos < Aula.MIN_PUESTOS || puestos > Aula.MAX_PUESTOS)
			throw new IllegalArgumentException("El número de puestos no es correcto.");
		this.puestos = puestos;
	}

	public float getPuntos() {
		return this.puestos * Aula.PUNTOS_POR_PUESTO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Aula))
			return false;
		Aula other = (Aula) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}	

	@Override
	public String toString() {
		return "[nombre=" + nombre + ", puestos=" + puestos + "]";
	}
}
