package gestion.temporizador;

import modelo.Participante;

/**
 * Representa un participante con su respectivo tiempo de llegada
 */
public class ParticipanteTiempoLlegada {
    private Participante participante;
    private long tiempo;

    /**
     * Constructor el cual guarda el participante con su respectivo tiempo de llegada
     * @param participante Participante que llegó a la meta
     * @param tiempo Tiempo de llegada
     */
    public ParticipanteTiempoLlegada(Participante participante,
                                     long tiempo){
        this.participante = participante;
        this.tiempo = tiempo;
    }

    /**
     * Getter del participante que llegó a la meta
     * @return Participante del tiempo de llegada
     */
    public Participante getParticipanteTiempoLlegada(){
        return participante;
    }

    /**
     * Retorna el tiempo de llegada
     * @return Variable long que representa el tiempo
     */
    public long getTiempoLlegada(){
        return tiempo;
    }
}
