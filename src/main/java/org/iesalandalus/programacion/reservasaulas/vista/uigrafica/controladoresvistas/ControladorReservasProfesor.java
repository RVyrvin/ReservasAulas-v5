package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import java.util.List;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ControladorReservasProfesor {

	private List<Profesor> profesores;

	@FXML
	private Button btnMostrarReservas;

	@FXML
	private ComboBox<Profesor> cbProfesores;

	@FXML
	private TextArea taReservas;

	private IControladorReservasAulas controladorMVC;

	public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	@FXML
	void onCBSelect(ActionEvent event) {
		taReservas.setText("");
	}

	@FXML
	void onClickBtnMostrarReservas(ActionEvent event) {

		Profesor profesor = cbProfesores.getValue();

		if (profesor != null) {
			List<Reserva> reservas = controladorMVC.getReservasProfesor(profesor);
			for (Reserva res : reservas) {
				if (res.getProfesor().getNombre().equals(profesor.getNombre()))
					taReservas.appendText(
							res.getAula().getNombre() + " esta reservada para " + res.getPermanencia() + "\n");
			}
		} else {
			Dialogos.mostrarDialogoInformacion("Reservas Profesor", "Hay que selecciónar profesor");
		}

	}

	public void init() {

		profesores = controladorMVC.getProfesores();

		ObservableList<Profesor> olProfesores = FXCollections.observableArrayList();

		for (Profesor p : profesores) {
			olProfesores.add(p);
		}

		cbProfesores.setItems(olProfesores);

	}

}
