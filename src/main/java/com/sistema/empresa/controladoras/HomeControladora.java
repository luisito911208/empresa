package com.sistema.empresa.controladoras;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControladora {

    @GetMapping("/")
    public String mostrarHome(Model model) {
        return "home";
    }


}
