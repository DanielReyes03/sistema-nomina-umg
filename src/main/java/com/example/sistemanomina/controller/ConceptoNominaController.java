// src/main/java/com/example/sistemanomina/controller/ConceptoNominaController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import com.example.sistemanomina.dao.ConceptoNominaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.ConceptoNomina;
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

public class ConceptoNominaController {

    @FXML
    private TableView<ConceptoNomina> tablaNomina;
    @FXML
    private TableColumn<ConceptoNomina, Integer> colId;
    @FXML
    private TableColumn<ConceptoNomina, String> colNombre;
    @FXML
    private TableColumn<ConceptoNomina, String> colDescripcion;
    @FXML
    private TableColumn<ConceptoNomina, String> colTipo;
    @FXML
    private TableColumn<ConceptoNomina, String> colTipoCalculo;
    @FXML
    private TableColumn<ConceptoNomina, Double> colValor;
    @FXML
    private TableColumn<ConceptoNomina, Boolean> colAplicaAutomatico;

    @FXML
    private Button btnCrear;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;

    private ObservableList<ConceptoNomina> conceptos;
    private ConceptoNominaDAO conceptoNominaDAO;
    private ConceptoNomina conceptoSeleccionado;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            conceptoNominaDAO = new ConceptoNominaDAO(conn);

            conceptos = FXCollections.observableArrayList(conceptoNominaDAO.obtenerTodos());

            colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
            colDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
            colTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo()));
            colTipoCalculo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoCalculo()));
            colValor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getValor()).asObject());
            colAplicaAutomatico.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().getAplicaAutomatico()).asObject());

            tablaNomina.setItems(conceptos);

            btnActualizar.setDisable(true);
            btnEliminar.setDisable(true);

            tablaNomina.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                boolean seleccionado = newSelection != null;
                btnActualizar.setDisable(!seleccionado);
                btnEliminar.setDisable(!seleccionado);
                conceptoSeleccionado = newSelection;
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
            mostrarAlerta("Error", "No se pudo crear el concepto.");
        }
    }

    @FXML
    private void onBtnActualizar() {
        if (conceptoSeleccionado == null) {
            mostrarAlerta("Selección", "Seleccione un concepto para actualizar.");
            return;
        }
        try {
            abrirVentanaCrearEditar(conceptoSeleccionado);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar el concepto.");
        }
    }

    @FXML
    private void onBtnEliminar() {
        if (conceptoSeleccionado == null) {
            mostrarAlerta("Selección", "Seleccione un concepto para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Concepto");
        confirm.setHeaderText("¿Está seguro de eliminar el concepto seleccionado?");
        confirm.setContentText("Esta acción no se puede deshacer.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    conceptoNominaDAO.eliminar(conceptoSeleccionado.getId());
                    conceptos.remove(conceptoSeleccionado);
                    conceptoSeleccionado = null;
                    btnActualizar.setDisable(true);
                    btnEliminar.setDisable(true);
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se pudo eliminar el concepto.");
                }
            }
        });
    }

    public void actualizarTabla() {
        try {
            conceptos.clear();
            conceptos.addAll(conceptoNominaDAO.obtenerTodos());
            tablaNomina.setItems(conceptos);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar la tabla de conceptos.");
        }
    }

    private void abrirVentanaCrearEditar(ConceptoNomina concepto) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("crear-concepto-nomina.fxml"));
            Parent root = loader.load();

            CrearConceptoNominaController controller = loader.getController();
            controller.setConceptoNominaControllerPadre(this);
            controller.setConceptoEditar(concepto); // null para crear, objeto para editar

            Stage stage = new Stage();
            stage.setTitle(concepto == null ? "Crear Concepto Nómina" : "Editar Concepto Nómina");
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
