package org.iesalandalus.programacion.reservasaulas.modelo.mongodb.dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Reservas {

	private static float MAX_PUNTOS_PROFESORES_MES = 200f;
	
	public static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
	private static final String COLECCION = "reservas";
	private MongoCollection<Document> coleccionReservas;

	public Reservas() {
		coleccionReservas = MongoDB.getBD().getCollection(COLECCION);
	}

	public List<Reserva> getReservas() {
		List<Reserva> reservas = new ArrayList<>();
		for(Document documentoReserva: coleccionReservas.find()) {
			reservas.add(MongoDB.obtenerReservaDesdeDocumento(documentoReserva));
		}
		return reservas;
	}

	public int getNumReservas() {
		return (int)coleccionReservas.countDocuments();
	}

	public void insertar(Reserva reserva) throws OperationNotSupportedException {

		if (reserva == null)
			throw new IllegalArgumentException("No se puede realizar una reserva nula.");

		if (!esMesSiguienteOPosterior(reserva))
			throw new OperationNotSupportedException(
					"Sólo se pueden hacer reservas para el mes que viene o posteriores.");

		if (reserva.getPuntos() + getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESORES_MES)
			throw new OperationNotSupportedException(
					"Esta reserva excede los puntos máximos por mes para dicho profesor.");

		if (buscar(reserva) != null)
			throw new OperationNotSupportedException("La reserva ya existe.");
		else {

		Reserva res = getReservaDia(reserva.getPermanencia().getDia());

		if (res != null) {

			if (res.getPermanencia() instanceof PermanenciaPorTramo
					&& reserva.getPermanencia() instanceof PermanenciaPorHora)
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por tramo para este día y aula.");

			if (res.getPermanencia() instanceof PermanenciaPorHora
					&& reserva.getPermanencia() instanceof PermanenciaPorTramo)
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por hora para este día y aula.");

		}

		coleccionReservas.insertOne(MongoDB.obtenerDocumentoDesdeReserva(reserva));
		}
	}

	private boolean esMesSiguienteOPosterior(Reserva reserva) {
		
		boolean isSiguiente = true;
		int annoReserva = reserva.getPermanencia().getDia().getYear();
		int mesReserva = reserva.getPermanencia().getDia().getMonthValue();
		int annoCurrent = LocalDate.now().getYear();
		int mesCurrent = LocalDate.now().getMonthValue();
		
		//Las reservas no se pueden realizar para el mes en curso (sólo para el mes que viene o posteriores).
		if (annoReserva < annoCurrent) { 
			isSiguiente = false;
		} else if (annoReserva == annoCurrent) {
			if (mesReserva <= mesCurrent) {
				isSiguiente = false;
			}
		}
		
		return isSiguiente;
	}

	private float getPuntosGastadosReserva(Reserva reserva) {
		float puntos = 0f;
		for (Reserva res : getReservasProfesorMes(reserva.getProfesor(), reserva.getPermanencia().getDia())) {
			puntos += res.getPuntos();
		}
		return puntos;
	}

	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate dia) {
		List<Reserva> reservas = new ArrayList<>();
		for (Document documentoReserva: coleccionReservas.find()
				.filter(eq(MongoDB.PROFESOR_NOMBRE, profesor.getNombre()))
				.filter(eq(MongoDB.PERMANENCIA_DIA, dia.getMonthValue()))) {
			
			reservas.add(MongoDB.obtenerReservaDesdeDocumento(documentoReserva));
		}
		return reservas;
	}

	private Reserva getReservaDia(LocalDate dia) {			
		Reserva reserva = null;
		for (Reserva res : getReservas()) {
			if (res.getPermanencia().getDia().equals(dia))
				reserva = res;
		}
		return reserva;		
	}

	public Reserva buscar(Reserva reserva) {
		
		Document documentoReserva = null;
		String dia = reserva.getPermanencia().getDia().format(FORMATO_DIA);
		
		if (reserva.getPermanencia() instanceof PermanenciaPorHora) {
			String hora = ((PermanenciaPorHora)reserva.getPermanencia()).getHora().format(FORMATO_HORA);
			documentoReserva = coleccionReservas
					.find()
					.filter(and (
							eq(MongoDB.AULA + "." + MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_DIA, dia),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_POR_HORA, hora)))
					.first();
			
		} else if (reserva.getPermanencia() instanceof PermanenciaPorTramo) {
			String tramo = ((PermanenciaPorTramo)reserva.getPermanencia()).getTramo().toString();
			documentoReserva = coleccionReservas
					.find()
					.filter(and (
							eq(MongoDB.AULA + "." + MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_DIA, dia),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_POR_TRAMO, tramo)))
					.first();
		} else {
			throw new IllegalArgumentException("Error en la persictencia de la permanencia");
		}
		
		
		return MongoDB.obtenerReservaDesdeDocumento(documentoReserva);
	}

	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("No se puede anular una reserva nula.");
		} else {
			if (buscar(reserva) != null) {
				String dia = reserva.getPermanencia().getDia().format(FORMATO_DIA);
				if (reserva.getPermanencia() instanceof PermanenciaPorHora) {
					String hora = ((PermanenciaPorHora)reserva.getPermanencia()).getHora().format(FORMATO_HORA);
					coleccionReservas.deleteOne(and(
							eq(MongoDB.AULA + "." + MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_DIA, dia),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_POR_HORA, hora)));
					
				} else if (reserva.getPermanencia() instanceof PermanenciaPorTramo) {
					String tramo = ((PermanenciaPorTramo)reserva.getPermanencia()).getTramo().toString();
					coleccionReservas.deleteOne(and(
							eq(MongoDB.AULA + "." + MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_DIA, dia),
							eq(MongoDB.PERMANENCIA + "." + MongoDB.PERMANENCIA_POR_TRAMO, tramo)));
				} else {
					throw new IllegalArgumentException("Error en la persictencia de la permanencia");
				}
				
			} else {
				throw new OperationNotSupportedException("La reserva a anular no existe.");
			}
		}
	}

	public List<String> representar() {
		List<String> representacion = new ArrayList<>();
		for (Reserva reserva : getReservas()) {
			representacion.add(reserva.toString());
		}
		return representacion;
	}

	public List<Reserva> getReservasProfesor(Profesor profesor) {
		List<Reserva> reservaProfesor = new ArrayList<>();
		for (Reserva reserva : getReservas()) {
			if (reserva.getProfesor().equals(profesor)) {
				reservaProfesor.add(new Reserva(reserva));
			}
		}
		return reservaProfesor;
	}

	public List<Reserva> getReservasAula(Aula aula) {
		List<Reserva> reservaAula = new ArrayList<>();
		for (Reserva reserva : getReservas()) {
			if (reserva.getAula().equals(aula)) {
				reservaAula.add(new Reserva(reserva));
			}
		}
		return reservaAula;
	}

	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {
		List<Reserva> reservaPermanencia = new ArrayList<>();
		for (Reserva reserva : getReservas()) {
			if (reserva.getPermanencia().equals(permanencia)) {
				reservaPermanencia.add(new Reserva(reserva));
			}
		}
		return reservaPermanencia;
	}

	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) {
		boolean isDisponible = true;

		if (aula == null)
			throw new IllegalArgumentException("No se puede consultar la disponibilidad de un aula nula.");
		if (permanencia == null)
			throw new IllegalArgumentException("No se puede consultar la disponibilidad de una permanencia nula.");

		for (Reserva reserva : getReservas()) {
			if (reserva.getAula().equals(aula) && reserva.getPermanencia().equals(permanencia))
				isDisponible = false;
		}
		return isDisponible;
	}
}
