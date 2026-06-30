package modelo;

import java.util.ArrayList;

public class MatchResultado {

    /** Futbol y basket tienen puntajes enteros
     *  Ajedrez tiene puntajes flotantes */
    private double puntajeUno;
    private double puntajeDos;

    /** Enum para ver si existe un ganador o empate */
    private DeterminarGanador outcome;

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

    /** Constructor para Futbol y basket */
    public MatchResultado(int puntajeUno, int puntajeDos){
        this.puntajeUno = (double)puntajeUno;
        this.puntajeDos = (double)puntajeDos;

        if (puntajeUno < 0 || puntajeDos < 0){
            throw new IllegalArgumentException();
        }
    }

    /** Lista de sets para tenis */
    private ArrayList<SetTenis> setsTenisIngresados;

    /**
     * Constructor para Tenis
     * @param setsTenisIngresados lista de sets de tenis
     */
    public MatchResultado(ArrayList<SetTenis> setsTenisIngresados){

        /** Un partido de tenis debe tener por lo mínimo 2 resultados */
        if (setsTenisIngresados == null || setsTenisIngresados.size() < 2){
            throw new IllegalArgumentException();
        }
        this.setsTenisIngresados = setsTenisIngresados;

        /** A diferencia de determinar los puntajes al crear un enfrentamiento,
         *  en el tenis se saca automáticamente dependiendo del ganador de
         *  cada set del ArrayList */
        int puntajeUno = 0;
        int puntajeDos = 0;

        for (int s = 0; s < setsTenisIngresados.size(); s++){
            SetTenis setActual = setsTenisIngresados.get(s);

            if (setActual.determinarGanadorTenis() == DeterminarGanador.PARTICIPANTE_UNO_GANA){
                puntajeUno++;
            }
            else puntajeDos++;
        }
        this.puntajeUno = puntajeUno;
        this.puntajeDos = puntajeDos;
        this.outcome = setDeterminarGanador(puntajeUno, puntajeDos);
    }

    private void validarPuntajesAjedrez(double puntajeUno, double puntajeDos){
        boolean puntajeUnoValido = puntajeUno == 0.0 || puntajeUno == 0.5
                || puntajeUno == 1.0;
        boolean puntajeDosValido = puntajeDos == 0.0 || puntajeDos == 0.5
                || puntajeDos == 1.0;

        /** Los puntajes deben ser válidos */
        if (!puntajeUnoValido || !puntajeDosValido){
            throw new IllegalArgumentException();
        }

        /** Al sumar los dos puntajes, no deben sobrepasar 1.0 */
        if (Double.compare(puntajeUno + puntajeDos, 1.0) != 0){
            throw new IllegalArgumentException();
        }
    }

    /** Constructor para Ajedrez */
    public MatchResultado(double puntajeUno, double puntajeDos, boolean esAjedrez){
        if (!esAjedrez) {
            validarPuntajesAjedrez(puntajeUno, puntajeDos);
        }

        this.puntajeUno = puntajeUno;
        this.puntajeDos = puntajeDos;
        this.outcome = setDeterminarGanador(puntajeUno, puntajeDos);
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
