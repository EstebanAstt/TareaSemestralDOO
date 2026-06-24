package modelo.disciplina;

import modelo.Torneo;

public class Basket implements Disciplina {
    Torneo torneoBasket;

    @Override
    public void getPuntajes(Torneo torneo){
        torneoBasket = torneo;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }
}
