package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.ConceptoNominaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.ConceptoNomina;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;

public class CrearConceptoNominaController {

    @FXML
    private TextField inputNombre;
    @FXML
    private TextArea inputDescripcion;
    @FXML
    private ComboBox<String> comboTipo;
    @FXML
    private ComboBox<String> comboTipoCalculo;
    @FXML
    private TextField inputValor;
    @FXML
    private CheckBox checkboxAplicacionAutomatica;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Text mensajeAccion;

    private ConceptoNomina conceptoEditar;
    private ConceptoNominaController conceptoNominaControllerPadre;
    private ConceptoNominaDAO conceptoNominaDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            conceptoNominaDAO = new ConceptoNominaDAO(conn);

            comboTipo.setItems(FXCollections.observableArrayList("PERCEPCION", "DEDUCCION"));
            comboTipoCalculo.setItems(FXCollections.observableArrayList("FIJO", "PORCENTAJE", "MULTIPLICACION", "DIVISION"));

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la información.");
        }
    }

    @FXML
    private void onBtnGuardar() {
        String nombre = inputNombre.getText().trim();
        String descripcion = inputDescripcion.getText().trim();
        String tipo = comboTipo.getValue();
        String tipoCalculo = comboTipoCalculo.getValue();
        String valorStr = inputValor.getText().trim();
        boolean aplicaAutomatico = checkboxAplicacionAutomatica.isSelected();

        if (nombre.isEmpty() || tipo == null || tipoCalculo == null || valorStr.isEmpty()) {
            mostrarAlerta("Campos requeridos", "Nombre, Tipo, Tipo de Calculo y Valor son obligatorios.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Valor inválido", "El valor debe ser un número.");
            return;
        }

        try {
            if (conceptoEditar == null) {
                // Crear nuevo concepto
                ConceptoNomina nuevo = new ConceptoNomina(nombre, descripcion, tipo, tipoCalculo, valor, aplicaAutomatico);
                conceptoNominaDAO.insertar(nuevo);
                mostrarAlerta("Éxito", "Concepto creado correctamente.");
            } else {
                // Editar concepto existente
                conceptoEditar.setNombre(nombre);
                conceptoEditar.setDescripcion(descripcion);
                conceptoEditar.setTipo(tipo);
                conceptoEditar.setTipoCalculo(tipoCalculo);
                conceptoEditar.setValor(valor);
                conceptoEditar.setAplicaAutomatico(aplicaAutomatico);
                conceptoNominaDAO.actualizar(conceptoEditar);
                mostrarAlerta("Éxito", "Concepto actualizado correctamente.");
            }
            if (conceptoNominaControllerPadre != null) {
                conceptoNominaControllerPadre.actualizarTabla();
            }
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el concepto.");
        }
    }

    @FXML
    private void onBtnRegresar() {
        cerrarVentana();
    }

    public void setConceptoNominaControllerPadre(ConceptoNominaController controller) {
        this.conceptoNominaControllerPadre = controller;
    }

    public void setConceptoEditar(ConceptoNomina concepto) {
        this.conceptoEditar = concepto;
        if (conceptoEditar != null) {
            mensajeAccion.setText("Editar Concepto De Nomina");
            inputNombre.setText(conceptoEditar.getNombre());
            inputDescripcion.setText(conceptoEditar.getDescripcion());
            comboTipo.setValue(conceptoEditar.getTipo());
            comboTipoCalculo.setValue(conceptoEditar.getTipoCalculo());
            inputValor.setText(String.valueOf(conceptoEditar.getValor()));
            checkboxAplicacionAutomatica.setSelected(conceptoEditar.getAplicaAutomatico());
        } else {
            mensajeAccion.setText("Crear Nuevo Concepto De Nomina");
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
