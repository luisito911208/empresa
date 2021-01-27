package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.MaterialContratado;
import com.sistema.empresa.repositorios.MaterialContratadoRepositorio;
import com.sistema.empresa.serviciosinterface.IMaterialContratadoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialContratadoServiciosJpa implements IMaterialContratadoServicios {

    @Autowired
    private MaterialContratadoRepositorio materialContratadoRepositorio;

    @Override
    public void guardar(MaterialContratado materialContratado) {
        materialContratadoRepositorio.save(materialContratado);
    }

    @Override
    public List<MaterialContratado> buscarTodas() {
        return materialContratadoRepositorio.findAll();
    }

    @Override
    public MaterialContratado buscarPorId(Integer id) {
        Optional<MaterialContratado> material = materialContratadoRepositorio.findById(id);
        if(material.isPresent()){
            return material.get();
        }
        else{
            return null;
        }
    }

    @Override
    public void eliminar(int id) {
        materialContratadoRepositorio.deleteById(id);
    }
}
