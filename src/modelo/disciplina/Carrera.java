package modelo.disciplina;

import modelo.MatchResultado;
import modelo.formato.TorneoFormato;
import java.util.ArrayList;

public class Carrera implements Disciplina {
    @Override
    public double puntajeVictoria(){
        throw new UnsupportedOperationException();
    }

    @Override
    public double puntajeEmpate(){
        throw new UnsupportedOperationException();
    }

    @Override
    public double puntajePerdida(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean validarFormato(TorneoFormato formatoFutbol){
        /** Este tipo de disciplina no ocupa ningún tipo de formato */
        return false;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }

    @Override
    public String getNombreDisciplina(){
        return "Carrera";
    }

    @Override
    public boolean validarMatchResultado(MatchResultado resultado){
        /** Los resultados no se registran como MatchResultado
         *  en cambio, se registran los tiempos de llegada */
        return false;
    }

    @Override
    public ArrayList<CriterioDesempate> ordenCriteriosDesempate(){
        ArrayList<CriterioDesempate> orden = new ArrayList<CriterioDesempate>();
        return orden;
    }

    /** Determina que la disciplina ocupa un formato de tiempo cronometrado */
    public boolean esCronometrada() {
        return true;
    }
}
