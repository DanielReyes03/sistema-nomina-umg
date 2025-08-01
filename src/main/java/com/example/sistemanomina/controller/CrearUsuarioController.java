package com.example.sistemanomina.controller;

import com.example.sistemanomina.util.PasswordUtil;
import com.example.sistemanomina.dao.UsuarioDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;

public class CrearUsuarioController {

    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtContra;
    @FXML private ComboBox<String> comboRol;
    @FXML private TextField txtNombre; // este es el campo de ID Empleado
    @FXML private Button btnguardare;
    @FXML private Button btnregresar;
    @FXML private Button btnActualizarTb;

    private UsuarioDAO usuarioDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            usuarioDAO = new UsuarioDAO(conn);

            comboRol.getItems().addAll("ADMIN", "EMPLEADO", "GERENTE", "CONTADOR");
            comboRol.setValue("EMPLEADO"); // valor por defecto
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onbtnguardare() {
        try {
            String username = txtNombreUsuario.getText().trim();
            String password = txtContra.getText().trim();
            String rol = comboRol.getValue();
            String idEmpleadoStr = txtNombre.getText().trim();

            if (username.isEmpty() || password.isEmpty() || rol == null) {
                mostrarAlerta("Faltan datos", "Todos los campos excepto ID Empleado son obligatorios.");
                return;
            }

            if (rol.equalsIgnoreCase("EMPLEADO") && idEmpleadoStr.isEmpty()) {
                mostrarAlerta("Faltan datos", "Para el rol EMPLEADO, debes ingresar un ID de empleado.");
                return;
            }

            if (usuarioDAO.buscarPorUsername(username) != null) {
                mostrarAlerta("Usuario duplicado", "El nombre de usuario ya existe.");
                return;
            }

            // Hash de la contraseña antes de crear el usuario
            String passwordHasheada = PasswordUtil.hashPassword(password);
            Integer idEmpleado = idEmpleadoStr.isEmpty() ? null : Integer.parseInt(idEmpleadoStr);

            Usuario nuevo = new Usuario(username, passwordHasheada, rol, idEmpleado, true);
            usuarioDAO.insertarUsuario(nuevo);

            mostrarAlerta("Éxito", "Usuario creado exitosamente.");
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de empleado debe ser numérico.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al crear el usuario.");
        }
    }


    @FXML
    private void onbtnregresar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnregresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
