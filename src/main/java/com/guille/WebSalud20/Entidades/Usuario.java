package com.guille.WebSalud20.Entidades;

import com.guille.WebSalud20.Enumeracion.Rol;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rol",
        discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;


    private String nombreUsuario;
    private String password;

    private Long DNI;
    private String nombre;
    private String apellido;

    private String email;


    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeNacimiento;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeAlta;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", insertable = false, updatable = false)
    private Rol rol;
    @OneToOne
    private Imagen imagen;


    private Boolean activo = true;


}
