// src/main/java/com/example/sistemanomina/controller/NominaController.java
package com.example.sistemanomina.controller;

import com.example.sistemanomina.Main;
import com.example.sistemanomina.dao.*;
import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.*;
import com.example.sistemanomina.util.Alertas;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NominaController {

    @FXML
    private TableView<Nomina> tblNomina;
    @FXML
    private TableColumn<Nomina, Integer> colID;
    @FXML
    private TableColumn<Nomina, String> colPeriodoInicio;
    @FXML
    private TableColumn<Nomina, String> colPeriodoFin;
    @FXML
    private TableColumn<Nomina, String> colFechaGeneracion;
    @FXML
    private TableColumn<Nomina, String> colTipo;
    @FXML
    private TableColumn<Nomina, String> colEstado;

    @FXML
    private Button btnCrearNomina;
    @FXML
    private Button btnGenerarNomina;
    @FXML
    private Button btnVerDetalleNomina;
    @FXML
    private Button btnAgregarMovimiento;

    private ObservableList<Nomina> nominas;
    private NominaDAO nominaDAO;
    private Nomina nominaSeleccionada;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            nominaDAO = new NominaDAO(conn);

            nominas = FXCollections.observableArrayList(nominaDAO.obtenerTodos());

            colID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colPeriodoInicio.setCellValueFactory(cellData -> new SimpleStringProperty(
                    cellData.getValue().getPeriodoInicio() != null ? cellData.getValue().getPeriodoInicio().format(dateFormatter) : ""));
            colPeriodoFin.setCellValueFactory(cellData -> new SimpleStringProperty(
                    cellData.getValue().getPeriodoFin() != null ? cellData.getValue().getPeriodoFin().format(dateFormatter) : ""));
            colFechaGeneracion.setCellValueFactory(cellData -> new SimpleStringProperty(
                    cellData.getValue().getFechaGeneracion() != null ? cellData.getValue().getFechaGeneracion().format(dateFormatter) : ""));
            colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo()));
            colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(
                    cellData.getValue().getEstado() != null ? cellData.getValue().getEstado() : ""));

            tblNomina.setItems(nominas);

            btnVerDetalleNomina.setDisable(true);
            btnAgregarMovimiento.setDisable(true);
            btnGenerarNomina.setDisable(true);

            tblNomina.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                boolean seleccionado = newSelection != null;
                btnVerDetalleNomina.setDisable(!seleccionado);
                btnAgregarMovimiento.setDisable(!seleccionado);
                btnGenerarNomina.setDisable(!seleccionado);
                nominaSeleccionada = newSelection;
            });

        } catch (Exception e) {
            Alertas.mostrarError("Error de conexión", "No se pudo establecer la conexión con la base de datos.");
            e.printStackTrace();
        }
    }
    @FXML
    private void onCrearNomina() {
        try {
            abrirVentanaCrearNomina(null);
        } catch (Exception e) {
            Alertas.mostrarError("Error", "Oops, algo salió mal al abrir la pantalla de nómina.");
        }
    }
    @FXML
    private void onVerDetalleNomina() {
        if (nominaSeleccionada == null) {
            mostrarAlerta("Selección", "Seleccione una nómina para ver el detalle.");
            return;
        }
        try {
            abrirVentanaDetalleNomina(nominaSeleccionada);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo mostrar el detalle de la nómina.");
        }
    }
    @FXML
    private void onVerMovimientosNomina() {
        if (nominaSeleccionada == null) {
            mostrarAlerta("Selección", "Seleccione una nómina para ver el detalle.");
            return;
        }
        try {
            abrirVentanaDetalleMovimientosNomina(nominaSeleccionada);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo mostrar el detalle de la nómina.");
        }
    }
    @FXML
    private void onAgregarMovimiento() {
        if (nominaSeleccionada == null) {
            mostrarAlerta("Selección", "Seleccione una nómina para agregar movimientos.");
            return;
        }
        try {
            abrirVentanaAgregarMovimiento(nominaSeleccionada);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo agregar movimientos a la nómina.");
        }
    }
    @FXML
    private void onCalcularNomina() {
        if (nominaSeleccionada == null) {
            mostrarAlerta("Selección", "Seleccione una nómina para calcularla.");
            return;
        }
        try {
            boolean acepto = Alertas.mostrarConfirmacion("Confirmación", "¿Está seguro de calcular esta nómina? Esta acción no se puede deshacer.");
            if (acepto) {
                calcularNomina(nominaSeleccionada);
                actualizarTabla();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo agregar movimientos a la nómina.");
        }
    }
    public void actualizarTabla() {
        try {
            nominas.clear();
            nominas.addAll(nominaDAO.obtenerTodos());
            tblNomina.setItems(nominas);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar la tabla de nóminas.");
        }
    }
    private void abrirVentanaCrearNomina(Nomina nomina) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("crear-nomina.fxml"));
            Parent root = loader.load();

            CrearNominaController controller = loader.getController();
            controller.setNominaControllerPadre(this);
            controller.setNominaEditar(nomina);

            Stage stage = new Stage();
            stage.setTitle(nomina == null ? "Generar Nómina" : "Editar Nómina");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch (Exception e){
            Alertas.mostrarError("Error", "No se pudo cargar la ventana de creación de nómina.");
        }
    }
    private void abrirVentanaDetalleMovimientosNomina(Nomina nomina) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("movimientos-nomina.fxml"));
            Parent root = loader.load();

            MovimientosNominaController controller = loader.getController();
            controller.setNomina(nomina);

            Stage stage = new Stage();
            stage.setTitle("Detalle de Nómina");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de detalle.");
            e.printStackTrace();
        }
    }
    private void abrirVentanaDetalleNomina(Nomina nomina) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("detalle-nomina.fxml"));
            Parent root = loader.load();

            DetalleNominaController controller = loader.getController();
            controller.setNomina(nomina);

            Stage stage = new Stage();
            stage.setTitle("Detalle de Nómina");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de detalle.");
            e.printStackTrace();
        }
    }
    private void abrirVentanaAgregarMovimiento(Nomina nomina) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("crear-movimiento-nomina.fxml"));
            Parent root = loader.load();

            CrearMovimientoNominaController controller = loader.getController();
            controller.setNomina(nomina);

            Stage stage = new Stage();
            stage.setTitle(nomina == null ? "Agregar movimiento nomina" : "Editar Movimiento Nómina");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de movimientos de nomina.");
            e.printStackTrace();
        }
    }
    private void calcularNomina(Nomina nomina) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            EmpleadoDAO empleadoDAO = new EmpleadoDAO(conn);
            HorasExtraDAO horasExtraDAO = new HorasExtraDAO(conn);
            AnticiposDAO anticiposDAO = new AnticiposDAO(conn);
            MovimientoNominaDAO movimientoNominaDAO = new MovimientoNominaDAO(conn);
            DetalleNominaDAO detalleNominaDAO = new DetalleNominaDAO(conn);
            AsistenciaDAO asistenciaDAO = new AsistenciaDAO(conn);
            VacacionesDAO vacacionesDAO = new VacacionesDAO(conn);
            LocalDate periodoInicio = nomina.getPeriodoInicio();
            LocalDate periodoFin = nomina.getPeriodoFin();

            List<Empleado> empleados = empleadoDAO.obtenerEmpleados();

            for (Empleado empleado : empleados) {
                int empleadoId = empleado.getId();

                // 1. Días laborados (cuando tengas el DAO de asistencia)
                int diasLaborados = 0;
                // 2. Ausencias no justificadas (cuando tengas el DAO de asistencia)
                int ausencias = 0;
                // 3. Vacaciones aprobadas en el período
                List<Vacaciones> vacaciones = vacacionesDAO.obtenerVacacionesAprobadasEnPeriodo(
                        empleadoId, Date.valueOf(periodoInicio), Date.valueOf(periodoFin));

                int DIAS_DEL_PERIODO = periodoFin.getDayOfMonth() - periodoInicio.getDayOfMonth() + 1;
                diasLaborados = asistenciaDAO.contarDiasAsistidos(empleadoId, periodoInicio, periodoFin);
                int diasVacaciones = vacaciones.stream().mapToInt(Vacaciones::getDias).sum();
                ausencias = DIAS_DEL_PERIODO - diasLaborados - diasVacaciones;
                if (ausencias < 0) ausencias = 0;

                // 4. Horas extra aprobadas en el período
                List<HorasExtra> horasExtra = horasExtraDAO.obtenerHorasExtraAprobadasEnPeriodo(
                        empleadoId, Date.valueOf(periodoInicio), Date.valueOf(periodoFin));
                System.out.println(horasExtra);
                int totalHorasExtra = horasExtra.stream().mapToInt(HorasExtra::getHoras).sum();
                double valorHoraExtra = calcularValorHoraExtra(empleado.getSalario());
                double pagoHorasExtra = totalHorasExtra * valorHoraExtra;

                // 5. Anticipos en el período
                List<Anticipo> anticipos = anticiposDAO.obtenerAnticiposEnPeriodo(
                        empleadoId, Date.valueOf(periodoInicio), Date.valueOf(periodoFin));
                double totalAnticipos = anticipos.stream().mapToDouble(Anticipo::getMonto).sum();

                // 6. Movimientos de nómina (percepciones y deducciones manuales)
                double percepcionesAdicionales = movimientoNominaDAO.sumarMovimientosPorTipo(
                        nomina.getId(), empleadoId, periodoInicio, periodoFin, "PERCEPCION");
                double deduccionesAdicionales = movimientoNominaDAO.sumarMovimientosPorTipo(
                        nomina.getId(), empleadoId, periodoInicio, periodoFin, "DEDUCCION");

                // 7. Sueldo base proporcional (ajusta DIAS_DEL_PERIODO según tu lógica)
                double sueldoBase = (empleado.getSalario() / DIAS_DEL_PERIODO) * (diasLaborados + diasVacaciones);

                // 8. Deducción por ausencias
                double deduccionAusencias = (empleado.getSalario() / DIAS_DEL_PERIODO) * ausencias;

                // 9. Calcular percepciones y deducciones totales
                double percepciones = sueldoBase + pagoHorasExtra + percepcionesAdicionales;
                double deducciones = totalAnticipos + deduccionAusencias + deduccionesAdicionales;

                // 10. Sueldo líquido
                double sueldoLiquido = percepciones - deducciones;

                // 11. Crear registro en detalles_nomina
                DetalleNomina detalle = new DetalleNomina(
                    nomina.getId(), empleadoId, ausencias, diasLaborados + diasVacaciones,
                    percepciones, deducciones, sueldoLiquido
                );
                detalleNominaDAO.insertar(detalle);
            }

            // 12. Cambiar estado de la nómina a "CERRADA"
            nomina.setEstado("GENERADA");
            nominaDAO.actualizar(nomina);

            mostrarAlerta("Éxito", "Nómina generada correctamente.");

        } catch (Exception e) {
            Alertas.mostrarError("Error", "No se pudo generar la nómina.");
            e.printStackTrace();
        }
    }
    public static double calcularValorHoraExtra(double salarioMensual) {
        double valorHoraOrdinaria = salarioMensual / 30.0 / 8.0;
        return valorHoraOrdinaria * 1.5;
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
