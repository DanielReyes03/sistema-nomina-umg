package com.example.sistemanomina.controller;

import com.example.sistemanomina.util.PasswordUtil;
import com.example.sistemanomina.dao.UsuarioDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class ActualizarUsuarioController {

    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtContra;
    @FXML private ComboBox<String> comboRol;
    @FXML private TextField txtEstado;
    private Usuario usuarioSeleccionado;
    private UsuarioDAO usuarioDAO;

    public void setUsuario(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
        cargarDatos();
    }

    private void cargarDatos() {
        txtNombreUsuario.setText(usuarioSeleccionado.getUsername());
        txtContra.setText(usuarioSeleccionado.getPasswordHash());
        comboRol.setValue(usuarioSeleccionado.getRol());
        txtEstado.setText(usuarioSeleccionado.getEstado() ? "1" : "0");
    }

    @FXML
    public void initialize() {
        comboRol.getItems().addAll("ADMIN", "EMPLEADO", "GERENTE", "CONTADOR");
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            usuarioDAO = new UsuarioDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onbtnguardare() {
        try {
            String nuevoUsername = txtNombreUsuario.getText();
            String nuevaContra = txtContra.getText();
            String nuevoRol = comboRol.getValue();
            Boolean nuevoEstado = "1".equals(txtEstado.getText());

            usuarioSeleccionado.setUsername(nuevoUsername);
            usuarioSeleccionado.setPasswordHash(PasswordUtil.hashPassword(nuevaContra));
            usuarioSeleccionado.setRol(nuevoRol);
            usuarioSeleccionado.setEstado(nuevoEstado);

            usuarioDAO.actualizarUsuario(usuarioSeleccionado);

            mostrarAlertaInformacion("Éxito", "Usuario actualizado correctamente.");

            // 🔒 Cierra la ventana actual
            Stage stage = (Stage) txtNombreUsuario.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error", "No se pudo actualizar el usuario");
        }
    }




    @FXML
    private void onbtnregresar() {
        volverAVistaUsuarios();
    }

    private void volverAVistaUsuarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/view/usuarios.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtNombreUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la vista de usuarios.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Asegúrate de que sea INFORMATION
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
