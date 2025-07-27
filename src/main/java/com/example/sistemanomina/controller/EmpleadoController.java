package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.util.Alertas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmpleadoController {

    @FXML private TableView<Empleado> tblempleados;
    @FXML private TableColumn<Empleado, Integer> colID;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colApellido;
    @FXML private TableColumn<Empleado, String> colDPI;
    @FXML private TableColumn<Empleado, String> colPuesto;
    @FXML private TableColumn<Empleado, String> colFechin;
    @FXML private TableColumn<Empleado, String> colDepto;
    @FXML private TableColumn<Empleado, Double> colSalario;

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoController() throws SQLException {
        Connection conexion = DatabaseConnection.getInstance().getConnection();
        this.empleadoDAO = new EmpleadoDAO(conexion);
    }

    @FXML
    public void initialize() {
        try {
            colID.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colNombre.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getNombre()));
            colApellido.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getApellido()));
            colDPI.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getDpi()));
            colPuesto.setCellValueFactory(cellData ->
                    new SimpleStringProperty("Puesto #" + cellData.getValue().getPuestoId()));
            colFechin.setCellValueFactory(cellData ->
                    new SimpleStringProperty(
                            cellData.getValue().getFechaIngreso()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            colDepto.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getNombreDepartamento()));
            colSalario.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty(cellData.getValue().getSalario()).asObject());

            cargarEmpleados();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al inicializar la tabla: " + e.getMessage());
        }
    }

    public void cargarEmpleados() {
        List<Empleado> lista = empleadoDAO.obtenerEmpleados();
        lista.forEach(emp -> System.out.println(emp.getNombre() + " - " + emp.getApellido()));
        ObservableList<Empleado> empleados = FXCollections.observableArrayList(lista);
        tblempleados.setItems(empleados);
    }

    @FXML
    private void onbtncrear() {
        try {
            this.abrirFormulario(null);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario para crear empleado.");
        }
    }

    @FXML
    private void onbtnactualizar() {
        Empleado seleccionado = tblempleados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            this.abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Debes seleccionar un empleado para actualizar.");
        }
    }

    @FXML
    private void onbtneliminar() {
        Empleado seleccionado = tblempleados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean eliminado = empleadoDAO.eliminarEmpleado(seleccionado.getId());
            if (eliminado) {
                mostrarAlerta("Empleado eliminado correctamente.");
                cargarEmpleados();
            } else {
                mostrarAlerta("Error al eliminar el empleado.");
            }
        } else {
            mostrarAlerta("Debes seleccionar un empleado para eliminar.");
        }
    }

    private void abrirFormulario(Empleado empleado){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-empleado.fxml"));
            Parent root = loader.load();

            CrearEmpleadoController controller = loader.getController();
            controller.setEmpleadoController(this);
            //controller.setDepartamentoEditar(departamento);

            Stage stage = new Stage();
            stage.setTitle("Crear Nuevo Empleado");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            Alertas.mostrarError("Error", "Error al abrir el formulario de empleado");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
