package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.Departamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {
    private final Connection connection;

    public DepartamentoDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertar(Departamento departamento) throws SQLException {
        String sql = "INSERT INTO departamentos (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departamento.getNombre());
            stmt.setString(2, departamento.getDescripcion());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    departamento.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void actualizar(Departamento departamento) throws SQLException {
        String sql = "UPDATE departamentos SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departamento.getNombre());
            stmt.setString(2, departamento.getDescripcion());
            stmt.setInt(3, departamento.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM departamentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Departamento obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM departamentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Departamento(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
            }
        }
        return null;
    }

    public List<Departamento> obtenerTodos() throws SQLException {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM departamentos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Departamento d = new Departamento(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
                lista.add(d);
            }
        }
        return lista;
    }

    public boolean departamentoExiste(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM departamentos WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al verificar existencia del departamento: " + e.getMessage());
        }
        return false;
    }
}
