package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.SolicitudMaterial;

import java.util.List;

public interface ISolicitudServicios {
    void guardar(SolicitudMaterial solicitudMaterial);
    List<SolicitudMaterial> buscarTodas();
    SolicitudMaterial buscarPorId(Integer id);
    void eliminar(int id);
}
