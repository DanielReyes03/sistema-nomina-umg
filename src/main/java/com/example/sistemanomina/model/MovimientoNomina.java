// MovimientoNomina.java
package com.example.sistemanomina.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoNomina {
    private int id;
    private Integer nominaId;
    private int empleadoId;
    private int conceptoId;
    private BigDecimal monto;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
    private String nombreEmpleado;
    private String nombreConcepto;

    // Constructor completo con nombres
    public MovimientoNomina(int id, Integer nominaId, int empleadoId, int conceptoId, BigDecimal monto,
                            LocalDate periodoInicio, LocalDate periodoFin,
                            String nombreEmpleado, String nombreConcepto) {
        this.id = id;
        this.nominaId = nominaId;
        this.empleadoId = empleadoId;
        this.conceptoId = conceptoId;
        this.monto = monto;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.nombreEmpleado = nombreEmpleado;
        this.nombreConcepto = nombreConcepto;
    }

    // Constructor original (sin nombres)
    public MovimientoNomina(int id, Integer nominaId, int empleadoId, int conceptoId, BigDecimal monto,
                            LocalDate periodoInicio, LocalDate periodoFin) {
        this(id, nominaId, empleadoId, conceptoId, monto, periodoInicio, periodoFin, null, null);
    }

    // Constructor para inserción (sin id, sin nombres)
    public MovimientoNomina(Integer nominaId, int empleadoId, int conceptoId, BigDecimal monto,
                            LocalDate periodoInicio, LocalDate periodoFin) {
        this(0, nominaId, empleadoId, conceptoId, monto, periodoInicio, periodoFin, null, null);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getNominaId() { return nominaId; }
    public void setNominaId(Integer nominaId) { this.nominaId = nominaId; }

    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }

    public int getConceptoId() { return conceptoId; }
    public void setConceptoId(int conceptoId) { this.conceptoId = conceptoId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(LocalDate periodoInicio) { this.periodoInicio = periodoInicio; }

    public LocalDate getPeriodoFin() { return periodoFin; }
    public void setPeriodoFin(LocalDate periodoFin) { this.periodoFin = periodoFin; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getNombreConcepto() { return nombreConcepto; }
    public void setNombreConcepto(String nombreConcepto) { this.nombreConcepto = nombreConcepto; }

    @Override
    public String toString() {
        return "MovimientoNomina{" +
                "id=" + id +
                ", nominaId=" + nominaId +
                ", empleadoId=" + empleadoId +
                ", conceptoId=" + conceptoId +
                ", monto=" + monto +
                ", periodoInicio=" + periodoInicio +
                ", periodoFin=" + periodoFin +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", nombreConcepto='" + nombreConcepto + '\'' +
                '}';
    }
}
