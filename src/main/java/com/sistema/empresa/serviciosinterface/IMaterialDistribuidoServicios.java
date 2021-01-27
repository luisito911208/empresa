package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.MaterialContratado;
import com.sistema.empresa.modelos.MaterialDistribuido;

import java.util.List;

public interface IMaterialDistribuidoServicios {
    void guardar(MaterialDistribuido materialDistribuido);
    List<MaterialDistribuido> buscarTodas();
    MaterialDistribuido buscarPorId(Integer id);
    void eliminar(int id);
}
