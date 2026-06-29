package modelo.disciplina;

import modelo.Torneo;

public interface Disciplina {
    /** Hasta ahora se retorna un número, posteriormente se debería
     *  retornar los puntajes de cada equipo por separado (como una lista) */
    int getPuntajes(Torneo torneo);
    boolean permiteEmpate();
}
