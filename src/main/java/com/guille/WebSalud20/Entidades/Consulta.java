package com.guille.WebSalud20.Entidades;

import com.guille.WebSalud20.Enumeracion.Provincias;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    private Paciente paciente;
    @OneToOne
    private Profesional profesional;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeConsulta;
    
    private LocalTime horaInicio;
    private int duracionConsulta; // Duración en minutos

    private String diagnostico;
    private String tratamiento;

    private String observaciones;

    private boolean atendido = false;

    //el horario y la duración
    public Consulta(LocalTime horaInicio, int duracionConsulta) {
        this.horaInicio = horaInicio;
        this.duracionConsulta = duracionConsulta;
    }
    
    @Enumerated(EnumType.STRING)
    private Provincias provincias;
    
    private String localidad;
    private String direccion;

    private int precioConsulta;
    
    private int puntuacion;
    
    private String motivoConsulta;

}
