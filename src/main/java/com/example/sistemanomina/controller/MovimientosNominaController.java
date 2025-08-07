// MovimientosNominaController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.MovimientoNominaDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.MovimientoNomina;
import com.example.sistemanomina.model.Nomina;
import com.example.sistemanomina.util.Alertas;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class MovimientosNominaController {

    @FXML
    private TableView<MovimientoNomina> tblDetalleNomina;
    @FXML
    private TableColumn<MovimientoNomina, Integer> colID;
    @FXML
    private TableColumn<MovimientoNomina, Integer> colIdNomina;
    @FXML
    private TableColumn<MovimientoNomina, Integer> colIdEmpleado;
    @FXML
    private TableColumn<MovimientoNomina, String> colNombreEmpleado;
    @FXML
    private TableColumn<MovimientoNomina, LocalDate> colPeriodoInicio;
    @FXML
    private TableColumn<MovimientoNomina, LocalDate> colPeriodoFin;
    @FXML
    private TableColumn<MovimientoNomina, String> colNombreConcepto;
    @FXML
    private TableColumn<MovimientoNomina, BigDecimal> colMonto;
    @FXML
    private Button btnGenerarRecibo;

    private MovimientoNominaDAO movimientoNominaDAO;
    private Nomina nominaSeleccionada;

    private ObservableList<MovimientoNomina> movimientosList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            movimientoNominaDAO = new MovimientoNominaDAO(conn);
            // Configurar columnas
            colID.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
            colIdNomina.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNominaId()));
            colIdEmpleado.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEmpleadoId()));
            colNombreEmpleado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreEmpleado()));
            colPeriodoInicio.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPeriodoInicio()));
            colPeriodoFin.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPeriodoFin()));
            colNombreConcepto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreConcepto()));
            colMonto.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMonto()));

            tblDetalleNomina.setItems(movimientosList);

            // Por defecto, deshabilitar el botón
            btnGenerarRecibo.setDisable(true);
        }catch (Exception e){
            Alertas.mostrarError("Error al inicializar", "No se pudo establecer la conexión con la base de datos:\n" + e.getMessage());
        }
    }

    /**
     * Método para que otro controlador le pase la nómina seleccionada.
     */
    public void setNomina(Nomina nomina) {
        this.nominaSeleccionada = nomina;
        cargarMovimientos();
        // Habilitar el botón solo si el estado es "GENERADO"
        if (nomina != null && "GENERADO".equalsIgnoreCase(nomina.getEstado())) {
            btnGenerarRecibo.setDisable(false);
        } else {
            btnGenerarRecibo.setDisable(true);
        }
    }

    private void cargarMovimientos() {
        movimientosList.clear();
        if (nominaSeleccionada != null) {
            try {
                List<MovimientoNomina> movimientos = movimientoNominaDAO.obtenerPorNominaId(nominaSeleccionada.getId());
                movimientosList.addAll(movimientos);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudieron cargar los movimientos de la nómina:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void onGenerarRecibo() {
        MovimientoNomina seleccionado = tblDetalleNomina.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un movimiento para imprimir el recibo.");
            return;
        }
        // Aquí va la lógica para imprimir el recibo
        mostrarAlerta("Recibo", "Funcionalidad de impresión de recibo para el movimiento ID: " + seleccionado.getId());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Getter para la nómina seleccionada (opcional, si lo necesitas)
    public Nomina getNominaSeleccionada() {
        return nominaSeleccionada;
    }
}
