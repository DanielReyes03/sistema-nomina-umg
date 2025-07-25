package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AsistenciaDAO;
import com.example.sistemanomina.model.Asistencia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CrearAsistenciaController {

    @FXML
    private ComboBox<Integer> comboempleados;

    @FXML
    private TextField inputfecha;

    @FXML
    private TextField inputhoraentrada;

    @FXML
    private TextField inputhorasalida;

    @FXML
    private Button inputguardadasistencia;

    @FXML
    private Button inputverasistencia;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnEmpleados;

    @FXML
    private Button btnAsistencia;

    @FXML
    private Button btnVacaciones;

    @FXML
    private Button btnHorasExtra;

    @FXML
    private Button btnAnticipos;

    @FXML
    private Button btnPlanilla;

    @FXML
    private Button btnPlanilla1;

    @FXML
    private Button btnDepartamentos;

    @FXML
    private Button btnPuestos;

    private AsistenciaDAO asistenciaDAO;

    @FXML
    public void initialize() {
        asistenciaDAO = new AsistenciaDAO();
        // Initialize ComboBox with employee IDs (example IDs, replace with actual data)
        ObservableList<Integer> empleados = FXCollections.observableArrayList(1, 2, 3, 4, 5); // Replace with actual employee IDs
        comboempleados.setItems(empleados);

        // Set action handlers for buttons
        inputguardadasistencia.setOnAction(event -> guardarAsistencia());
        inputverasistencia.setOnAction(event -> verAsistencias());

        // Navigation buttons (implement navigation logic as needed)
        btnUsuarios.setOnAction(event -> navigateTo("Usuarios"));
        btnEmpleados.setOnAction(event -> navigateTo("Empleados"));
        btnAsistencia.setOnAction(event -> navigateTo("Asistencia"));
        btnVacaciones.setOnAction(event -> navigateTo("Vacaciones"));
        btnHorasExtra.setOnAction(event -> navigateTo("HorasExtra"));
        btnAnticipos.setOnAction(event -> navigateTo("Anticipos"));
        btnPlanilla.setOnAction(event -> navigateTo("Planilla"));
        btnPlanilla1.setOnAction(event -> navigateTo("Reporteria"));
        btnDepartamentos.setOnAction(event -> navigateTo("Departamentos"));
        btnPuestos.setOnAction(event -> navigateTo("Puestos"));
    }

    private void guardarAsistencia() {
        try {
            // Validate inputs
            if (comboempleados.getValue() == null || inputfecha.getText().isEmpty() ||
                    inputhoraentrada.getText().isEmpty() || inputhorasalida.getText().isEmpty()) {
                showAlert("Error", "Por favor, complete todos los campos.");
                return;
            }

            // Parse inputs
            int empleadoId = comboempleados.getValue();
            LocalDate fecha = LocalDate.parse(inputfecha.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime horaEntrada = LocalTime.parse(inputhoraentrada.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime horaSalida = LocalTime.parse(inputhorasalida.getText(), DateTimeFormatter.ofPattern("HH:mm"));

            // Create Asistencia object
            Asistencia asistencia = new Asistencia(empleadoId, fecha, horaEntrada, horaSalida);

            // Save to database
            asistenciaDAO.insertarAsistencia(asistencia);
            showAlert("Éxito", "Asistencia guardada correctamente.");
            clearFields();

        } catch (DateTimeParseException e) {
            showAlert("Error", "Formato de fecha u hora inválido. Use yyyy-MM-dd para fecha y HH:mm para hora.");
        } catch (Exception e) {
            showAlert("Error", "Error al guardar la asistencia: " + e.getMessage());
        }
    }

    private void verAsistencias() {
        try {
            Integer empleadoId = comboempleados.getValue();
            ObservableList<Asistencia> asistencias;
            if (empleadoId != null) {
                asistencias = FXCollections.observableArrayList(asistenciaDAO.buscarAsistenciaPorEmpleadoId(empleadoId));
            } else {
                asistencias = FXCollections.observableArrayList(asistenciaDAO.obtenerTodasAsistencias());
            }

            // TODO: Implement logic to display asistencias (e.g., open new window or update UI)
            showAlert("Información", "Se han encontrado " + asistencias.size() + " registros de asistencia.");

        } catch (Exception e) {
            showAlert("Error", "Error al obtener las asistencias: " + e.getMessage());
        }
    }

    private void navigateTo(String section) {
        // TODO: Implement navigation logic to switch to different views
        showAlert("Navegación", "Navegando a la sección: " + section);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        comboempleados.setValue(null);
        inputfecha.clear();
        inputhoraentrada.clear();
        inputhorasalida.clear();
    }
}