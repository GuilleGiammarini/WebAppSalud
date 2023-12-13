package com.guille.WebSalud20.Controladores;


import com.guille.WebSalud20.Entidades.Usuario;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.UsuarioRepositorio;
import com.guille.WebSalud20.Servicios.ProfesionalServicio;
import com.guille.WebSalud20.Servicios.UsuarioServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/portal")
public class PortalControlador {

    @Autowired
    public UsuarioServicio us;

    @Autowired
    public ProfesionalServicio profesionalServicio;

    @Autowired
    public UsuarioRepositorio ur;


    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "El usuario o la contraseña son incorrectos");
        }
        return "login";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = new Usuario();
        usuario = us.getOne(id);
        modelo.addAttribute("usuario", usuario);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificarUsuario(@RequestParam String id, /*MultipartFile archivo,*/@RequestParam String nombreUsuario, @RequestParam String nombre, @RequestParam String apellido, @RequestParam(required = false) Long DNI, @RequestParam("fechaDeNacimiento") String fechaDeNacimientoStr, @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam boolean activo, ModelMap modelo) throws MiException {

        Date fechaDeNacimiento;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fechaDeNacimiento = dateFormat.parse(fechaDeNacimientoStr);

        } catch (ParseException p) {
            modelo.put("error", "la fecha no puede venir vacía");
            return "redirect:/portal/registroUsuario";
        }

        try {

            us.modificarUsuario(id,/* archivo,*/  nombreUsuario, nombre, apellido, DNI, fechaDeNacimiento, email, password, password2, true);
            modelo.put("exito", "Usuario modificado con exito");

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return null;
        }
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        ur.deleteById(id);
        return "index";
    }
}
