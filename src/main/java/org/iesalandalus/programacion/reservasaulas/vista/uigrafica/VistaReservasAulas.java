package org.iesalandalus.programacion.reservasaulas.vista.uigrafica;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.vista.IVistaReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas.ControladorVentanaPrincipal;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VistaReservasAulas extends Application implements IVistaReservasAulas {

	private static final String ERROR = "ERROR: ";
	// private static final String NOMBRE_VALIDO = "\\w+";
	private static final String CORREO_VALIDO = "good@mail.com";

	// private IControladorReservasAulas controlador;

	private IControladorReservasAulas controladorMVC = null;
	private static VistaReservasAulas instancia = null;

	
	/**
	 * no entiendo
	 */
	public VistaReservasAulas() {
		if (instancia != null) {
			controladorMVC = instancia.controladorMVC;
		} else {
			instancia = this;
		}
	}

	@Override
	public void setControlador(IControladorReservasAulas controlador) {
		this.controladorMVC = controlador;
	}

	@Override
	public void comenzar() {
		launch(this.getClass());
	}

	@Override
	public void start(Stage escenarioPrincipal) throws Exception {

		try {
			FXMLLoader cargadorVentanaPrincipal = new FXMLLoader(getClass().getResource("vistas/VentanaPrincipal.fxml"));
			VBox raiz = cargadorVentanaPrincipal.load();
			ControladorVentanaPrincipal cVentanaPrincipal = cargadorVentanaPrincipal.getController();
			cVentanaPrincipal.setControlador(controladorMVC);
			cVentanaPrincipal.init();
			Scene escena = new Scene(raiz);
			escenarioPrincipal.setTitle("Gestión de Aulas");
			escenarioPrincipal.setScene(escena);
			escenarioPrincipal.setResizable(false);
			escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
			escenarioPrincipal.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void confirmarSalida(Stage escenarioPrincipal, WindowEvent e) {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", escenarioPrincipal)) {
			controladorMVC.salir();
			escenarioPrincipal.close();
		}
		else
			e.consume();	
	}
}
