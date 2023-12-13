package com.guille.WebSalud20.Servicios;

import com.guille.WebSalud20.Entidades.Imagen;
import com.guille.WebSalud20.Entidades.Usuario;
import com.guille.WebSalud20.Enumeracion.Rol;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UtilServicio utilServicio;
    @Autowired
    private UsuarioRepositorio ur;
    @Autowired
    private ImagenServicio imagenServicio;



    @Transactional
    public void crearUsuario(MultipartFile archivo, String nombreUsuario, String nombre, String apellido,
                             Long DNI, Date fechaDeNacimiento, String email, String password, String password2) throws MiException {

        utilServicio.validar(nombreUsuario, password, password2, nombre, apellido, fechaDeNacimiento, DNI, email);

        System.out.println("EJECUTANDO  servicio crearUsuario REGISTRAR");
        Usuario usuario = new Usuario();

        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setDNI(DNI);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFechaDeNacimiento(fechaDeNacimiento);
        usuario.setEmail(email);
        usuario.setFechaDeAlta(new Date());
        usuario.setRol(Rol.PACIENTE);
        usuario.setActivo(true);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        ur.save(usuario);
    }

    @Transactional
    public void modificarUsuario(String id, /*MultipartFile archivo,*/ String nombreUsuario, String nombre, String apellido,
                                 Long DNI, Date fechaDeNacimiento, String email, String password, String password2, boolean activo) throws MiException {

        utilServicio.validar(nombreUsuario, password, password2, nombre, apellido, fechaDeNacimiento, DNI, email);

        Optional<Usuario> respuesta = ur.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setDNI(DNI);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setFechaDeNacimiento(fechaDeNacimiento);
            usuario.setEmail(email);
            usuario.setFechaDeAlta(new Date());
            usuario.setRol(Rol.PACIENTE);
            usuario.setActivo(activo);

            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            
            /*Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
            usuario.setImagen(imagen);*/
            ur.save(usuario);
        }

    }

    @Transactional
    public void eliminarUsuario(String id) {

        ur.deleteById(id);
    }

    public List<Usuario> listarUsuario(String palabra) {

        if (palabra != null) {
            return ur.buscarUsuarioPorNombre(palabra);
        }
        return ur.findAll();
    }

    public List<Usuario> listarTodos() {
        return ur.findAll();
    }

    public Usuario buscarPorID(String id) {

        Optional<Usuario> oUsuario = ur.findById(id);
        Usuario Usuario = null;
        if (oUsuario.isPresent()) {

            Usuario = oUsuario.get();

        }
        return Usuario;
    }

    @org.springframework.transaction.annotation.Transactional
    public void bajaUsuario(String id) throws MiException {
        if (id == null || id.isEmpty()) {
            throw new MiException("El id ingresado no puede ser nulo o estar vacio");
        }
        Optional<Usuario> respuesta = ur.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = (Usuario) respuesta.get();
            user.setActivo(false); // Establece el estado del profesional como inactivo
            ur.save(user);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void altaUsuario(String id) throws MiException {
        if (id == null || id.isEmpty()) {
            throw new MiException("El id ingresado no puede ser nulo o estar vacio");
        }
        Optional<Usuario> respuesta = ur.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = (Usuario) respuesta.get();
            user.setActivo(true); // Establece el estado del profesional como activo
            ur.save(user);
        }
    }
    

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

        Usuario u = ur.buscarPorNombre(nombreUsuario);

        if (u != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + u.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", u);
            return new User(u.getNombreUsuario(), u.getPassword(), permisos);

        } else {

            return null;
        }

    }

    public Usuario buscarPorDni(Long dni) {
        return ur.buscarPorDni(dni);
    }

    public Usuario getOne(String id) {
        return ur.getOne(id);
    }

}
