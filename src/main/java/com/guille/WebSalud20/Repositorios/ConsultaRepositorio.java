package com.guille.WebSalud20.Repositorios;

import com.guille.WebSalud20.Entidades.Consulta;
import com.guille.WebSalud20.Entidades.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepositorio extends JpaRepository<Consulta, String> {
    List<Consulta> findByPacienteId(String pacienteId);


    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :id")
    List<Consulta> listarPorIdPaciente(String id);

    List<Consulta> findByPaciente(Paciente paciente);
}
