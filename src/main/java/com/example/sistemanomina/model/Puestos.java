// src/main/java/com/example/sistemanomina/model/Puestos.java
package com.example.sistemanomina.model;

public class Puestos {
    private int id;
    private String nombre;
    private String descripcion;
    private String rangoSalarios;
    private int idDepartamento;
    private String nombreDepartamento; // Nuevo campo

    public Puestos(int id, String nombre, String descripcion, String rangoSalarios, int idDepartamento, String nombreDepartamento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rangoSalarios = rangoSalarios;
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
    }

    public Puestos(String nombre, String descripcion, String rangoSalarios, int idDepartamento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rangoSalarios = rangoSalarios;
        this.idDepartamento = idDepartamento;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRangoSalarios() { return rangoSalarios; }
    public void setRangoSalarios(String rangoSalarios) { this.rangoSalarios = rangoSalarios; }

    public int getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(int idDepartamento) { this.idDepartamento = idDepartamento; }

    public String getNombreDepartamento() { return nombreDepartamento; }
    public void setNombreDepartamento(String nombreDepartamento) { this.nombreDepartamento = nombreDepartamento; }
}
