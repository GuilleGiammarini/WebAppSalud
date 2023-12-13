package com.guille.WebSalud20.Entidades;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Turno")
public class Turno {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fecha;
    private String hora;
    private String consulta;
    private boolean disponibilidad;
    private boolean atendido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente")
    private Paciente paciente;

    @OneToOne
    private Profesional profesional;

}
