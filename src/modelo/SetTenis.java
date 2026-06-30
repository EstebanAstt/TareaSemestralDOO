package modelo;

/** Representa un set individual de tenis */
public class SetTenis {
    private int puntajeTenisUno;
    private int puntajeTenisDos;

    public SetTenis(int puntajeTenisUno, int puntajeTenisDos){
        if (puntajeTenisUno < 0 || puntajeTenisUno < 0){
            throw new IllegalArgumentException();
        }
        this.puntajeTenisUno = puntajeTenisUno;
        this.puntajeTenisDos = puntajeTenisDos;
    }

    public int getPuntajeTenisUno(){
        return puntajeTenisUno;
    }

    public int getPuntajeTenisDos(){
        return puntajeTenisDos;
    }

    /**
     * Se retorna el ganador del set actual, no existe empate
     * @return resultado del set de tenis
     */
    public DeterminarGanador determinarGanadorTenis(){
        return puntajeTenisUno > puntajeTenisDos ? DeterminarGanador.PARTICIPANTE_UNO_GANA
                : DeterminarGanador.PARTICIPANTE_DOS_GANA;
    }
}
