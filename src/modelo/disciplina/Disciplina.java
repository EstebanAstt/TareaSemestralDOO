package modelo.disciplina;

import modelo.MatchResultado;
import modelo.formato.TorneoFormato;
import java.util.ArrayList;

/** A partir del patrón Strategy, las disciplinas heredan esta interfaz */
public interface Disciplina {

    /** Métodos de puntaje, cada disciplina aplica distinto puntaje
     *  a las victorias, perdidas y empates, estos métodos reemplazan getPuntaje */
    double puntajeVictoria();
    double puntajeEmpate();
    double puntajePerdida();

    /** Verifica que el formato sea válido dependiendo la disciplina,
     *  este reemplaza a setFormato */
    boolean validarFormato(TorneoFormato formato);

    boolean permiteEmpate();
    String getNombreDisciplina();

    /** Verifica que el MatchResultado ingresado sea coherente a la disciplina */
    boolean validarMatchResultado(MatchResultado resultado);

    /** Ordena los criterios de la disciplina dependiendo de su importancia */
    ArrayList<CriterioDesempate> ordenCriteriosDesempate();
}
