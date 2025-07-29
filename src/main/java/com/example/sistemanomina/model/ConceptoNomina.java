package com.example.sistemanomina.model;

public class ConceptoNomina {
    private int id;
    private String nombre;
    private String descripcion;
    private String tipo; // "PERCEPCION" o "DEDUCCION"
    private String tipoCalculo; // "FIJO", "PORCENTAJE", "MULTIPLICACION", "DIVISION"
    private double valor;
    private boolean aplicaAutomatico;

    public ConceptoNomina() {}

    public ConceptoNomina(int id, String nombre, String descripcion, String tipo, String tipoCalculo, double valor, boolean aplicaAutomatico) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.tipoCalculo = tipoCalculo;
        this.valor = valor;
        this.aplicaAutomatico = aplicaAutomatico;
    }

    public ConceptoNomina(String nombre, String descripcion, String tipo, String tipoCalculo, double valor, boolean aplicaAutomatico) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.tipoCalculo = tipoCalculo;
        this.valor = valor;
        this.aplicaAutomatico = aplicaAutomatico;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTipoCalculo() { return tipoCalculo; }
    public void setTipoCalculo(String tipoCalculo) { this.tipoCalculo = tipoCalculo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public boolean getAplicaAutomatico() { return aplicaAutomatico; }
    public void setAplicaAutomatico(boolean aplicaAutomatico) { this.aplicaAutomatico = aplicaAutomatico; }
}
