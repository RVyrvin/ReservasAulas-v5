package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import java.time.LocalDate;
import java.util.List;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class ControladorAddReserva {

	private List<Profesor> profesores;
	private List<Aula> aulas;

	@FXML
	private ComboBox<Aula> cbAulas;

	@FXML
	private DatePicker dpDate;

	@FXML
	private Button btnAnadir;

	@FXML
	private ComboBox<Profesor> cbProfesores;

	@FXML
	private ComboBox<String> cbValores;

	@FXML
	private ComboBox<String> cbTipo;

	private IControladorReservasAulas controladorMVC;

	public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	@FXML
	void onClickBtnAnadir(ActionEvent event) {
		try {
			Profesor profesor = cbProfesores.getValue();
			Aula aula = cbAulas.getValue();
			Permanencia permanencia;
			LocalDate dia = dpDate.getValue();

			if (cbTipo.getSelectionModel().getSelectedItem().equals("Hora"))
				permanencia = new PermanenciaPorHora(dia, cbValores.getSelectionModel().getSelectedItem());
			else {
				if (cbValores.getSelectionModel().getSelectedItem().equals("Tarde"))
					permanencia = new PermanenciaPorTramo(dia, Tramo.TARDE);
				else
					permanencia = new PermanenciaPorTramo(dia, Tramo.MANANA);
			}
			
			controladorMVC.realizarReserva(new Reserva(profesor, aula, permanencia));
			
			Stage propietario = ((Stage) btnAnadir.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir reserva", "Reserva se ha añadido satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir reserva", e.getMessage());
		}

	}

	@FXML
	void cbTipoSelected(ActionEvent event) {

		if (cbTipo.getSelectionModel().getSelectedItem().equals("Hora")) {
			cbValores.setItems(FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
					"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
					"17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"));
		}
		if (cbTipo.getSelectionModel().getSelectedItem().equals("Tramo")) {
			System.out.println("Tramo");
			cbValores.setItems(FXCollections.observableArrayList("Mañana", "Tarde"));
		}

	}

	public void init() {

		profesores = controladorMVC.getProfesores();

		ObservableList<Profesor> olProfesores = FXCollections.observableArrayList();

		for (Profesor p : profesores) {
			olProfesores.add(p);
		}

		cbProfesores.setItems(olProfesores);

		aulas = controladorMVC.getAulas();

		ObservableList<Aula> olAulas = FXCollections.observableArrayList();

		for (Aula a : aulas) {
			olAulas.add(a);
		}

		cbAulas.setItems(olAulas);

		cbTipo.setItems(FXCollections.observableArrayList("Hora", "Tramo"));

	}

}
