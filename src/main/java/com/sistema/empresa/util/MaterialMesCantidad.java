package com.sistema.empresa.util;

public class MaterialMesCantidad implements Comparable<MaterialMesCantidad> {
    private Integer mes;
    private String material;
    private double cantidad;
    private String unidadmedida;
    private double preciounitario;

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadmedida() {
        return unidadmedida;
    }

    public void setUnidadmedida(String unidadmedida) {
        this.unidadmedida = unidadmedida;
    }

    public double getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(double preciounitario) {
        this.preciounitario = preciounitario;
    }

    @Override
    public int compareTo(MaterialMesCantidad o) {
        return mes.compareTo(o.mes);
    }
}
