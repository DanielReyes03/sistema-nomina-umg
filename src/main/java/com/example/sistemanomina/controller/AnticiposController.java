package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.AnticiposDAO;
import com.example.sistemanomina.dao.EmpleadoDAO;
import com.example.sistemanomina.model.Anticipo;
import com.example.sistemanomina.model.Empleado;
import com.example.sistemanomina.db.DatabaseConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnticiposController {

    @FXML
    private ComboBox<Empleado> dropboxnombresemp;

    @FXML
    private TableView<Anticipo> tabladeanticipos;

    @FXML
    private TableColumn<Anticipo, String> fechaanticipo;

    @FXML
    private TableColumn<Anticipo, Double> monto;

    @FXML
    private TableColumn<Anticipo, String> motivo;

    @FXML
    private TableColumn<Anticipo, Double> saldopendiente;

    @FXML
    private Button botonconsulta;

    @FXML
    private Button botonsolicitar;

    @FXML
    private Button botonmodificar;

    @FXML
    private Button botoneliminar;

    private ObservableList<Empleado> empleados;
    private ObservableList<Anticipo> anticipos;

    private EmpleadoDAO empleadoDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            empleadoDAO = new EmpleadoDAO(conn);

            cargarEmpleados();
            configurarTabla();
            cargarTodosLosAnticipos();

            dropboxnombresemp.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    consultarAnticiposPorEmpleado();
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarEmpleados() {
        List<Empleado> lista = empleadoDAO.obtenerEmpleados();
        empleados = FXCollections.observableArrayList(lista);
        dropboxnombresemp.setItems(empleados);
    }

    private void configurarTabla() {
        fechaanticipo.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFecha().format(formatter));
        });

        monto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getMonto()));
        motivo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMotivo()));
        saldopendiente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSaldoPendiente()));
    }

    private void cargarTodosLosAnticipos() {
        try {
            List<Anticipo> lista = new AnticiposDAO(DatabaseConnection.getInstance().getConnection()).obtenerTodos();
            anticipos = FXCollections.observableArrayList(lista);
            tabladeanticipos.setItems(anticipos);
        } catch (Exception e) {
            mostrarAlerta("Error cargando anticipos: " + e.getMessage());
        }
    }

    @FXML
    private void btnconsulta() {
        cargarTodosLosAnticipos();
        dropboxnombresemp.getSelectionModel().clearSelection();
    }

    @FXML
    private void btnsolicitar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/solicitaranticipo.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Solicitar Anticipo");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (dropboxnombresemp.getValue() != null) {
                consultarAnticiposPorEmpleado();
            } else {
                cargarTodosLosAnticipos();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de solicitud: " + e.getMessage());
        }
    }

    @FXML
    private void modificar() {
        Anticipo seleccionado = tabladeanticipos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/editaranticipo.fxml"));
                Scene scene = new Scene(loader.load());

                EditarAnticipoController controller = loader.getController();
                controller.setAnticipo(seleccionado);

                Stage stage = new Stage();
                stage.setTitle("Modificar Anticipo");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                if (dropboxnombresemp.getValue() != null) {
                    consultarAnticiposPorEmpleado();
                } else {
                    cargarTodosLosAnticipos();
                }

            } catch (IOException e) {
                mostrarAlerta("Error al abrir la ventana de modificación: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Por favor selecciona un anticipo para modificar.");
        }
    }

    @FXML
    private void eliminar() {
        Anticipo seleccionado = tabladeanticipos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Deseas eliminar este anticipo?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    try {
                        AnticiposDAO dao = new AnticiposDAO(DatabaseConnection.getInstance().getConnection());
                        dao.eliminarAnticipo(seleccionado.getId());

                        if (dropboxnombresemp.getValue() != null) {
                            consultarAnticiposPorEmpleado();
                        } else {
                            cargarTodosLosAnticipos();
                        }

                    } catch (Exception e) {
                        mostrarAlerta("Error al eliminar anticipo: " + e.getMessage());
                    }
                }
            });

        } else {
            mostrarAlerta("Selecciona un anticipo para eliminar.");
        }
    }

    @FXML
    private void consultarAnticiposPorEmpleado() {
        try {
            Empleado seleccionado = dropboxnombresemp.getValue();
            if (seleccionado != null) {
                List<Anticipo> lista = new AnticiposDAO(DatabaseConnection.getInstance().getConnection()).obtenerTodos();
                anticipos = FXCollections.observableArrayList();

                for (Anticipo a : lista) {
                    if (a.getEmpleadoId() == seleccionado.getId()) {
                        anticipos.add(a);
                    }
                }

                tabladeanticipos.setItems(anticipos);
            } else {
                mostrarAlerta("Selecciona un empleado para consultar sus anticipos.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
