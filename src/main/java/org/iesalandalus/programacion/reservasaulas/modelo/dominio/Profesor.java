package org.iesalandalus.programacion.reservasaulas.modelo.dominio;

import java.io.Serializable;

public class Profesor implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String ER_TELEFONO = "[69][0-9]{8}";
	private static final String ER_CORREO = "\\w[.\\w]*@[a-zA-Z]+\\.[a-zA-Z]{2,5}";

	private String nombre;
	private String correo;
	private String telefono;

	public Profesor(String nombre, String correo) {
		this.setNombre(nombre);
		this.setCorreo(correo);
		this.telefono = null;
	}
	

	public Profesor(String nombre, String correo, String telefono) {
		this.setNombre(nombre);
		this.setCorreo(correo);
		this.setTelefono(telefono);
	}
	

	public Profesor(Profesor profesor) {

		if (profesor == null)
			throw new IllegalArgumentException("No se puede copiar un profesor nulo.");
		else {
			this.setNombre(profesor.nombre);
			this.setCorreo(profesor.correo);
			this.setTelefono(profesor.telefono);
		}

	}	

	public String getNombre() {
		return nombre;
	}	

	private void setNombre(String nombre) {
		if (nombre == null)
			throw new IllegalArgumentException("El nombre del profesor no puede ser nulo.");
		else if (nombre.trim().equals(""))
			throw new IllegalArgumentException("El nombre del profesor no puede estar vacío.");
		else
			this.nombre = nombre;
	}	

	public String getCorreo() {
		return correo;
	}	

	public void setCorreo(String correo) {
		if (correo == null)
			throw new IllegalArgumentException("El correo del profesor no puede ser nulo.");
		else if (!correo.matches(ER_CORREO))
			throw new IllegalArgumentException("El correo del profesor no es válido.");
		else
			this.correo = correo;
	}	

	public String getTelefono() {
		return telefono;
	}	

	public void setTelefono(String telefono) {
		if (telefono == null)
			this.telefono = null;
		else {
			if (!telefono.matches(ER_TELEFONO))
				throw new IllegalArgumentException("El teléfono del profesor no es válido.");
			else
				this.telefono = telefono;
		}
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
		if (getClass() != obj.getClass())
			return false;
		Profesor other = (Profesor) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}	
	
	@Override
	public String toString() {
		if (telefono == null)
			return "[nombre=" + nombre + ", correo=" + correo + "]";
		else
			return "[nombre=" + nombre + ", correo=" + correo + ", telefono=" + telefono + "]";
	}
}

