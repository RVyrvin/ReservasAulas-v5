package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

import java.io.Serializable;
import java.time.LocalDate;

public class PermanenciaPorTramo extends Permanencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PUNTOS = 10;
	private Tramo tramo;

	public PermanenciaPorTramo(LocalDate dia, Tramo tramo) {
		super(dia);
		setTramo(tramo);
	}

	public PermanenciaPorTramo(String dia, Tramo tramo) {
		super(dia);
		setTramo(tramo);
	}

	public PermanenciaPorTramo(PermanenciaPorTramo permanenciaPorTramo) {
		super(permanenciaPorTramo);
		setTramo(permanenciaPorTramo.getTramo());
	}

	public Tramo getTramo() {
		return tramo;
	}

	private void setTramo(Tramo tramo) {
		if (tramo == null)
			throw new NullPointerException("El tramo de una permanencia no puede ser nulo.");
		this.tramo = tramo;
	}

	@Override
	public int getPuntos() {
		return PUNTOS;
	}

	@Override
	public String toString() {
		return "[dia=" + dia.format(FORMATO_DIA) + ", tramo=" + tramo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tramo == null) ? 0 : tramo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof PermanenciaPorTramo))
			return false;
		PermanenciaPorTramo other = (PermanenciaPorTramo) obj;
		if (tramo != other.tramo)
			return false;
		return true;
	}
}
