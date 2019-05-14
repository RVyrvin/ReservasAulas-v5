package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import java.util.List;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorAddAula {
	
    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfPuestos;

    @FXML
    private Button btnAnadir;

    @FXML
    void onClickBtnAnadir(ActionEvent event) {
    	try {
			Aula aula = new Aula (tfNombre.getText(), Integer.parseInt(tfPuestos.getText()));
			controladorMVC.insertarAula(aula);
			Stage propietario =((Stage) btnAnadir.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir Aula", "Aula se ha añadido satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir cliente", e.getMessage());
		}
    }
    
    private IControladorReservasAulas controladorMVC;	
    public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}    

}
