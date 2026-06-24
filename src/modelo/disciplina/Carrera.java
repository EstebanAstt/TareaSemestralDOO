package modelo.disciplina;

import modelo.Torneo;

public class Carrera implements Disciplina {
    Torneo torneoCarrera;

    @Override
    public void getPuntajes(Torneo torneo){
        torneoCarrera = torneo;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }
}
