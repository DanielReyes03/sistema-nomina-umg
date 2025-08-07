package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.VacacionesDAO;
import com.example.sistemanomina.model.Vacaciones;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class CrearVacacionesController {

    @FXML
    private Button BotGuardar;

    @FXML
    private Button BotRegresar;

    @FXML
    private ComboBox<String> ComboEmpleado;

    @FXML
    private ComboBox<String> ComboAprobada;

    @FXML
    private DatePicker InputFechaInicio;

    @FXML
    private DatePicker InputFechaFin;

    @FXML
    private Spinner<Integer> SpinnerDias;

    @FXML
    private Text MensajeAccion; // ✅ Ahora usa javafx.scene.text.Text

    private final VacacionesDAO vacacionesDAO = new VacacionesDAO();

    @FXML
    public void initialize() {
        // Cargar datos iniciales
        ComboEmpleado.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
        ComboAprobada.setItems(FXCollections.observableArrayList("Sí", "No"));
        ComboAprobada.setValue("No");
        SpinnerDias.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1));
    }

    @FXML
    private void botguardar(ActionEvent event) {
        try {
            if (ComboEmpleado.getValue() == null || InputFechaInicio.getValue() == null || InputFechaFin.getValue() == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            int empleadoId = Integer.parseInt(ComboEmpleado.getValue());
            LocalDate fechaInicio = InputFechaInicio.getValue();
            LocalDate fechaFin = InputFechaFin.getValue();
            int dias = SpinnerDias.getValue();
            boolean aprobada = ComboAprobada.getValue().equals("Sí");

            Vacaciones vacaciones = new Vacaciones();
            vacaciones.setEmpleadoId(empleadoId);
            vacaciones.setFechaInicio(Date.valueOf(fechaInicio));
            vacaciones.setFechaFin(Date.valueOf(fechaFin));
            vacaciones.setDias(dias);
            vacaciones.setAprobada(aprobada);

            vacacionesDAO.insertar(vacaciones);

            mostrarAlerta("Éxito", "Vacaciones guardadas correctamente.");
            limpiarFormulario();

        } catch (SQLException e) {
            mostrarAlerta("Error BD", "No se pudo guardar: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void botregresar(ActionEvent event) {
        Stage stage = (Stage) BotRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarFormulario() {
        ComboEmpleado.setValue(null);
        InputFechaInicio.setValue(null);
        InputFechaFin.setValue(null);
        SpinnerDias.getValueFactory().setValue(1);
        ComboAprobada.setValue("No");
    }
}
