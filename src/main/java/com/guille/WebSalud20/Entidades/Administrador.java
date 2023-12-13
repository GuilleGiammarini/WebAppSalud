package com.guille.WebSalud20.Entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("ADMIN")
public class Administrador extends Usuario {

}
