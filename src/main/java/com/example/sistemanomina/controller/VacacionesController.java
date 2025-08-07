package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AsistenciaDAO;
import com.example.sistemanomina.dao.VacacionesDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Vacaciones;
import com.example.sistemanomina.util.Alertas;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VacacionesController {

    @FXML private TableView<Vacaciones> InputTablaVacaciones;
    @FXML private TableColumn<Vacaciones, Integer> ColID;
    @FXML private TableColumn<Vacaciones, String> ColNombre;
    @FXML private TableColumn<Vacaciones, Integer> ColCodigo;
    @FXML private TableColumn<Vacaciones, String> ColFechaInicio;
    @FXML private TableColumn<Vacaciones, String> ColFechaFin;
    @FXML private TableColumn<Vacaciones, Integer> ColDias;
    @FXML private TableColumn<Vacaciones, Boolean> ColAprobada;

    private VacacionesDAO vacacionesDAO;

    @FXML
    public void initialize() {
        try {
            try {
                Connection conn = DatabaseConnection.getInstance().getConnection();
                vacacionesDAO = new VacacionesDAO(conn);
            } catch (Exception e) {
                Alertas.mostrarError("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
            ColID.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

            // ⚠️ Aquí deberías traer el nombre del empleado con JOIN si lo tienes en BD
            ColNombre.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getNombreEmpleado()));

            ColCodigo.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getEmpleadoId()).asObject());

            ColFechaInicio.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getFechaInicio().toString()));

            ColFechaFin.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getFechaFin().toString()));

            ColDias.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getDias()).asObject());

            ColAprobada.setCellValueFactory(cellData ->
                    new SimpleBooleanProperty(cellData.getValue().isAprobada()).asObject());

            cargarVacaciones();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al inicializar la tabla: " + e.getMessage());
        }
    }

    public void cargarVacaciones() {
        try {
            List<Vacaciones> lista = vacacionesDAO.listarTodos();
            ObservableList<Vacaciones> vacaciones = FXCollections.observableArrayList(lista);
            InputTablaVacaciones.setItems(vacaciones);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar vacaciones: " + e.getMessage());
        }
    }

    @FXML
    private void botNuevo() {
        abrirFormulario(null);
    }

    @FXML
    private void botactualizar() {
        Vacaciones seleccionada = InputTablaVacaciones.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            abrirFormulario(seleccionada);
        } else {
            mostrarAlerta("Debes seleccionar una solicitud para actualizar.");
        }
    }

    @FXML
    private void boteliminar() {
        Vacaciones seleccionada = InputTablaVacaciones.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            try {
                vacacionesDAO.eliminar(seleccionada.getId());
                mostrarAlerta("Vacación eliminada correctamente.");
                cargarVacaciones();
            } catch (SQLException e) {
                mostrarAlerta("Error al eliminar: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Debes seleccionar una solicitud para eliminar.");
        }
    }

    private void abrirFormulario(Vacaciones vacacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-vacaciones.fxml"));
            Parent root = loader.load();

            CrearVacacionesController controller = loader.getController();
            controller.setVacacionesController(this);
            controller.setVacacionEditar(vacacion);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Vacaciones");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            Alertas.mostrarError("Error", "No se pudo abrir el formulario de vacaciones");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
