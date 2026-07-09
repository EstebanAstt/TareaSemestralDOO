package modelo;

/**
 * Representa un set individual de tenis
 */
public class SetTenis {
    private int puntajeTenisUno;
    private int puntajeTenisDos;

    /**
     * Constructor por defecto, contiene los puntajes
     * @param puntajeTenisUno Puntaje entero del primer jugador
     * @param puntajeTenisDos Puntaje entero del segundo jugador
     */
    public SetTenis(int puntajeTenisUno, int puntajeTenisDos){
        if (puntajeTenisUno < 0 || puntajeTenisUno < 0){
            throw new IllegalArgumentException();
        }
        this.puntajeTenisUno = puntajeTenisUno;
        this.puntajeTenisDos = puntajeTenisDos;
    }

    /**
     * Getter del primer puntaje
     * @return Variable entera del primer jugador
     */
    public int getPuntajeTenisUno(){
        return puntajeTenisUno;
    }

    /**
     * Getter del segundo puntaje
     * @return Variable entera del segundo jugador
     */
    public int getPuntajeTenisDos(){
        return puntajeTenisDos;
    }

    /**
     * Se retorna el ganador del set actual, no existe empate
     * @return Resultado del set de tenis
     */
    public DeterminarGanador determinarGanadorTenis(){
        return puntajeTenisUno > puntajeTenisDos ? DeterminarGanador.PARTICIPANTE_UNO_GANA
                : DeterminarGanador.PARTICIPANTE_DOS_GANA;
    }
}
