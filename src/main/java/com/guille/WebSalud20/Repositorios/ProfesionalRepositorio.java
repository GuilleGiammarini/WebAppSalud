package com.guille.WebSalud20.Repositorios;

import com.guille.WebSalud20.Entidades.Profesional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional, String> {

    @Query("SELECT m FROM Profesional m WHERE m.email = :email")
    public Profesional buscarPorEmail(@Param("email") String email);
    @Query("SELECT p FROM Profesional p WHERE p.nombre= :nombre")
    Profesional buscarNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Profesional p ORDER BY p.nombre DESC")
    List<Profesional> buscarOrdenado();

    @Query("SELECT p FROM Profesional p ORDER BY p.apellido ASC")
    List<Profesional> listarOrdenadoPorApellido();

    @Query("SELECT p FROM Profesional p ORDER BY p.especialidad ASC")
    List<Profesional> listarOrdenadoPorEspecialidad();

    @Query("SELECT p FROM Profesional p WHERE p.especialidad LIKE ?1% ORDER BY p.apellido ASC")
    List<Profesional> buscarProfesionalPorEspecialidad(String especialidad);

   
    
    @Query("SELECT p FROM Profesional p " +
           "WHERE (:especialidad IS NULL OR p.especialidad LIKE %:especialidad%) " +
           "ORDER BY CASE WHEN :columna = 'nombre' THEN p.nombre END, " +
                     "CASE WHEN :columna = 'apellido' THEN p.apellido END, " +
                     "CASE WHEN :columna = 'especialidad' THEN p.especialidad END, " +
                     "CASE WHEN :columna = 'PrecioConsulta' THEN p.PrecioConsulta END, " +
                     "CASE WHEN :columna = 'valoracionProfesional' THEN p.valoracionProfesional END DESC")
    List<Profesional> findByEspecialidadAndSort(
            @Param("especialidad") String especialidad,
            @Param("columna") String columna,
            Sort sort
    );

}
