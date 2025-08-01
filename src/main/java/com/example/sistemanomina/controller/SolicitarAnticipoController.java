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

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class SolicitarAnticipoController {

    @FXML
    private ComboBox<Empleado> dropnombresEMPLEADO;

    @FXML
    private DatePicker fechaanticipo;

    @FXML
    private TextField monto;

    @FXML
    private TextField motivodeanticipo;

    @FXML
    private Button botonguardar;

    @FXML
    private Button botoncancelar;

    private ObservableList<Empleado> empleados;
    private EmpleadoDAO empleadoDAO;
    private AnticiposDAO anticiposDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            empleadoDAO = new EmpleadoDAO(conn);
            anticiposDAO = new AnticiposDAO(conn);

            cargarEmpleados();
        } catch (Exception e) {
            mostrarAlerta("Error al inicializar el controlador: " + e.getMessage());
        }
    }

    private void cargarEmpleados() {
        List<Empleado> lista = empleadoDAO.obtenerEmpleados();
        empleados = FXCollections.observableArrayList(lista);
        dropnombresEMPLEADO.setItems(empleados);
    }

    @FXML
    private void btnguardar() {
        Empleado seleccionado = dropnombresEMPLEADO.getValue();
        LocalDate fecha = fechaanticipo.getValue();
        String motivo = motivodeanticipo.getText().trim();
        String montoTexto = monto.getText().trim();

        if (seleccionado == null || fecha == null || motivo.isEmpty() || montoTexto.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        try {
            double montoValor = Double.parseDouble(montoTexto);
            Anticipo anticipo = new Anticipo();
            anticipo.setEmpleadoId(seleccionado.getId());
            anticipo.setFecha(fecha);
            anticipo.setMotivo(motivo);
            anticipo.setMonto(montoValor);
            anticipo.setSaldoPendiente(montoValor);

            anticiposDAO.insertar(anticipo);
            mostrarInfo("Anticipo guardado correctamente.");
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("El monto debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error al guardar el anticipo: " + e.getMessage());
        }
    }

    @FXML
    private void btncancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botoncancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
