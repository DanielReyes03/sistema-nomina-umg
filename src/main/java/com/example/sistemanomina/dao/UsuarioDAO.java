// src/main/java/com/example/sistemanomina/dao/UsuarioDAO.java
package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.Usuario;
import java.sql.*;

public class UsuarioDAO {
    private final Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public int contarUsuarios() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public Usuario buscarPorUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("rol"),
                            rs.getObject("empleado_id") != null ? rs.getInt("empleado_id") : null,
                            rs.getBoolean("estado")
                    );
                }
            }
        }
        return null;
    }

    public void insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password_hash, rol, empleado_id, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getRol());
            if (usuario.getEmpleadoId() != null) {
                stmt.setInt(4, usuario.getEmpleadoId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setBoolean(5, usuario.getEstado());
            stmt.executeUpdate();
        }
    }
}
