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
        Collections.shuffle(participantes);

        int n = participantes.size();

        // Calcular la potencia de 2 más cercana hacia arriba
        int potencia = 1;
        while (potencia < n) potencia *= 2;

        int numByes = potencia - n;

        /** ── Distribuir byes correctamente ─────────────────────────────
         *
         *Mezclamos reales y byes intercalados para garantizar que
         *nunca se enfrenten dos byes entre sí (null vs null).
         * Esto se hace porque si se enfrentan dos byes luego un
         * enfrentamiento puede quedar incompleto y no se puede finalizar
         * un torneo.
         *Estrategia: real en posición izquierda, bye en posición derecha.
         *
         */
        List<Participante> ordenados = new ArrayList<>(participantes);
        for (int i = 0; i < numByes; i++) ordenados.add(null);

        // Intercalar: tomamos de izquierda y derecha alternadamente
        // Así: [A,B,C,D,E,null,null,null] → [A,null,B,null,C,null,D,E]
        List<Participante> semilla = new ArrayList<>();
        int izq = 0, der = ordenados.size() - 1;
        while (izq <= der) {
            semilla.add(ordenados.get(izq++));
            if (izq <= der) semilla.add(ordenados.get(der--));
        }

        ArrayList<Match> todos = new ArrayList<>();

        /**
         * primero se crea una lista con tamaño de la potencia de dos siguiente al
         * numero de participantes (en el caso de que el numero de participantes
         * sea igual a una potencia de dos el tamaño de la lista queda igual)
         */
        int ronda = 1;
        int matchesPorRonda = potencia / 2;
        while (matchesPorRonda >= 1) {
            for (int pos = 0; pos < matchesPorRonda; pos++) {
                todos.add(new Match(null, null, ronda, pos, null));
            }
            matchesPorRonda /= 2;
            ronda++;
        }

        /**
         * Luego se asignan participantes a la primera ronda
         */
        List<Match> primeraRonda = todos.stream()
                .filter(m -> m.getRonda() == 1)
                .sorted(java.util.Comparator.comparingInt(Match::getPosicionBracket))
                .collect(java.util.stream.Collectors.toList());

        for (int i = 0; i < primeraRonda.size(); i++) {
            Participante p1 = semilla.get(i * 2);
            Participante p2 = semilla.get(i * 2 + 1);
            Match match = primeraRonda.get(i);

            if (p1 == null && p2 != null) {
                // bye: p2 avanza directo a ronda 2
                asignarGanadorASiguiente(p2, match, todos);
            } else if (p2 == null && p1 != null) {
                // bye: p1 avanza directo a ronda 2
                asignarGanadorASiguiente(p1, match, todos);
            } else if (p1 != null && p2 != null) {
                // partido real
                match.setParticipanteUno(p1);
                match.setParticipanteDos(p2);
            }
            /**
             * Esto soluciona cualquier problema con enfrentamientos bye vs bye
             */
        }

        return todos;
    }

    /**
     * Asigna un participante (ganador o bye) al match de la ronda siguiente.
     */
    private void asignarGanadorASiguiente(Participante ganador, Match matchActual,
                                          ArrayList<Match> todos) {
        int rondaSiguiente = matchActual.getRonda() + 1;
        int posActual      = matchActual.getPosicionBracket();
        int posSiguiente   = posActual / 2;

        for (Match siguiente : todos) {
            if (siguiente.getRonda() == rondaSiguiente
                    && siguiente.getPosicionBracket() == posSiguiente) {
                if (posActual % 2 == 0) siguiente.setParticipanteUno(ganador);
                else                    siguiente.setParticipanteDos(ganador);
                break;
            }
        }
    }

    @Override
    public String getNombreFormato(){
        return "Partido Único";
    }

    @Override
    public int getCantidadMatches(Equipo equipoTorneo){
        return equipoTorneo.getEquipoSize() - 1;
    }

    @Override
    public void actualizarBracket(Match matchJugado, BracketsGestion bracketsGestion) {
        Participante ganador = matchJugado.getGanadorMatch();
        if (ganador == null) return;

        asignarGanadorASiguiente(ganador, matchJugado,
                bracketsGestion.getPartidosBracketsGestion());

        bracketsGestion.notificarMatchActualizado(matchJugado);
    }
}
