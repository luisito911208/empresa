package com.sistema.empresa.modelos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "solicitudmateriales")
public class SolicitudMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSolicitud;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaSolicitud;
    private double cantidad;
    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idMaterialContratado" ,nullable = false)
    private MaterialContratado materialContratado;


    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
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
