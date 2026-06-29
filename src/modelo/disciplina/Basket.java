package modelo.disciplina;

import modelo.Torneo;
import modelo.formato.TorneoFormato;

public class Basket implements Disciplina {
    Torneo torneoBasket;
    TorneoFormato formatoBasket;
    int puntajesBasket;

    public void setFormatoBasket(TorneoFormato formatoBasket){
        this.formatoBasket = formatoBasket;
    }

    @Override
    public int getPuntajes(Torneo torneo){
        torneoBasket = torneo;
        return puntajesBasket;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }
}
