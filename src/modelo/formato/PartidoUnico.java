package modelo.formato;

import java.util.ArrayList;

import gestion.BracketsGestion;
import modelo.Equipo;
import modelo.Match;

public class PartidoUnico implements TorneoFormato {
    private BracketsGestion gestionPartidoUnico;

    @Override
    public void setBracketsGestion(BracketsGestion gestionPartidoUnico){
        this.gestionPartidoUnico = gestionPartidoUnico;
    }

    @Override
    public int getMinimoParticipantes(){
        return 2;
    }

    @Override
    public ArrayList<Match> generarMatches(Equipo equipoTorneo){
        /** Hasta ahora se prueba un ArrayList de prueba, posteriormente
         *  se debe cambiar el cálculo de la cantidad de matches a BracketsGestion */

        ArrayList<Match> matchesPartidoUnico = new ArrayList<>();

        if (equipoTorneo.getEquipoSize() < getMinimoParticipantes()) {
            throw new IllegalArgumentException("Se necesitan por lo menos 2 jugadores en un equipo");
        }
        return matchesPartidoUnico;
    }

    @Override
    public String getNombreFormato(){
        return "Partido Único";
    }

    @Override
    public int getCantidadMatches(Equipo equipoTorneo){
        int cantidadParticipantes = equipoTorneo.getEquipoSize();
        return cantidadParticipantes - 1;
    }

    @Override
    public void actualizarBracket(Match match, BracketsGestion bracketsGestion){
        /** Hasta ahora solamente se notifica que se produjo un cambio en
         *  el bracket dependiendo de la partida ingresada */
        bracketsGestion.notificarMatchActualizado(match);
    }
}
