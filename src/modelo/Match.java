package modelo;

import java.time.LocalDateTime;

/**
 * Representa un enfrentamiento
 */
public class Match {
    private Participante participanteUno;
    private Participante participanteDos;
    private final boolean esVuelta;

    /** Se registra el resultado en esta variable */
    private MatchResultado resultado;

    /** Fecha del enfrentamiento */
    private LocalDateTime fecha;

    private int ronda;
    private int posicionBracket;


    /**
     * util para el formato ida y vuelta
     */
    private MatchResultado resultadoIda;
    private MatchResultado resultadoVuelta;
    private boolean esIdaVuelta = false;

    /** Constructor sin posición en bracket, sirve para el formato de liga */
    public Match(Participante participanteUno, Participante participanteDos, int ronda){
        this.participanteUno = participanteUno;
        this.participanteDos = participanteDos;
        this.ronda = ronda;
        this.esVuelta = false;
    }

    /** Constructor con posición en bracket, sirve para eliminatorias */
    public Match(Participante participanteUno, Participante participanteDos,
                 int ronda, int posicionBracket, LocalDateTime fecha, boolean esVuelta){

        if (ronda < 1){
            throw new IllegalArgumentException("Debe existir por lo menos una ronda");
        }
        // participantes pueden ser null en rondas futuras (se asignan cuando avanza el ganador)
        if (participanteUno != null && participanteUno.equals(participanteDos)){
            throw new IllegalArgumentException("Los participantes deben ser distintos");
        }
        this.participanteUno = participanteUno;
        this.participanteDos = participanteDos;
        this.ronda = ronda;
        this.posicionBracket = posicionBracket;
        this.fecha = fecha;
        this.esVuelta = esVuelta;

    }

    /**
     * Revisa si el enfrentamiento tiene resultado o no
     * @return resultado actual del enfrentamiento
     */
    public boolean tieneResultado(){
        return resultado != null;
    }

    /**
     * Getter que retorna el participante ganador del enfrentamiento
     * @return el participante ganador
     */
    public Participante getGanadorMatch(){
        Participante participanteGanador;
        if (!tieneResultado()) return null;

        switch (resultado.getOutcome()){
            case PARTICIPANTE_UNO_GANA -> { return participanteUno; }
            case PARTICIPANTE_DOS_GANA -> { return participanteDos; }
            default -> { return null; }
        }
    }

    /**
     * Getter que retorna el participante perdedor del enfrentamiento
     * @return el participante perdedor
     */
    public Participante getPerdedorMatch(){
        Participante participantePerdedor;
        if (!tieneResultado()) return null;

        switch (resultado.getOutcome()){
            case PARTICIPANTE_UNO_GANA -> { return participanteDos; }
            case PARTICIPANTE_DOS_GANA -> { return participanteUno; }
            default -> { return null; }
        }
    }

    /**
     * Setter el cual ingresa el resultado una única vez
     * @param resultado variable MatchResultado ingresado
     */
    public void setResultadoMatch(MatchResultado resultado){
        if (resultado == null){
            throw new IllegalArgumentException("El resultado no puede ser nulo");
        }
        if (tieneResultado()){
            throw new IllegalStateException("Ya hay existe un resultado");
        }
        this.resultado = resultado;
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

    public int getRonda() { return ronda; }
    public boolean isEsVuelta() { return esVuelta; }

    /**
     * Asigna el participante uno y dos al match.
     * Solo se usa para poblar matches de rondas futuras en eliminatoria.
     */
    public void setParticipanteUno(Participante p) { this.participanteUno = p; }
    public void setParticipanteDos(Participante p) { this.participanteDos = p; }

    public int getPosicionBracket() {
        return posicionBracket;
    }

    /**
     * setters y getters para el formato de ida y vuelta
     */

    public void setResultadoIda(MatchResultado r)    { this.resultadoIda = r; }
    public void setResultadoVuelta(MatchResultado r) { this.resultadoVuelta = r; }
    public boolean tieneIda()    { return resultadoIda != null; }
    public boolean tieneVuelta() { return resultadoVuelta != null; }
    public boolean isIdaVuelta() { return esIdaVuelta; }
    public void setEsIdaVuelta(boolean v) { this.esIdaVuelta = v; }

    /** Marcador global: suma de ambos partidos, normalizado */
    public int getMarcadorGlobalUno() {
        int golesIda    = tieneIda()    ? resultadoIda.getMarcadorUno()    : 0;
        int golesVuelta = tieneVuelta() ? resultadoVuelta.getMarcadorUno() : 0; //igual posición
        return golesIda + golesVuelta;
    }

    public int getMarcadorGlobalDos() {
        int golesIda    = tieneIda()    ? resultadoIda.getMarcadorDos()    : 0;
        int golesVuelta = tieneVuelta() ? resultadoVuelta.getMarcadorDos() : 0; //igual posición
        return golesIda + golesVuelta;
    }


    public Participante getGanadorGlobal() {
        if (!tieneIda() || !tieneVuelta()) return null;
        int g1 = getMarcadorGlobalUno();
        int g2 = getMarcadorGlobalDos();
        if (g1 > g2) return participanteUno;
        if (g2 > g1) return participanteDos;
        return null; // empate global
    }
}
