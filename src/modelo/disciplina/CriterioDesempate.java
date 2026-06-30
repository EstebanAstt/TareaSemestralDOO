package modelo.disciplina;

/** Enum encargado de definir cada criterio de desempate
 *  Permite que cada disciplina tenga sus propios criterios */
public enum CriterioDesempate {

    /** Puntos totales acumulados */
    PUNTOS,

    /** Diferencia entre puntos a favor y en contra */
    DIFERENCIA_PUNTOS,

    /** Puntos a favor */
    PUNTOS_A_FAVOR,

    /** Resultado entre el match de empatados */
    RESULTADO_EMPATADOS,

    SETS_GANADOS,
    GAMES_GANADOS,

    /** Suma de puntos de rivales derrotados */
    SUMA_RIVALES;
}
