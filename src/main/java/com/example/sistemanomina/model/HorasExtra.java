package com.example.sistemanomina.model;

import java.sql.Date;

public class HorasExtra {
    private int id;
    private int empleadoId;
    private Date fecha;
    private int horas;
    private String motivo;
    private boolean aprobado;

    // ✅ Nuevo campo para mostrar nombre y apellido del empleado
    private String nombreEmpleado;

    public HorasExtra() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }

    // ✅ Getter y Setter para el nombre completo del empleado
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
}
