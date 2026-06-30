package modelo;

public class MatchResultado {

    /** Futbol y basket tienen puntajes enteros
     *  Ajedrez tiene puntajes flotantes */
    private double puntajeUno;
    private double puntajeDos;

    /** Enum para ver si existe un ganador o empate */
    private DeterminarGanador outcome;

    /** Constructor para Futbol y basket */
    public MatchResultado(int puntajeUno, int puntajeDos){
        this.puntajeUno = (double)puntajeUno;
        this.puntajeDos = (double)puntajeDos;

        if (puntajeUno < 0 || puntajeDos < 0){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Determina un ganador empate dependiendo de los puntajes ingresados
     * @param puntaje1
     * @param puntaje2
     * @return el resultado dependiendo los puntajes ingresados
     */
    public DeterminarGanador setDeterminarGanador(double puntaje1, double puntaje2){
        if (puntaje1 > puntaje2) return DeterminarGanador.PARTICIPANTE_UNO_GANA;
        if (puntaje2 > puntaje1) return DeterminarGanador.PARTICIPANTE_DOS_GANA;
        return DeterminarGanador.EMPATE;
    }

    /**
     * Verifica que el resultado actual es empate
     * @return variable enum si resulta en empate
     */
    public boolean esEmpate(){
        return outcome == DeterminarGanador.EMPATE;
    }

    /** Getters */
    public DeterminarGanador getOutcome(){
        return outcome;
    }
}
