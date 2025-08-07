package com.example.sistemanomina.controller;

import com.example.sistemanomina.model.Asistencia;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VerAsistenciaController {

    @FXML private TableView<Asistencia> tablaAsistencias;
    @FXML private TableColumn<Asistencia, Integer> colId;
    @FXML private TableColumn<Asistencia, Integer> colEmpleadoId;
    @FXML private TableColumn<Asistencia, String> colFecha;
    @FXML private TableColumn<Asistencia, String> colEntrada;
    @FXML private TableColumn<Asistencia, String> colSalida;

    public void setAsistencias(ObservableList<Asistencia> asistencias) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<>("empleadoId"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));

        tablaAsistencias.setItems(asistencias);
    }
}
