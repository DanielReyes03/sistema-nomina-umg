package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AsistenciaDAO;
import com.example.sistemanomina.model.Asistencia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CrearAsistenciaController {
    @FXML private ComboBox<Integer> comboempleados;
    @FXML private DatePicker inputfecha;
    @FXML private ComboBox<String> comboHoraEntradaHora;
    @FXML private ComboBox<String> comboHoraEntradaMinuto;
    @FXML private ComboBox<String> comboHoraSalidaHora;
    @FXML private ComboBox<String> comboHoraSalidaMinuto;
    @FXML private Button inputguardadasistencia;
    @FXML private Button inputverasistencia;

    private AsistenciaDAO asistenciaDAO;

    @FXML
    public void initialize() {
        asistenciaDAO = new AsistenciaDAO();

        ObservableList<Integer> empleados = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        comboempleados.setItems(empleados);

        initializeTimePickers();

        inputguardadasistencia.setOnAction(event -> guardarAsistencia());
        inputverasistencia.setOnAction(event -> verAsistencias());
    }

    private void initializeTimePickers() {
        ObservableList<String> horas = FXCollections.observableArrayList();
        ObservableList<String> minutos = FXCollections.observableArrayList();

        for (int i = 0; i < 24; i++) {
            horas.add(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i += 5) {
            minutos.add(String.format("%02d", i));
        }

        comboHoraEntradaHora.setItems(horas);
        comboHoraEntradaMinuto.setItems(minutos);
        comboHoraSalidaHora.setItems(horas);
        comboHoraSalidaMinuto.setItems(minutos);
    }

    private void guardarAsistencia() {
        try {
            if (comboempleados.getValue() == null || inputfecha.getValue() == null ||
                    comboHoraEntradaHora.getValue() == null || comboHoraEntradaMinuto.getValue() == null ||
                    comboHoraSalidaHora.getValue() == null || comboHoraSalidaMinuto.getValue() == null) {
                showAlert("Error", "Por favor, complete todos los campos.");
                return;
            }

            String horaEntradaStr = comboHoraEntradaHora.getValue() + ":" + comboHoraEntradaMinuto.getValue();
            String horaSalidaStr = comboHoraSalidaHora.getValue() + ":" + comboHoraSalidaMinuto.getValue();

            if (!horaValida(horaEntradaStr) || !horaValida(horaSalidaStr)) {
                showAlert("Error", "Formato de hora inválido.");
                return;
            }

            int empleadoId = comboempleados.getValue();
            LocalDate fecha = inputfecha.getValue();
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaSalida = LocalTime.parse(horaSalidaStr);

            Asistencia asistencia = new Asistencia(empleadoId, fecha, horaEntrada, horaSalida);
            asistenciaDAO.insertarAsistencia(asistencia);

            showAlert("Éxito", "Asistencia guardada correctamente.");
            clearFields();

        } catch (Exception e) {
            showAlert("Error", "Error al guardar la asistencia: " + e.getMessage());
        }
    }

    private void verAsistencias() {
        try {
            Integer empleadoId = comboempleados.getValue();
            ObservableList<Asistencia> asistencias;

            if (empleadoId != null) {
                asistencias = FXCollections.observableArrayList(asistenciaDAO.buscarAsistenciaPorEmpleadoId(empleadoId));
            } else {
                asistencias = FXCollections.observableArrayList(asistenciaDAO.obtenerTodasAsistencias());
            }

            showAlert("Información", "Se han encontrado " + asistencias.size() + " registros de asistencia.");

        } catch (Exception e) {
            showAlert("Error", "Error al obtener las asistencias: " + e.getMessage());
        }
    }

    private boolean horaValida(String horaTexto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(horaTexto, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        comboempleados.setValue(null);
        inputfecha.setValue(null);
        comboHoraEntradaHora.setValue(null);
        comboHoraEntradaMinuto.setValue(null);
        comboHoraSalidaHora.setValue(null);
        comboHoraSalidaMinuto.setValue(null);
    }
}
