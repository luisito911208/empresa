package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.Contrato;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IProveedorServicios;
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
@RequestMapping("/contrato")
public class ContratosControladora {

    @Value("${empresa.ruta.imagenes}")
    private String ruta;

    @Autowired
    private IProveedorServicios proveedorServicios;

    @Autowired
    private IContratoServicios contratoProveedorServicios;

    @GetMapping("/lista")
    public String mostrarLista(){
        return "contrato/listarcontratos";
    }

    @GetMapping("/agregar")
    public String modal(Contrato contrato, Model model){
        model.addAttribute("contrato", contrato);
        return "contrato/contrato";
    }

    @PostMapping("/guardar")
    public String guardar(Contrato contrato, @RequestParam("archivoImagen") MultipartFile multiPart){
        if (!multiPart.isEmpty()) {
            String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
            if (nombreImagen != null){ // La imagen si se subio
                contrato.setImagen(nombreImagen);
            }
        }

        contratoProveedorServicios.guardar(contrato);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        Contrato contrato = contratoProveedorServicios.buscarPorId(id);
        model.addAttribute("contrato", contrato);
        return "contrato/contrato";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        contratoProveedorServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        Contrato contrato = contratoProveedorServicios.buscarPorId(id);
        model.addAttribute("contrato", contrato);
        return "contrato/eliminarcontrato";
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
        model.addAttribute("contratos",contratoProveedorServicios.buscarTodas());
        model.addAttribute("proveedores",proveedorServicios.buscarTodas());

    }
}
