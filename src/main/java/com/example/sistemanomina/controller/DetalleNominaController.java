// src/main/java/com/example/sistemanomina/controller/DetalleNominaController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.DetalleNominaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.DetalleNomina;
import com.example.sistemanomina.model.Nomina;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.util.List;

public class DetalleNominaController {

    @FXML
    private TableView<DetalleNomina> tblDetalleNomina;
    @FXML
    private TableColumn<DetalleNomina, Integer> colID;
    @FXML
    private TableColumn<DetalleNomina, Integer> colIdNomina;
    @FXML
    private TableColumn<DetalleNomina, Integer> colIdEmpleado;
    @FXML
    private TableColumn<DetalleNomina, String> colNombreEmpleado;
    @FXML
    private TableColumn<DetalleNomina, Integer> colAusencias;
    @FXML
    private TableColumn<DetalleNomina, Integer> colDiasLaborados;
    @FXML
    private TableColumn<DetalleNomina, Double> colPercepciones;
    @FXML
    private TableColumn<DetalleNomina, Double> colDeducciones;
    @FXML
    private TableColumn<DetalleNomina, Double> colSueldoLiquido;

    @FXML
    private Button btnGenerarRecibo;

    private Nomina nomina;

    private ObservableList<DetalleNomina> detallesNomina;
    private DetalleNominaDAO detalleNominaDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            detalleNominaDAO = new DetalleNominaDAO(conn);
            if (nomina == null) {
                // mostrarAlerta("Error", "No se ha seleccionado una nómina válida.");
                return;
            }
            detallesNomina = FXCollections.observableArrayList(detalleNominaDAO.obtenerDetallePorNominaId(nomina.getId()));

            colID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colIdNomina.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNominaId()).asObject());
            colIdEmpleado.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEmpleadoId()).asObject());
            colNombreEmpleado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreEmpleado()));
            colAusencias.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAusencias()).asObject());
            colDiasLaborados.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDiasLaborados()).asObject());
            colPercepciones.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPercepciones()).asObject());
            colDeducciones.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getDeducciones()).asObject());
            colSueldoLiquido.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSueldoLiquido()).asObject());

            tblDetalleNomina.setItems(detallesNomina);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos o cargar los detalles de nómina.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onGenerarRecibo() {
        mostrarAlerta("Funcionalidad", "Funcionalidad de imprimir recibo no implementada.");
    }

    public void setNomina(Nomina nomina) {
        this.nomina = nomina;
        if (nomina != null) {
            initialize();
        } else {
            mostrarAlerta("Error", "No se ha seleccionado una nómina válida.");
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
