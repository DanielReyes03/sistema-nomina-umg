// src/main/java/com/example/sistemanomina/controller/CrearMovimientoNominaController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.ConceptoNominaDAO;
import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.dao.MovimientoNominaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.ConceptoNomina;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.model.MovimientoNomina;
import com.example.sistemanomina.model.Nomina;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class CrearMovimientoNominaController {

    @FXML
    private DatePicker dpPeriodoInicio;
    @FXML
    private DatePicker dpPeriodoFin;
    @FXML
    private ComboBox<Empleado> comboEmpleado;
    @FXML
    private ComboBox<ConceptoNomina> comboConceptoNomina;
    @FXML
    private TextField txtMonto;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Text mensajeAccion;

    private MovimientoNominaDAO movimientoNominaDAO;
    private EmpleadoDAO empleadosDAO;
    private ConceptoNominaDAO conceptosNominaDAO;

    private Nomina nomina;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            movimientoNominaDAO = new MovimientoNominaDAO(conn);
            empleadosDAO = new EmpleadoDAO(conn);
            conceptosNominaDAO = new ConceptoNominaDAO(conn);

            // Llenar combo de empleados
            List<Empleado> empleados = empleadosDAO.obtenerEmpleados();
            ObservableList<Empleado> empleadosObs = FXCollections.observableArrayList(empleados);
            comboEmpleado.setItems(empleadosObs);

            // Llenar combo de conceptos de nómina
            List<ConceptoNomina> conceptos = conceptosNominaDAO.obtenerConceptosNoAutomaticos();
            ObservableList<ConceptoNomina> conceptosObs = FXCollections.observableArrayList(conceptos);
            comboConceptoNomina.setItems(conceptosObs);

            // Mostrar nombre en vez de toString si es necesario
            comboEmpleado.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Empleado item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.toString());
                }
            });
            comboEmpleado.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Empleado item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.toString());
                }
            });

            comboConceptoNomina.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(ConceptoNomina item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });
            comboConceptoNomina.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(ConceptoNomina item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
            btnGuardar.setDisable(true);
        }
    }
    public void setNomina(Nomina nomina) {
        this.nomina = nomina;
        if (nomina != null) {
            dpPeriodoInicio.setValue(nomina.getPeriodoInicio());
            dpPeriodoFin.setValue(nomina.getPeriodoFin());
            dpPeriodoInicio.setDisable(true);
            dpPeriodoFin.setDisable(true);
        }
    }

    @FXML
    private void onBtnGuardar() {
        Empleado empleado = comboEmpleado.getValue();
        ConceptoNomina concepto = comboConceptoNomina.getValue();
        String montoStr = txtMonto.getText();

        if (empleado == null || concepto == null || montoStr == null || montoStr.isEmpty() || nomina == null) {
            mostrarAlerta("Campos requeridos", "Todos los campos son obligatorios.");
            return;
        }

        BigDecimal monto;
        try {
            monto = new BigDecimal(montoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Monto inválido", "Ingrese un monto válido.");
            return;
        }

        try {
            MovimientoNomina movimiento = new MovimientoNomina(
                    nomina.getId(),
                    empleado.getId(),
                    concepto.getId(),
                    monto,
                    nomina.getPeriodoInicio(),
                    nomina.getPeriodoFin()
            );
            movimientoNominaDAO.insertar(movimiento);
            mostrarAlerta("Éxito", "Movimiento de nómina agregado correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el movimiento de nómina.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtnRegresar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
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
