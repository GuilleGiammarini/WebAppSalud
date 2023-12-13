package com.guille.WebSalud20.Enumeracion;

public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes");

    private final String nombreEnCastellano;

    DiaSemana(String nombreEnCastellano) {
        this.nombreEnCastellano = nombreEnCastellano;
    }

    public String getNombreEnCastellano() {
        return nombreEnCastellano;
    }
}