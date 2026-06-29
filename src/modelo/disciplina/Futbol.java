package modelo.disciplina;

import modelo.Torneo;
import modelo.formato.TorneoFormato;

public class Futbol implements Disciplina {
    Torneo torneoFutbol;
    TorneoFormato formatoFutbol;
    int puntajesFutbol;

    /** Este tipo de torneo permite todos los formatos posibles */
    public void setFormatoFutbol(TorneoFormato formatoFutbol){
        this.formatoFutbol = formatoFutbol;
    }

    @Override
    public int getPuntajes(Torneo torneo){
        torneoFutbol = torneo;
        return puntajesFutbol;
    }

    @Override
    public boolean permiteEmpate(){
        return true;
    }
}
