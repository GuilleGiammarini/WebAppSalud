package com.guille.WebSalud20.Controladores;

import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Entidades.Turno;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Servicios.ConsultaServicio;
import com.guille.WebSalud20.Servicios.PacienteServicio;
import com.guille.WebSalud20.Servicios.ProfesionalServicio;
import com.guille.WebSalud20.Servicios.TurnoServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequestMapping("/paciente")
public class PacienteControlador {

    @Autowired
    PacienteServicio pacienteServicio;

    @Autowired
    ProfesionalServicio profesionalServicio;

    @Autowired
    ConsultaServicio consultaServicio;

    @Autowired
    TurnoServicio turnoServicio;

    @GetMapping("/solicitar/{id}")
    public String solicitarCita(@PathVariable String id, ModelMap modelo, HttpSession session) {

        if (session.getAttribute("usuariosession") == null) {
            return "login";
        } else {

//            Consulta consulta = consultaServicio.buscarPorIdPaciente(session.getId());

//            modelo.addAttribute("consulta",consulta);

            var profesional = profesionalServicio.getOne(id);
            if (profesional != null) {

                List<Turno> turnos = turnoServicio.obtenerTurnosDeProfesionalOrdenados(profesional);
                modelo.addAttribute("turnos", turnos);
                modelo.addAttribute("profesional", profesional);

                return "cita_solicitud";
            } else {
                return "error";
            }
        }
    }

    @PostMapping("/solicitar/{id}")
    public String reservarCita(@PathVariable String id, @RequestParam String idProfesional,
                               @RequestParam Date fecha, @RequestParam LocalTime horario,
                               HttpServletRequest request, ModelMap model) throws MiException, ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = dateFormat.parse(request.getParameter("fecha"));

        var Paciente = pacienteServicio.getOne(id);
        var Profesional = profesionalServicio.getOne(idProfesional);

//        consultaServicio.crearConsulta(Paciente, Profesional, fechaDate, horario);

        return "redirect:/paciente/citas";
    }

    @GetMapping("/historia")
    public String historiaClinica(HttpSession session, ModelMap modelMap) {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");

        var consultas = consultaServicio.obtenerConsultasDelPaciente(paciente);

        modelMap.addAttribute("consultas", consultas);
        return "historia_clinica";
    }


    //    MODIFICAR DATOS COMO ADMIN
    @GetMapping("/modificar/{id}")
    public String modificarPaciente() {
        return "usuarioModificar";
    }
//    MODIFICAR DATOS COMO PACIENTE
    @GetMapping("/editar")
    public String editarPaciente(ModelMap modelo, HttpSession session) {

        Paciente pacienteActualizado = (Paciente) session.getAttribute("pacienteActualizado");
        session.removeAttribute("pacienteActualizado");
        modelo.addAttribute("paciente", pacienteActualizado);

        return "paciente_edit";
    }

    @PostMapping("/editar/{id}")
    public String editarPaciente(@PathVariable String id, @RequestParam String nombreUsuario, @RequestParam String nombre, @RequestParam String apellido,
                                 @RequestParam Long DNI, @RequestParam("fechaDeNacimiento") String fechaDeNacimientoStr, @RequestParam String email, @RequestParam String password,
                                 @RequestParam String password2,
                                 MultipartFile archivo, ModelMap modelo, HttpSession session) {

        Date fechaDeNacimiento;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fechaDeNacimiento = dateFormat.parse(fechaDeNacimientoStr);

        } catch (ParseException p) {
            modelo.addAttribute("error", "La fecha no puede venir vacía");
            return "redirect:/paciente/editar";
        }

        try {
            pacienteServicio.modificarPacientes(archivo, id, nombreUsuario, nombre, apellido, DNI, fechaDeNacimiento, email, password, password2);
            modelo.addAttribute("exito", "cambios realizados con éxito");

            Paciente pacienteActualizado = pacienteServicio.getOne(id);
            session.setAttribute("pacienteActualizado", pacienteActualizado);

        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "redirect:/paciente/editar";
        }
        return "redirect:/";
    }

    @GetMapping("/citas")
    public String listarCitas(ModelMap modelMap, HttpSession session) {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");
        if (paciente == null) {
            return "redirect:/portal/login";
        }

        var misTurnos = turnoServicio.obtenerTurnosDelPaciente(paciente);
        modelMap.addAttribute("misTurnos", misTurnos);
        return "paciente_citas";
    }
}
