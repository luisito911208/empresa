package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.MaterialContratado;
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
@RequestMapping("/materialcontratado")
public class MaterialContratadoControladora {

    @Autowired
    private IMaterialContratadoServicios materialContratadoServicios;

    @Autowired
    private IContratoServicios contratoServicios;

    @GetMapping("/lista")
    public String mostrarLista(){
        return "materialescontratados/listarmaterialcontratado";
    }

    @GetMapping("/agregar")
    public String modal(MaterialContratado materialContratado,Model model){
        model.addAttribute("materialcontratado", materialContratado);
        return "materialescontratados/materialcontratado";
    }

    @PostMapping("/guardar")
    public String guardar(MaterialContratado materialContratado){
        materialContratado.setSubtotal(materialContratado.getCantidad()* materialContratado.getPrecio());
        materialContratado.setSubtotaliva((materialContratado.getSubtotal()* materialContratado.getIva())/100);
        materialContratado.setTotal(materialContratado.getSubtotal()+ materialContratado.getSubtotaliva());
        materialContratadoServicios.guardar(materialContratado);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        MaterialContratado materialContratado = materialContratadoServicios.buscarPorId(id);
        model.addAttribute("materialcontratado", materialContratado);
        return "materialescontratados/materialcontratado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        materialContratadoServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        MaterialContratado materialContratado = materialContratadoServicios.buscarPorId(id);
        model.addAttribute("materialcontratado", materialContratado);
        return "materialescontratados/eliminar";
    }

    // Para controlar que la fecha que capture Data Binding este correcta
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
    //    Lista
    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("materialescontratados",materialContratadoServicios.buscarTodas());
        model.addAttribute("contratos",contratoServicios.buscarTodas());

    }
}
