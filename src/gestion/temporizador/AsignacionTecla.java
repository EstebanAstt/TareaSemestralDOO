package gestion.temporizador;

import modelo.Participante;

/**
 * Clase encargada de asignar una tecla del teclado a un participante
 * Funciona para la disciplina de "Carrera"
 */
public class AsignacionTecla {
    private char tecla;
    private Participante participante;

    /**
     * Asigna una tecla a un participante
     * @param tecla Tecla del teclado
     * @param participante Participante el cual se le quiere asignar una tecla
     */
    public AsignacionTecla(char tecla, Participante participante){
        this.tecla = tecla;
        this.participante = participante;
    }

    /**
     * Getter de la tecla guardada en el constructor
     * @return Variable char de la tecla
     */
    public char getTeclaAsignacionTecla(){
        return tecla;
    }

    /**
     * Getter del participante
     * @return Participante guardado en el constructor
     */
    public Participante getParticipanteAsignacionTecla(){
        return participante;
    }
}
