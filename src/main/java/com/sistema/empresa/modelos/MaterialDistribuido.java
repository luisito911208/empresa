package com.sistema.empresa.modelos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "materialesdistribuidos")
public class MaterialDistribuido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMaterialDistribuido;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fecha;
    private double cantidad;
    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idMaterialRecibido", nullable = false)
    private MaterialRecibido materialRecibido;


    public int getIdMaterialDistribuido() {
        return idMaterialDistribuido;
    }

    public void setIdMaterialDistribuido(int idMaterialDistribuido) {
        this.idMaterialDistribuido = idMaterialDistribuido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public MaterialRecibido getMaterialRecibido() {
        return materialRecibido;
    }

    public void setMaterialRecibido(MaterialRecibido materialRecibido) {
        this.materialRecibido = materialRecibido;
    }
}
