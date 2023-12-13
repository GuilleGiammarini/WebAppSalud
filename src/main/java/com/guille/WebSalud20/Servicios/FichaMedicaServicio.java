package com.guille.WebSalud20.Servicios;


import com.guille.WebSalud20.Entidades.FichaMedica;
import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Repositorios.FichaMedicaRepositorio;
import com.guille.WebSalud20.Repositorios.PacienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FichaMedicaServicio {

    @Autowired
    FichaMedicaRepositorio fichaMedicaRepositorio;
    @Autowired
    PacienteRepositorio pacienteRepositorio;

    public void crearFichaMedica(Paciente paciente) {

        FichaMedica ficha = fichaMedicaRepositorio.buscarIdPaciente(paciente.getId());

        if (ficha == null) {
            FichaMedica fichaMedica = new FichaMedica();
            fichaMedica.setPaciente(paciente);
            fichaMedicaRepositorio.save(fichaMedica);

            paciente.setFichaMedica(fichaMedica);
            pacienteRepositorio.save(paciente);
        }
    }

    public void modificarFichaMedica(Paciente paciente, String antecedentes, String obraSocial, Long afiliado,
                                     String grupoSanguineo, double altura, double peso) {

        FichaMedica ficha = buscarPorIdPaciente(paciente.getId());
        ficha.setAntecedentes(antecedentes);
        ficha.setObraSocial(obraSocial);
        ficha.setNumeroAfiliado(afiliado);
        ficha.setGrupoSanguineo(grupoSanguineo);
        ficha.setAltura(altura);
        ficha.setPeso(peso);
        fichaMedicaRepositorio.save(ficha);
    }

    public FichaMedica buscarPorIdPaciente(String id) {

        return fichaMedicaRepositorio.buscarIdPaciente(id);
    }
}
