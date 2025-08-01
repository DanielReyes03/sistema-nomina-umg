package com.example.sistemanomina.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import com.example.sistemanomina.util.ViewManager;

public class LayoutController {
    @FXML
    private StackPane mainContent;
    @FXML
    private Text welcomeText;
    @FXML
    private Text viewTitle;

    @FXML
    public void initialize() {
        ViewManager.getInstance().setMainContent(mainContent);
        ViewManager.getInstance().setLayoutController(this); // NUEVO
    }

    public void setViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void showWelcome(boolean show) {
        welcomeText.setVisible(show);
    }
}
