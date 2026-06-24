package modelo.disciplina;

import modelo.Torneo;

public class Futbol implements Disciplina {
    Torneo torneoFutbol;

    @Override
    public void getPuntajes(Torneo torneo){
        torneoFutbol = torneo;
    }

    @Override
    public boolean permiteEmpate(){
        return true;
    }
}
