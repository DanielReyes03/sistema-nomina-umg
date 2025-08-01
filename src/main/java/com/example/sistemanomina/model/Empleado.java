package com.example.sistemanomina.model;

import java.time.LocalDate;

public class Empleado {

    private int id;
    private String nombre;
    private String apellido;
    private String dpi;
    private LocalDate fechaIngreso;
    private double salario;
    private int puestoId;

    // Nuevos campos
    private String nombrePuesto;
    private String nombreDepartamento;

    // Constructor vacío
    public Empleado() {}

    // Constructor completo sin nombres
    public Empleado(int id, String nombre, String apellido, String dpi, LocalDate fechaIngreso,
                    double salario, int puestoId) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dpi = dpi;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.puestoId = puestoId;
    }

    // Constructor completo con nombres
    public Empleado(int id, String nombre, String apellido, String dpi, LocalDate fechaIngreso,
                    double salario, int puestoId, String nombrePuesto, String nombreDepartamento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dpi = dpi;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.puestoId = puestoId;
        this.nombrePuesto = nombrePuesto;
        this.nombreDepartamento = nombreDepartamento;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public int getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(int puestoId) {
        this.puestoId = puestoId;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    // 👇 Este método es clave para que el ComboBox muestre nombres legibles
    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
