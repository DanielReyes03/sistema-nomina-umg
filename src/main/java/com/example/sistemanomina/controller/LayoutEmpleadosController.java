package com.example.sistemanomina.controller;

import com.example.sistemanomina.util.ViewManagerEmpleados;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LayoutEmpleadosController {
    @FXML
    private StackPane mainContent;
    @FXML
    private Text welcomeText;
    @FXML
    private Text viewTitle;

    @FXML
    public void initialize() {
        ViewManagerEmpleados.getInstance().setMainContent(mainContent);
        ViewManagerEmpleados.getInstance().setLayoutController(this); // NUEVO
    }

    public void setViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void showWelcome(boolean show) {
        welcomeText.setVisible(show);
    }
}
