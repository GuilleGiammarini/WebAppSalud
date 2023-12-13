package com.guille.WebSalud20.Entidades;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "PACIENTE")
@DiscriminatorValue("PACIENTE")
@Data
public class Paciente extends Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToMany
    private List<Consulta> consulta;

    @OneToOne
    FichaMedica fichaMedica;
}
