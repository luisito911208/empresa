package com.sistema.empresa.repositorios;

import com.sistema.empresa.modelos.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepositorio extends JpaRepository<Proveedor,Integer> {
}
