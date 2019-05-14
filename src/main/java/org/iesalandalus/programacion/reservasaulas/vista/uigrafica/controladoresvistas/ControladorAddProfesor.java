package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorAddProfesor {

	@FXML
	private TextField tfCorreo;

	@FXML
	private TextField tfNombre;

	@FXML
	private TextField tfTelefono;

	@FXML
	private Button btnAnadir;

	private IControladorReservasAulas controladorMVC;

	public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	@FXML
	void onClickBtnAnadir(ActionEvent event) {

		try {
			
			Profesor profesor;
			
			if (tfTelefono.getText().trim().equals("")) {
				profesor = new Profesor(tfNombre.getText(), tfCorreo.getText(), null);
			} else {
				profesor = new Profesor(tfNombre.getText(), tfCorreo.getText(), tfTelefono.getText());
			}
			
			controladorMVC.insertarProfesor(profesor);
			
			Stage propietario = ((Stage) btnAnadir.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir Profesor", "Profesor se ha añadido satisfactoriamente",
					propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir Profesor", e.getMessage());
		}

	}

}
