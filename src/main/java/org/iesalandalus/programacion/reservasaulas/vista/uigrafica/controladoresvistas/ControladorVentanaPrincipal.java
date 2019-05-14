package org.iesalandalus.programacion.reservasaulas.vista.uigrafica.controladoresvistas;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dao.Reservas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.reservasaulas.vista.uigrafica.utilidades.Dialogos;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
	private Button btnCargar;

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
	private Button btnAnadir;

	@FXML
	private MenuItem miReservas;

	@FXML
	private RadioButton rbtnProfsores;

	@FXML
	private Button btnBuscar;

	@FXML
	private RadioButton rbtnAulas;

	private IControladorReservasAulas controladorMVC;
	private Stage addAula;

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

		ObservableList<Object> profesores = FXCollections.observableArrayList(new Profesor("Pepito", "correo@mail.com"),
				new Profesor("Bob", "Bob@mail.com", "633655699"), new Profesor("Juan", "Juan@mail.com", "633655699"),
				new Profesor("Perico", "Perico@mail.com", "633655699"),
				new Profesor("Juana", "Juana@mail.com", "633655699"));

		tvTabla.setItems(profesores);

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

		ObservableList<Object> reservas = FXCollections.observableArrayList(
				new Reserva(new Profesor("Roman", "roman@mail.com", "666999888"), new Aula("Sala", 20),
						new PermanenciaPorTramo("01/12/2028", Tramo.TARDE)),
				// new Profesor("Bob", "Bob@mail.com", "633655699"),
				// new Profesor("Juan", "Juan@mail.com", "633655699"),
				// new Profesor("Perico", "Perico@mail.com", "633655699"),
				new Reserva(new Profesor("Romaaan", "roman@mail.com", "666999888"), new Aula("Salaaa", 20),
						new PermanenciaPorTramo("01/12/2028", Tramo.MANANA)));

		tvTabla.setItems(reservas);

		System.out.println("Reservas");

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

	}

	private void addAula() throws Exception {

		// if (addAula == null) {

		addAula = new Stage();

		FXMLLoader cargadorAddAula = new FXMLLoader(getClass().getResource("../vistas/AddAula.fxml"));

		VBox raizAddAula = cargadorAddAula.load();
		ControladorAddAula cAddAula = cargadorAddAula.getController();
		cAddAula.setControladorMVC(controladorMVC);

		Scene escenaAddAula = new Scene(raizAddAula);
		addAula.setTitle("Añadir aula");
		addAula.initModality(Modality.APPLICATION_MODAL);
		addAula.setScene(escenaAddAula);
		addAula.showAndWait();
		System.out.println("!!!");
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
				}else {
					Dialogos.mostrarDialogoError("Error al borrar la aula", "Para borrar hace falta selecciónar una aula en la tabla");
				}
			} catch (OperationNotSupportedException e) {
				Dialogos.mostrarDialogoError("Error al borrar la aula", e.getMessage());
			}
		}

	}

	@FXML
	void onClickBtnReservas(ActionEvent event) {

		if (rbtnAulas.isSelected()) {
			try {
				mostrarReservasAula();
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Error al mostrar reservas de aula", e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private void mostrarReservasAula() throws Exception {
		addAula = new Stage();

		FXMLLoader cargador = new FXMLLoader(getClass().getResource("../vistas/ReservasAula.fxml"));

		VBox raiz = cargador.load();
		ControladorReservasAula contr = cargador.getController();
		contr.setControladorMVC(controladorMVC);
		contr.init();

		Scene escena = new Scene(raiz);
		addAula.setTitle("Añadir aula");
		addAula.initModality(Modality.APPLICATION_MODAL);
		addAula.setScene(escena);
		addAula.showAndWait();

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
}
