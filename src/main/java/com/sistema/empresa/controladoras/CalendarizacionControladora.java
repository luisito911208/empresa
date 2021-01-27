package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.Calendarizacion;
import com.sistema.empresa.modelos.MaterialContratado;
import com.sistema.empresa.modelos.MaterialDistribuido;
import com.sistema.empresa.serviciosinterface.ICalendarizacionServicios;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialContratadoServicios;
import com.sistema.empresa.util.Utileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/calendarizacion")
public class CalendarizacionControladora {

    @Autowired
    private ICalendarizacionServicios calendarizacionServicios;

    @Autowired
    private IMaterialContratadoServicios materialContratadoServicios;

    @GetMapping("/lista")
    public String mostrarLista(){
        return "calendarizacion/listarcalendarizacion";
    }

    @GetMapping("/agregar")
    public String modal(Calendarizacion calendarizacion, Model model){
        model.addAttribute("calendar", calendarizacion);
        return "calendarizacion/calendarizacion";
    }

    @PostMapping("/guardar")
    public String guardar(Calendarizacion calendarizacion){
        calendarizacionServicios.guardar(calendarizacion);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        Calendarizacion calendarizacion = calendarizacionServicios.buscarPorId(id);
        model.addAttribute("calendar", calendarizacion);
        return "calendarizacion/calendarizacion";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        calendarizacionServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        Calendarizacion calendarizacion = calendarizacionServicios.buscarPorId(id);
        model.addAttribute("calendar", calendarizacion);
        return "calendarizacion/eliminar";
    }

    // Para controlar que la fecha que capture Data Binding este correcta
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }


    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("calendarizaciones",calendarizacionServicios.buscarTodas());
        model.addAttribute("materialescontratados",materialContratadoServicios.buscarTodas());
    }

}
