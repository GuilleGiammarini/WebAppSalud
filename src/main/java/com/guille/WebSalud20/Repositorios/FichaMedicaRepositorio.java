package com.guille.WebSalud20.Repositorios;

import com.guille.WebSalud20.Entidades.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaMedicaRepositorio extends JpaRepository<FichaMedica, String>{

    @Query("SELECT f FROM FichaMedica f WHERE f.paciente.id = :id")
    FichaMedica buscarIdPaciente(@Param("id") String id);
}
