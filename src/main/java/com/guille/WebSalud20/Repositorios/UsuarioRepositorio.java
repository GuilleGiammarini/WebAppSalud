package com.guille.WebSalud20.Repositorios;


import com.guille.WebSalud20.Entidades.Paciente;
import com.guille.WebSalud20.Entidades.Profesional;
import com.guille.WebSalud20.Entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    Usuario buscarPorNombre(@Param("nombreUsuario") String nombreUsuario);

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROFESIONAL'")
    List<Usuario> buscarProfesional();

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PACIENTE'")
    List<Usuario> buscarPaciente();

    @Query("SELECT u FROM Usuario u WHERE u.DNI = :dni")
    Usuario buscarPorDni(@Param("dni") Long dni);

    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE ?1%")
    List<Usuario> buscarUsuarioPorNombre(String palabra);

    @Query("SELECT DISTINCT t.paciente FROM Turno t WHERE t.profesional = :profesional")
    List<Paciente> findPacientesByProfesional(@Param("profesional") Profesional profesional);
}
