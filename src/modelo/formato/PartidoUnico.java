package modelo.formato;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gestion.BracketsGestion;
import modelo.Equipo;
import modelo.Match;
import modelo.Participante;

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
    public ArrayList<Match> generarMatches(Equipo equipoTorneo) {
        if (equipoTorneo.getEquipoSize() < getMinimoParticipantes()) {
            throw new IllegalArgumentException("Se necesitan por lo menos 2 jugadores en un equipo");
        }

        List<Participante> participantes = new ArrayList<>(equipoTorneo.getJugadores());

        // Mezclar aleatoriamente para el sorteo
        Collections.shuffle(participantes);

        // Calcular la potencia de 2 más cercana hacia arriba
        int n = participantes.size();
        int potencia = 1;
        while (potencia < n) potencia *= 2;

        // Rellenar con nulls (byes) hasta completar la potencia de 2
        while (participantes.size() < potencia) participantes.add(null);

        // Generar rondas por niveles (cuartos, semis, final...)
        ArrayList<Match> todos = new ArrayList<>();
        List<Participante> rondaActual = new ArrayList<>(participantes);
        int numeroRonda = 1;

        while (rondaActual.size() > 1) {
            List<Participante> siguienteRonda = new ArrayList<>();

            for (int i = 0; i < rondaActual.size(); i += 2) {
                Participante p1 = rondaActual.get(i);
                Participante p2 = rondaActual.get(i + 1);

                if (p1 == null) {
                    // p2 pasa directo (bye)
                    siguienteRonda.add(p2);
                } else if (p2 == null) {
                    // p1 pasa directo (bye)
                    siguienteRonda.add(p1);
                } else {
                    // Partido real
                    Match match = new Match(p1, p2, numeroRonda, i / 2, null);
                    todos.add(match);
                    siguienteRonda.add(null); // el ganador se conoce después
                }
            }
            rondaActual = siguienteRonda;
            numeroRonda++;
        }

        return todos;
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
