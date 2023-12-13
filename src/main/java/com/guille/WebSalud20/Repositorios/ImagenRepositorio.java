package com.guille.WebSalud20.Repositorios;

import com.guille.WebSalud20.Entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String> {


}
