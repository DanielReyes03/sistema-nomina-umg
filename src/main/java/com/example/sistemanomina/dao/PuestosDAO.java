// src/main/java/com/example/sistemanomina/dao/PuestosDAO.java
package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.Puestos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PuestosDAO {
    private final Connection connection;

    public PuestosDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertar(Puestos puesto) throws SQLException {
        String sql = "INSERT INTO puestos (nombre, descripcion, rango_salarios, departamento_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, puesto.getNombre());
            stmt.setString(2, puesto.getDescripcion());
            stmt.setString(3, puesto.getRangoSalarios());
            stmt.setInt(4, puesto.getIdDepartamento());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    puesto.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void actualizar(Puestos puesto) throws SQLException {
        String sql = "UPDATE puestos SET nombre = ?, descripcion = ?, rango_salarios = ?, departamento_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, puesto.getNombre());
            stmt.setString(2, puesto.getDescripcion());
            stmt.setString(3, puesto.getRangoSalarios());
            stmt.setInt(4, puesto.getIdDepartamento());
            stmt.setInt(5, puesto.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM puestos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Puestos obtenerPorId(int id) throws SQLException {
        String sql = "SELECT p.*, d.nombre AS nombre_departamento FROM puestos p LEFT JOIN departamentos d ON p.departamento_id = d.id WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Puestos(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("rango_salarios"),
                            rs.getInt("departamento_id"),
                            rs.getString("nombre_departamento")
                    );
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al obtener puesto por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Puestos> obtenerTodos() throws SQLException {
        List<Puestos> lista = new ArrayList<>();
        String sql = "SELECT p.*, d.nombre AS nombre_departamento FROM puestos p LEFT JOIN departamentos d ON p.departamento_id = d.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Puestos p = new Puestos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("rango_salarios"),
                        rs.getInt("departamento_id"),
                        rs.getString("nombre_departamento")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public boolean puestoExiste(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM puestos WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia del puesto: " + e.getMessage());
        }
        return false;
    }

    public List<Puestos> obtenerPorDepartamento(Integer departamentoId) {
        List<Puestos> lista = new ArrayList<>();
        String sql = "SELECT p.*, d.nombre AS nombre_departamento FROM puestos p LEFT JOIN departamentos d ON p.departamento_id = d.id WHERE p.departamento_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departamentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Puestos p = new Puestos(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("rango_salarios"),
                            rs.getInt("departamento_id"),
                            rs.getString("nombre_departamento")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener puestos por departamento: " + e.getMessage());
        }
        return lista;
    }
}
