// DetalleNominaDAO.java
package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.DetalleNomina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleNominaDAO {
    private final Connection connection;

    public DetalleNominaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertar(DetalleNomina detalle) throws SQLException {
        String sql = "INSERT INTO detalles_nomina (nomina_id, empleado_id, ausencias, dias_laborados, percepciones, deducciones, sueldo_liquido) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detalle.getNominaId());
            stmt.setInt(2, detalle.getEmpleadoId());
            stmt.setInt(3, detalle.getAusencias());
            stmt.setInt(4, detalle.getDiasLaborados());
            stmt.setDouble(5, detalle.getPercepciones());
            stmt.setDouble(6, detalle.getDeducciones());
            stmt.setDouble(7, detalle.getSueldoLiquido());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    detalle.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void actualizar(DetalleNomina detalle) throws SQLException {
        String sql = "UPDATE detalles_nomina SET nomina_id = ?, empleado_id = ?, ausencias = ?, dias_laborados = ?, percepciones = ?, deducciones = ?, sueldo_liquido = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getNominaId());
            stmt.setInt(2, detalle.getEmpleadoId());
            stmt.setInt(3, detalle.getAusencias());
            stmt.setInt(4, detalle.getDiasLaborados());
            stmt.setDouble(5, detalle.getPercepciones());
            stmt.setDouble(6, detalle.getDeducciones());
            stmt.setDouble(7, detalle.getSueldoLiquido());
            stmt.setInt(8, detalle.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM detalles_nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<DetalleNomina> obtenerTodos() throws SQLException {
        List<DetalleNomina> lista = new ArrayList<>();
        String sql = "SELECT dn.*, CONCAT(e.nombre, ' ', e.apellido) AS nombre_empleado " +
                "FROM detalles_nomina dn " +
                "INNER JOIN empleados e ON dn.empleado_id = e.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DetalleNomina d = new DetalleNomina(
                        rs.getInt("id"),
                        rs.getInt("nomina_id"),
                        rs.getInt("empleado_id"),
                        rs.getInt("ausencias"),
                        rs.getInt("dias_laborados"),
                        rs.getDouble("percepciones"),
                        rs.getDouble("deducciones"),
                        rs.getDouble("sueldo_liquido"),
                        rs.getString("nombre_empleado") // Nuevo campo para el nombre completo
                );
                lista.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los detalles de nómina: " + e.getMessage());
        }
        return lista;
    }

}
