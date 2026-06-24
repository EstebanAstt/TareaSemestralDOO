package modelo.disciplina;

import modelo.Torneo;

public class Ajedrez implements Disciplina {
    Torneo torneoAjedrez;

    @Override
    public void getPuntajes(Torneo torneo){
        torneoAjedrez = torneo;
    }

    @Override
    public boolean permiteEmpate(){
        return true;
    }
}
