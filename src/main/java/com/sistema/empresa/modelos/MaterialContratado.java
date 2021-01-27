package com.sistema.empresa.modelos;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "materialescontratados")
public class MaterialContratado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMaterial;
    private String nombreMaterial;
    private String unidadMedida;
    private double cantidad;
    private double precio;
    private double iva;
    @NumberFormat(pattern="#0.00")
    private double subtotal;
    @NumberFormat(pattern="#0.00")
    private double subtotaliva;
    @NumberFormat(pattern="#0.00")
    private double total;
    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="contratosidContrato", nullable=false)
    private Contrato contrato;

    @OneToMany(mappedBy = "materialContratado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SolicitudMaterial> solicitud;

    @OneToMany(mappedBy = "materialContratado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MaterialRecibido> materialRecibidos;

    @OneToMany(mappedBy = "materialContratado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Calendarizacion> calendarizacion;

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getSubtotaliva() {
        return subtotaliva;
    }

    public void setSubtotaliva(double subtotaliva) {
        this.subtotaliva = subtotaliva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Set<SolicitudMaterial> getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Set<SolicitudMaterial> solicitud) {
        this.solicitud = solicitud;
    }

    public Set<MaterialRecibido> getMaterialRecibidos() {
        return materialRecibidos;
    }

    public void setMaterialRecibidos(Set<MaterialRecibido> materialRecibidos) {
        this.materialRecibidos = materialRecibidos;
    }

    public Set<Calendarizacion> getCalendarizacion() {
        return calendarizacion;
    }

    public void setCalendarizacion(Set<Calendarizacion> calendarizacions) {
        this.calendarizacion = calendarizacions;
    }
}
