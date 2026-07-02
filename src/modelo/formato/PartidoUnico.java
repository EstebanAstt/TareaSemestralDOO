package modelo.formato;

import java.util.ArrayList;
import modelo.Equipo;
import modelo.Match;

public class PartidoUnico implements TorneoFormato {
    @Override
    public int getMinimoParticipantes(){
        return 2;
    }

    @Override
    public ArrayList<Match> generarMatches(Equipo equipoTorneo){
        /** Hasta ahora se prueba un ArrayList de prueba, posteriormente
         *  se debe cambiar el cálculo de la cantidad de matches a BracketsGestion */

        // Revisar gestión después de los formatos

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
}
