package gestion.temporizador;

import modelo.Participante;

/**
 * Clase encargada de asignar una tecla del teclado a un participante
 * Funciona para la disciplina de "Carrera"
 */
public class AsignacionTecla {
    private char tecla;
    private Participante participante;

    public AsignacionTecla(char tecla, Participante participante){
        this.tecla = tecla;
        this.participante = participante;
    }

    /** Getters */
    public char getTeclaAsignacionTecla(){
        return tecla;
    }

    public Participante getParticipanteAsignacionTecla(){
        return participante;
    }
}
