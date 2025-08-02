package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.UsuarioDAO;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class UsuarioController {

    @FXML private TableView<Usuario> tblUsuarios;

    @FXML private TableColumn<Usuario, Integer> colID;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Integer> colEmpleadoID;
    @FXML private TableColumn<Usuario, Boolean> colEstado;

    @FXML private Button btncrear;
    @FXML private Button btnactualizar;
    @FXML private Button btneliminar;
    @FXML private Button btnActualizarTb;

    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            usuarioDAO = new UsuarioDAO(conn);
            configurarColumnas();
            cargarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColumnas() {
        colID.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colUsuario.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsername()));
        colRol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRol()));
        colEmpleadoID.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getEmpleadoId()));
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getEstado()));
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            listaUsuarios = FXCollections.observableArrayList(usuarios);
            tblUsuarios.setItems(listaUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onbtncrear() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-usuario.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Crear nuevo usuario");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar vista", "No se pudo cargar la vista para crear usuario.");
        }
    }

    @FXML
    private void onbtnactualizar() {
        Usuario seleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/actualizar-usuario.fxml"));
                Parent root = loader.load();

                ActualizarUsuarioController controller = loader.getController();
                controller.setUsuario(seleccionado);

                Stage stage = new Stage();
                stage.setTitle("Actualizar usuario");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                cargarUsuarios();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo abrir la vista de actualización.");
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor selecciona un usuario para actualizar.");
        }
    }

    @FXML
    private void onbtneliminar() {
        Usuario seleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                usuarioDAO.eliminarUsuario(seleccionado.getId());
                cargarUsuarios();  // Recarga la tabla

                mostrarAlertaInformacion("Eliminado", "El usuario ha sido eliminado correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlertaError("Error", "Ocurrió un error al eliminar el usuario.");
            }
        } else {
            mostrarAlertaError("Selección requerida", "Por favor selecciona un usuario para eliminar.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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


    @FXML
    private void onbtnActualizarTb() {
        cargarUsuarios();
    }
}
