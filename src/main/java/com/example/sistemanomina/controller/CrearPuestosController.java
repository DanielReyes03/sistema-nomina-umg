// src/main/java/com/example/sistemanomina/controller/CrearPuestosController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.DepartamentoDAO;
import com.example.sistemanomina.dao.PuestosDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Departamento;
import com.example.sistemanomina.model.Puestos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class CrearPuestosController {

    @FXML
    private TextField inputNombre;
    @FXML
    private TextArea inputDescripcion;
    @FXML
    private TextField inputSalario;
    @FXML
    private ComboBox<Departamento> comboDepartamento;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Text mensajeAccion;

    private Puestos puestoEditar;
    private PuestosController puestosControllerPadre;
    private PuestosDAO puestosDAO;
    private DepartamentoDAO departamentoDAO;
    private ObservableList<Departamento> departamentos;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            puestosDAO = new PuestosDAO(conn);
            departamentoDAO = new DepartamentoDAO(conn);

            List<Departamento> listaDepartamentos = departamentoDAO.obtenerTodos();
            departamentos = FXCollections.observableArrayList(listaDepartamentos);
            comboDepartamento.setItems(departamentos);

            // Mostrar el nombre en el ComboBox
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
            mostrarAlerta("Error", "No se pudo cargar la información de departamentos.");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onBtnGuardar() {
        String nombre = inputNombre.getText().trim();
        String descripcion = inputDescripcion.getText().trim();
        String rangoSalarios = inputSalario.getText().trim();
        Departamento departamento = comboDepartamento.getValue();

        if (nombre.isEmpty() || descripcion.isEmpty() || rangoSalarios.isEmpty() || departamento == null) {
            mostrarAlerta("Campos requeridos", "Todos los campos son obligatorios.");
            return;
        }

        try {
            if (puestoEditar == null) {
                // Crear nuevo puesto
                Puestos nuevo = new Puestos(nombre, descripcion, rangoSalarios, departamento.getId());
                puestosDAO.insertar(nuevo);
                mostrarAlerta("Éxito", "Puesto creado correctamente.");
            } else {
                // Editar puesto existente
                puestoEditar.setNombre(nombre);
                puestoEditar.setDescripcion(descripcion);
                puestoEditar.setRangoSalarios(rangoSalarios);
                puestoEditar.setIdDepartamento(departamento.getId());
                puestosDAO.actualizar(puestoEditar);
                mostrarAlerta("Éxito", "Puesto actualizado correctamente.");
            }
            if (puestosControllerPadre != null) {
                puestosControllerPadre.actualizarTabla();
            }
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el puesto.");
        }
    }

    @FXML
    private void onBtnRegresar() {
        cerrarVentana();
    }

    public void setPuestosControllerPadre(PuestosController controller) {
        this.puestosControllerPadre = controller;
    }

    public void setPuestoEditar(Puestos puesto) {
        this.puestoEditar = puesto;
        if (puestoEditar != null) {
            mensajeAccion.setText("Editar Puesto");
            inputNombre.setText(puestoEditar.getNombre());
            inputDescripcion.setText(puestoEditar.getDescripcion());
            inputSalario.setText(puestoEditar.getRangoSalarios());
            for (Departamento d : departamentos) {
                if (d.getId() == puestoEditar.getIdDepartamento()) {
                    comboDepartamento.getSelectionModel().select(d);
                    break;
                }
            }
        } else {
            mensajeAccion.setText("Crear Nuevo Puesto");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
