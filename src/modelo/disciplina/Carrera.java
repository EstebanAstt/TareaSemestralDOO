package modelo.disciplina;

import modelo.Torneo;
import modelo.formato.TorneoFormato;

public class Carrera implements Disciplina {
    Torneo torneoCarrera;
    TorneoFormato formatoCarrera;
    int puntajesCarrera;

    public void setFormatoCarrera(TorneoFormato formatoCarrera){
        this.formatoCarrera = formatoCarrera;
    }

    @Override
    public int getPuntajes(Torneo torneo){
        torneoCarrera = torneo;
        return puntajesCarrera;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }
}
