package com.example.sistemanomina.model;

public class DetalleNomina {
    private int id;
    private int nominaId;
    private int empleadoId;
    private int ausencias;
    private int diasLaborados;
    private double percepciones;
    private double deducciones;
    private double sueldoLiquido;
    private String nombreEmpleado;

    public DetalleNomina(int id, int nominaId, int empleadoId, int ausencias, int diasLaborados, double percepciones, double deducciones, double sueldoLiquido, String nombreEmpleado) {
        this.id = id;
        this.nominaId = nominaId;
        this.empleadoId = empleadoId;
        this.ausencias = ausencias;
        this.diasLaborados = diasLaborados;
        this.percepciones = percepciones;
        this.deducciones = deducciones;
        this.sueldoLiquido = sueldoLiquido;
        this.nombreEmpleado = nombreEmpleado;
    }

    public DetalleNomina(int nominaId, int empleadoId, int ausencias, int diasLaborados, double percepciones, double deducciones, double sueldoLiquido) {
        this(0, nominaId, empleadoId, ausencias, diasLaborados, percepciones, deducciones, sueldoLiquido, null);
    }

    @Override
    public String toString() {
        return "Empleado ID: " + empleadoId + ", Sueldo Líquido: " + sueldoLiquido;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNominaId() { return nominaId; }
    public void setNominaId(int nominaId) { this.nominaId = nominaId; }
    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }
    public int getAusencias() { return ausencias; }
    public void setAusencias(int ausencias) { this.ausencias = ausencias; }
    public int getDiasLaborados() { return diasLaborados; }
    public void setDiasLaborados(int diasLaborados) { this.diasLaborados = diasLaborados; }
    public double getPercepciones() { return percepciones; }
    public void setPercepciones(double percepciones) { this.percepciones = percepciones; }
    public double getDeducciones() { return deducciones; }
    public void setDeducciones(double deducciones) { this.deducciones = deducciones; }
    public double getSueldoLiquido() { return sueldoLiquido; }
    public void setSueldoLiquido(double sueldoLiquido) { this.sueldoLiquido = sueldoLiquido; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
}
