// src/main/java/com/example/sistemanomina/controller/CrearNominaController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.NominaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Nomina;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.time.LocalDate;

public class CrearNominaController {

    @FXML
    private DatePicker dpPeriodoInicio;
    @FXML
    private DatePicker dpPeriodoFin;
    @FXML
    private ComboBox<String> comboTipo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Text mensajeAccion;

    private NominaDAO nominaDAO;
    private NominaController nominaControllerPadre;
    private Nomina nominaEditar; // null para crear, objeto para editar

    @FXML
    public void initialize() {
        // Opciones por defecto para el ComboBox
        comboTipo.getItems().addAll("Mensual", "Quincenal", "Otro");
        comboTipo.getSelectionModel().selectFirst();

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            nominaDAO = new NominaDAO(conn);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
            btnGuardar.setDisable(true);
        }

        if (nominaEditar != null) {
            mensajeAccion.setText("Editar nómina");
            dpPeriodoInicio.setValue(nominaEditar.getPeriodoInicio());
            dpPeriodoFin.setValue(nominaEditar.getPeriodoFin());
            comboTipo.setValue(nominaEditar.getTipo());
        } else {
            mensajeAccion.setText("Crear nueva nómina");
        }
    }

    @FXML
    private void onBtnGuardar() {
        LocalDate inicio = dpPeriodoInicio.getValue();
        LocalDate fin = dpPeriodoFin.getValue();
        String tipo = comboTipo.getValue();

        if (inicio == null || fin == null || tipo == null || tipo.isEmpty()) {
            mostrarAlerta("Campos requeridos", "Todos los campos son obligatorios.");
            return;
        }

        try {
            if (nominaEditar == null) {
                Nomina nuevaNomina = new Nomina(inicio, fin, tipo, LocalDate.now(), "PENDIENTE");
                nominaDAO.insertar(nuevaNomina);
                mostrarAlerta("Éxito", "Nómina creada correctamente.");
            } else {
                nominaEditar.setPeriodoInicio(inicio);
                nominaEditar.setPeriodoFin(fin);
                nominaEditar.setTipo(tipo);
                nominaDAO.actualizar(nominaEditar);
                mostrarAlerta("Éxito", "Nómina actualizada correctamente.");
            }
            if (nominaControllerPadre != null) {
                nominaControllerPadre.actualizarTabla();
            }
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar la nómina.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtnRegresar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos para comunicación con el controlador padre y edición
    public void setNominaControllerPadre(NominaController padre) {
        this.nominaControllerPadre = padre;
    }

    public void setNominaEditar(Nomina nomina) {
        this.nominaEditar = nomina;
    }
}
