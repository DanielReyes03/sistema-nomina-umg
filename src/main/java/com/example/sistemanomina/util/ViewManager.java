package com.example.sistemanomina.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import com.example.sistemanomina.controller.LayoutController;
import java.io.IOException;

public class ViewManager {
    private static ViewManager instance;
    private StackPane mainContent;
    private LayoutController layoutController; // NUEVO

    private ViewManager() {}

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    public void setLayoutController(LayoutController controller) {
        this.layoutController = controller;
    }

    public void showView(String fxmlPath, String title) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().setAll(view);
            if (layoutController != null) {
                layoutController.setViewTitle(title);
                layoutController.showWelcome(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Para mostrar solo el mensaje de bienvenida
    public void showWelcome() {
        if (layoutController != null) {
            layoutController.setViewTitle("Sistema de nominas");
            layoutController.showWelcome(true);
            mainContent.getChildren().clear();
        }
    }
}
