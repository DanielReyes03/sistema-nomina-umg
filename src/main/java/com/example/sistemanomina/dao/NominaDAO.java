// NominaDAO.java
package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.Nomina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NominaDAO {
    private final Connection connection;

    public NominaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertar(Nomina nomina) throws SQLException {
        String sql = "INSERT INTO nomina (periodo_inicio, periodo_fin, fecha_generacion, tipo, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(nomina.getPeriodoInicio()));
            stmt.setDate(2, Date.valueOf(nomina.getPeriodoFin()));
            stmt.setDate(3, Date.valueOf(nomina.getFechaGeneracion()));
            stmt.setString(4, nomina.getTipo());
            stmt.setString(5, nomina.getEstado());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    nomina.setId(generatedKeys.getInt(1));
                }
            }
        }catch (Exception e) {
            System.out.println("Error al insertar nómina: " + e.getMessage());
            throw e;
        }
    }

    public void actualizar(Nomina nomina) throws SQLException {
        String sql = "UPDATE nomina SET periodo_inicio = ?, periodo_fin = ?, fecha_generacion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(nomina.getPeriodoInicio()));
            stmt.setDate(2, Date.valueOf(nomina.getPeriodoFin()));
            stmt.setDate(3, Date.valueOf(nomina.getFechaGeneracion()));
            stmt.setString(4, nomina.getTipo());
            stmt.setString(5, nomina.getEstado());
            stmt.setInt(6, nomina.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Nomina obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Nomina(
                            rs.getInt("id"),
                            rs.getDate("periodo_inicio").toLocalDate(),
                            rs.getDate("periodo_fin").toLocalDate(),
                            rs.getString("tipo"),
                            rs.getDate("fecha_generacion").toLocalDate(),
                            rs.getString("estado")
                    );
                }
            }
        }
        return null;
    }

    public List<Nomina> obtenerTodos() throws SQLException {
        List<Nomina> lista = new ArrayList<>();
        String sql = "SELECT * FROM nomina";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Nomina n = new Nomina(
                        rs.getInt("id"),
                        rs.getDate("periodo_inicio").toLocalDate(),
                        rs.getDate("periodo_fin").toLocalDate(),
                        rs.getString("tipo"),
                        rs.getDate("fecha_generacion").toLocalDate(),
                        rs.getString("estado")
                );
                lista.add(n);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las nóminas: " + e.getMessage());
        }
        return lista;
    }
}
