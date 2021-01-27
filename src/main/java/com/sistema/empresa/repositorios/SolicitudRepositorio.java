package com.sistema.empresa.repositorios;

import com.sistema.empresa.modelos.SolicitudMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepositorio  extends JpaRepository<SolicitudMaterial,Integer> {

}
