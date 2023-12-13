package com.guille.WebSalud20.Servicios;


import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Entidades.Profesional;
import com.guille.WebSalud20.Entidades.Turno;
import com.guille.WebSalud20.Enumeracion.DiaSemana;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.TurnoRepositorio;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoServicio {

    @Autowired
    private TurnoRepositorio turnoRepositorio;

    @Transactional
    public List<Turno> generarTurnos(Profesional profesional) {
        List<Turno> turnos = new ArrayList<>();
        LocalTime horaActual = profesional.getHorarioEntrada();

        for (DiaSemana dia : profesional.getDiasDisponibles()) {
            String nombreDia = dia.getNombreEnCastellano();

            while (horaActual.isBefore(profesional.getHorarioSalida())) {
                Turno turno = new Turno();
                turno.setProfesional(profesional);
                turno.setDisponibilidad(true);
                turno.setFecha(nombreDia);
                turno.setHora(horaActual.toString());

                turnos.add(turno);
                horaActual = horaActual.plusMinutes(30);
            }
            horaActual = profesional.getHorarioEntrada();
        }

        turnoRepositorio.saveAll(turnos);
        return turnos;
    }

    @Transactional
    public void asignarTurnoAPaciente(String turnoId, Paciente paciente) {

        Optional<Turno> turnoOptional = turnoRepositorio.findById(turnoId);

        if (turnoOptional.isPresent()) {
            Turno turno = turnoOptional.get();

            if (turno.isDisponibilidad()) {
                turno.setPaciente(paciente);
                turno.setDisponibilidad(false);

                turnoRepositorio.save(turno);
            } else {
                throw new RuntimeException("El turno seleccionado ya no está disponible.");
            }

        } else {
            throw new RuntimeException("El turno seleccionado no existe.");
        }
    }

    public List<Turno> obtenerTurnosDelPaciente(Paciente paciente) {
        return turnoRepositorio.findByPaciente(paciente);
    }

    public List<Turno> obtenerTurnosDeProfesional(Profesional profesional) {
        return turnoRepositorio.findByProfesional(profesional);
    }

    public List<Turno> obtenerTodosLosTurnos() {
        return turnoRepositorio.findAll();
    }

    public List<Turno> obtenerTurnosDeProfesionalOrdenados(Profesional profesional) {
        return turnoRepositorio.findByProfesionalOrderByFechaAscHoraAsc(profesional);
    }

    public List<Turno> listarTurnos() {
        try {

            List<Turno> turnos = turnoRepositorio.findAll();
            return turnos;
        } catch (Exception e) {
            System.err.println("Error al listar los turnos: " + e.getMessage());
            return Collections.emptyList(); // Devuelve una lista vacía en caso de error
        }
    }

    public List<Paciente> obtenerNombresPacientesConTurnoPorMedico(String profesionalId) {
        List<Paciente> pacientes = turnoRepositorio.obtenerNombresPacientesConTurnoPorMedico(profesionalId);
        return pacientes;
    }

    @Transactional
    public void eliminarTurno(String id) {
        try {
            // Buscar un turno por su ID en la base de datos
            Optional<Turno> respuesta = turnoRepositorio.findById(id);

            // Verificar si hay un valor presente en el Optional "respuesta"
            // Si se encuentra el turno en la base de datos, eliminarlo
            respuesta.ifPresent(turno -> turnoRepositorio.delete(turno));
        } catch (Exception e) {
            System.err.println("No es posible eliminar el turno: " + e.getMessage());
        }
    }

    @Transactional
    public boolean actualizarDisponibilidad(String id, boolean nuevaDisponibilidad) {
        try {
            // Intentar buscar un turno por su ID en la base de datos
            Optional<Turno> respuesta = turnoRepositorio.findById(id);

            // Verificar si se encontró un turno con el ID proporcionado
            if (respuesta.isPresent()) {
                // Obtener el turno desde el Optional
                Turno turno = respuesta.get();

                // Actualizar la disponibilidad con el nuevo valor
                turno.setDisponibilidad(nuevaDisponibilidad);

                // Guardar los cambios en la base de datos
                turnoRepositorio.save(turno);

                return true; // Indicar que la actualización fue exitosa
            }

            // Si no se encontró un turno con el ID, retornar false
            return false;
        } catch (Exception e) {
            // Manejo de Exceptiones en caso de algún problema
            System.err.println("No es posible actualizar la disponibilidad: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean elegirTurno(String idTurno, Paciente paciente) throws MiException {
        try {
            // Buscar el turno por su ID en la base de datos
            Optional<Turno> respuesta = turnoRepositorio.findById(idTurno);

            // Verificar si se encontró un turno con el ID proporcionado
            if (respuesta.isPresent()) {
                // Obtener el turno desde el Optional
                Turno turno = respuesta.get();

                // Verificar si el turno está disponible
                if (!turno.isDisponibilidad()) {
                    throw new MiException("El turno no está disponible.");
                }

                // Asociar el paciente al turno
                turno.setPaciente(paciente);

                // Actualizar la disponibilidad del turno
                turno.setDisponibilidad(false);

                // Guardar los cambios en la base de datos
                turnoRepositorio.save(turno);

                return true; // Indicar que la elección fue exitosa
            }

            // Si no se encontró un turno con el ID, retornar false
            return false;
        } catch (Exception e) {
            // Manejo de Exceptiones en caso de algún problema
            System.err.println("No es posible elegir el turno: " + e.getMessage());
            return false;
        }
    }

    public Turno obtenerTurnoPorId(String id) throws MiException {
        try {
            Optional<Turno> respuesta = turnoRepositorio.findById(id);
            if (respuesta.isPresent()) {
                return respuesta.get();
            } else {
                throw new MiException("No se encontró el turno con el ID proporcionado.");
            }
        } catch (Exception e) {
            throw new MiException("Error al obtener el turno: " + e.getMessage());
        }
    }

    protected void validar(String fecha, String hora, String consulta, String idProfesional, Boolean disponibilidad)
            throws MiException {
        if (fecha == null || fecha.isEmpty()) {
            throw new MiException("Debe indicar uan fecha para el turno");
        }
        if (hora == null || hora.isEmpty()) {
            throw new MiException("Debe indicar un horario para el turno");
        }
        if (consulta == null || consulta.isEmpty()) {
            throw new MiException("Debe ingresar una descripcion de la visita");
        }

        if (idProfesional == null || idProfesional.isEmpty()) {
            throw new MiException("El turno debe tener un Profesional Asociado");
        }
        if (disponibilidad == null) {
            throw new MiException("El turno debe tener un estado");
        }

    }

    public void atenderTurno(Turno turno) {

        turno.setAtendido(true);
        turnoRepositorio.save(turno);
    }
}
