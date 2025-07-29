package model;

import java.sql.Date;

public class HorasExtra {
    private int id;
    private int empleadoId;
    private Date fecha;
    private int horas;
    private String motivo;
    private boolean aprobado;

    public HorasExtra() {
    }

    public HorasExtra(int id, int empleadoId, Date fecha, int horas, String motivo, boolean aprobado) {
        this.id = id;
        this.empleadoId = empleadoId;
        this.fecha = fecha;
        this.horas = horas;
        this.motivo = motivo;
        this.aprobado = aprobado;
    }

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public String toString() {
        return "HorasExtra{" +
                "id=" + id +
                ", empleadoId=" + empleadoId +
                ", fecha=" + fecha +
                ", horas=" + horas +
                ", motivo='" + motivo + '\'' +
                ", aprobado=" + aprobado +
                '}';
    }
}
