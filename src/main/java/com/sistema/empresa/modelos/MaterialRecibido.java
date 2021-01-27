package com.sistema.empresa.modelos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "materialesrecibidos")
public class MaterialRecibido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMaterialRecibido;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaEntrega;
    private double cantidad;
    private String numeroDeFolio;
    private String codigo;
    private String imagen = "no-image.png";
    private String imagenMaterial = "no-image.png";
    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idMaterialContratado", nullable = false)
    private MaterialContratado materialContratado;

    @OneToMany(mappedBy = "materialRecibido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MaterialDistribuido> materialDistribuidos;

    public int getIdMaterialRecibido() {
        return idMaterialRecibido;
    }

    public void setIdMaterialRecibido(int idMaterialRecibido) {
        this.idMaterialRecibido = idMaterialRecibido;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getNumeroDeFolio() {
        return numeroDeFolio;
    }

    public void setNumeroDeFolio(String numeroDeFolio) {
        this.numeroDeFolio = numeroDeFolio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagenMaterial() {
        return imagenMaterial;
    }

    public void setImagenMaterial(String imagenMaterial) {
        this.imagenMaterial = imagenMaterial;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public MaterialContratado getMaterialContratado() {
        return materialContratado;
    }

    public void setMaterialContratado(MaterialContratado materialContratado) {
        this.materialContratado = materialContratado;
    }

    public Set<MaterialDistribuido> getMaterialDistribuidos() {
        return materialDistribuidos;
    }

    public void setMaterialDistribuidos(Set<MaterialDistribuido> materialDistribuidos) {
        this.materialDistribuidos = materialDistribuidos;
    }
}
