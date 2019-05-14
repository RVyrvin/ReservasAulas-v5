package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import java.util.List;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dao.Reservas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ControladorReservasAula {
	
	List<Aula> aulas;
	
    @FXML
    private Button btnMostrarReservas;

    @FXML
    private ComboBox<Aula> cbAulas;
    
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
    	
    	Aula aula = cbAulas.getValue();    	
    	
    	if (aula != null) {
    		List<Reserva> reservas = controladorMVC.getReservasAula(aula);
    		for (Reserva res: reservas){
    			if (res.getAula().getNombre().equals(aula.getNombre()))
    				taReservas.appendText(res.getProfesor().getNombre() + " ha reservado para " + res.getPermanencia() + "\n");
    		}
    	} else {
    		Dialogos.mostrarDialogoInformacion("Reservas Aula", "Hay que selecciónar aula");
    	}

    }
    
    public void init() {
    	aulas = controladorMVC.getAulas();

		ObservableList<Aula> olAulas = FXCollections.observableArrayList();

		for (Aula a : aulas) {
			olAulas.add(a);
		}
		
		cbAulas.setItems(olAulas);
		
    }

}
