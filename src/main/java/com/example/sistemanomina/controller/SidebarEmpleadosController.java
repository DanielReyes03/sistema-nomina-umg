package com.example.sistemanomina.controller;

import com.example.sistemanomina.util.ViewManager;
import com.example.sistemanomina.util.ViewManagerEmpleados;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class SidebarEmpleadosController {

    @FXML
    public void abrirMenuRecibos() {
        abrirVentana("/com/example/sistemanomina/recibos-empleados.fxml", "Usuarios");
    }

    @FXML
    public void abrirMenuArchivos() {
        abrirVentana("/com/example/sistemanomina/archivos-empleado.fxml", "Empleados");
    }


    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            ViewManagerEmpleados.getInstance().showView(fxmlPath, titulo);
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
