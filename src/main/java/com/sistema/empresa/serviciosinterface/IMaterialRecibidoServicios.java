package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.MaterialRecibido;

import java.util.List;

public interface IMaterialRecibidoServicios {
    void guardar(MaterialRecibido material);
    List<MaterialRecibido> buscarTodas();
    MaterialRecibido buscarPorId(Integer id);
    MaterialRecibido buscarPorMaterial(String material);
    void eliminar(int id);
}
