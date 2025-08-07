package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.VacacionesDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.Vacaciones;
import com.example.sistemanomina.util.Alertas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CrearVacacionesController {

    private static final int LIMITE_ANUAL = 15; // ✅ Límite legal de días por año en Guatemala

    @FXML private Button BotGuardar;
    @FXML private Button BotRegresar;
    @FXML private ComboBox<String> ComboEmpleado;  // formato "ID - Nombre Apellido"
    @FXML private DatePicker InputFechaInicio;
    @FXML private DatePicker InputFechaFin;
    @FXML private Spinner<Integer> SpinnerDias;
    @FXML private Text MensajeAccion;

    private VacacionesDAO vacacionesDAO;
    private EmpleadoDAO empleadoDAO;

    private VacacionesController vacacionesController;
    private Vacaciones vacacionesEditar;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            vacacionesDAO = new VacacionesDAO(conn);
        } catch (Exception e) {
            Alertas.mostrarError("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        cargarEmpleados();
        SpinnerDias.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1));

        if (vacacionesEditar != null) {
            rellenarCamposEdicion();
        }
    }

    public void setVacacionEditar(Vacaciones vacacion) {
        this.vacacionesEditar = vacacion;
        if (ComboEmpleado != null) {
            rellenarCamposEdicion();
        }
    }

    private void cargarEmpleados() {
        try {
            List<Empleado> lista = empleadoDAO.obtenerEmpleados();
            ComboEmpleado.setItems(FXCollections.observableArrayList());

            for (Empleado emp : lista) {
                String texto = emp.getId() + " - " + emp.getNombre() + " " + emp.getApellido();
                ComboEmpleado.getItems().add(texto);
            }
        } catch (Exception e) {
            mostrarAlerta("Error BD", "No se pudieron cargar los empleados: " + e.getMessage());
        }
    }

    private void rellenarCamposEdicion() {
        if (vacacionesEditar == null) return;

        try {
            Empleado emp = empleadoDAO.obtenerEmpleadoPorId(vacacionesEditar.getEmpleadoId());
            if (emp != null) {
                String valorCombo = emp.getId() + " - " + emp.getNombre() + " " + emp.getApellido();
                ComboEmpleado.setValue(valorCombo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputFechaInicio.setValue(vacacionesEditar.getFechaInicio().toLocalDate());
        InputFechaFin.setValue(vacacionesEditar.getFechaFin().toLocalDate());
        SpinnerDias.getValueFactory().setValue(vacacionesEditar.getDias());
    }

    @FXML
    private void botguardar(ActionEvent event) {
        try {
            if (ComboEmpleado.getValue() == null || InputFechaInicio.getValue() == null || InputFechaFin.getValue() == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            String seleccionado = ComboEmpleado.getValue();
            int empleadoId = Integer.parseInt(seleccionado.split(" - ")[0]);

            LocalDate fechaInicio = InputFechaInicio.getValue();
            LocalDate fechaFin = InputFechaFin.getValue();
            int dias = SpinnerDias.getValue();

            // ✅ Paso 2 y 3: Validar que no exceda los 15 días por año
            int year = fechaInicio.getYear();
            int diasTomados = vacacionesDAO.obtenerDiasTomadosPorEmpleadoEnAnio(empleadoId, year);

            if (vacacionesEditar != null) {
                diasTomados -= vacacionesEditar.getDias(); // Para permitir editar sin duplicar
            }

            if (diasTomados + dias > LIMITE_ANUAL) {
                mostrarAlerta("Error", "El empleado ya ha tomado " + diasTomados +
                        " días de vacaciones en " + year + ". No puede exceder " + LIMITE_ANUAL + " días según la ley de Guatemala.");
                return;
            }

            if (vacacionesEditar == null) {
                Vacaciones vac = new Vacaciones();
                vac.setEmpleadoId(empleadoId);
                vac.setFechaInicio(Date.valueOf(fechaInicio));
                vac.setFechaFin(Date.valueOf(fechaFin));
                vac.setDias(dias);
                vac.setAprobada(false);

                vacacionesDAO.insertar(vac);
                mostrarAlerta("Éxito", "Vacaciones guardadas correctamente.");
            } else {
                vacacionesEditar.setEmpleadoId(empleadoId);
                vacacionesEditar.setFechaInicio(Date.valueOf(fechaInicio));
                vacacionesEditar.setFechaFin(Date.valueOf(fechaFin));
                vacacionesEditar.setDias(dias);
                vacacionesEditar.setAprobada(false);

                vacacionesDAO.actualizar(vacacionesEditar);
                mostrarAlerta("Éxito", "Vacaciones actualizadas correctamente.");
            }

            limpiarFormulario();

            if (vacacionesController != null) {
                vacacionesController.cargarVacaciones();
            }

            cerrarVentana();

        } catch (SQLException e) {
            mostrarAlerta("Error BD", "No se pudo guardar: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void botregresar(ActionEvent event) {
        cerrarVentana();
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
        vacacionesEditar = null;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) BotGuardar.getScene().getWindow();
        stage.close();
    }

    public void setVacacionesController(VacacionesController vacacionesController) {
        this.vacacionesController = vacacionesController;
    }

    public void setVacacionesEditar(Vacaciones vacacionesEditar) {
        this.vacacionesEditar = vacacionesEditar;
        rellenarCamposEdicion();
    }
}
