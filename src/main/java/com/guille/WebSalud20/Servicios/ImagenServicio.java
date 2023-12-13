package com.guille.WebSalud20.Servicios;

import com.guille.WebSalud20.Entidades.Imagen;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.ImagenRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepo;

    @Transactional
    public Imagen guardar(MultipartFile img) {

        if (img != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(img.getContentType());
                imagen.setNombre(img.getName());
                imagen.setContenido(img.getBytes());

                return imagenRepo.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException {

        try {
            Imagen imagen = new Imagen();
            if (idImagen != null) {
                Optional<Imagen> respuesta = imagenRepo.findById(idImagen);
                if (respuesta.isPresent()) imagen = respuesta.get();
            }
            imagen.setMime(archivo.getContentType());
            imagen.setNombre(archivo.getName());
            imagen.setContenido(archivo.getBytes());
            return imagenRepo.save(imagen);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
