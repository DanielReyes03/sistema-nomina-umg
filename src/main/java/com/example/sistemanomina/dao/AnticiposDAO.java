// CARLO ANDREE BARQUERO BOCHE 0901-22-601 DESARROLLO DEL APARTADO DE ANTICIPOS

package com.example.sistemanomina.dao;

import com.example.sistemanomina.db.DatabaseConnection;
import com.example.sistemanomina.model.Anticipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnticiposDAO {

    private final Connection connection;

    public AnticiposDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean insertar(Anticipo anticipo) {
        String sql = "INSERT INTO anticipos (empleado_id, monto, fecha, motivo, saldo_pendiente) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, anticipo.getEmpleadoId());
            stmt.setDouble(2, anticipo.getMonto());
            stmt.setDate(3, Date.valueOf(anticipo.getFecha()));
            stmt.setString(4, anticipo.getMotivo());
            stmt.setDouble(5, anticipo.getSaldoPendiente());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Anticipo> obtenerTodos() {
        List<Anticipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM anticipos";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Anticipo a = new Anticipo();
                a.setId(rs.getInt("id"));
                a.setEmpleadoId(rs.getInt("empleado_id"));
                a.setMonto(rs.getDouble("monto"));
                a.setFecha(rs.getDate("fecha").toLocalDate());
                a.setMotivo(rs.getString("motivo"));
                a.setSaldoPendiente(rs.getDouble("saldo_pendiente"));
                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    public void actualizarAnticipo(Anticipo anticipo) {
        String sql = "UPDATE anticipos SET fecha = ?, monto = ?, motivo = ?, saldo_pendiente = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(anticipo.getFecha()));
            stmt.setDouble(2, anticipo.getMonto());
            stmt.setString(3, anticipo.getMotivo());
            stmt.setDouble(4, anticipo.getSaldoPendiente());
            stmt.setInt(5, anticipo.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar anticipo", e);
        }
    }
    public void eliminarAnticipo(int id) {
        String sql = "DELETE FROM anticipos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
