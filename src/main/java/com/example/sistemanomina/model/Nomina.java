package com.example.sistemanomina.model;

import java.time.LocalDate;

public class Nomina {
    private int id;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
    private String tipo; // "QUINCENAL" o "MENSUAL" o "OTRO"
    private LocalDate fechaGeneracion;
    private String estado; // "PENDIENTE", "GENERADA"

    public Nomina(int id, LocalDate periodoInicio, LocalDate periodoFin, String tipo, LocalDate fechaGeneracion, String estado) {
        this.id = id;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
        this.estado = estado;
    }

    public Nomina(LocalDate periodoInicio, LocalDate periodoFin, String tipo, LocalDate fechaGeneracion, String estado) {
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return tipo + " (" + periodoInicio + " - " + periodoFin + ")";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(LocalDate periodoInicio) { this.periodoInicio = periodoInicio; }
    public LocalDate getPeriodoFin() { return periodoFin; }
    public void setPeriodoFin(LocalDate periodoFin) { this.periodoFin = periodoFin; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDate fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado;}

}
