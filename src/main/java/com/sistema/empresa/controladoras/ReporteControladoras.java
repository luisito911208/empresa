package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.MaterialRecibido;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialRecibidoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reporte")
public class ReporteControladoras {

    @Autowired
    private IMaterialRecibidoServicios materialRecibidoServicios;

    @Autowired
    private IContratoServicios contratoServicios;

    @GetMapping("/avancefisico")
    public String modalAvanceFisico(Model model) {
        return "reportes/avancefisico";
    }

    @GetMapping("/generaravancefisico")
    public String generarFisico(Model model) {
        return "redirect:/";
    }

    @GetMapping("/concentrado")
    public String modalConcentrado(Model model) {

        return "reportes/concentrado";
    }

    @GetMapping("/generarconcentrado")
    public String generarConcentrado(Model model) {



        return "redirect:/";
    }

    public List<String> meses(){
        List<String> lista = new ArrayList<>();
        for (MaterialRecibido material:materialRecibidoServicios.buscarTodas()) {
            if(material.getFechaEntrega().getMonth() == 0){
                String mes = "Enero";
                lista.add(mes);
            }
            else{
                if(material.getFechaEntrega().getMonth() == 1){
                    String mes = "Febrero";
                    lista.add(mes);
                }
                else{
                    if(material.getFechaEntrega().getMonth() == 2){
                        String mes = "Marzo";
                        lista.add(mes);
                    }
                    else{
                        if(material.getFechaEntrega().getMonth() == 3) {
                            String mes = "Abril";
                            lista.add(mes);
                        }
                        else {
                            if(material.getFechaEntrega().getMonth() == 4){
                                String mes = "Mayo";
                                lista.add(mes);
                            }
                            else {
                                if(material.getFechaEntrega().getMonth() == 5){
                                    String mes = "Junio";
                                    lista.add(mes);
                                }
                                else {
                                    if(material.getFechaEntrega().getMonth() == 6){
                                        String mes = "Julio";
                                        lista.add(mes);
                                    }
                                    else {
                                        if(material.getFechaEntrega().getMonth() == 7){
                                            String mes = "Agosto";
                                            lista.add(mes);
                                        }
                                        else {
                                            if(material.getFechaEntrega().getMonth() == 8){
                                                String mes = "Septiembre";
                                                lista.add(mes);
                                            }
                                            else {
                                                if(material.getFechaEntrega().getMonth() == 9){
                                                    String mes = "Octubre";
                                                    lista.add(mes);
                                                }
                                                else {
                                                    if(material.getFechaEntrega().getMonth() == 10){
                                                        String mes = "Noviembre";
                                                        lista.add(mes);
                                                    }
                                                    else {
                                                        if(material.getFechaEntrega().getMonth() == 11){
                                                            String mes = "Diciembre";
                                                            lista.add(mes);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
        lista = lista.stream().distinct().collect(Collectors.toList());
        return lista;
    }

    public List<String> anno(){
        List<String> lista = new ArrayList<>();
        for (MaterialRecibido material:materialRecibidoServicios.buscarTodas()) {
                String mes = material.getFechaEntrega().toString().substring(0,4);
                lista.add(mes);

        }
        lista = lista.stream().distinct().collect(Collectors.toList());
        return lista;
    }

    public List<String> material(){
        List<String> lista = new ArrayList<>();
        for (MaterialRecibido material:materialRecibidoServicios.buscarTodas()) {
            lista.add(material.getMaterialContratado().getNombreMaterial());

        }
        lista = lista.stream().distinct().collect(Collectors.toList());
        return lista;
    }
    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("anno",anno());
        model.addAttribute("meses",meses());
        model.addAttribute("materiales",material());
        model.addAttribute("contratos",contratoServicios.buscarTodas());



    }




}
