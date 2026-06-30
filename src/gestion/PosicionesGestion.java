package gestion;

import modelo.Equipo;
import modelo.MatchResultado;
import modelo.disciplina.Disciplina;

public class PosicionesGestion {
    Disciplina disciplinaGestion;
    MatchResultado resultado;
    Equipo equipoUno;
    Equipo equipoDos;

    public void PosicionesGestion(Disciplina disciplinaGestion){
        this.disciplinaGestion = disciplinaGestion;
    }

    public void procesarResultado(Equipo equipoUno, Equipo equipoDos, MatchResultado resultado){
    }

    public int getPuntaje(Equipo equipo){
        return 0;
    }
}
