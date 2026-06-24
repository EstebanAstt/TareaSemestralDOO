package modelo.disciplina;

import modelo.Torneo;

public class Tenis implements Disciplina {
    Torneo torneoTenis;

    @Override
    public void getPuntajes(Torneo torneo){
        torneoTenis = torneo;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }
}
