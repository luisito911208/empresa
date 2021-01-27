package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.MaterialContratado;

import java.util.List;

public interface IMaterialContratadoServicios {
    void guardar(MaterialContratado materialContratado);
    List<MaterialContratado> buscarTodas();
    MaterialContratado buscarPorId(Integer id);
    void eliminar(int id);
}
