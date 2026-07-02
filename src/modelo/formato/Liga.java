package modelo.formato;

import modelo.Equipo;
import modelo.Match;

import java.util.ArrayList;

public class Liga implements TorneoFormato{
    @Override
    public int getMinimoParticipantes(){
        return 2;
    }

    @Override
    public ArrayList<Match> generarMatches(Equipo equipoTorneo){
        ArrayList<Match> matchesLiga = new ArrayList<>();
        if (equipoTorneo.getEquipoSize() < getMinimoParticipantes()) {
            throw new IllegalArgumentException("Se necesitan por lo menos 2 jugadores");
        }

        /** Se crea una cantidad de matches equivalentes a la cantidad de equipos
         *  Esto puede cambiar al agregar otro tipo de liga */

        for (int i = 0; i < equipoTorneo.getEquipoSize(); i++){
            for (int j = 0; j < equipoTorneo.getEquipoSize(); j++){
                int rondaLocal = 0;

                Match matchLocal = new Match(equipoTorneo.getJugadorIndividual(i),
                        equipoTorneo.getJugadorIndividual(j), rondaLocal);
                matchesLiga.add(matchLocal);
                rondaLocal++;
            }
        }
        return matchesLiga;
    }

    @Override
    public String getNombreFormato(){
        return "Liga";
    }

    @Override
    public int getCantidadMatches(Equipo equipoTorneo){
        int cantidadParticipantes = equipoTorneo.getEquipoSize();
        return cantidadParticipantes * (cantidadParticipantes - 1) / 2;
    }
}
