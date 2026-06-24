package modelo.disciplina;

import modelo.Torneo;

public interface Disciplina {
    void getPuntajes(Torneo torneo);
    boolean permiteEmpate();
}
