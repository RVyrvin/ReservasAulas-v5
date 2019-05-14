package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import java.time.LocalDate;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaPrincipal {

	@FXML
	private DatePicker dpData;

	@FXML
	private ComboBox<String> cbTipo;

	@FXML
	private ComboBox<String> cbValores;

	@FXML
	private MenuItem miClientes;

	@FXML
	private RadioButton rbtnReservas;

	@FXML
	private Button btnAnadir;

	@FXML
	private Button btnCargar;

	@FXML
	private Button btnReservas;

	@FXML
	private Button btnDisponibilidad;

	@FXML
	private MenuItem miAulas;

	@FXML
	private MenuItem miAcercaDe;

	@FXML
	private MenuItem miContextTabla;

	@FXML
	private TableView<Object> tvTabla;

	@FXML
	private MenuItem miSalir;

	@FXML
	private MenuItem miReservas;

	@FXML
	private RadioButton rbtnProfsores;

	@FXML
	private RadioButton rbtnAulas;

	private IControladorReservasAulas controladorMVC;
	private Stage stage;

	public ControladorVentanaPrincipal() {
		// init();
	}

	public void init() {

		ToggleGroup tvSelector = new ToggleGroup();

		rbtnAulas.setToggleGroup(tvSelector);
		rbtnProfsores.setToggleGroup(tvSelector);
		rbtnReservas.setToggleGroup(tvSelector);
		rbtnAulas.setSelected(true);

		cbTipo.setItems(FXCollections.observableArrayList("Hora", "Tramo"));

		// btnCargar.setOnAction(e -> onClickBtnCargar(e));

	}

	public void setControlador(IControladorReservasAulas controlador) {
		this.controladorMVC = controlador;
	}

	// events

	@FXML
	void miAcercaDe(ActionEvent event) {

		Alert dialogo = new Alert(AlertType.INFORMATION);
		dialogo.setTitle("Acerca de ...");
		DialogPane panelDialogo = dialogo.getDialogPane();
		// panelDialogo.getStylesheets().add(getClass().getResource("../iugventanas.css").toExternalForm());
		panelDialogo.lookupButton(ButtonType.OK).setId("btAceptar");
		VBox contenido = new VBox();
		contenido.setAlignment(Pos.CENTER);
		contenido.setPadding(new Insets(20, 20, 0, 20));
		contenido.setSpacing(20);
		// Image logo = new
		// Image(getClass().getResourceAsStream("res/img/logo-ies.png"), 200, 200, true,
		// true);
		Label texto = new Label("Módulo de Programación - JavaFX");
		texto.setStyle("-fx-font: 20 Arial");
		// contenido.getChildren().addAll(new ImageView(logo), texto);
		contenido.getChildren().add(texto);
		panelDialogo.setHeader(contenido);
		dialogo.showAndWait();

	}

	@FXML
	void miSalir(ActionEvent event) {
		System.out.println("qwe");
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?",
				null)) {
			controladorMVC.salir();
			System.exit(0);
		}
	}

	@FXML
	void onClickBtnCargar(ActionEvent event) {

		if (rbtnAulas.isSelected()) {
			cargarAulas();
		}
		if (rbtnProfsores.isSelected()) {
			cargarProfesores();
		}
		if (rbtnReservas.isSelected()) {
			cargarReservas();
		}

	}

	private void cargarAulas() {

		TableColumn<Object, String> columnaNombre = new TableColumn<Object, String>("Nombre");
		TableColumn<Object, Integer> columnaPuestos = new TableColumn<Object, Integer>("Puestos");

		tvTabla.getColumns().clear();

		tvTabla.getColumns().add(columnaNombre);
		tvTabla.getColumns().add(columnaPuestos);

		tvTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// columnaNombre.setMinWidth(100);
		columnaNombre.setCellValueFactory(new PropertyValueFactory<Object, String>("Nombre"));
		// columnaPuestos.setMinWidth(100);
		columnaPuestos.setCellValueFactory(new PropertyValueFactory<Object, Integer>("Puestos"));

		List<Aula> aulas = controladorMVC.getAulas();

		ObservableList<Object> olAulas = FXCollections.observableArrayList();

		for (Aula a : aulas) {
			olAulas.add(a);
		}

		tvTabla.setItems(olAulas);

	}

	private void cargarProfesores() {

		TableColumn<Object, String> columnaNombre = new TableColumn<Object, String>("Nombre");
		TableColumn<Object, String> columnaCorreo = new TableColumn<Object, String>("Correo");
		TableColumn<Object, String> columnaTelefono = new TableColumn<Object, String>("Telefono");

		tvTabla.getColumns().clear();

		tvTabla.getColumns().add(columnaNombre);
		tvTabla.getColumns().add(columnaCorreo);
		tvTabla.getColumns().add(columnaTelefono);

		tvTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// columnaNombre.setMinWidth(100);
		columnaNombre.setCellValueFactory(new PropertyValueFactory<Object, String>("Nombre"));
		// columnaCorreo.setMinWidth(100);
		columnaCorreo.setCellValueFactory(new PropertyValueFactory<Object, String>("Correo"));
		// columnaTelefono.setMinWidth(100);
		columnaTelefono.setCellValueFactory(new PropertyValueFactory<Object, String>("Telefono"));

		List<Profesor> profesores = controladorMVC.getProfesores();

		ObservableList<Object> olProfesores = FXCollections.observableArrayList();

		for (Profesor p : profesores) {
			olProfesores.add(p);
		}

		tvTabla.setItems(olProfesores);

	}

	private void cargarReservas() {

		TableColumn<Object, String> columnaProfesor = new TableColumn<Object, String>("Profesor");
		TableColumn<Object, String> columnaAula = new TableColumn<Object, String>("Aula");
		TableColumn<Object, String> columnaDia = new TableColumn<Object, String>("Fecha");
		// TableColumn<Object, String> columnaPermanenciaTipo = new TableColumn<Object,
		// String>("Hora/Tramo");

		tvTabla.getColumns().clear();

		tvTabla.getColumns().add(columnaProfesor);
		tvTabla.getColumns().add(columnaAula);
		tvTabla.getColumns().add(columnaDia);
		// tvTabla.getColumns().add(columnaPermanenciaTipo);

		tvTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// columnaNombreProfesor.setMinWidth(100);
		columnaProfesor.setCellValueFactory(new PropertyValueFactory<Object, String>("profesor"));
		// columnaNombreAula.setMinWidth(100);
		columnaAula.setCellValueFactory(new PropertyValueFactory<Object, String>("Aula"));
		// columnaPermanenciaDia.setMinWidth(100);
		columnaDia.setCellValueFactory(new PropertyValueFactory<Object, String>("Permanencia"));
		// columnaPermanenciaTipo.setMinWidth(100);
		// columnaPermanenciaTipo.setCellValueFactory(new PropertyValueFactory<Object,
		// String>("Tipo"));

		List<Reserva> reservas = controladorMVC.getReservas();

		ObservableList<Object> olReservas = FXCollections.observableArrayList();

		for (Reserva r : reservas) {
			olReservas.add(r);
		}

		tvTabla.setItems(olReservas);

	}

	@FXML
	void onClickBtnAnadir(ActionEvent event) {

		if (rbtnAulas.isSelected()) {
			try {
				addAula();
				cargarAulas();
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Ventana principal", e.getMessage());
			}
		}

		if (rbtnProfsores.isSelected()) {
			try {
				addProfesor();
				cargarProfesores();
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Ventana principal", e.getMessage());
			}
		}

		if (rbtnReservas.isSelected()) {
			try {
				addReserva();
				cargarReservas();
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Ventana principal", e.getMessage());
			}
		}

	}

	private void addReserva() throws Exception {
		// if (addAula == null) {

		stage = new Stage();

		FXMLLoader cargador = new FXMLLoader(getClass().getResource("../vistas/AddReserva.fxml"));

		VBox raiz = cargador.load();
		ControladorAddReserva contr = cargador.getController();
		contr.setControladorMVC(controladorMVC);
		contr.init();

		Scene escena = new Scene(raiz);
		stage.setTitle("Añadir reserva");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(escena);
		stage.showAndWait();
		// }

	}

	private void addProfesor() throws Exception {
		// if (addAula == null) {

		stage = new Stage();

		FXMLLoader cargador = new FXMLLoader(getClass().getResource("../vistas/AddProfesor.fxml"));

		VBox raiz = cargador.load();
		ControladorAddProfesor contr = cargador.getController();
		contr.setControladorMVC(controladorMVC);

		Scene escena = new Scene(raiz);
		stage.setTitle("Añadir profesor");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(escena);
		stage.showAndWait();
		// }

	}

	private void addAula() throws Exception {

		// if (addAula == null) {

		stage = new Stage();

		FXMLLoader cargador = new FXMLLoader(getClass().getResource("../vistas/AddAula.fxml"));

		VBox raiz = cargador.load();
		ControladorAddAula cont = cargador.getController();
		cont.setControladorMVC(controladorMVC);

		Scene escenaAddAula = new Scene(raiz);
		stage.setTitle("Añadir aula");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(escenaAddAula);
		stage.showAndWait();
		// }
	}

	@FXML
	void onClickBtnBorrar(ActionEvent event) {

		if (rbtnAulas.isSelected()) {
			try {
				Aula aula = (Aula) tvTabla.getSelectionModel().getSelectedItem();
				if (aula != null) {
					controladorMVC.borrarAula(aula);
					Dialogos.mostrarDialogoInformacion("Borrar Aula", "La aula se ha borrado satisfactoriamente");
					cargarAulas();
				} else {
					Dialogos.mostrarDialogoError("Error al borrar la aula",
							"Para borrar hace falta selecciónar una aula en la tabla");
				}
			} catch (OperationNotSupportedException e) {
				Dialogos.mostrarDialogoError("Error al borrar la aula", e.getMessage());
			}
		}

		if (rbtnProfsores.isSelected()) {
			try {
				Profesor profesor = (Profesor) tvTabla.getSelectionModel().getSelectedItem();
				if (profesor != null) {
					controladorMVC.borrarProfesor(profesor);
					Dialogos.mostrarDialogoInformacion("Borrar Profesor",
							"El profesor se ha borrado satisfactoriamente");
					cargarProfesores();
				} else {
					Dialogos.mostrarDialogoError("Error al borrar el profesor",
							"Para borrar hace falta selecciónar un profesor en la tabla");
				}
			} catch (OperationNotSupportedException e) {
				Dialogos.mostrarDialogoError("Error al borrar el profesor", e.getMessage());
			}
		}

		if (rbtnReservas.isSelected()) {
			try {
				Reserva reserva = (Reserva) tvTabla.getSelectionModel().getSelectedItem();
				if (reserva != null) {
					controladorMVC.anularReserva(reserva);
					Dialogos.mostrarDialogoInformacion("Anular Reserva", "La reserva fue anulada satisfactoriamente");
					cargarReservas();
				} else {
					Dialogos.mostrarDialogoError("Error al anular la reserva",
							"Para anular la reserva hace falta seleccionar una en la tabla");
				}
			} catch (OperationNotSupportedException e) {
				Dialogos.mostrarDialogoError("Error al borrar el profesor", e.getMessage());
			}
		}

	}

	@FXML
	void onClickBtnReservas(ActionEvent event) {

		if (rbtnAulas.isSelected()) {
			try {
				mostrarReservasAula();
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Error al mostrar reservas de la aula", e.getMessage());
				e.printStackTrace();
			}
		}

		if (rbtnProfsores.isSelected()) {
			try {
				mostrarReservasProfesor();
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Error al mostrar reservas del profesor", e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private void mostrarReservasProfesor() throws Exception {

		stage = new Stage();

		FXMLLoader cargador = new FXMLLoader(getClass().getResource("../vistas/ReservasProfesor.fxml"));

		VBox raiz = cargador.load();
		ControladorReservasProfesor contr = cargador.getController();
		contr.setControladorMVC(controladorMVC);
		contr.init();

		Scene escena = new Scene(raiz);
		stage.setTitle("Añadir aula");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(escena);
		stage.showAndWait();

	}

	private void mostrarReservasAula() throws Exception {

		stage = new Stage();

		FXMLLoader cargador = new FXMLLoader(getClass().getResource("../vistas/ReservasAula.fxml"));

		VBox raiz = cargador.load();
		ControladorReservasAula contr = cargador.getController();
		contr.setControladorMVC(controladorMVC);
		contr.init();

		Scene escena = new Scene(raiz);
		stage.setTitle("Añadir aula");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(escena);
		stage.showAndWait();

	}

	@FXML
	void onClickBtnDisp(ActionEvent event) {

		if (rbtnAulas.isSelected()) {
			Aula aula = (Aula) tvTabla.getSelectionModel().getSelectedItem();
			if (aula != null) {
				boolean isDisponible = false;
				LocalDate dia = dpData.getValue();

				if (cbTipo.getSelectionModel().getSelectedItem().equals("Hora"))
					isDisponible = controladorMVC.consultarDisponibilidad(aula,
							new PermanenciaPorHora(dia, cbValores.getSelectionModel().getSelectedItem()));
				else {
					if (cbValores.getSelectionModel().getSelectedItem().equals("Tarde"))
						isDisponible = controladorMVC.consultarDisponibilidad(aula,
								new PermanenciaPorTramo(dia, Tramo.TARDE));
					else
						isDisponible = controladorMVC.consultarDisponibilidad(aula,
								new PermanenciaPorTramo(dia, Tramo.MANANA));
				}

				if (isDisponible) {
					Dialogos.mostrarDialogoInformacion("Disponibilidad", "Aula esta disponible");
				} else {
					Dialogos.mostrarDialogoInformacion("Disponibilidad", "Aula no esta disponible");
				}

				System.out.println(aula.getNombre());
			} else {
				Dialogos.mostrarDialogoInformacion("Consultar disponibilidad",
						"Ne esta seleccionada ninguna aula en la tabla");
			}
		}

	}

	@FXML
	void cbTipoSelected(ActionEvent event) {
		if (cbTipo.getSelectionModel().getSelectedItem().equals("Hora")) {
			System.out.println("Hora");
			cbValores.setItems(FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
					"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
					"17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"));
		}
		if (cbTipo.getSelectionModel().getSelectedItem().equals("Tramo")) {
			System.out.println("Tramo");
			cbValores.setItems(FXCollections.observableArrayList("Mañana", "Tarde"));
		}
	}

	@FXML
	void rbtnAulasAction(ActionEvent event) {
		aulaDis();
	}

	@FXML
	void rbtnProfesoresAction(ActionEvent event) {
		profesorDis();
	}

	@FXML
	void rbtnReservasAction(ActionEvent event) {
		reservasDis();
	}

	@FXML
	void miAulasAction(ActionEvent event) {
		rbtnAulas.setSelected(true);
		aulaDis();
		cargarAulas();
	}

	@FXML
	void miProfesoresAction(ActionEvent event) {
		rbtnProfsores.setSelected(true);
		profesorDis();
		cargarProfesores();
	}

	@FXML
	void miReservasAction(ActionEvent event) {
		rbtnReservas.setSelected(true);
		reservasDis();
		cargarReservas();
	}

	private void aulaDis() {
		btnReservas.setDisable(false);
		btnDisponibilidad.setDisable(false);
		dpData.setDisable(false);
		cbTipo.setDisable(false);
		cbValores.setDisable(false);
	}

	private void profesorDis() {
		btnReservas.setDisable(false);
		btnDisponibilidad.setDisable(true);
		dpData.setDisable(true);
		cbTipo.setDisable(true);
		cbValores.setDisable(true);
	}
	
	private void reservasDis() {
		btnReservas.setDisable(true);
		btnDisponibilidad.setDisable(true);
		dpData.setDisable(true);
		cbTipo.setDisable(true);
		cbValores.setDisable(true);
	}
}
