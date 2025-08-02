package com.example.sistemanomina.util;

import com.example.sistemanomina.controller.LayoutController;
import com.example.sistemanomina.controller.LayoutEmpleadosController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ViewManagerEmpleados {
    private static ViewManagerEmpleados instance;
    private StackPane mainContent;
    private LayoutEmpleadosController layoutEmpleadosController; // NUEVO

    private ViewManagerEmpleados() {}

    public static ViewManagerEmpleados getInstance() {
        if (instance == null) {
            instance = new ViewManagerEmpleados();
        }
        return instance;
    }

    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    public void setLayoutController(LayoutEmpleadosController controller) {
        this.layoutEmpleadosController = controller;
    }

    public void showView(String fxmlPath, String title) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().setAll(view);
            if (layoutEmpleadosController != null) {
                layoutEmpleadosController.setViewTitle(title);
                layoutEmpleadosController.showWelcome(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Para mostrar solo el mensaje de bienvenida
    public void showWelcome() {
        if (layoutEmpleadosController != null) {
            layoutEmpleadosController.setViewTitle("Sistema de nominas");
            layoutEmpleadosController.showWelcome(true);
            mainContent.getChildren().clear();
        }
    }
}
