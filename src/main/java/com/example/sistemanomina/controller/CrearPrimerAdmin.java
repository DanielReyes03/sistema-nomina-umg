// src/main/java/com/example/sistemanomina/controller/CrearPrimerAdmin.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.UsuarioDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Usuario;
import com.example.sistemanomina.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

public class CrearPrimerAdmin {

    @FXML
    private TextField nuevoUsuario;

    @FXML
    private PasswordField nuevaContrasena;

    @FXML
    private Button loginBoton;

    private UsuarioDAO usuarioDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            usuarioDAO = new UsuarioDAO(conn);

            loginBoton.setOnAction(event -> handleCrearAdmin());
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
        }
    }

    private void handleCrearAdmin() {
        String username = nuevoUsuario.getText();
        String password = nuevaContrasena.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Completa todos los campos.");
            return;
        }

        try {
            // Crea el usuario administrador con estado ACTIVO
            Usuario nuevoAdmin = new Usuario(username, PasswordUtil.hashPassword(password), "ADMIN", null, true);
            usuarioDAO.insertarUsuario(nuevoAdmin);
            mostrarAlerta("Éxito", "Administrador creado. Ahora puedes iniciar sesión.");
            // Cierra la ventana
            Stage stage = (Stage) loginBoton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo crear el administrador.");
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
