package com.guille.WebSalud20.Servicios;


import com.guille.WebSalud20.Entidades.Consulta;
import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Entidades.Profesional;
import com.guille.WebSalud20.Enumeracion.Provincias;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.ConsultaRepositorio;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaServicio {

    @Autowired
    private ConsultaRepositorio consultaRepositorio;

    @Autowired
    private FichaMedicaServicio fichaMedicaServicio;

    @Autowired
    private PacienteServicio pacienteServicio;

    @Autowired
    private TurnoServicio turnoServicio;


    @Transactional
    public void crearConsulta(String motivoConsulta, Paciente paciente, Profesional profesional, String obraSocial, Long afiliado, String antecedentes, String grupoSanguineo, Double altura,
                              Double peso, String observaciones, String diagnostico, String tratamiento, Date fecha, LocalTime horario) throws MiException {

//        validar(paciente, profesional);

        Consulta consulta = new Consulta();

        consulta.setPaciente(paciente);
        consulta.setMotivoConsulta(motivoConsulta);
        consulta.setProfesional(profesional);
        consulta.setFechaDeConsulta(fecha);
        consulta.setHoraInicio(horario);
        consulta.setDiagnostico(diagnostico);
        consulta.setTratamiento(tratamiento);
        consulta.setObservaciones(observaciones);
        consulta.setAtendido(true);

        fichaMedicaServicio.modificarFichaMedica(paciente, antecedentes, obraSocial, afiliado, grupoSanguineo, altura, peso);
        consultaRepositorio.save(consulta);

    }

    public void crearConsulta(String motivoConsulta, String id) {

        Paciente paciente = pacienteServicio.getOne(id);

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMotivoConsulta(motivoConsulta);

        consultaRepositorio.save(consulta);
    }

    @Transactional
    public void modificarConsulta(String id, Paciente paciente, Profesional profesional, Provincias provincias, String localidad, String direccion, int precioConsulta) throws MiException {
        validar(paciente, profesional);

        Optional<Consulta> respuesta = consultaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Consulta consulta = respuesta.get();
            consulta.setPaciente(paciente);
            consulta.setProfesional(profesional);
            consulta.setProvincias(provincias);
            consulta.setLocalidad(localidad);
            consulta.setDireccion(direccion);

            consulta.setPrecioConsulta(precioConsulta);
            consulta.setFechaDeConsulta(new Date());
            consultaRepositorio.save(consulta);
        }

    }

    @Transactional
    public void eliminarConsulta(String id) {
        consultaRepositorio.deleteById(id);
    }


    private void validar(Paciente paciente, Profesional profesional) throws MiException {
        if (paciente == null) {
            throw new MiException("El paciente  no puede ser Nulo");
        }

        if (profesional == null) {
            throw new MiException("El profesional no puede ser nulo");
        }
    }

    public List<Consulta> obtenerConsultasDelPaciente(Paciente paciente) {
        return consultaRepositorio.findByPaciente(paciente);
    }

    public List<Consulta> listarConsultas() {

        return consultaRepositorio.findAll();
    }

    public Consulta getOne(String id) {
        return consultaRepositorio.getOne(id);
    }


    public Consulta buscarPorIdPaciente(String id) {

        return (Consulta) consultaRepositorio.findByPacienteId(id);

    }

    public List<Consulta> listarConsultasPorIdPaciente(String id) {

        return consultaRepositorio.listarPorIdPaciente(id);
    }
}
