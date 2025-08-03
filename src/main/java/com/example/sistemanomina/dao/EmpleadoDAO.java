package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private final Connection connection;

    public EmpleadoDAO(Connection conexion) {
        this.connection = conexion;
    }

    // Agregar nuevo empleado
    public boolean agregarEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, apellido, dpi, fecha_ingreso, salario, puesto_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getDpi());
            stmt.setDate(4, Date.valueOf(empleado.getFechaIngreso()));
            stmt.setDouble(5, empleado.getSalario());
            stmt.setInt(6, empleado.getPuestoId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todos los empleados
    public List<Empleado> obtenerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = """
        SELECT e.*, p.nombre AS nombre_puesto, d.nombre AS nombre_departamento
        FROM empleados e
        JOIN puestos p ON e.puesto_id = p.id
        JOIN departamentos d ON p.departamento_id = d.id
    """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setId(rs.getInt("id"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApellido(rs.getString("apellido"));
                empleado.setDpi(rs.getString("dpi"));
                empleado.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                empleado.setSalario(rs.getDouble("salario"));
                empleado.setPuestoId(rs.getInt("puesto_id"));

                // Nuevos campos
                empleado.setNombrePuesto(rs.getString("nombre_puesto"));
                empleado.setNombreDepartamento(rs.getString("nombre_departamento"));

                empleados.add(empleado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empleados;
    }


    // Buscar empleado por ID
    public Empleado obtenerEmpleadoPorId(int id) {
        String sql = "SELECT * FROM empleados WHERE id = ?";
        Empleado empleado = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                empleado = new Empleado();
                empleado.setId(rs.getInt("id"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApellido(rs.getString("apellido"));
                empleado.setDpi(rs.getString("dpi"));
                empleado.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                empleado.setSalario(rs.getDouble("salario"));
                empleado.setPuestoId(rs.getInt("puesto_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empleado;
    }

    // Actualizar empleado
    public boolean actualizarEmpleado(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, dpi = ?, fecha_ingreso = ?, salario = ?, puesto_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getDpi());
            stmt.setDate(4, Date.valueOf(empleado.getFechaIngreso()));
            stmt.setDouble(5, empleado.getSalario());
            stmt.setInt(6, empleado.getPuestoId());
            stmt.setInt(7, empleado.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar empleado
    public boolean eliminarEmpleado(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Empleado validarEmpleadoUnico(String DPI){
        try {
            String sql = "SELECT * FROM empleados WHERE dpi = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, DPI);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setId(rs.getInt("id"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApellido(rs.getString("apellido"));
                empleado.setDpi(rs.getString("dpi"));
                empleado.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                empleado.setSalario(rs.getDouble("salario"));
                empleado.setPuestoId(rs.getInt("puesto_id"));
                return empleado;
            }
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}