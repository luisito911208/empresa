package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.Contrato;
import com.sistema.empresa.modelos.MaterialDistribuido;
import com.sistema.empresa.repositorios.ContratoRepositorio;
import com.sistema.empresa.repositorios.MaterialDistribuidoRepositorio;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialDistribuidoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialDistribuidoServiciosJpa implements IMaterialDistribuidoServicios {

    @Autowired
    private MaterialDistribuidoRepositorio materialDistribuidoRepositorio;

    @Override
    public void guardar(MaterialDistribuido materialDistribuido) {
        materialDistribuidoRepositorio.save(materialDistribuido);
    }

    @Override
    public List<MaterialDistribuido> buscarTodas() {
        List<MaterialDistribuido> lista = materialDistribuidoRepositorio.findAll();
        return lista;
    }

    @Override
    public MaterialDistribuido buscarPorId(Integer id) {
        Optional<MaterialDistribuido> materialDistribuido = materialDistribuidoRepositorio.findById(id);
        if(materialDistribuido.isPresent()){
            return materialDistribuido.get();
        }
        else{
            return null;
        }
    }

    @Override
    public void eliminar(int id) {
        materialDistribuidoRepositorio.deleteById(id);
    }

}
