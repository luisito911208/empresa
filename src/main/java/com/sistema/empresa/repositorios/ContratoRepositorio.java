package com.sistema.empresa.repositorios;

import com.sistema.empresa.modelos.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepositorio extends JpaRepository<Contrato,Integer> {
}
