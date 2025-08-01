package com.example.sistemanomina.model;

public class Usuario {
    private int id;
    private String username;
    private String passwordHash;
    private String rol;
    private Integer empleadoId;
    private Boolean estado; // ACTIVO o INACTIVO

    public Usuario(int id, String username, String passwordHash, String rol, Integer empleadoId, Boolean estado) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.empleadoId = empleadoId;
        this.estado = estado;
    }

    public Usuario(String username, String passwordHash, String rol, Integer empleadoId, Boolean estado) {
        this(0, username, passwordHash, rol, empleadoId, estado);
    }

    // Getters y setters...
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRol() { return rol; }
    public Integer getEmpleadoId() { return empleadoId; }
    public Boolean getEstado() { return estado; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
