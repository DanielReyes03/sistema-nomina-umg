// src/main/java/com/example/sistemanomina/controller/DepartamentoController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import com.example.sistemanomina.dao.DepartamentoDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Departamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

public class DepartamentoController {

    @FXML
    private TableView<Departamento> tablaDepartamentos;
    @FXML
    private TableColumn<Departamento, Integer> colId;
    @FXML
    private TableColumn<Departamento, String> colNombre;
    @FXML
    private TableColumn<Departamento, String> colDescripcion;

    @FXML
    private Button btnCrear;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;

    private ObservableList<Departamento> departamentos;
    private DepartamentoDAO departamentoDAO;
    private Departamento departamentoSeleccionado;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            departamentoDAO = new DepartamentoDAO(conn);

            departamentos = FXCollections.observableArrayList(departamentoDAO.obtenerTodos());

            colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
            colDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));

            tablaDepartamentos.setItems(departamentos);

            btnActualizar.setDisable(true);
            btnEliminar.setDisable(true);

            tablaDepartamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                boolean seleccionado = newSelection != null;
                btnActualizar.setDisable(!seleccionado);
                btnEliminar.setDisable(!seleccionado);
                departamentoSeleccionado = newSelection;
            });

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
            e.printStackTrace();
        }
    }
    @FXML
    private void onBtnCrear() {
        try {
            abrirVentanaCrearEditar(null);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo crear el departamento.");
        }
    }
    @FXML
    private void onBtnActualizar() {
        if (departamentoSeleccionado == null) {
            mostrarAlerta("Selección", "Seleccione un departamento para actualizar.");
            return;
        }
        try {
            abrirVentanaCrearEditar(departamentoSeleccionado);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar el departamento.");
        }
    }
    @FXML
    private void onBtnEliminar() {
        if (departamentoSeleccionado == null) {
            mostrarAlerta("Selección", "Seleccione un departamento para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Departamento");
        confirm.setHeaderText("¿Está seguro de eliminar el departamento seleccionado?");
        confirm.setContentText("Esta acción no se puede deshacer.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    departamentoDAO.eliminar(departamentoSeleccionado.getId());
                    departamentos.remove(departamentoSeleccionado);
                    departamentoSeleccionado = null;
                    btnActualizar.setDisable(true);
                    btnEliminar.setDisable(true);
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se pudo eliminar el departamento.");
                }
            }
        });
    }

    public void actualizarTabla() {
        try {
            departamentos.clear();
            departamentos.addAll(departamentoDAO.obtenerTodos());
            tablaDepartamentos.setItems(departamentos);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar la tabla de departamentos.");
        }
    }

    private void abrirVentanaCrearEditar(Departamento departamento) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("crear-departamento.fxml"));
            Parent root = loader.load();

            CrearDepartamentoController controller = loader.getController();
            controller.setDepartamentoControllerPadre(this);
            controller.setDepartamentoEditar(departamento); // null para crear, objeto para editar

            Stage stage = new Stage();
            stage.setTitle(departamento == null ? "Crear Departamento" : "Editar Departamento");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana.");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
