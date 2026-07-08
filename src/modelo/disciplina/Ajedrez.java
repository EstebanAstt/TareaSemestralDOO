package modelo.disciplina;

import modelo.MatchResultado;
import modelo.formato.TorneoFormato;
import java.util.ArrayList;

public class Ajedrez implements Disciplina {
    /** Hasta ahora no se implementa Buchholz */

    @Override
    public double puntajeVictoria(){
        return 2.0;
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
    public boolean validarFormato(TorneoFormato formato){
        boolean validacion;

        switch (formato.getNombreFormato()){
            case "Liga", "Ida y Vuelta": validacion = true;
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
        return "Ajedrez";
    }

    @Override
    public boolean validarMatchResultado(MatchResultado resultado){
        boolean validacion = true;

        if (resultado == null){
            validacion = false;
        }
        double puntajeUno = resultado.getPuntajeUno();
        double puntajeDos = resultado.getPuntajeDos();

        /** Se prueba el validarPuntajes y si atrapa algunas de las
         *  excepciones, se invalida */
        try {
            resultado.validarPuntajesAjedrez(puntajeUno, puntajeDos);
        } catch (Exception e) {
            validacion = false;
        }
        return validacion;
    }

    @Override
    public ArrayList<CriterioDesempate> ordenCriteriosDesempate(){
        ArrayList<CriterioDesempate> orden = new ArrayList<CriterioDesempate>();
        orden.add(CriterioDesempate.PUNTOS);
        orden.add(CriterioDesempate.RESULTADO_EMPATADOS);
        return orden;
    }
}
