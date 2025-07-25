package com.example.sistemanomina.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Asistencia {
    private int id;
    private int empleadoId;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    // Constructores
    public Asistencia() {
    }

    public Asistencia(int empleadoId, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida) {
        this.empleadoId = empleadoId;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public Asistencia(int id, int empleadoId, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida) {
        this.id = id;
        this.empleadoId = empleadoId;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public String toString() {
        return "Asistencia{" +
                "id=" + id +
                ", empleadoId=" + empleadoId +
                ", fecha=" + fecha +
                ", horaEntrada=" + horaEntrada +
                ", horaSalida=" + horaSalida +
                '}';
    }
}
