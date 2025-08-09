package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.example.sistemanomina.util.ViewManager;

public class SidebarController {

    @FXML
    public void abrirMenuUsuarios() {
        abrirVentana("/com/example/sistemanomina/usuarios.fxml", "Usuarios");
    }


    @FXML
    public void abrirMenuEmpleados() {
        abrirVentana("/com/example/sistemanomina/empleado.fxml", "Empleados");
    }

    @FXML
    public void abrirMenuAsistencia() {
        abrirVentana("/com/example/sistemanomina/asistencia.fxml", "Asistencia");
    }

    @FXML
    public void abrirMenuVacaciones() {
        abrirVentana("/com/example/sistemanomina/vacaciones.fxml", "Vacaciones");
    }

    @FXML
    public void abrirMenuHorasExtra() {
        abrirVentana("/com/example/sistemanomina/horas-extra.fxml", "Horas Extra");
    }

    @FXML
    public void abrirMenuAnticipos() {
        abrirVentana("/com/example/sistemanomina/anticipos.fxml", "Anticipos");
    }

    @FXML
    public void abrirMenuPlanilla() {
        abrirVentana("/com/example/sistemanomina/planilla.fxml", "Planilla");
    }

    @FXML
    public void abrirMenuReporteria() {
        abrirVentana("/com/example/sistemanomina/reporteria.fxml", "Reporteria");
    }

    @FXML
    public void abrirMenuDepartamentos() {
        abrirVentana("/com/example/sistemanomina/departamento.fxml", "Departamentos");
    }

    @FXML
    public void abrirMenuPuestos() {
        abrirVentana("/com/example/sistemanomina/puestos.fxml", "Puestos");
    }

    @FXML
    public void abrirMenuNomina() {
        abrirVentana("/com/example/sistemanomina/nomina.fxml", "Nomina");
    }

    @FXML
    public void abrirMenuConceptoNomina() {
        abrirVentana("/com/example/sistemanomina/concepto-nomina.fxml", "Conceptos Nomina");
    }

    @FXML
    public void abrirMenuayuda() {
        abrirVentana("/com/example/sistemanomina/ayuda.fxml", "Conceptos Nomina");
    }


    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            ViewManager.getInstance().showView(fxmlPath, titulo);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana: " + titulo);
            e.printStackTrace();
        }
    }

    @FXML
    private void salirMenu(){
        try {
            ViewManager.getInstance().showWelcome();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de bienvenida.");
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
