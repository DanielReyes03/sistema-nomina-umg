package com.example.sistemanomina.model;

public class Vacaciones {

    private int id;
    private int empleadoId;
    private java.sql.Date fechaInicio;
    private java.sql.Date fechaFin;
    private int dias;
    private boolean aprobada;
    private String nombreEmpleado;

    // Constructor vacío
    public Vacaciones() {}

    // Constructor con parámetros
    public Vacaciones(int id, int empleadoId, java.sql.Date fechaInicio, java.sql.Date fechaFin, int dias, boolean aprobada) {
        this.id = id;
        this.empleadoId = empleadoId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.dias = dias;
        this.aprobada = aprobada;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public java.sql.Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(java.sql.Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public java.sql.Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(java.sql.Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Vacaciones{" +
                "id=" + id +
                ", empleadoId=" + empleadoId +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", dias=" + dias +
                ", aprobada=" + aprobada +
                '}';
    }
}

