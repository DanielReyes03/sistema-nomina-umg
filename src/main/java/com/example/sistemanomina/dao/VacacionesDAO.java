package com.example.sistemanomina.dao;

import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Vacaciones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacacionesDAO {

    private final Connection connection;
    public VacacionesDAO(Connection conexion) {
        this.connection = conexion;
    }
    // Crear un nuevo registro
    public void insertar(Vacaciones vacaciones) throws SQLException {
        String sql = "INSERT INTO vacaciones (empleado_id, fecha_inicio, fecha_fin, dias, aprobada) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vacaciones.getEmpleadoId());
            stmt.setDate(2, vacaciones.getFechaInicio());
            stmt.setDate(3, vacaciones.getFechaFin());
            stmt.setInt(4, vacaciones.getDias());
            stmt.setBoolean(5, vacaciones.isAprobada());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vacaciones.setId(rs.getInt(1));
                }
            }
        }
    }

    // ✅ Obtener vacaciones por ID con nombre de empleado
    public Vacaciones obtenerPorId(int id) throws SQLException {
        String sql = """
            SELECT v.*, e.nombre AS nombre_empleado, e.apellido AS apellido_empleado
            FROM vacaciones v
            JOIN empleados e ON v.empleado_id = e.id
            WHERE v.id = ?
        """;
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    // ✅ Listar vacaciones con nombre de empleado
    public List<Vacaciones> listarTodos() throws SQLException {
        List<Vacaciones> lista = new ArrayList<>();
        String sql = """
            SELECT v.*, e.nombre AS nombre_empleado, e.apellido AS apellido_empleado
            FROM vacaciones v
            JOIN empleados e ON v.empleado_id = e.id
        """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    // ✅ Nuevo método para obtener los días tomados en un año
    public int obtenerDiasTomadosPorEmpleadoEnAnio(int empleadoId, int anio) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(dias), 0) AS total
            FROM vacaciones
            WHERE empleado_id = ? AND YEAR(fecha_inicio) = ?
        """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empleadoId);
            stmt.setInt(2, anio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // Actualizar un registro
    public void actualizar(Vacaciones vacaciones) throws SQLException {
        String sql = "UPDATE vacaciones SET empleado_id = ?, fecha_inicio = ?, fecha_fin = ?, dias = ?, aprobada = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vacaciones.getEmpleadoId());
            stmt.setDate(2, vacaciones.getFechaInicio());
            stmt.setDate(3, vacaciones.getFechaFin());
            stmt.setInt(4, vacaciones.getDias());
            stmt.setBoolean(5, vacaciones.isAprobada());
            stmt.setInt(6, vacaciones.getId());

            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vacaciones WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // VacacionesDAO.java

    public List<Vacaciones> obtenerVacacionesAprobadasEnPeriodo(int empleadoId, Date periodoInicio, Date periodoFin) throws SQLException {
        List<Vacaciones> lista = new ArrayList<>();
        String sql = """
        SELECT v.*, e.nombre AS nombre_empleado, e.apellido AS apellido_empleado
        FROM vacaciones v
        JOIN empleados e ON v.empleado_id = e.id
        WHERE v.empleado_id = ?
          AND v.aprobada = 1
          AND (
                (v.fecha_inicio BETWEEN ? AND ?)
             OR (v.fecha_fin BETWEEN ? AND ?)
             OR (v.fecha_inicio <= ? AND v.fecha_fin >= ?)
          )
    """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            stmt.setDate(2, periodoInicio);
            stmt.setDate(3, periodoFin);
            stmt.setDate(4, periodoInicio);
            stmt.setDate(5, periodoFin);
            stmt.setDate(6, periodoInicio);
            stmt.setDate(7, periodoFin);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }
        return lista;
    }


    // ✅ Mapear resultados con nombre completo
    private Vacaciones mapResultSet(ResultSet rs) throws SQLException {
        Vacaciones v = new Vacaciones();
        v.setId(rs.getInt("id"));
        v.setEmpleadoId(rs.getInt("empleado_id"));
        v.setFechaInicio(rs.getDate("fecha_inicio"));
        v.setFechaFin(rs.getDate("fecha_fin"));
        v.setDias(rs.getInt("dias"));
        v.setAprobada(rs.getBoolean("aprobada"));

        String nombre = rs.getString("nombre_empleado");
        String apellido = rs.getString("apellido_empleado");
        v.setNombreEmpleado(nombre + " " + apellido);

        return v;
    }
}

