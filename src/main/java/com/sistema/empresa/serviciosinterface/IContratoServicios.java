package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.Contrato;

import java.util.List;

public interface IContratoServicios {
    void guardar(Contrato contrato);
    List<Contrato> buscarTodas();
    Contrato buscarPorId(Integer id);
    void eliminar(int id);
}
