package modelo;

import java.time.LocalDateTime;

public class Match {
    private Participante participanteUno;
    private Participante participanteDos;

    /** Se registra el resultado en esta variable */
    private MatchResultado resultado;

    /** Fecha del enfrentamiento */
    private LocalDateTime fecha;

    private int ronda;
    private int posicionBracket;

    /** Constructor sin posición en bracket, sirve para el formato de liga */
    public Match(Participante participanteUno, Participante participanteDos, int ronda){
        this.participanteUno = participanteUno;
        this.participanteDos = participanteDos;
        this.ronda = ronda;
    }

    /** Constructor con posición en bracket, sirve para eliminatorias */
    public Match(Participante participanteUno, Participante participanteDos,
                 int ronda, int posicionBracket, LocalDateTime fecha){

        /** Desde aquí se lanzan excepciones */
        if (participanteUno == null || participanteDos == null){
            throw new IllegalArgumentException();
        }
        if (participanteUno.equals(participanteDos)){
            throw new IllegalArgumentException();
        }
        if (ronda < 1){
            throw new IllegalArgumentException();
        }

        this.participanteUno = participanteUno;
        this.participanteDos = participanteDos;
        this.ronda = ronda;
        this.posicionBracket = posicionBracket;
        this.fecha = fecha;
    }

    /**
     * Revisa si el enfrentamiento tiene resultado o no
     * @return resultado actual del enfrentamiento
     */
    public boolean tieneResultado(){
        return resultado != null;
    }

    /** Getters */
    public Participante getParticipanteUno(){
        return participanteUno;
    }

    public Participante getParticipanteDos(){
        return participanteDos;
    }

    public MatchResultado getResultado(){
        return resultado;
    }
}
