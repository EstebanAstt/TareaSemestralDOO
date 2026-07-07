package gestion.temporizador;

import modelo.Participante;

/**
 * Representa un participante con su respectivo tiempo de llegada
 */
public class ParticipanteTiempoLlegada {
    private Participante participante;
    private long tiempo;

    public ParticipanteTiempoLlegada(Participante participante,
                                     long tiempo){
        this.participante = participante;
        this.tiempo = tiempo;
    }

    /** Getters */
    public Participante getParticipanteTiempoLlegada(){
        return participante;
    }

    public long getTiempoLlegada(){
        return tiempo;
    }
}
