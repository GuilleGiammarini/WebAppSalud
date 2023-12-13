package com.guille.WebSalud20.Servicios;

import com.guille.WebSalud20.Exception.MiException;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class UtilServicio {

    public void validar(String nombreUsuario, String password, String password2, String nombre, String
            apellido, Date fechaDeNacimiento, Long DNI, String email) throws MiException {


        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiException("El nombre de usuario no puede estar vacio o Nulo");
        }

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacío o ser nulo");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("El apellido no puede estar vacío o ser nulo");
        }

        if (DNI == null) {
            throw new MiException("El DNI no puede ser nulo");
        }

        if (fechaDeNacimiento == null) {
            throw new MiException("La fecha de nacimiento no puede ser nula");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacío o ser nulo");
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("Las contraseñas no pueden estar vacias y tener menos de 5 caracteres ");
        }

        if (!password.equals(password2)) {
            throw new MiException("las contraseñas deben coincidir");
        }
    }
}
