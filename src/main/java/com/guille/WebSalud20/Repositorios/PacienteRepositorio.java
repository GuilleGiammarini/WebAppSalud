package com.guille.WebSalud20.Repositorios;

import com.guille.WebSalud20.Entidades.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, String> {

    @Query("SELECT p FROM Paciente p WHERE p.nombre= :nombre")
    Paciente buscarNombre(@Param("nombre") String titulo);

    @Query("SELECT p FROM Paciente p ORDER BY p.nombre DESC")
    List<Paciente> buscarOrdenado();
}
