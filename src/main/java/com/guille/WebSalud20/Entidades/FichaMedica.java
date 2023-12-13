package com.guille.WebSalud20.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
public class FichaMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private Integer id;

    @OneToOne
    private Paciente paciente;

    private String obraSocial;
    private long numeroAfiliado;
    private String grupoSanguineo;
    private Double altura;
    private Double peso;
    private String antecedentes;

    public FichaMedica(Paciente paciente, String antecedentes, String obraSocial, long numeroAfiliado, String grupoSanguineo, Double altura, Double peso) {
        this.paciente = paciente;
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
        this.grupoSanguineo = grupoSanguineo;
        this.altura = altura;
        this.peso = peso;
        this.antecedentes = antecedentes;
    }
}

