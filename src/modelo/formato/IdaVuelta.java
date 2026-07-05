package modelo.formato;

import java.util.ArrayList;
import gestion.BracketsGestion;
import modelo.Equipo;
import modelo.Match;

public class IdaVuelta implements TorneoFormato{
    private BracketsGestion gestionIdaVuelta;

    @Override
    public void setBracketsGestion(BracketsGestion gestionIdaVuelta){
        this.gestionIdaVuelta = gestionIdaVuelta;
    }

    @Override
    public int getMinimoParticipantes(){
        return 3;
    }

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

    @Override
    public void actualizarBracket(Match match, BracketsGestion bracketsGestion){
        /** Aquí se debe ingresar la lógica de "Winners Bracket" y
         * "Losers Bracket", se toman los métodos de match y
         *  se registran las actualizaciones con bracketsGestion
         */
    }
}
