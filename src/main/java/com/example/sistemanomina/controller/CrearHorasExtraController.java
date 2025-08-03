package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.HorasExtraDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.HorasExtra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CrearHorasExtraController {

    @FXML private ComboBox<String> ComboEmpleado; // "ID - Nombre Apellido"
    @FXML private DatePicker InputFecha;
    @FXML private Spinner<Integer> SpinnerHoras;
    @FXML private TextArea TxtaMotivo;
    @FXML private CheckBox chbAprobada;
    @FXML private Button btnGuardar;
    @FXML private Button btnRegresar;
    @FXML private Label MensajeAccion;

    private HorasExtraDAO horasExtraDAO;
    private EmpleadoDAO empleadoDAO;
    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            horasExtraDAO = new HorasExtraDAO(conn);
            empleadoDAO = new EmpleadoDAO(conn);

            cargarEmpleados();

            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12, 0);
            SpinnerHoras.setValueFactory(valueFactory);

        } catch (SQLException e) {
            mostrarMensaje("❌ Error al conectar con la base de datos.");
            e.printStackTrace();
        }
    }

    private void cargarEmpleados() {
        try {
            List<Empleado> empleados = empleadoDAO.obtenerEmpleados();
            ObservableList<String> opciones = FXCollections.observableArrayList();

            for (Empleado emp : empleados) {
                String texto = emp.getId() + " - " + emp.getNombre() + " " + emp.getApellido();
                opciones.add(texto);
            }

            ComboEmpleado.setItems(opciones);
        } catch (Exception e) {
            mostrarMensaje("❌ Error al cargar empleados.");
            e.printStackTrace();
        }
    }

@FXML
    private void botguardar() {
        try {
            String seleccionado = ComboEmpleado.getValue();
            if (seleccionado == null || InputFecha.getValue() == null || TxtaMotivo.getText().isEmpty() || SpinnerHoras.getValue() <= 0) {
                mostrarMensaje("⚠️ Complete todos los campos.");
                return;
            }

            int empleadoId = Integer.parseInt(seleccionado.split(" - ")[0]);
            LocalDate fecha = InputFecha.getValue();
            int horas = SpinnerHoras.getValue();
            String motivo = TxtaMotivo.getText();
            boolean aprobado = chbAprobada.isSelected();

            HorasExtra h = new HorasExtra();
            h.setEmpleadoId(empleadoId);
            h.setFecha(Date.valueOf(fecha));
            h.setHoras(horas);
            h.setMotivo(motivo);
            h.setAprobado(aprobado);

            horasExtraDAO.agregarHorasExtra(h);
            mostrarMensaje("✅ Hora extra guardada correctamente.");

            if (horasExtraController != null) {
                horasExtraController.actualizarTabla();
            }

            limpiarFormulario();

        } catch (Exception e) {
            mostrarMensaje("❌ Error al guardar.");
            e.printStackTrace();
        }
    }

    private HorasExtraController horasExtraController;

    public void setHorasExtraController(HorasExtraController controller) {
        this.horasExtraController = controller;
    }



    @FXML
    private void botregresar() {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String mensaje) {
        MensajeAccion.setText(mensaje);
    }

    private void limpiarFormulario() {
        ComboEmpleado.setValue(null);
        InputFecha.setValue(null);
        SpinnerHoras.getValueFactory().setValue(0);
        TxtaMotivo.clear();
        chbAprobada.setSelected(false);
    }
}