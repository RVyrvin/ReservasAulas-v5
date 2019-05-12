package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PermanenciaPorHora extends Permanencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PUNTOS = 3;
	private static final int HORA_INICIO = 8;
	private static final int HORA_FIN = 22;
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
	private LocalTime hora;

	public PermanenciaPorHora(LocalDate dia, LocalTime hora) {
		super(dia);
		setHora(hora);
	}

	public PermanenciaPorHora(String dia, LocalTime hora) {
		super(dia);
		setHora(hora);
	}

	public PermanenciaPorHora(LocalDate dia, String hora) {
		super(dia);
		setHora(hora);
	}

	public PermanenciaPorHora(String dia, String hora) {
		super(dia);
		setHora(hora);
	}

	public PermanenciaPorHora(PermanenciaPorHora permanenciaPorHora) {
		super(permanenciaPorHora);
		setHora(permanenciaPorHora.getHora());
	}

	public LocalTime getHora() {
		return hora;
	}

	private void setHora(LocalTime hora) {

		if (hora == null)
			throw new NullPointerException("La hora de una permanencia no puede ser nula.");

		if (hora.getHour() < HORA_INICIO || hora.getHour() > HORA_FIN)
			throw new IllegalArgumentException(
					"La hora de una permanencia debe estar comprendida entre las 8 y las 22.");

		if (hora.getMinute() != 0)
			throw new IllegalArgumentException("La hora de una permanencia debe ser una hora en punto.");

		this.hora = hora;
	}

	private void setHora(String hora) {

		LocalTime parseHora = null;

		if (hora == null)
			throw new NullPointerException("La hora de una permanencia no puede ser nula.");

		try {
			parseHora = LocalTime.parse(hora, FORMATO_HORA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("El formato de la hora de la permanencia no es correcto.");
		}

		if (parseHora.getHour() < HORA_INICIO || parseHora.getHour() > HORA_FIN)
			throw new IllegalArgumentException(
					"La hora de una permanencia debe estar comprendida entre las 8 y las 22.");

		if (parseHora.getMinute() != 0)
			throw new IllegalArgumentException("La hora de una permanencia debe ser una hora en punto.");
		this.hora = parseHora;
	}

	@Override
	public int getPuntos() {
		return PUNTOS;
	}

	@Override
	public String toString() {
		return "[dia=" + dia.format(FORMATO_DIA) + ", hora=" + hora.format(FORMATO_HORA) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof PermanenciaPorHora))
			return false;
		PermanenciaPorHora other = (PermanenciaPorHora) obj;
		if (hora == null) {
			if (other.hora != null)
				return false;
		} else if (!hora.equals(other.hora))
			return false;
		return true;
	}
}
