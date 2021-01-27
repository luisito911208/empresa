package com.sistema.empresa.modelos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "calendarizacion")
public class Calendarizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCalendarizacion;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fecha;
    private double montoEstimado;
    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idMaterialContratado" ,nullable = false)
    private MaterialContratado materialContratado;

    public int getIdCalendarizacion() {
        return idCalendarizacion;
    }

    public void setIdCalendarizacion(int idCalendarizacion) {
        this.idCalendarizacion = idCalendarizacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getMontoEstimado() {
        return montoEstimado;
    }

    public void setMontoEstimado(double montoEstimado) {
        this.montoEstimado = montoEstimado;
    }

    public MaterialContratado getMaterialContratado() {
        return materialContratado;
    }

    public void setMaterialContratado(MaterialContratado materialContratado) {
        this.materialContratado = materialContratado;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
