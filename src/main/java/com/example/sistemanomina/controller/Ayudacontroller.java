package com.example.sistemanomina.controller;

import javafx.fxml.FXML;
import java.awt.Desktop;
import java.net.URI;

public class Ayudacontroller {

    @FXML
    public void abrirGoogle() {
        try {
            Desktop.getDesktop().browse(new URI("https://drive.google.com/file/d/1yf5soZl0zhvitl3xvDPr6y8UyuFDeun1/view?usp=sharing"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
