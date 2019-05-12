package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

import java.io.Serializable;

public enum Tramo implements Serializable{

	MANANA("Mañana"), TARDE("Tarde");

	private String cadenaAMostrar;

	private Tramo(String cadenaAMostrar) {
		this.cadenaAMostrar = cadenaAMostrar;
	}

	@Override
	public String toString() {
		return this.cadenaAMostrar;
	}
}
