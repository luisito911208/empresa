package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.Proveedor;

import java.util.List;

public interface IProveedorServicios {
    void guardar(Proveedor proveedor);
    List<Proveedor> buscarTodas();
    Proveedor buscarPorId(Integer id);
    void eliminar(int id);
}
