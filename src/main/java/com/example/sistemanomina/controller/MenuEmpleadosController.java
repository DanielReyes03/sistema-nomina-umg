// src/main/java/com/example/sistemanomina/controller/MenuAdminController.java

package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MenuEmpleadosController {

    @FXML
    public void abrirConceptoNomina() {
        abrirVentana("concepto-nomina.fxml", "Concepto Nomina");
    }

    @FXML
    public void abrirMenuUsuarios() {
        abrirVentana("usuarios.fxml", "Usuarios");
    }

    @FXML
    public void abrirMenuEmpleados() {
        abrirVentana("empleados.fxml", "Empleados");
    }

    @FXML
    public void abrirMenuAsistencia() {
        abrirVentana("asistencia.fxml", "Asistencia");
    }

    @FXML
    public void abrirMenuVacaciones() {
        abrirVentana("vacaciones.fxml", "Vacaciones");
    }

    @FXML
    public void abrirMenuHorasExtra() {
        abrirVentana("horas-extra.fxml", "Horas Extra");
    }

    @FXML
    public void abrirMenuAnticipos() {
        abrirVentana("anticipos.fxml", "Anticipos");
    }

    @FXML
    public void abrirMenuPlanilla() {
        abrirVentana("nomina.fxml", "Nomina");
    }

    @FXML
    public void abrirMenuReporteria() {
        abrirVentana("reporteria.fxml", "Reporteria");
    }

    @FXML
    public void abrirMenuDepartamentos() {
        abrirVentana("departamento.fxml", "Departamentos");
    }

    @FXML
    public void abrirMenuPuestos() {
        abrirVentana("puestos.fxml", "Puestos");
    }

    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana: " + titulo);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
