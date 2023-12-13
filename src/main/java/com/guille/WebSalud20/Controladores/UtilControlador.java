package com.guille.WebSalud20.Controladores;


import com.guille.WebSalud20.Enumeracion.DiaSemana;
import com.guille.WebSalud20.Enumeracion.Especialidad;
import com.guille.WebSalud20.Enumeracion.Provincias;
import com.guille.WebSalud20.Exception.MiException;
import com.guille.WebSalud20.Repositorios.PacienteRepositorio;
import com.guille.WebSalud20.Repositorios.ProfesionalRepositorio;
import com.guille.WebSalud20.Servicios.PacienteServicio;
import com.guille.WebSalud20.Servicios.ProfesionalServicio;
import com.guille.WebSalud20.Servicios.TurnoServicio;
import java.time.LocalTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/util")
public class UtilControlador {

    @Autowired
    ProfesionalServicio profesionalServicio;

    @Autowired
    TurnoServicio turnoServicio;

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    PacienteServicio pacienteServicio;

    @Autowired
    PacienteRepositorio pacienteRepositorio;

    @GetMapping("/profesionalRandom")
    public String cargarDataBase() throws MiException {

        String[] nombres = {"Juan", "José", "Antonio", "Manuel", "David", "Carlos", "Francisco", "Jesús", "Pedro", "Pablo", "Luis", "Diego", "Andrés", "Alejandro", "Miguel", "Fernando", "Jorge", "Óscar", "María", "Ana", "Laura", "Carmen", "Elena", "Isabel", "Marta", "Julia", "Sara", "Paula", "Inés", "Teresa", "Cristina", "Blanca", "Rocío", "Alba", "Daniela", "Andrea"};
        String[] apellidos = {"García", "López", "García", "López", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero", "Gómez", "Fernández", "Hernández", "Díaz", "Muñoz", "Castro", "Jiménez", "Moreno", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero"};
        String[] localidades = {"La Plata", "Mar del Plata", "Quilmes", "San Fernando del Valle de Catamarca", "San Isidro", "Andalgalá", "Resistencia", "Barranqueras ", "Quitilipi"};
        String[] direcciones = {"9 de Julio 1995", "Rivadavia 110", "San Martín 44", "Belgrano 5962", "Sarmiento 74", "Mitre 998", "Independencia 785", "Pueyrredón 404", "Córdoba 753", "Santa Fe 620"};

        Random random = new Random();

        String nombre = nombres[random.nextInt(nombres.length)];

        String apellido = apellidos[random.nextInt(apellidos.length)];

        String nombreUsuario = (nombre + apellido.charAt(0)).toLowerCase();

        nombreUsuario = nombreUsuario.replace("á", "a");
        nombreUsuario = nombreUsuario.replace("ú", "u");
        nombreUsuario = nombreUsuario.replace("é", "e");
        nombreUsuario = nombreUsuario.replace("í", "i");
        nombreUsuario = nombreUsuario.replace("ó", "o");
        nombreUsuario = nombreUsuario.replace("ñ", "n");

        Long dni = random.nextLong(5000000, 50000000);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(2024 - 2000) + 2000);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31));
        Date fechaNacimiento = calendar.getTime();

        String email = nombre.toLowerCase() + apellido.substring(0, 1).toLowerCase() + "@mail.com";

        String password = "123123";

        Long matricula = random.nextLong(1000, 99999);

        Especialidad especialidad = Especialidad.values()[random.nextInt(Especialidad.values().length)];

        Provincias provincias = Provincias.values()[random.nextInt(Provincias.values().length)];
        System.out.println("provincias = " + provincias);

        String localidad = localidades[random.nextInt(localidades.length)];
        System.out.println("localidad = " + localidad);

        String direccion = direcciones[random.nextInt(direcciones.length)];
        System.out.println("direccion = " + direccion);

        List<DiaSemana> diasDisponibles = new ArrayList<>();
        DiaSemana[] diasSemana = DiaSemana.values();
        for (int i = 1; i < 3; i++) {
            DiaSemana diaAleatorio = diasSemana[random.nextInt(diasSemana.length)];
            diasDisponibles.add(diaAleatorio);
        }


        LocalTime[] horariosEntrada = new LocalTime[17];
        for (int i = 0; i < 17; i++) {
            horariosEntrada[i] = LocalTime.of(8 + i / 2, (i % 2) * 30);
        }

        LocalTime[] horariosSalida = new LocalTime[17];
        for (int i = 0; i < 17; i++) {
            horariosSalida[i] = LocalTime.of(12 + i / 2, (i % 2) * 30);
        }

        LocalTime horarioEntrada = horariosEntrada[random.nextInt(horariosEntrada.length)];
        LocalTime horarioSalida = horariosSalida[random.nextInt(horariosSalida.length)];

        int valorAleatorio = random.nextInt(36) + 15;
        int precioConsulta = Math.round(valorAleatorio * 100.0f / 100) * 100;

        profesionalServicio.crearProfesional(null, nombreUsuario, password, password, nombre, apellido, email, fechaNacimiento, dni, especialidad, provincias, localidad, direccion, matricula, diasDisponibles, horarioEntrada, horarioSalida, precioConsulta);

        var registrado = profesionalRepositorio.buscarPorEmail(email);

        turnoServicio.generarTurnos(registrado);

        return "redirect:../listaProfesionales";
    }

    @GetMapping("/pacienteRandom")
    public String cargarDataBasePaciente() throws MiException {

        String[] nombres = {"Juan", "José", "Antonio", "Manuel", "David", "Carlos", "Francisco", "Jesús", "Pedro", "Pablo", "Luis", "Diego", "Andrés", "Alejandro", "Miguel", "Fernando", "Jorge", "Óscar", "María", "Ana", "Laura", "Carmen", "Elena", "Isabel", "Marta", "Julia", "Sara", "Paula", "Inés", "Teresa", "Cristina", "Blanca", "Rocío", "Alba", "Daniela", "Andrea"};
        String[] apellidos = {"García", "López", "García", "López", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero", "Gómez", "Fernández", "Hernández", "Díaz", "Muñoz", "Castro", "Jiménez", "Moreno", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero"};
        String[] localidades = {"La Plata", "Mar del Plata", "Quilmes", "San Fernando del Valle de Catamarca", "San Isidro", "Andalgalá", "Resistencia", "Barranqueras ", "Quitilipi"};
        String[] direcciones = {"9 de Julio 1995", "Rivadavia 110", "San Martín 44", "Belgrano 5962", "Sarmiento 74", "Mitre 998", "Independencia 785", "Pueyrredón 404", "Córdoba 753", "Santa Fe 620"};

        Random random = new Random();

        String nombre = nombres[random.nextInt(nombres.length)];

        String apellido = apellidos[random.nextInt(apellidos.length)];

        String nombreUsuario = (nombre + apellido.charAt(0)).toLowerCase();

        nombreUsuario = nombreUsuario.replace("á", "a");
        nombreUsuario = nombreUsuario.replace("ú", "u");
        nombreUsuario = nombreUsuario.replace("é", "e");
        nombreUsuario = nombreUsuario.replace("í", "i");
        nombreUsuario = nombreUsuario.replace("ó", "o");
        nombreUsuario = nombreUsuario.replace("ñ", "n");

        Long dni = random.nextLong(5000000, 50000000);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(2024 - 2000) + 2000);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31));
        Date fechaNacimiento = calendar.getTime();

        String email = nombre.toLowerCase() + apellido.substring(0, 1).toLowerCase() + "@mail.com";

        String password = "123123";
        String password2 = "123123";

        Provincias provincias = Provincias.values()[random.nextInt(Provincias.values().length)];
        System.out.println("provincias = " + provincias);

        String localidad = localidades[random.nextInt(localidades.length)];
        System.out.println("localidad = " + localidad);

        String direccion = direcciones[random.nextInt(direcciones.length)];
        System.out.println("direccion = " + direccion);

        pacienteServicio.crearPaciente(null, nombreUsuario, nombre, apellido, dni, fechaNacimiento, email, password, password2);

        return "redirect:../listaProfesionales";
    }
}
