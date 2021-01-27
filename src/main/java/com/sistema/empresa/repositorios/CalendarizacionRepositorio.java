package com.sistema.empresa.repositorios;

import com.sistema.empresa.modelos.Calendarizacion;
import com.sistema.empresa.modelos.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarizacionRepositorio extends JpaRepository<Calendarizacion,Integer> {
}
