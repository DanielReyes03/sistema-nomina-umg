// src/main/java/com/example/sistemanomina/controller/LoginController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import com.example.sistemanomina.dao.UsuarioDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Usuario;
import com.example.sistemanomina.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

public class LoginController {

    @FXML
    private TextField loginUsuario;

    @FXML
    private PasswordField loginContrasena;

    @FXML
    private Button loginBoton;

    private UsuarioDAO usuarioDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            usuarioDAO = new UsuarioDAO(conn);

            // Si no hay usuarios, permite login solo con admin/admin
            loginBoton.setOnAction(event -> handleLogin());
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
        }
    }

    private void handleLogin() {
        String usuario = loginUsuario.getText();
        String contrasena = loginContrasena.getText();

        try {
            int totalUsuarios = usuarioDAO.contarUsuarios();

            // 1. Primer ingreso: no hay usuarios, solo permite admin/admin
            if (totalUsuarios == 0) {
                if ("admin".equals(usuario) && "admin".equals(contrasena)) {
                    mostrarCrearAdmin();
                    return;
                } else {
                    mostrarAlerta("Primer ingreso", "Debes ingresar con usuario y contraseña 'admin' para crear tu usuario administrador.");
                    return;
                }
            }

            // 2. Login normal
            Usuario user = usuarioDAO.buscarPorUsername(usuario);
            if (user == null) {
                mostrarAlerta("Error", "Usuario no encontrado.");
                return;
            }
            if (!user.getPasswordHash().equals(PasswordUtil.hashPassword(contrasena))) {
                mostrarAlerta("Error", "Contraseña incorrecta.");
                return;
            }
            if (!user.getEstado()) {
                mostrarAlerta("Usuario inactivo", "Este usuario está inactivo. Contacta al administrador.");
                return;
            }

            // 3. Redirección por rol
            Stage stage = (Stage) loginBoton.getScene().getWindow();
            if ("ADMIN".equalsIgnoreCase(user.getRol())) {
                cargarVentana("menu-admin.fxml", "Menú Administrador", stage);
            } else {
                cargarVentana("menu-usuario.fxml", "Menú Usuario", stage);
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al iniciar sesión.");
            e.printStackTrace();
        }
    }

    private void mostrarCrearAdmin() {
        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("crear-primer-admin.fxml"));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("crear-primer-admin.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Crear Administrador");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            // Al cerrar, limpiar campos para que vuelva al login
            loginUsuario.clear();
            loginContrasena.clear();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo mostrar la ventana para crear administrador.");
            e.printStackTrace();
        }
    }

    private void cargarVentana(String fxml, String titulo, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setTitle(titulo);
            stage.setScene(scene);
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
