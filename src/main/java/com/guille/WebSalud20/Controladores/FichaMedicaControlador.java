package com.guille.WebSalud20.Controladores;

import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Servicios.PacienteServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/historia_clinica")
public class FichaMedicaControlador {

    @Autowired
    PacienteServicio pacienteServicio;


    @GetMapping("/ficha_medica")
    public String verFichaMedica(HttpSession session, ModelMap model) {

        Paciente pacienteActualizado = (Paciente) session.getAttribute("pacienteActualizado");
        session.removeAttribute("pacienteActualizado");
        model.addAttribute("paciente", pacienteActualizado);


        return "paciente_fichaMedica";

    }

    @GetMapping("/fichaMedicaProfesional")
    public String verFichaMedicaProfesional(@RequestParam String id, ModelMap model) {

        Paciente paciente = pacienteServicio.getOne(id);

        model.addAttribute("paciente", paciente);

        return "profesional_fichaMedica";
    }


}
