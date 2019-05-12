package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public abstract class Permanencia implements Serializable {

	private static final long serialVersionUID = 1L;
	protected static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	protected LocalDate dia;

	protected Permanencia(Permanencia permanencia) {
		if (permanencia == null)
			throw new NullPointerException("No se puede copiar una permanencia nula.");
		setDia(permanencia.getDia());

	}

	protected Permanencia(LocalDate dia) {
		setDia(dia);
	}

	protected Permanencia(String dia) {
		setDia(dia);
	}

	public LocalDate getDia() {
		return dia;
	}

	public void setDia(LocalDate dia) {
		if (dia == null)
			throw new NullPointerException("El día de una permanencia no puede ser nulo.");
		this.dia = dia;
	}

	public void setDia(String dia) {
		if (dia == null)
			throw new NullPointerException("El día de una permanencia no puede ser nulo.");

		try {
			this.dia = LocalDate.parse(dia, FORMATO_DIA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("El formato del día de la permanencia no es correcto.");
		}
	}

	public abstract int getPuntos();

	public abstract String toString();

	@Override
	public int hashCode() {
		return Objects.hash(dia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permanencia other = (Permanencia) obj;
		return Objects.equals(dia, other.dia);
	}
}
