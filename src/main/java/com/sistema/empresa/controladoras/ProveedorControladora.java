package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.Proveedor;
import com.sistema.empresa.serviciosinterface.IProveedorServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControladora {

    @Autowired
    private IProveedorServicios proveedorServicios;

    @GetMapping("/lista")
    public String mostrarLista(){
        return "proveedor/listarproveedor";
    }

    @GetMapping("/agregar")
    public String modal(Proveedor proveedor){
        return "proveedor/proveedor";
    }

    @PostMapping("/guardar")
    public String guardar(Proveedor proveedor, RedirectAttributes attributes){
        proveedorServicios.guardar(proveedor);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        Proveedor proveedor = proveedorServicios.buscarPorId(id);
        model.addAttribute("proveedor",proveedor);
        return "proveedor/proveedor";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        proveedorServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        Proveedor proveedor = proveedorServicios.buscarPorId(id);
        model.addAttribute("proveedor",proveedor);
        return "proveedor/eliminarproveedor";
    }

//    Lista
    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("proveedores",proveedorServicios.buscarTodas());

    }

}
