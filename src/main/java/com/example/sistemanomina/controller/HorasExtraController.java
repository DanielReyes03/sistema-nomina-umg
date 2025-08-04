package com.example.sistemanomina.controller;

import com.example.sistemanomina.dao.HorasExtraDAO;
import com.example.sistemanomina.model.HorasExtra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class HorasExtraController {

    @FXML
    private TableView<HorasExtra> InputTablaVacaciones;

    @FXML
    private TableColumn<HorasExtra, Integer> ColID;

    @FXML
    private TableColumn<HorasExtra, Integer> ColEmpleadoId;

    @FXML
    private TableColumn<HorasExtra, String> ColNombre;

    @FXML
    private TableColumn<HorasExtra, java.util.Date> ColFecha;

    @FXML
    private TableColumn<HorasExtra, Integer> ColHora;

    @FXML
    private TableColumn<HorasExtra, String> ColMotivo;

    @FXML
    private TableColumn<HorasExtra, Boolean> ColAprobado;

    @FXML
    private Button botNuevo;

    @FXML
    private Button botactualizar;

    @FXML
    private Button BotEliminar;



    private HorasExtraDAO horasExtraDAO;
    private ObservableList<HorasExtra> listaHorasExtra;

    private Connection conn; // ✅ SE AGREGA

    /** ✅ Método para que el código que abre esta ventana inyecte la conexión */
    public void setConnection(Connection conn) {
        this.conn = conn;
        this.horasExtraDAO = new HorasExtraDAO(conn);
        configurarColumnas();
        cargarDatosTabla();
    }
    @FXML
    public void initialize() {
        try {
            conn = com.example.sistemanomina.db.DatabaseConnection.getInstance().getConnection();
            horasExtraDAO = new HorasExtraDAO(conn);
            configurarColumnas();
            cargarDatosTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ✅ Configurar columnas */
    private void configurarColumnas() {
        ColID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColEmpleadoId.setCellValueFactory(new PropertyValueFactory<>("empleadoId"));
        ColNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        ColFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        ColHora.setCellValueFactory(new PropertyValueFactory<>("horas"));
        ColMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        ColAprobado.setCellValueFactory(new PropertyValueFactory<>("aprobado"));
    }

    /** ✅ Botón CREAR NUEVO */
    /** ✅ Botón CREAR NUEVO */
    @FXML
    private void botNuevo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-horasextra.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana CrearHorasExtra
            CrearHorasExtraController crearController = loader.getController();

            // Pasar referencia de este controlador para que se pueda actualizar la tabla después de crear
            crearController.setHorasExtraController(this);

            Stage stage = new Stage();
            stage.setTitle("Crear Horas Extra");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al abrir la ventana de creación.");
        }
    }

    public void actualizarTabla() {
        cargarDatosTabla();
    }


    /** ✅ Botón ACTUALIZAR */
    @FXML
    private void botactualizar(ActionEvent event) {
        HorasExtra seleccion = InputTablaVacaciones.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistemanomina/crear-horasextra.fxml"));
                Parent root = loader.load();

                CrearHorasExtraController crearController = loader.getController();
                crearController.setHorasExtraController(this);

                // ✅ AHORA SÍ: Pasar el registro seleccionado
                crearController.setRegistroEditar(seleccion);

                Stage stage = new Stage();
                stage.setTitle("Editar Horas Extra");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un registro para editar.");
        }
    }



    /** ✅ Botón ELIMINAR */
    @FXML
    private void boteliminar(ActionEvent event) {
        HorasExtra seleccion = InputTablaVacaciones.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            try {
                horasExtraDAO.eliminarHorasExtra(seleccion.getId());
                listaHorasExtra.remove(seleccion);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Registro eliminado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar el registro.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un registro para eliminar.");
        }
    }

    /** ✅ Método para llenar la tabla */
    private void cargarDatosTabla() {
        try {
            List<HorasExtra> lista = horasExtraDAO.listarHorasExtra();
            listaHorasExtra = FXCollections.observableArrayList(lista);
            InputTablaVacaciones.setItems(listaHorasExtra);
            InputTablaVacaciones.refresh();  // <--- Aquí
            System.out.println("Registros cargados: " + lista.size());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar datos.");
        }
    }


    /** ✅ Método para mostrar alertas */
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje, ButtonType.OK);
        alert.showAndWait();
    }
}