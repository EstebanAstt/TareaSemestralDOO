package modelo.formato;

import java.util.ArrayList;

import gestion.BracketUtils;
import gestion.BracketsGestion;
import modelo.Equipo;
import modelo.Match;
import modelo.Participante;

import gestion.BracketUtils;

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
    public ArrayList<Match> generarMatches(Equipo equipoTorneo) {
        PartidoUnico base = new PartidoUnico();
        ArrayList<Match> matches = base.generarMatches(equipoTorneo);
        // Marcar todos como ida y vuelta
        matches.forEach(m -> m.setEsIdaVuelta(true));
        return matches;
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
