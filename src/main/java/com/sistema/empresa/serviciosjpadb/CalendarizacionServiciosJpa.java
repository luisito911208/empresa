package com.sistema.empresa.serviciosjpadb;

import com.sistema.empresa.modelos.Calendarizacion;
import com.sistema.empresa.modelos.Contrato;
import com.sistema.empresa.repositorios.CalendarizacionRepositorio;
import com.sistema.empresa.repositorios.ContratoRepositorio;
import com.sistema.empresa.serviciosinterface.ICalendarizacionServicios;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarizacionServiciosJpa implements ICalendarizacionServicios {

    @Autowired
    private CalendarizacionRepositorio calendarizacionRepositorio;

    @Override
    public void guardar(Calendarizacion calendarizacion) {
        calendarizacionRepositorio.save(calendarizacion);
    }

    @Override
    public List<Calendarizacion> buscarTodas() {
        List<Calendarizacion> lista = calendarizacionRepositorio.findAll();
        return lista;
    }

    @Override
    public Calendarizacion buscarPorId(Integer id) {
        Optional<Calendarizacion> calendarizacion = calendarizacionRepositorio.findById(id);
        if(calendarizacion.isPresent()){
            return calendarizacion.get();
        }
        else{
            return null;
        }
    }

    @Override
    public void eliminar(int id) {
        calendarizacionRepositorio.deleteById(id);
    }

}
