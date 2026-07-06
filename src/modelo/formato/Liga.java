package modelo.formato;

import java.util.ArrayList;
import java.util.List;

import gestion.BracketsGestion;
import gestion.GeneradorLigaGestion;
import gestion.JornadaGestion;
import modelo.Equipo;
import modelo.Match;

public class Liga implements TorneoFormato{
    private BracketsGestion gestionLiga;

    @Override
    public void setBracketsGestion(BracketsGestion gestionLiga){
        this.gestionLiga = gestionLiga;
    }

    @Override
    public int getMinimoParticipantes(){
        return 2;
    }

    @Override
    public ArrayList<Match> generarMatches(Equipo equipoTorneo) {
        if (equipoTorneo.getEquipoSize() < getMinimoParticipantes()) {
            throw new IllegalArgumentException("Se necesitan por lo menos 2 jugadores");
        }
        // Delega al GeneradorLigaGestion
        List<JornadaGestion> jornadas = GeneradorLigaGestion.generar(equipoTorneo.getJugadores());

        // Aplana las jornadas en una lista de matches para BracketsGestion (por el momento)
        ArrayList<Match> todos = new ArrayList<>();
        jornadas.forEach(j -> todos.addAll(j.getPartidos()));
        return todos;
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

    @Override
    public void actualizarBracket(Match match, BracketsGestion bracketsGestion){
        bracketsGestion.notificarMatchActualizado(match);
    }
}
