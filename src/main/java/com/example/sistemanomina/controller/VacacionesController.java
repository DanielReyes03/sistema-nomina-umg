package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.VacacionesDAO;
import com.example.sistemanomina.model.Vacaciones;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class VacacionesController {

    @FXML
    private TableView<Vacaciones> InputTablaVacaciones;
    @FXML
    private TableColumn<Vacaciones, Integer> ColID;
    @FXML
    private TableColumn<Vacaciones, Integer> ColCodigo;
    @FXML
    private TableColumn<Vacaciones, String> ColNombre; // Aquí necesitarás unir con empleados si quieres mostrar nombres
    @FXML
    private TableColumn<Vacaciones, Date> ColFechaInicio;
    @FXML
    private TableColumn<Vacaciones, Date> ColFechaFin;
    @FXML
    private TableColumn<Vacaciones, Integer> ColDias;
    @FXML
    private TableColumn<Vacaciones, Boolean> ColAprobada;

    private final VacacionesDAO vacacionesDAO = new VacacionesDAO();
    private final ObservableList<Vacaciones> listaVacaciones = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar las columnas
        ColID.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        ColCodigo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEmpleadoId()).asObject());
        ColFechaInicio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFechaInicio()));
        ColFechaFin.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFechaFin()));
        ColDias.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getDias()).asObject());
        ColAprobada.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isAprobada()).asObject());

        // Por ahora, ColNombre se deja vacío (requeriría JOIN con tabla empleados)
        ColNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("Empleado " + cellData.getValue().getEmpleadoId()));

        InputTablaVacaciones.setItems(listaVacaciones);

        cargarVacaciones();
    }

    private void cargarVacaciones() {
        listaVacaciones.clear();
        try {
            List<Vacaciones> datos = vacacionesDAO.listarTodos();
            listaVacaciones.addAll(datos);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar datos", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void botcrear() {
        try {
            Vacaciones nueva = new Vacaciones();
            nueva.setEmpleadoId(1); // Aquí podrías abrir un formulario para capturar datos
            nueva.setFechaInicio(Date.valueOf("2025-01-01"));
            nueva.setFechaFin(Date.valueOf("2025-01-10"));
            nueva.setDias(10);
            nueva.setAprobada(false);

            vacacionesDAO.insertar(nueva);
            listaVacaciones.add(nueva);
            mostrarAlerta("Éxito", "Vacaciones creadas correctamente", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            mostrarAlerta("Error al crear vacaciones", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void botactualizar() {
        Vacaciones seleccionada = InputTablaVacaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona un registro para actualizar", Alert.AlertType.WARNING);
            return;
        }

        try {
            seleccionada.setAprobada(!seleccionada.isAprobada()); // Ejemplo: cambiar aprobado
            vacacionesDAO.actualizar(seleccionada);
            InputTablaVacaciones.refresh();
            mostrarAlerta("Éxito", "Registro actualizado correctamente", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            mostrarAlerta("Error al actualizar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void boteliminar() {
        Vacaciones seleccionada = InputTablaVacaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona un registro para eliminar", Alert.AlertType.WARNING);
            return;
        }

        try {
            vacacionesDAO.eliminar(seleccionada.getId());
            listaVacaciones.remove(seleccionada);
            mostrarAlerta("Éxito", "Registro eliminado correctamente", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            mostrarAlerta("Error al eliminar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
