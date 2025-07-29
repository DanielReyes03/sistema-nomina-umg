// src/main/java/com/example/sistemanomina/controller/PuestosController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import com.example.sistemanomina.dao.PuestosDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Puestos;
import com.example.sistemanomina.controller.CrearPuestosController;
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

public class PuestosController {

    @FXML
    private TableView<Puestos> tablaDepartamentos;
    @FXML
    private TableColumn<Puestos, Integer> colId;
    @FXML
    private TableColumn<Puestos, String> colNombre;
    @FXML
    private TableColumn<Puestos, String> colDescripcion;
    @FXML
    private TableColumn<Puestos, String> colRangoSalarios; // Rango Salarios
    @FXML
    private TableColumn<Puestos, String> colDepartamento; // Departamento

    @FXML
    private Button btnCrear;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;

    private ObservableList<Puestos> puestos;
    private PuestosDAO puestosDAO;
    private Puestos puestoSeleccionado;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            puestosDAO = new PuestosDAO(conn);

            puestos = FXCollections.observableArrayList(puestosDAO.obtenerTodos());

            colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
            colDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
            colRangoSalarios.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRangoSalarios()));
            colDepartamento.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreDepartamento()));

            tablaDepartamentos.setItems(puestos);

            btnActualizar.setDisable(true);
            btnEliminar.setDisable(true);

            tablaDepartamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                boolean seleccionado = newSelection != null;
                btnActualizar.setDisable(!seleccionado);
                btnEliminar.setDisable(!seleccionado);
                puestoSeleccionado = newSelection;
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
            mostrarAlerta("Error", "No se pudo crear el puesto.");
        }
    }

    @FXML
    private void onBtnActualizar() {
        if (puestoSeleccionado == null) {
            mostrarAlerta("Selección", "Seleccione un puesto para actualizar.");
            return;
        }
        try {
            abrirVentanaCrearEditar(puestoSeleccionado);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar el puesto.");
        }
    }

    @FXML
    private void onBtnEliminar() {
        if (puestoSeleccionado == null) {
            mostrarAlerta("Selección", "Seleccione un puesto para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Puesto");
        confirm.setHeaderText("¿Está seguro de eliminar el puesto seleccionado?");
        confirm.setContentText("Esta acción no se puede deshacer.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    puestosDAO.eliminar(puestoSeleccionado.getId());
                    puestos.remove(puestoSeleccionado);
                    puestoSeleccionado = null;
                    btnActualizar.setDisable(true);
                    btnEliminar.setDisable(true);
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se pudo eliminar el puesto.");
                }
            }
        });
    }

    public void actualizarTabla() {
        try {
            puestos.clear();
            puestos.addAll(puestosDAO.obtenerTodos());
            tablaDepartamentos.setItems(puestos);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar la tabla de puestos.");
        }
    }

    private void abrirVentanaCrearEditar(Puestos puesto) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("crear-puestos.fxml"));
            Parent root = loader.load();

            CrearPuestosController controller = loader.getController();
            controller.setPuestosControllerPadre(this);
            controller.setPuestoEditar(puesto); // null para crear, objeto para editar

            Stage stage = new Stage();
            stage.setTitle(puesto == null ? "Crear Puesto" : "Editar Puesto");
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
