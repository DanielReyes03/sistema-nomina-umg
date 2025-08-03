package com.example.sistemanomina.dao;

import com.example.sistemanomina.model.HorasExtra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorasExtraDAO {
    private final Connection connection;

    public HorasExtraDAO(Connection connection) {
        this.connection = connection;
    }

    // ✅ Agregar horas extra
    public boolean agregarHorasExtra(HorasExtra h) throws SQLException {
        String sql = "INSERT INTO horas_extra (empleado_id, fecha, horas, motivo, aprobado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, h.getEmpleadoId());
            ps.setDate(2, h.getFecha());
            ps.setInt(3, h.getHoras());
            ps.setString(4, h.getMotivo());
            ps.setBoolean(5, h.isAprobado());
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Listar horas extra con nombre de empleado
    public List<HorasExtra> listarHorasExtra() throws SQLException {
        List<HorasExtra> lista = new ArrayList<>();

        String sql = "SELECT he.id, he.empleado_id, e.nombre, e.apellido, he.fecha, he.horas, he.motivo, he.aprobado " +
                "FROM horas_extra he " +
                "JOIN empleados e ON he.empleado_id = e.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HorasExtra h = new HorasExtra();
                h.setId(rs.getInt("id"));
                h.setEmpleadoId(rs.getInt("empleado_id"));

                // ✅ Asignar nombre y apellido del empleado
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                h.setNombreEmpleado(nombreCompleto);

                h.setFecha(rs.getDate("fecha"));
                h.setHoras(rs.getInt("horas"));
                h.setMotivo(rs.getString("motivo"));
                h.setAprobado(rs.getBoolean("aprobado"));
                lista.add(h);
            }
        }
        return lista;
    }

    // ✅ Eliminar horas extra por ID
    public boolean eliminarHorasExtra(int id) throws SQLException {
        String sql = "DELETE FROM horas_extra WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
