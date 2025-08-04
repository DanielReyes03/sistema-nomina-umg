package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.HorasExtraDAO;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.HorasExtra;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CrearHorasExtraController {

    @FXML private ComboBox<Empleado> ComboEmpleado;
    @FXML private DatePicker InputFecha;
    @FXML private Spinner<Integer> SpinnerHoras;
    @FXML private TextArea TxtaMotivo;
    @FXML private CheckBox chbAprobada;
    @FXML private Button btnGuardar;
    @FXML private Button btnRegresar;

    private HorasExtraDAO horasExtraDAO;
    private EmpleadoDAO empleadoDAO;
    private HorasExtraController horasExtraController;
    private HorasExtra registroEditando;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = com.example.sistemanomina.db.DatabaseConnection.getInstance().getConnection();
            horasExtraDAO = new HorasExtraDAO(conn);
            empleadoDAO = new EmpleadoDAO(conn);

            // ✅ Cargar empleados desde la BD
            List<Empleado> empleados = empleadoDAO.obtenerEmpleados();
            ComboEmpleado.getItems().addAll(empleados);

            // ✅ Mostrar ID + Nombre + Apellido
            ComboEmpleado.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Empleado empleado, boolean empty) {
                    super.updateItem(empleado, empty);
                    setText(empty || empleado == null ? "" :
                            empleado.getId() + " - " + empleado.getNombre() + " " + empleado.getApellido());
                }
            });
            ComboEmpleado.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Empleado empleado, boolean empty) {
                    super.updateItem(empleado, empty);
                    setText(empty || empleado == null ? "" :
                            empleado.getId() + " - " + empleado.getNombre() + " " + empleado.getApellido());
                }
            });

            // ✅ Spinner de horas
            SpinnerHoras.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHorasExtraController(HorasExtraController controller) {
        this.horasExtraController = controller;
    }

    // ✅ Para editar registro existente
    public void setRegistroEditar(HorasExtra registro) {
        this.registroEditando = registro;
        if (registro != null) {
            for (Empleado emp : ComboEmpleado.getItems()) {
                if (emp.getId() == registro.getEmpleadoId()) {
                    ComboEmpleado.getSelectionModel().select(emp);
                    break;
                }
            }

            InputFecha.setValue(registro.getFecha().toLocalDate());
            SpinnerHoras.getValueFactory().setValue(registro.getHoras());
            TxtaMotivo.setText(registro.getMotivo());
            chbAprobada.setSelected(registro.isAprobado());
        }
    }

    @FXML
    private void botguardar() {
        try {
            Empleado empleadoSeleccionado = ComboEmpleado.getValue();
            if (empleadoSeleccionado == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un empleado.");
                return;
            }

            HorasExtra h = new HorasExtra();
            h.setEmpleadoId(empleadoSeleccionado.getId());
            h.setFecha(Date.valueOf(InputFecha.getValue()));
            h.setHoras(SpinnerHoras.getValue());
            h.setMotivo(TxtaMotivo.getText());
            h.setAprobado(chbAprobada.isSelected());

            if (registroEditando != null) {
                h.setId(registroEditando.getId());
                horasExtraDAO.actualizarHorasExtra(h);
            } else {
                horasExtraDAO.agregarHorasExtra(h);
            }

            if (horasExtraController != null) {
                horasExtraController.actualizarTabla();
            }

            Stage stage = (Stage) btnGuardar.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar el registro.");
        }
    }

    @FXML
    private void botregresar() {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje, ButtonType.OK);
        alert.showAndWait();
    }
}
