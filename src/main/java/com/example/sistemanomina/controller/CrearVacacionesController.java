package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.VacacionesDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.Vacaciones;
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

    @FXML private Button BotGuardar;

    @FXML private Button BotRegresar;
    @FXML private ComboBox<String> ComboEmpleado;  // formato "ID - Nombre Apellido"
    @FXML private DatePicker InputFechaInicio;
    @FXML private DatePicker InputFechaFin;
    @FXML private Spinner<Integer> SpinnerDias;
    @FXML private Text MensajeAccion;

    private final VacacionesDAO vacacionesDAO = new VacacionesDAO();
    private EmpleadoDAO empleadoDAO;

    // Referencia a VacacionesController para actualizar tabla y pasar datos
    private VacacionesController vacacionesController;

    // Vacaciones para editar (si es null, es creación)
    private Vacaciones vacacionesEditar;

    public CrearVacacionesController() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            empleadoDAO = new EmpleadoDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        cargarEmpleados();
        SpinnerDias.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1));

        // ✅ Si ya tenemos un objeto para editar, rellenar
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

        // Buscar el empleado para mostrar nombre en combo
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
        // NO ponemos aprobada porque está fija en false
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

            if (vacacionesEditar == null) {
                // Crear nuevo
                Vacaciones vac = new Vacaciones();
                vac.setEmpleadoId(empleadoId);
                vac.setFechaInicio(Date.valueOf(fechaInicio));
                vac.setFechaFin(Date.valueOf(fechaFin));
                vac.setDias(dias);
                vac.setAprobada(false); // Siempre false

                vacacionesDAO.insertar(vac);
                mostrarAlerta("Éxito", "Vacaciones guardadas correctamente.");
            } else {
                // Actualizar existente
                vacacionesEditar.setEmpleadoId(empleadoId);
                vacacionesEditar.setFechaInicio(Date.valueOf(fechaInicio));
                vacacionesEditar.setFechaFin(Date.valueOf(fechaFin));
                vacacionesEditar.setDias(dias);
                vacacionesEditar.setAprobada(false); // Siempre false

                vacacionesDAO.actualizar(vacacionesEditar);
                mostrarAlerta("Éxito", "Vacaciones actualizadas correctamente.");
            }

            limpiarFormulario();

            // Refrescar tabla en VacacionesController si existe referencia
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

    // Setter para la referencia al VacacionesController
    public void setVacacionesController(VacacionesController vacacionesController) {
        this.vacacionesController = vacacionesController;
    }

    // Setter para pasar Vacaciones a editar y actualizar formulario
    public void setVacacionesEditar(Vacaciones vacacionesEditar) {
        this.vacacionesEditar = vacacionesEditar;
        rellenarCamposEdicion(); // ✅ Se llama aquí y también desde initialize()
    }

    }

