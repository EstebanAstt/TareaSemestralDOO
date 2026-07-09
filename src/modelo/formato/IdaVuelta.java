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
    public void actualizarBracket(Match matchJugado, BracketsGestion bracketsGestion) {
        if (!matchJugado.tieneIda() || !matchJugado.tieneVuelta()) {
            // Solo se jugó uno de los dos → refrescar UI para mostrar marcador parcial
            bracketsGestion.notificarMatchActualizado(matchJugado);
            return;
        }

        Participante ganador = matchJugado.getGanadorGlobal(); // ya vive en Match
        if (ganador == null) {
            // Empate global → no debería llegar aquí
            bracketsGestion.notificarMatchActualizado(matchJugado);
            return;
        }

        BracketUtils.asignarGanadorASiguiente(
                ganador, matchJugado, bracketsGestion.getPartidosBracketsGestion());
        bracketsGestion.notificarMatchActualizado(matchJugado);
    }

    private Match encontrarCompañero(Match match, ArrayList<Match> todos) {
        for (Match m : todos) {
            if (m != match
                    && m.getRonda() == match.getRonda()
                    && m.getPosicionBracket() == match.getPosicionBracket()
                    && m.isEsVuelta() != match.isEsVuelta()) {
                return m;
            }
        }
        return null;
    }

    private Participante calcularGanadorGlobal(Match m1, Match m2) {
        // Identificar cuál es ida y cuál es vuelta
        Match ida    = m1.isEsVuelta() ? m2 : m1;
        Match vuelta = m1.isEsVuelta() ? m1 : m2;

        // En la vuelta los participantes están invertidos, hay que normalizar
        int golesUnoIda     = ida.getResultado().getMarcadorUno();
        int golesDosIda     = ida.getResultado().getMarcadorDos();
        // En vuelta: participanteUno es el que era DOS en la ida
        int golesUnoVuelta  = vuelta.getResultado().getMarcadorDos(); // normalizado
        int golesDosVuelta  = vuelta.getResultado().getMarcadorUno(); // normalizado

        int totalUno = golesUnoIda + golesUnoVuelta;
        int totalDos = golesDosIda + golesDosVuelta;

        if (totalUno > totalDos) return ida.getParticipanteUno();
        if (totalDos > totalUno) return ida.getParticipanteDos();
        return null; // empate global
    }
}
