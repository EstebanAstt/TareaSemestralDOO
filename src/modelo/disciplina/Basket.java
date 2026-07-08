package modelo.disciplina;

import modelo.DeterminarGanador;
import modelo.MatchResultado;
import modelo.formato.TorneoFormato;
import java.util.ArrayList;

public class Basket implements Disciplina {
    @Override
    public double puntajeVictoria(){
        return 2.0;
    }

    @Override
    public double puntajeEmpate(){
        throw new UnsupportedOperationException("Esta disciplina no permite empates");
    }

    @Override
    public double puntajePerdida(){
        return 1.0;
    }

    @Override
    public boolean validarFormato(TorneoFormato formato){
        boolean validacion;

        switch (formato.getNombreFormato()){
            case "Liga", "Ida y Vuelta", "Partido Único": validacion = true;
            default: validacion = false;
        }
        return validacion;
    }

    @Override
    public boolean permiteEmpate(){
        return false;
    }

    @Override
    public String getNombreDisciplina(){
        return "Basket";
    }

    @Override
    public boolean validarMatchResultado(MatchResultado resultado){
        boolean validacion = true;

        if (resultado == null){
            validacion = false;
        }
        if (resultado.getOutcome() == DeterminarGanador.EMPATE){
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
