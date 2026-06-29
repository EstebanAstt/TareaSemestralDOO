package modelo.disciplina;

import modelo.Torneo;
import modelo.formato.TorneoFormato;

public class Tenis implements Disciplina {
    Torneo torneoTenis;
    TorneoFormato formatoTenis;
    int puntajesTenis;

    @Override
    public void setFormato(TorneoFormato formatoTenis){
        this.formatoTenis = formatoTenis;
    }

    @Override
    public int getPuntajes(Torneo torneo){
        torneoTenis = torneo;
        return puntajesTenis;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }
}
