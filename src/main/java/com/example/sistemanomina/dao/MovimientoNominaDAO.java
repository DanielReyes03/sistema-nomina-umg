// MovimientoNominaDAO.java
package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.MovimientoNomina;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoNominaDAO {
    private final Connection connection;

    public MovimientoNominaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertar(MovimientoNomina movimiento) throws SQLException {
        String sql = "INSERT INTO movimientos_nomina (nomina_id, empleado_id, concepto_id, monto, periodo_inicio, periodo_fin) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, movimiento.getNominaId());
            stmt.setInt(2, movimiento.getEmpleadoId());
            stmt.setInt(3, movimiento.getConceptoId());
            stmt.setBigDecimal(4, movimiento.getMonto());
            stmt.setDate(5, Date.valueOf(movimiento.getPeriodoInicio()));
            stmt.setDate(6, Date.valueOf(movimiento.getPeriodoFin()));
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movimiento.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void actualizar(MovimientoNomina movimiento) throws SQLException {
        String sql = "UPDATE movimientos_nomina SET nomina_id = ?, empleado_id = ?, concepto_id = ?, monto = ?, periodo_inicio = ?, periodo_fin = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, movimiento.getNominaId());
            stmt.setInt(2, movimiento.getEmpleadoId());
            stmt.setInt(3, movimiento.getConceptoId());
            stmt.setBigDecimal(4, movimiento.getMonto());
            stmt.setDate(5, Date.valueOf(movimiento.getPeriodoInicio()));
            stmt.setDate(6, Date.valueOf(movimiento.getPeriodoFin()));
            stmt.setInt(7, movimiento.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM movimientos_nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public MovimientoNomina obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM movimientos_nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer detalleNominaId = rs.getObject("detalle_nomina_id") != null ? rs.getInt("detalle_nomina_id") : null;
                    return new MovimientoNomina(
                            rs.getInt("id"),
                            detalleNominaId,
                            rs.getInt("empleado_id"),
                            rs.getInt("concepto_id"),
                            rs.getBigDecimal("monto"),
                            rs.getDate("periodo_inicio").toLocalDate(),
                            rs.getDate("periodo_fin").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    public List<MovimientoNomina> obtenerTodos() throws SQLException {
        List<MovimientoNomina> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos_nomina";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer nominaId = rs.getObject("nomina_id") != null ? rs.getInt("nomina_id") : null;
                MovimientoNomina m = new MovimientoNomina(
                        rs.getInt("id"),
                        nominaId,
                        rs.getInt("empleado_id"),
                        rs.getInt("concepto_id"),
                        rs.getBigDecimal("monto"),
                        rs.getDate("periodo_inicio").toLocalDate(),
                        rs.getDate("periodo_fin").toLocalDate()
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los movimientos de nómina: " + e.getMessage());
        }
        return lista;
    }

    public List<MovimientoNomina> obtenerPorNominaId(int nominaId) throws SQLException {
        List<MovimientoNomina> lista = new ArrayList<>();
        String sql = "SELECT mn.*, CONCAT(e.nombre, ' ', e.apellido) AS nombre_empleado, c.nombre AS nombre_concepto " +
                "FROM movimientos_nomina mn " +
                "INNER JOIN empleados e ON mn.empleado_id = e.id " +
                "INNER JOIN conceptos_nomina c ON mn.concepto_id = c.id " +
                "WHERE mn.nomina_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nominaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovimientoNomina m = new MovimientoNomina(
                            rs.getInt("id"),
                            rs.getInt("nomina_id"),
                            rs.getInt("empleado_id"),
                            rs.getInt("concepto_id"),
                            rs.getBigDecimal("monto"),
                            rs.getDate("periodo_inicio").toLocalDate(),
                            rs.getDate("periodo_fin").toLocalDate(),
                            rs.getString("nombre_empleado"),
                            rs.getString("nombre_concepto")
                    );
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener movimientos por nómina ID: " + e.getMessage());
        }
        return lista;
    }

    public List<MovimientoNomina> obtenerMovimientosPorEmpleadoEnNominaYPeriodo(int nominaId, int empleadoId, LocalDate periodoInicio, LocalDate periodoFin) throws SQLException {
        List<MovimientoNomina> lista = new ArrayList<>();
        String sql = """
        SELECT mn.*, c.tipo AS tipo_concepto
        FROM movimientos_nomina mn
        INNER JOIN conceptos_nomina c ON mn.concepto_id = c.id
        WHERE mn.nomina_id = ?
          AND mn.empleado_id = ?
          AND mn.periodo_inicio = ?
          AND mn.periodo_fin = ?
    """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nominaId);
            stmt.setInt(2, empleadoId);
            stmt.setDate(3, Date.valueOf(periodoInicio));
            stmt.setDate(4, Date.valueOf(periodoFin));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovimientoNomina m = new MovimientoNomina(
                            rs.getInt("id"),
                            rs.getInt("nomina_id"),
                            rs.getInt("empleado_id"),
                            rs.getInt("concepto_id"),
                            rs.getBigDecimal("monto"),
                            rs.getDate("periodo_inicio").toLocalDate(),
                            rs.getDate("periodo_fin").toLocalDate()
                    );
                    // Puedes agregar el tipo de concepto si tu modelo lo soporta
                    lista.add(m);
                }
            }
        }
        return lista;
    }

    public double sumarMovimientosPorTipo(int nominaId, int empleadoId, LocalDate periodoInicio, LocalDate periodoFin, String tipoConcepto) throws SQLException {
        String sql = """
        SELECT COALESCE(SUM(mn.monto), 0) AS total
        FROM movimientos_nomina mn
        INNER JOIN conceptos_nomina c ON mn.concepto_id = c.id
        WHERE mn.nomina_id = ?
          AND mn.empleado_id = ?
          AND mn.periodo_inicio = ?
          AND mn.periodo_fin = ?
          AND c.tipo = ?
    """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nominaId);
            stmt.setInt(2, empleadoId);
            stmt.setDate(3, Date.valueOf(periodoInicio));
            stmt.setDate(4, Date.valueOf(periodoFin));
            stmt.setString(5, tipoConcepto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }
}
