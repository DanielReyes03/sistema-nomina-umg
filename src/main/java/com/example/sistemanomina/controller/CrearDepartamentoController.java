// src/main/java/com/example/sistemanomina/controller/CrearDepartamentoController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.DepartamentoDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Departamento;
import com.example.sistemanomina.util.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;

public class CrearDepartamentoController {

    @FXML
    private TextField inputNombre;
    @FXML
    private TextArea inputDescripcion;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Text mensajeAccion;
    private DepartamentoController departamentoControllerPadre;
    private DepartamentoDAO departamentoDAO;
    private Departamento departamentoEditado = null;
    private boolean esEdicion = false;
    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            departamentoDAO = new DepartamentoDAO(conn);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
            btnGuardar.setDisable(true);
        }

        btnGuardar.setOnAction(event -> guardarDepartamento());
        btnRegresar.setOnAction(event -> cerrarVentana());
    }

    public void setDepartamentoControllerPadre(DepartamentoController controller) {
        this.departamentoControllerPadre = controller;
    }

    public void setDepartamentoEditar(Departamento departamento) {
        if (departamento != null) {
            this.departamentoEditado = departamento;
            inputNombre.setText(departamento.getNombre());
            inputDescripcion.setText(departamento.getDescripcion());
            mensajeAccion.setText("Editar Departamento");
            btnGuardar.setText("Actualizar");
            esEdicion = true;
        } else {
            mensajeAccion.setText("Crear Nuevo Departamento");
            btnGuardar.setText("Guardar");
            esEdicion = false;
        }
    }

    private void guardarDepartamento() {
        String nombre = inputNombre.getText().trim();
        String descripcion = inputDescripcion.getText().trim();
        Validadores.validarRequerido(inputNombre, "Nombre");
        Validadores.validarRequerido(inputDescripcion, "Descripción");
        try {
            if (departamentoEditado == null || !esEdicion) {
                validarDepartamentoExistente(nombre);
                Departamento nuevo = new Departamento(nombre, descripcion);
                departamentoDAO.insertar(nuevo);
                mostrarAlerta("Éxito", "Departamento creado correctamente.");
            } else {
                String nombreAnterior = departamentoEditado.getNombre();
                if(!nombreAnterior.equals(nombre)) {
                    validarDepartamentoExistente(nombre);
                    departamentoEditado.setNombre(nombre);
                }
                departamentoEditado.setDescripcion(descripcion);
                departamentoDAO.actualizar(departamentoEditado);
                mostrarAlerta("Éxito", "Departamento actualizado correctamente.");
            }
            if (departamentoControllerPadre != null) {
                departamentoControllerPadre.actualizarTabla();
            }

            limpiarCampos();
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el departamento: " + e.getMessage());
        }
    }

    private void validarDepartamentoExistente(String nombre) {
        try {
            if (departamentoDAO.departamentoExiste(nombre)) {
                mostrarAlerta("Validación", "Ya existe un departamento con el nombre '" + nombre + "'.");
                inputNombre.requestFocus();
            }
        }catch (Exception e) {
            mostrarAlerta("Error", "No se pudo validar el departamento: " + e.getMessage());
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

    public void limpiarCampos() {
        inputNombre.clear();
        inputDescripcion.clear();
        mensajeAccion.setText("Crear Nuevo Departamento");
        btnGuardar.setText("Guardar");
        departamentoEditado = null;
        esEdicion = false;
    }
}
