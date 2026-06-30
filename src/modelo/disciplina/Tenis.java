package modelo.disciplina;

import modelo.DeterminarGanador;
import modelo.MatchResultado;
import modelo.SetTenis;
import modelo.formato.TorneoFormato;
import java.util.ArrayList;

public class Tenis implements Disciplina {
    /** Se define una cantidad de sets predeterminados
     *  para que un jugador pueda ganar */
    private int setsParaGanar = 2;

    @Override
    public double puntajeVictoria(){
        return 1.0;
    }

    @Override
    public double puntajeEmpate(){
        /** Tenis no permite empates */
        throw new UnsupportedOperationException();
    }

    @Override
    public double puntajePerdida(){
        return 0.0;
    }

    @Override
    public boolean validarFormato(TorneoFormato formato){
        boolean validacion;

        switch (formato.getNombreFormato()){
            case "Ida y Vuelta", "Liga": validacion = true;
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
        return "Tenis";
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
        ArrayList<SetTenis> setsTenis = resultado.getSetsTenis();

        /** Desde aquí se verifica que los sets ganados de cada participante
         *  no sobrepase la cantidad necesaria de sets para ganar */
        if (setsTenis.size() > 0){
            int setsParticipanteUno = 0;
            int setsParticipanteDos = 0;

            /** Se cuentan los sets que gano cada participante */
            for (int sets = 0; sets < setsTenis.size(); sets++){
                SetTenis setActual = setsTenis.get(sets);
                if (setActual.determinarGanadorTenis() == DeterminarGanador.PARTICIPANTE_UNO_GANA){
                    setsParticipanteUno++;
                }
                else setsParticipanteDos++;
            }
            int setMaximos = setsParticipanteUno + setsParticipanteDos;

            /** Si algún jugador no alcanza los sets para ganar o la suma de los sets
             *  es mayor al mínimo para ganar, se invalida */
            if (setsParticipanteUno != setsParaGanar && setsParticipanteDos != setsParaGanar){
                validacion = false;
            }
            if (setMaximos > setsParaGanar * 2 - 1){
                validacion = false;
            }
        }
        return validacion;
    }

    @Override
    public ArrayList<CriterioDesempate> ordenCriteriosDesempate(){
        ArrayList<CriterioDesempate> orden = new ArrayList<CriterioDesempate>();
        orden.add(CriterioDesempate.PUNTOS);
        orden.add(CriterioDesempate.SETS_GANADOS);
        orden.add(CriterioDesempate.GAMES_GANADOS);
        orden.add(CriterioDesempate.RESULTADO_EMPATADOS);
        return orden;
    }
}
