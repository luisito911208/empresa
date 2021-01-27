package com.sistema.empresa.serviciosinterface;

import com.sistema.empresa.modelos.Calendarizacion;
import com.sistema.empresa.modelos.MaterialRecibido;

import java.util.List;

public interface ICalendarizacionServicios {
    void guardar(Calendarizacion calendarizacion);
    List<Calendarizacion> buscarTodas();
    Calendarizacion buscarPorId(Integer id);
    void eliminar(int id);
}
