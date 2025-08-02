package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AsistenciaDAO;
import com.example.sistemanomina.model.Asistencia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AsistenciaController {

    @FXML
    private TableView<Asistencia> tblasistencia;
    @FXML
    private TableColumn<Asistencia, Integer> colid;
    @FXML
    private TableColumn<Asistencia, Integer> colcodemp;
    @FXML
    private TableColumn<Asistencia, String> colnomemp; // Puedes implementar nombre en otra tabla relacionada si deseas
    @FXML
    private TableColumn<Asistencia, String> colfecha;
    @FXML
    private TableColumn<Asistencia, String> colhoraentrada;
    @FXML
    private TableColumn<Asistencia, String> colhorasalida;

    @FXML
    private Button BTCREAR;

    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private final ObservableList<Asistencia> listaAsistencias = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Enlazar columnas
        colid.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colcodemp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEmpleadoId()).asObject());
        colfecha.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        colhoraentrada.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoraEntrada().toString()));
        colhorasalida.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoraSalida().toString()));

        // Por ahora muestra solo el ID en la columna de nombre
        colnomemp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("Empleado #" + cellData.getValue().getEmpleadoId()));

        cargarDatosTabla();

        // Acción para el botón CREAR
        BTCREAR.setOnAction(e -> abrirVentanaCrearEditar(null));
    }

    private void cargarDatosTabla() {
        listaAsistencias.clear();
        List<Asistencia> asistencias = asistenciaDAO.obtenerTodasAsistencias();
        listaAsistencias.addAll(asistencias);
        tblasistencia.setItems(listaAsistencias);
    }

    private void abrirVentanaCrearEditar(Asistencia asistenciaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-asistencia.fxml"));
            Parent root = loader.load();

            CrearAsistenciaController controller = loader.getController();
            //controller.setAsistenciaControllerPadre(this);
            //controller.setAsistenciaEditar(asistenciaEditar); // null para crear, objeto para editar

            Stage stage = new Stage();
            stage.setTitle(asistenciaEditar == null ? "Crear Asistencia" : "Editar Asistencia");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana.");
            e.printStackTrace();
        }
    }

    public void refrescarTabla() {
        cargarDatosTabla();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

