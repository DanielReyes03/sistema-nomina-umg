package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AsistenciaDAO;
import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.model.Asistencia;
import com.example.sistemanomina.model.Empleado;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CrearAsistenciaController {

    @FXML private ChoiceBox<Empleado> cbEmpleadoId;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Asistencia asistenciaEditar;
    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private EmpleadoDAO empleadoDAO;
    private AsistenciaController asistenciaControllerPadre;

    public CrearAsistenciaController() {
        // Inicialización diferida de empleadoDAO
    }

    @FXML
    public void initialize() {
        // Defensive check for FXML binding
        if (txtHoraEntrada == null || txtHoraSalida == null) {
            System.err.println("Error: One or more TextField fields (txtHoraEntrada or txtHoraSalida) are null in CrearAsistenciaController");
            return;
        }

        // Initialize empleadoDAO if not already initialized
        if (empleadoDAO == null) {
            try {
                empleadoDAO = new EmpleadoDAO(com.example.sistemanomina.db.DatabaseConnection.getInstance().getConnection());
            } catch (SQLException e) {
                System.err.println("Error initializing EmpleadoDAO: " + e.getMessage());
                return;
            }
        }

        // Populate ChoiceBox with employees
        cbEmpleadoId.setItems(FXCollections.observableArrayList(empleadoDAO.obtenerEmpleados()));
        cbEmpleadoId.setConverter(new javafx.util.StringConverter<Empleado>() {
            @Override
            public String toString(Empleado empleado) {
                return empleado != null ? empleado.getId() + " - " + empleado.getNombre() + " " + empleado.getApellido() : "";
            }
            @Override
            public Empleado fromString(String string) {
                return null;
            }
        });

        // Ensure buttons are not null
        if (btnGuardar != null) {
            btnGuardar.setOnAction(e -> guardarAsistencia());
        } else {
            System.err.println("Error: btnGuardar is null in CrearAsistenciaController");
        }

        if (btnCancelar != null) {
            btnCancelar.setOnAction(e -> ((Stage) btnCancelar.getScene().getWindow()).close());
        } else {
            System.err.println("Error: btnCancelar is null in CrearAsistenciaController");
        }

        // Add ChangeListener for lenient input
        txtHoraEntrada.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("[0-2]?[0-9]?(:[0-5]?[0-9]?)?")) {
                txtHoraEntrada.setText(oldValue); // Revert only if completely invalid
            }
        });
        txtHoraSalida.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("[0-2]?[0-9]?(:[0-5]?[0-9]?)?")) {
                txtHoraSalida.setText(oldValue); // Revert only if completely invalid
            }
        });
    }

    public void setAsistenciaEditar(Asistencia asistencia) {
        this.asistenciaEditar = asistencia;
        if (asistencia != null) {
            cbEmpleadoId.getSelectionModel().select(empleadoDAO.obtenerEmpleadoPorId(asistencia.getEmpleadoId()));
            dpFecha.setValue(asistencia.getFecha());
            txtHoraEntrada.setText(asistencia.getHoraEntrada().format(DateTimeFormatter.ofPattern("HH:mm")));
            txtHoraSalida.setText(asistencia.getHoraSalida().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    public void setAsistenciaControllerPadre(AsistenciaController padre) {
        this.asistenciaControllerPadre = padre;
    }

    private void guardarAsistencia() {
        try {
            // Validate employee selection
            Empleado empleadoSeleccionado = cbEmpleadoId.getSelectionModel().getSelectedItem();
            if (empleadoSeleccionado == null) {
                mostrarAlerta("Error", "Debe seleccionar un empleado.");
                return;
            }

            // Validate date
            LocalDate fecha = dpFecha.getValue();
            if (fecha == null) {
                mostrarAlerta("Error", "Debe seleccionar una fecha.");
                return;
            }

            // Validate and parse time fields
            if (txtHoraEntrada.getText().isEmpty() || txtHoraSalida.getText().isEmpty()) {
                mostrarAlerta("Error", "Los campos de hora no pueden estar vacíos.");
                return;
            }

            LocalTime entrada;
            LocalTime salida;
            try {
                if (!txtHoraEntrada.getText().matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                    mostrarAlerta("Error", "La hora de entrada debe estar en formato HH:mm (ej. 08:00).");
                    return;
                }
                if (!txtHoraSalida.getText().matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                    mostrarAlerta("Error", "La hora de salida debe estar en formato HH:mm (ej. 08:00).");
                    return;
                }

                entrada = LocalTime.parse(txtHoraEntrada.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                salida = LocalTime.parse(txtHoraSalida.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                mostrarAlerta("Error", "Formato de hora inválido. Use HH:mm (ej. 08:00).");
                return;
            }

            // Validate logical time constraints
            if (salida.isBefore(entrada)) {
                mostrarAlerta("Error", "La hora de salida no puede ser anterior a la hora de entrada.");
                return;
            }

            // Save or update assistance
            if (asistenciaEditar == null) {
                Asistencia nueva = new Asistencia(0, empleadoSeleccionado.getId(), fecha, entrada, salida);
                asistenciaDAO.insertarAsistencia(nueva);
            } else {
                asistenciaEditar.setEmpleadoId(empleadoSeleccionado.getId());
                asistenciaEditar.setFecha(fecha);
                asistenciaEditar.setHoraEntrada(entrada);
                asistenciaEditar.setHoraSalida(salida);
                asistenciaDAO.actualizarAsistencia(asistenciaEditar);
            }

            asistenciaControllerPadre.cargarDatosTabla();
            mostrarAlerta("Éxito", "Asistencia guardada correctamente.");
            ((Stage) btnGuardar.getScene().getWindow()).close();

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar la asistencia: " + e.getMessage());
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