package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.MaterialRecibido;
import com.sistema.empresa.repositorios.MaterialRecibidoRepositorio;
import com.sistema.empresa.serviciosinterface.IMaterialRecibidoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialRecibidoServiciosJpa implements IMaterialRecibidoServicios {

    @Autowired
    private MaterialRecibidoRepositorio materialRecibidoRepositorio;

    @Override
    public void guardar(MaterialRecibido material) {
        materialRecibidoRepositorio.save(material);
    }

    @Override
    public List<MaterialRecibido> buscarTodas() {
        return materialRecibidoRepositorio.findAll();
    }

    @Override
    public MaterialRecibido buscarPorId(Integer id) {
        Optional<MaterialRecibido> material = materialRecibidoRepositorio.findById(id);
        if(material.isPresent()){
            return material.get();
        }
        else{
            return null;
        }
    }

    @Override
    public void eliminar(int id) {
        materialRecibidoRepositorio.deleteById(id);
    }
}
