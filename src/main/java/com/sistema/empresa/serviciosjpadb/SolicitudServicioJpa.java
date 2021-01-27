package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.SolicitudMaterial;
import com.sistema.empresa.repositorios.SolicitudRepositorio;
import com.sistema.empresa.serviciosinterface.ISolicitudServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudServicioJpa implements ISolicitudServicios {

    @Autowired
    private SolicitudRepositorio solicitudRepositorio;

    @Override
    public void guardar(SolicitudMaterial solicitudMaterial) {
        solicitudRepositorio.save(solicitudMaterial);
    }

    @Override
    public List<SolicitudMaterial> buscarTodas() {
        return solicitudRepositorio.findAll();
    }

    @Override
    public SolicitudMaterial buscarPorId(Integer id) {
        Optional<SolicitudMaterial> solicitudMaterial = solicitudRepositorio.findById(id);
        if(solicitudMaterial.isPresent()){
            return solicitudMaterial.get();
        }
        else{
            return null;
        }
    }

    @Override
    public void eliminar(int id) {
        solicitudRepositorio.deleteById(id);
    }
}
