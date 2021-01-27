package com.sistema.empresa.controladoras;

import com.sistema.empresa.modelos.Proveedor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioControladora {

    @GetMapping("/lista")
    public String mostrarLista(){
        return "usuarios/listarusuarios";
    }

    @GetMapping("/agregar")
    public String modalProveedor(Proveedor proveedor){
        return "usuarios/usuario";
    }


}
