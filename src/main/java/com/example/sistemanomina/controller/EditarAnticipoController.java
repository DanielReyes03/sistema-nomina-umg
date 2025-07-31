
// CARLO ANDREE BARQUERO BOCHE 0901-22-601  DESAROLLO DEL APARTADO DE ANTICIPOS
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AnticiposDAO;
import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Anticipo;
import com.example.sistemanomina.model.Empleado;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.util.List;

public class EditarAnticipoController {

    @FXML private DatePicker fechaanticipo;
    @FXML private TextField monto;
    @FXML private TextField motivodeanticipo;
    @FXML private TextField saldopendiente;
    @FXML private ComboBox<String> dropnombresEMPLEADO;

    private Anticipo anticipo;
    private EmpleadoDAO empleadoDAO;
    private ObservableList<String> nombresEmpleados;
    private List<Empleado> listaEmpleados;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            empleadoDAO = new EmpleadoDAO(conn);
            cargarNombresEmpleados();
        } catch (Exception e) {
            mostrarAlerta("Error al cargar empleados: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarNombresEmpleados() {
        listaEmpleados = empleadoDAO.obtenerEmpleados();
        nombresEmpleados = FXCollections.observableArrayList();
        for (Empleado emp : listaEmpleados) {
            String nombreCompleto = emp.getNombre() + " " + emp.getApellido();
            nombresEmpleados.add(nombreCompleto);
        }
        dropnombresEMPLEADO.setItems(nombresEmpleados);
    }

    public void setAnticipo(Anticipo anticipo) {
        this.anticipo = anticipo;
        fechaanticipo.setValue(anticipo.getFecha());
        monto.setText(String.valueOf(anticipo.getMonto()));
        motivodeanticipo.setText(anticipo.getMotivo());
        saldopendiente.setText(String.valueOf(anticipo.getSaldoPendiente()));
        saldopendiente.setDisable(true); // Campo no editable manualmente

        for (Empleado emp : listaEmpleados) {
            if (emp.getId() == anticipo.getEmpleadoId()) {
                String nombreCompleto = emp.getNombre() + " " + emp.getApellido();
                dropnombresEMPLEADO.setValue(nombreCompleto);
                break;
            }
        }
    }

    @FXML
    private void btnguardar(ActionEvent event) {
        try {
            double nuevoMonto = Double.parseDouble(monto.getText());
            double montoAnterior = anticipo.getMonto();
            double saldoAnterior = anticipo.getSaldoPendiente();
            double abonado = montoAnterior - saldoAnterior;


            if (nuevoMonto < abonado) {
                mostrarAlerta("El nuevo monto no puede ser menor al monto ya abonado (" + abonado + ")", Alert.AlertType.WARNING);
                return;
            }

            double nuevoSaldoPendiente = nuevoMonto - abonado;
            saldopendiente.setText(String.format("%.2f", nuevoSaldoPendiente)); // Mostrar en la interfaz

            anticipo.setFecha(fechaanticipo.getValue());
            anticipo.setMonto(nuevoMonto);
            anticipo.setSaldoPendiente(nuevoSaldoPendiente);
            anticipo.setMotivo(motivodeanticipo.getText());

            int empleadoId = obtenerEmpleadoIdSeleccionado();
            if (empleadoId == -1) {
                mostrarAlerta("Empleado seleccionado no válido", Alert.AlertType.WARNING);
                return;
            }
            anticipo.setEmpleadoId(empleadoId);

            AnticiposDAO dao = new AnticiposDAO(DatabaseConnection.getInstance().getConnection());
            dao.actualizarAnticipo(anticipo);

            mostrarAlerta("Anticipo modificado con éxito.", Alert.AlertType.INFORMATION);
            cerrarVentana();

        } catch (NumberFormatException nfe) {
            mostrarAlerta("Monto inválido. Ingrese un número válido.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error al modificar el anticipo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private int obtenerEmpleadoIdSeleccionado() {
        String nombreCompletoSeleccionado = dropnombresEMPLEADO.getValue();
        if (nombreCompletoSeleccionado == null) return -1;
        for (Empleado emp : listaEmpleados) {
            String nombreCompleto = emp.getNombre() + " " + emp.getApellido();
            if (nombreCompleto.equals(nombreCompletoSeleccionado)) {
                return emp.getId();
            }
        }
        return -1;
    }

    @FXML
    private void btncancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) fechaanticipo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.setHeaderText(null);
        alert.setTitle("Mensaje");
        alert.showAndWait();
    }
}
