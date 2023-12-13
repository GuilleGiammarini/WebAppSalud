package com.guille.WebSalud20.Controladores;

import com.guille.WebSalud20.Entidades.Consulta;
import com.guille.WebSalud20.Entidades.FichaMedica;
import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Entidades.Profesional;
import com.guille.WebSalud20.Entidades.Turno;
import com.guille.WebSalud20.Enumeracion.Provincias;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.ConsultaRepositorio;
import com.guille.WebSalud20.Repositorios.PacienteRepositorio;
import com.guille.WebSalud20.Servicios.ConsultaServicio;
import com.guille.WebSalud20.Servicios.FichaMedicaServicio;
import com.guille.WebSalud20.Servicios.PacienteServicio;
import com.guille.WebSalud20.Servicios.TurnoServicio;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consulta")
public class ConsultaControlador {

    @Autowired
    PacienteRepositorio pacienteRepositorio;
    @Autowired
    PacienteServicio pacienteServicio;
    @Autowired
    FichaMedicaServicio fichaMedicaServicio;
    @Autowired
    ConsultaServicio consultaServicio;
    @Autowired
    TurnoServicio turnoServicio;
    @Autowired
    private ConsultaServicio cs;
    @Autowired
    private ConsultaRepositorio cr;

    @GetMapping("/crear/{id}")
    public String crearConsulta(@PathVariable String id, String idTurno, ModelMap modelo) throws MiException {

        Turno turno = turnoServicio.obtenerTurnoPorId(idTurno);
        Paciente paciente = pacienteServicio.getOne(id);
        FichaMedica fichaMedica = fichaMedicaServicio.buscarPorIdPaciente(id);

        modelo.put("turno", turno);

        modelo.put("fichaMedica", fichaMedica);

        modelo.put("paciente", paciente);


        return "profesional_consulta";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN', 'ROLE_PACIENTE')")
    @PostMapping("/crear/{id}")
    public String crearConsulta(@PathVariable String id, HttpSession session,
                                String obraSocial, Long afiliado,
                                @RequestParam String idTurno,
                                @RequestParam String antecedentes, @RequestParam String grupoSanguineo,
                                @RequestParam Double altura, @RequestParam Double peso,
                                String observaciones, @RequestParam String diagnostico,
                                @RequestParam String tratamiento, @RequestParam String motivoConsulta, ModelMap modelo) throws MiException {

        Paciente paciente = pacienteServicio.getOne(id);
        Profesional profesional = (Profesional) session.getAttribute("session");
        Turno turno = turnoServicio.obtenerTurnoPorId(idTurno);


        Date fecha = new Date();
        LocalTime horario = LocalTime.now();

        try {
            cs.crearConsulta(motivoConsulta, paciente, profesional, obraSocial, afiliado, antecedentes, grupoSanguineo, altura, peso,
                    observaciones, diagnostico, tratamiento, fecha, horario);
            turnoServicio.atenderTurno(turno);
            modelo.put("exito", "La consulta fue creada con exito");
            return "redirect:/profesional/citasProfesional";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("paciente", pacienteServicio.getOne(id));
            return "redirect:/consulta/crear/{id}";
        }
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> listarConsultas() {
        List<Consulta> consultas = cs.listarConsultas();
        return new ResponseEntity<>(consultas, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificarConsulta(@PathVariable String id, ModelMap modelo) {
        new Consulta();

        Consulta consulta = cs.getOne(id);
        modelo.addAttribute("consulta", consulta);
        return null;
    }

    @PostMapping("/motivo")
    public String crearMotivo(@RequestParam String motivoConsulta, HttpSession session, ModelMap modelMap) {

        consultaServicio.crearConsulta(motivoConsulta, session.getId());

        modelMap.addAttribute("consulta", cr.findByPacienteId(session.getId()));

        return "redirect:/listaProfesionales";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificarConsulta(@PathVariable String id, @RequestParam Paciente paciente, @RequestParam Profesional profesional,
                                    @RequestParam("fechaDeConsulta") String fechaDeConsultaStr, @RequestParam Provincias provincias, @RequestParam String localidad, @RequestParam String direccion,
                                    @RequestParam int precioConsulta, @RequestParam int valoracion, ModelMap modelo) {

        try {
            cs.modificarConsulta(id, paciente, profesional, provincias, localidad, direccion, precioConsulta);
            modelo.put("exito", "Consulta modificada con exito");
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return null;
        }
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @PostMapping("/eliminar/{id}")
    public String eliminarConsulta(@PathVariable String id) {
        cr.deleteById(id);
        return null;
    }
}
