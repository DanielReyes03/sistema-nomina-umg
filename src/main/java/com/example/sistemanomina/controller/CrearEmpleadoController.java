package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.DepartamentoDAO;
import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.PuestosDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Departamento;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.Puestos;
import com.example.sistemanomina.util.Alertas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class CrearEmpleadoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDPI;
    @FXML private DatePicker DTFechin;
    @FXML private TextField txtSalario;
    @FXML private ComboBox<Departamento> comboDepartamento;
    @FXML private ComboBox<Puestos> comboPuesto;
    @FXML private Text MensajeAccion;
    @FXML private Button btnregresar;
    @FXML private Button btnguardare;

    private DepartamentoDAO departamentoDAO;
    private PuestosDAO puestosDAO;
    private EmpleadoDAO empleadoDAO;

    private EmpleadoController empleadoController;



    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            departamentoDAO = new DepartamentoDAO(conn);
            puestosDAO = new PuestosDAO(conn);
            empleadoDAO = new EmpleadoDAO(conn);

            cargarDepartamentos();
            configurarEventos();

        } catch (Exception e) {
            MensajeAccion.setText("Error al inicializar: " + e.getMessage());
        }
    }

    public void setEmpleadoController(EmpleadoController empleadoController) {
        this.empleadoController = empleadoController;
    }

    private void cargarDepartamentos() {
        try {
            List<Departamento> lista = departamentoDAO.obtenerTodos();
            ObservableList<Departamento> data = FXCollections.observableArrayList(lista);
            comboDepartamento.setItems(data);

            comboDepartamento.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Departamento item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });
            comboDepartamento.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Departamento item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void configurarEventos() {
        comboDepartamento.setOnAction(e -> {
            Departamento seleccionado = comboDepartamento.getValue();
            if (seleccionado != null) {
                List<Puestos> puestos = puestosDAO.obtenerPorDepartamento(seleccionado.getId());
                comboPuesto.setItems(FXCollections.observableArrayList(puestos));
            }
        });

        comboPuesto.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Puestos item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboPuesto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Puestos item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    }

    @FXML
    private void onbtnguardare() {
        try {
            if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() ||
                    txtDPI.getText().isEmpty() || txtSalario.getText().isEmpty() ||
                    DTFechin.getValue() == null || comboDepartamento.getValue() == null ||
                    comboPuesto.getValue() == null) {
                MensajeAccion.setText("Completa todos los campos.");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String dpi = txtDPI.getText().trim();
            LocalDate fechaIngreso = DTFechin.getValue();
            double salario = Double.parseDouble(txtSalario.getText());
            int puestoId = comboPuesto.getValue().getId();

            Empleado empleado = new Empleado(0, nombre, apellido, dpi, fechaIngreso, salario, puestoId);

            boolean exito = empleadoDAO.agregarEmpleado(empleado);

            if (exito) {
                Alertas.mostrarInfo("Éxito", "Concepto creado correctamente.");
                limpiarCampos();
                if (empleadoController != null) {
                    empleadoController.cargarEmpleados();
                }
                this.cerrarVentana();
            } else {
                Alertas.mostrarError("Error", "No se pudo guardar el empleado.");
            }

        } catch (NumberFormatException e) {
            Alertas.mostrarError("Error", "El salario debe ser un número válido.");
        } catch (Exception e) {
            e.printStackTrace();
            Alertas.mostrarError("Error", "Ocurrió un error inesperado al guardar el empleado: ");
        }
    }

    @FXML
    private void onbtnregresar() {
        try {
            this.cerrarVentana();
        } catch (Exception e) {
            MensajeAccion.setText("Error al regresar.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtDPI.clear();
        txtSalario.clear();
        DTFechin.setValue(null);
        comboDepartamento.setValue(null);
        comboPuesto.setItems(FXCollections.observableArrayList());
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnguardare.getScene().getWindow();
        stage.close();
    }
}
