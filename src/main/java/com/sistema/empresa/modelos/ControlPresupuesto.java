package com.sistema.empresa.modelos;

public class ControlPresupuesto {

    private Contrato contrato;
    private double montoinicio;
    private double montoUtilizado;
    private double montoFinal;

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public double getMontoinicio() {
        return montoinicio;
    }

    public void setMontoinicio(double montoinicio) {
        this.montoinicio = montoinicio;
    }

    public double getMontoUtilizado() {
        return montoUtilizado;
    }

    public void setMontoUtilizado(double montoUtilizado) {
        this.montoUtilizado = montoUtilizado;
    }

    public double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }
}
