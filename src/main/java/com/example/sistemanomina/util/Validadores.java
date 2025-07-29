// src/main/java/com/example/sistemanomina/util/Validadores.java
package com.example.sistemanomina.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;

public class Validadores {

    public static boolean validarRequerido(TextInputControl input, String nombreCampo) {
        if (input.getText() == null || input.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El campo '" + nombreCampo + "' es requerido.");
            input.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validarNoVacio(TextInputControl input, String nombreCampo) {
        return validarRequerido(input, nombreCampo);
    }

    public static boolean validarLongitud(TextInputControl input, String nombreCampo, int min, int max) {
        String texto = input.getText() == null ? "" : input.getText().trim();
        if (texto.length() < min || texto.length() > max) {
            mostrarAlerta("Validación", "El campo '" + nombreCampo + "' debe tener entre " + min + " y " + max + " caracteres.");
            input.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validarNumerico(TextInputControl input, String nombreCampo) {
        String texto = input.getText() == null ? "" : input.getText().trim();
        if (!texto.matches("\\d+")) {
            mostrarAlerta("Validación", "El campo '" + nombreCampo + "' debe contener solo números.");
            input.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validarEmail(TextInputControl input, String nombreCampo) {
        String texto = input.getText() == null ? "" : input.getText().trim();
        if (!texto.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            mostrarAlerta("Validación", "El campo '" + nombreCampo + "' debe ser un email válido.");
            input.requestFocus();
            return false;
        }
        return true;
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
