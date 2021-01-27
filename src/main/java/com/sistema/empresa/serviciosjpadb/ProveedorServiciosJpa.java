package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.Proveedor;
import com.sistema.empresa.repositorios.ProveedorRepositorio;
import com.sistema.empresa.serviciosinterface.IProveedorServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiciosJpa implements IProveedorServicios {

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Override
    public void guardar(Proveedor proveedor) {
        proveedorRepositorio.save(proveedor);
    }

    @Override
    public List<Proveedor> buscarTodas() {
        return proveedorRepositorio.findAll();
    }

    @Override
    public Proveedor buscarPorId(Integer idProveedor) {
         Optional<Proveedor> proveedor = proveedorRepositorio.findById(idProveedor);
         if(proveedor.isPresent()){
             return proveedor.get();
         }
         else{
             return null;
         }
    }

    @Override
    public void eliminar(int id) {
        proveedorRepositorio.deleteById(id);
    }

}
