package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.ConceptoNomina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConceptoNominaDAO {
    private final Connection connection;

    public ConceptoNominaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertar(ConceptoNomina concepto) throws SQLException {
        String sql = "INSERT INTO conceptos_nomina (nombre, descripcion, tipo, tipo_calculo, valor, aplica_automatico) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, concepto.getNombre());
            stmt.setString(2, concepto.getDescripcion());
            stmt.setString(3, concepto.getTipo());
            stmt.setString(4, concepto.getTipoCalculo());
            stmt.setDouble(5, concepto.getValor());
            stmt.setBoolean(6, concepto.getAplicaAutomatico());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    concepto.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void actualizar(ConceptoNomina concepto) throws SQLException {
        String sql = "UPDATE conceptos_nomina SET nombre = ?, descripcion = ?, tipo = ?, tipo_calculo = ?, valor = ?, aplica_automatico = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, concepto.getNombre());
            stmt.setString(2, concepto.getDescripcion());
            stmt.setString(3, concepto.getTipo());
            stmt.setString(4, concepto.getTipoCalculo());
            stmt.setDouble(5, concepto.getValor());
            stmt.setBoolean(6, concepto.getAplicaAutomatico());
            stmt.setInt(7, concepto.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM conceptos_nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public ConceptoNomina obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM conceptos_nomina WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToConcepto(rs);
                }
            }
        }
        return null;
    }

    public List<ConceptoNomina> obtenerTodos() throws SQLException {
        List<ConceptoNomina> lista = new ArrayList<>();
        String sql = "SELECT * FROM conceptos_nomina";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToConcepto(rs));
            }
        }
        return lista;
    }

    private ConceptoNomina mapResultSetToConcepto(ResultSet rs) throws SQLException {
        return new ConceptoNomina(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("tipo"),
                rs.getString("tipo_calculo"),
                rs.getDouble("valor"),
                rs.getBoolean("aplica_automatico")
        );
    }
}
