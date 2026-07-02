package modelo.formato;

import java.util.ArrayList;
import modelo.Equipo;
import modelo.Match;

public class IdaVuelta implements TorneoFormato{
    @Override
    public int getMinimoParticipantes(){
        return 3;
    }

    /** En BracketsGestion se debe implementar un "Winners Bracket" y
     * "Losers Bracket" para este formato en particular */
    @Override
    public ArrayList<Match> generarMatches(Equipo equipoTorneo){
        ArrayList<Match> matchesIdaVuelta = new ArrayList<>();

        if (equipoTorneo.getEquipoSize() < getMinimoParticipantes()) {
            throw new IllegalArgumentException("Se necesitan por lo menos 3 jugadores");
        }
        return matchesIdaVuelta;
    }

    @Override
    public String getNombreFormato(){
        return "Ida y Vuelta";
    }

    @Override
    public int getCantidadMatches(Equipo equipoTorneo){
        int cantidadParticipantes = equipoTorneo.getEquipoSize();
        return (cantidadParticipantes - 1) * 2 - 1;
    }
}
