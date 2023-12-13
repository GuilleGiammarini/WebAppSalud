package com.guille.WebSalud20.Controladores;

import com.guille.WebSalud20.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdmin(ModelMap modelo) {

        var usuarios = usuarioServicio.listarTodos();

        modelo.addAttribute("usuarios", usuarios);

        return "dashboard";
    }
}
