package modelo.disciplina;

import modelo.MatchResultado;
import modelo.formato.TorneoFormato;
import java.util.ArrayList;

public class Futbol implements Disciplina {
    @Override
    public double puntajeVictoria(){
        return 3.0;
    }

    @Override
    public double puntajeEmpate(){
        return 1.0;
    }

    @Override
    public double puntajePerdida(){
        return 0.0;
    }

    @Override
    public boolean validarFormato(TorneoFormato formatoFutbol){
        boolean validacion;

        switch (formatoFutbol.getNombreFormato()) {
            case "Liga", "Partido Único", "Ida y Vuelta": validacion = true;
            default: validacion = false;
        }
        return validacion;
    }

    @Override
    public boolean permiteEmpate(){
        return true;
    }

    @Override
    public String getNombreDisciplina(){
        return "Fútbol";
    }

    @Override
    public boolean validarMatchResultado(MatchResultado resultado){
        boolean validacion = true;
        if (resultado == null){
            validacion = false;
        }
        double puntajeUno = resultado.getPuntajeUno();
        double puntajeDos = resultado.getPuntajeDos();

        if (puntajeUno < 0 || puntajeDos < 0){
            validacion = false;
        }
        if (puntajeUno != (int)puntajeUno || puntajeDos != (int)puntajeDos){
            validacion = false;
        }
        return validacion;
    }

    @Override
    public ArrayList<CriterioDesempate> ordenCriteriosDesempate(){
        ArrayList<CriterioDesempate> orden = new ArrayList<CriterioDesempate>();
        orden.add(CriterioDesempate.PUNTOS);
        orden.add(CriterioDesempate.DIFERENCIA_PUNTOS);
        orden.add(CriterioDesempate.PUNTOS_A_FAVOR);
        orden.add(CriterioDesempate.RESULTADO_EMPATADOS);
        return orden;
    }
}
