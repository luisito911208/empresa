package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.MaterialRecibido;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialContratadoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialRecibidoServicios;
import com.sistema.empresa.serviciosinterface.IProveedorServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/materialrecibido")
public class MaterialRecibidoControladora {

    @Autowired
    private IMaterialRecibidoServicios materialRecibidoServicios;

    @Autowired
    private IMaterialContratadoServicios materialContratadoServicios;

    @Autowired
    private IProveedorServicios proveedorServicios;

    @GetMapping("/lista")
    public String mostrarLista(){
        return "materialesrecibidos/listarmaterialrecibido";
    }


    @GetMapping("/agregar")
    public String modal(MaterialRecibido materialRecibido, Model model){
        model.addAttribute("recibido", materialRecibido);
        return "materialesrecibidos/materialrecibido";
    }

    @PostMapping("/guardar")
    public String guardar(MaterialRecibido material){
        materialRecibidoServicios.guardar(material);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        MaterialRecibido materialRecibido = materialRecibidoServicios.buscarPorId(id);
        model.addAttribute("recibido", materialRecibido);
        return "materialesrecibidos/materialrecibido";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        materialRecibidoServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        MaterialRecibido materialRecibido = materialRecibidoServicios.buscarPorId(id);
        model.addAttribute("recibido", materialRecibido);
        return "materialesrecibidos/eliminar";
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
        model.addAttribute("materialescontratados",materialContratadoServicios.buscarTodas());
        model.addAttribute("proveedores",proveedorServicios.buscarTodas());


    }

}
