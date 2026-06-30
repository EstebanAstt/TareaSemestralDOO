package modelo.disciplina;

import modelo.Torneo;
import modelo.formato.TorneoFormato;

public class Ajedrez implements Disciplina {
    Torneo torneoAjedrez;
    TorneoFormato formatoAjedrez;
    int puntajesAjedrez;

    @Override
    public void setFormato(TorneoFormato formatoAjedrez){
        this.formatoAjedrez = formatoAjedrez;
    }

    @Override
    public int getPuntajes(Torneo torneo){
        torneoAjedrez = torneo;
        return puntajesAjedrez;
    }

    @Override
    public boolean permiteEmpate(){
        return true;
    }
}
