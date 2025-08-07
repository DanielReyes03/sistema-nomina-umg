package com.example.sistemanomina.dao;

import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Asistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class AsistenciaDAO {

    private final Connection connection;
    public AsistenciaDAO(Connection conexion) {
        this.connection = conexion;
    }

    public void insertarAsistencia(Asistencia asistencia) {
        String sql = "INSERT INTO asistencias (empleado_id, fecha, hora_entrada, hora_salida) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, asistencia.getEmpleadoId());
            stmt.setDate(2, Date.valueOf(asistencia.getFecha()));
            stmt.setTime(3, Time.valueOf(asistencia.getHoraEntrada()));
            stmt.setTime(4, Time.valueOf(asistencia.getHoraSalida()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar asistencia: " + e.getMessage());
        }
    }

    public List<Asistencia> obtenerTodasAsistencias() {
        List<Asistencia> asistencias = new ArrayList<>();
        String sql = "SELECT * FROM asistencias";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Asistencia asistencia = new Asistencia(
                        rs.getInt("id"),
                        rs.getInt("empleado_id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora_entrada").toLocalTime(),
                        rs.getTime("hora_salida").toLocalTime()
                );
                asistencias.add(asistencia);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener asistencias: " + e.getMessage());
        }

        return asistencias;
    }

    public void eliminarAsistenciaPorId(int id) {
        String sql = "DELETE FROM asistencias WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar asistencia: " + e.getMessage());
        }
    }

    public void actualizarAsistencia(Asistencia asistencia) {
        String sql = "UPDATE asistencias SET empleado_id = ?, fecha = ?, hora_entrada = ?, hora_salida = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, asistencia.getEmpleadoId());
            stmt.setDate(2, Date.valueOf(asistencia.getFecha()));
            stmt.setTime(3, Time.valueOf(asistencia.getHoraEntrada()));
            stmt.setTime(4, Time.valueOf(asistencia.getHoraSalida()));
            stmt.setInt(5, asistencia.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar asistencia: " + e.getMessage());
        }
    }

    public int contarDiasAsistidos(int empleadoId, LocalDate periodoInicio, LocalDate periodoFin) {
        String sql = "SELECT COUNT(DISTINCT fecha) AS total " +
                "FROM asistencias " +
                "WHERE empleado_id = ? AND fecha BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            stmt.setDate(2, Date.valueOf(periodoInicio));
            stmt.setDate(3, Date.valueOf(periodoFin));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar días asistidos: " + e.getMessage());
        }
        return 0;
    }


}
