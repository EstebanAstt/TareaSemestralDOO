package modelo.disciplina;

import modelo.Torneo;
import modelo.formato.TorneoFormato;

public interface Disciplina {
    /** Hasta ahora se retorna un número, posteriormente se debería
     *  retornar los puntajes de cada equipo por separado (como una lista) */
    int getPuntajes(Torneo torneo);

    /** Se configura el formato del tipo de disciplina */
    void setFormato(TorneoFormato formato);

    /** Permite revisar que tipo de disciplina permite empates */
    boolean permiteEmpate();
}
