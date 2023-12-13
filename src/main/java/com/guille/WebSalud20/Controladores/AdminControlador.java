package com.guille.WebSalud20.Controladores;

import com.guille.WebSalud20.Entidades.Usuario;
import com.guille.WebSalud20.Servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panelAdmin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio us;

    @GetMapping("/usuarios")
    public String usuarios(@Param("palabra") String palabra, ModelMap modelo, ModelMap modelo2) {

        List<Usuario> usuarios = us.listarUsuario(palabra);
        modelo.addAttribute("usuarios", usuarios);


        modelo.addAttribute("palabra", palabra);

        return "usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUs(@PathVariable String id) {

        us.eliminarUsuario(id);

        return "redirect:/dashboard/usuarios";
    }
}
