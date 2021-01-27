package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.Contrato;
import com.sistema.empresa.repositorios.ContratoRepositorio;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratoServiciosJpa implements IContratoServicios {

    @Autowired
    private ContratoRepositorio contratoRepositorio;

    @Override
    public void guardar(Contrato contrato) {
        contratoRepositorio.save(contrato);
    }

    @Override
    public List<Contrato> buscarTodas() {
        List<Contrato> lista = contratoRepositorio.findAll();
        return lista;
    }

    @Override
    public Contrato buscarPorId(Integer id) {
        Optional<Contrato> contratoProveedor = contratoRepositorio.findById(id);
        if(contratoProveedor.isPresent()){
            return contratoProveedor.get();
        }
        else{
            return null;
        }
    }

    @Override
    public void eliminar(int id) {
        contratoRepositorio.deleteById(id);
    }

}
