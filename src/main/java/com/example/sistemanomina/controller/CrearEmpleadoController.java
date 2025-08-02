// CrearEmpleadoController.java

package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.DepartamentoDAO;
import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.PuestosDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Departamento;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.Puestos;
import com.example.sistemanomina.util.Alertas;
import com.example.sistemanomina.util.Validadores;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private Empleado empleadoEditar;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            departamentoDAO = new DepartamentoDAO(conn);
            puestosDAO = new PuestosDAO(conn);
            empleadoDAO = new EmpleadoDAO(conn);
            cargarDepartamentos();
            configurarEventos();

            // Si estamos editando, rellenar los campos
            if (empleadoEditar != null) {
                rellenarCamposEdicion();
            }

        } catch (Exception e) {
            MensajeAccion.setText("Error al inicializar: " + e.getMessage());
        }
    }

    public void setEmpleadoController(EmpleadoController empleadoController) {
        this.empleadoController = empleadoController;
    }

    public void setEmpleadoEditar(Empleado empleado){
        this.empleadoEditar = empleado;
        // Si el FXML ya está inicializado, rellenar los campos
        if (comboDepartamento != null) {
            rellenarCamposEdicion();
        }
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

    private void rellenarCamposEdicion() {
        try {
            if (empleadoEditar == null) return;

            txtNombre.setText(empleadoEditar.getNombre());
            txtApellido.setText(empleadoEditar.getApellido());
            txtDPI.setText(empleadoEditar.getDpi());
            DTFechin.setValue(empleadoEditar.getFechaIngreso());
            txtSalario.setText(String.valueOf(empleadoEditar.getSalario()));

            // Buscar el puesto y departamento del empleado
            Puestos puestoEmpleado = puestosDAO.obtenerPorId(empleadoEditar.getPuestoId());
            if (puestoEmpleado != null) {
                Departamento departamentoEmpleado = departamentoDAO.obtenerPorId(puestoEmpleado.getIdDepartamento());
                if (departamentoEmpleado != null) {
                    comboDepartamento.setValue(departamentoEmpleado);

                    // Cargar los puestos del departamento seleccionado
                    List<Puestos> puestos = puestosDAO.obtenerPorDepartamento(departamentoEmpleado.getId());
                    comboPuesto.setItems(FXCollections.observableArrayList(puestos));

                    // Seleccionar el puesto correspondiente
                    for (Puestos p : puestos) {
                        if (p.getId() == puestoEmpleado.getId()) {
                            comboPuesto.setValue(p);
                            break;
                        }
                    }
                }
            }
        }catch (Exception e) {
            MensajeAccion.setText("Error al rellenar los campos de edición: " + e.getMessage());
        }
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

            boolean dpiValido = Validadores.validarLongitud(txtDPI, "DPI", 13, 13);
            if(!dpiValido){
                Alertas.mostrarError("Error", "El DPI debe tener exactamente 13 caracteres.");
                return;
            }

            Empleado empleadoExiste = empleadoDAO.validarEmpleadoUnico(txtDPI.getText().trim());
            if(empleadoExiste != null){
                Alertas.mostrarError("Error", "Ya existe un empleado con el mismo DPI y nombre.");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String dpi = txtDPI.getText().trim();
            LocalDate fechaIngreso = DTFechin.getValue();
            double salario = Double.parseDouble(txtSalario.getText());
            int puestoId = comboPuesto.getValue().getId();

            boolean exito;
            if (empleadoEditar != null) {
                // Actualizar empleado existente
                Empleado empleadoActualizado = new Empleado(
                        empleadoEditar.getId(), nombre, apellido, dpi, fechaIngreso, salario, puestoId
                );
                exito = empleadoDAO.actualizarEmpleado(empleadoActualizado);
            } else {
                // Crear nuevo empleado
                Empleado empleado = new Empleado(0, nombre, apellido, dpi, fechaIngreso, salario, puestoId);
                exito = empleadoDAO.agregarEmpleado(empleado);
            }

            if (exito) {
                Alertas.mostrarInfo("Éxito", empleadoEditar != null ? "Empleado actualizado correctamente." : "Empleado creado correctamente.");
                limpiarCampos();
                if (empleadoController != null) {
                    empleadoController.cargarEmpleados();
                }
                this.cerrarVentana();
            } else {
                Alertas.mostrarError("Error", empleadoEditar != null ? "No se pudo actualizar el empleado." : "No se pudo guardar el empleado.");
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
