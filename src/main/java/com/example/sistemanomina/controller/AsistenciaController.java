package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AsistenciaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
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

import java.io.IOException;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;

public class AsistenciaController {

    @FXML private TableView<Asistencia> tblasistencia;
    @FXML private TableColumn<Asistencia, Integer> colid;
    @FXML private TableColumn<Asistencia, Integer> colcodemp;
    @FXML private TableColumn<Asistencia, String> colnomemp;
    @FXML private TableColumn<Asistencia, String> colfecha;
    @FXML private TableColumn<Asistencia, String> colhoraentrada;
    @FXML private TableColumn<Asistencia, String> colhorasalida;
    @FXML private Button BTCREAR, BTEDITAR, BTELIMINAR;

    private AsistenciaDAO asistenciaDAO;
    private final ObservableList<Asistencia> listaAsistencias = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            asistenciaDAO = new AsistenciaDAO(conn);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        colid.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colcodemp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEmpleadoId()).asObject());
        colfecha.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        colhoraentrada.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoraEntrada().toString()));
        colhorasalida.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoraSalida().toString()));
        colnomemp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("Empleado #" + cellData.getValue().getEmpleadoId()));
        cargarDatosTabla();

        BTCREAR.setOnAction(e -> abrirVentanaCrearEditar(null));
        BTEDITAR.setOnAction(e -> {
            Asistencia seleccionada = tblasistencia.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                abrirVentanaCrearEditar(seleccionada);
                cargarDatosTabla();
            } else {
                mostrarAlerta("Advertencia", "Debe seleccionar una asistencia para editar.");
            }
        });
        BTELIMINAR.setOnAction(e -> {
            Asistencia seleccionada = tblasistencia.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                asistenciaDAO.eliminarAsistenciaPorId(seleccionada.getId());
                cargarDatosTabla();
            } else {
                mostrarAlerta("Advertencia", "Debe seleccionar una asistencia para eliminar.");
            }
        });
    }

    void cargarDatosTabla() {
        listaAsistencias.setAll(asistenciaDAO.obtenerTodasAsistencias());
        tblasistencia.setItems(listaAsistencias);
    }

    private void abrirVentanaCrearEditar(Asistencia asistenciaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-asistencia.fxml"));
            Parent root = loader.load();
            CrearAsistenciaController controller = loader.getController();
            controller.setAsistenciaControllerPadre(this);
            controller.setAsistenciaEditar(asistenciaEditar);
            Stage stage = new Stage();
            stage.setTitle(asistenciaEditar == null ? "Crear Asistencia" : "Editar Asistencia");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el archivo FXML: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al abrir la ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
