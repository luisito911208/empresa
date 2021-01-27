package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.MaterialContratado;
import com.sistema.empresa.modelos.MaterialDistribuido;
import com.sistema.empresa.modelos.MaterialRecibido;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialContratadoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialDistribuidoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialRecibidoServicios;
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
@RequestMapping("/materialdistribuido")
public class MaterialDistribuidoControladora {

    @Autowired
    private IMaterialRecibidoServicios materialRecibidoServicios;

    @Autowired
    private IMaterialDistribuidoServicios materialDistribuidoServicios;

    @GetMapping("/lista")
    public String mostrarLista(){
        return "materialesdistribuidos/listarmaterialdistribuido";
    }

    @GetMapping("/agregar")
    public String modal(MaterialDistribuido materialDistribuido, Model model){
        model.addAttribute("distribuido", materialDistribuido);
        return "materialesdistribuidos/materialdistribuido";
    }

    @PostMapping("/guardar")
    public String guardar(MaterialDistribuido material){
        materialDistribuidoServicios.guardar(material);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        MaterialDistribuido materialDistribuido = materialDistribuidoServicios.buscarPorId(id);
        model.addAttribute("distribuido", materialDistribuido);
        return "materialesdistribuidos/materialdistribuido";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        materialDistribuidoServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        MaterialDistribuido materialDistribuido = materialDistribuidoServicios.buscarPorId(id);
        model.addAttribute("distribuido", materialDistribuido);
        return "materialesdistribuidos/eliminar";
    }

    // Para controlar que la fecha que capture Data Binding este correcta
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }


    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("materialesrecibidos",materialRecibidoServicios.buscarTodas());
        model.addAttribute("materialesdistribuidos",materialDistribuidoServicios.buscarTodas());
    }
}
