package gestion;

import modelo.DeterminarGanador;
import modelo.Match;
import modelo.MatchResultado;
import modelo.Participante;
import modelo.Posiciones;
import modelo.PosicionesDatos;
import modelo.disciplina.Disciplina;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Calcula la tabla de posiciones de una liga a partir de los matches jugados.
 *
 * <p>Lógica de puntos:
 * <ul>
 *   <li>Victoria → disciplina.puntajeVictoria()</li>
 *   <li>Empate   → disciplina.puntajeEmpate()</li>
 *   <li>Derrota  → disciplina.puntajePerdida()</li>
 * </ul>
 *
 * <p>Compatibilidad con ambos tipos de {@link MatchResultado}:
 * <ul>
 *   <li>Resultado directo (constructor con puntajes) → usa getPuntajeUno/Dos</li>
 *   <li>Resultado por eventos (RegistrarResultadoDialogo) → usa getMarcadorUno/Dos</li>
 * </ul>
 */
public class PosicionesGestion {

    /**
     * Recorre todos los matches y acumula estadísticas por participante.
     *
     * @param partidos      lista de matches de la liga (con o sin resultado)
     * @param participantes todos los participantes inscritos en el torneo
     * @param disciplina    disciplina usada para calcular los puntos
     * @return              {@link PosicionesDatos} con la tabla completa
     */
    public PosicionesDatos calcularPosiciones(
            List<Match> partidos,
            List<Participante> participantes,
            Disciplina disciplina) {

        // LinkedHashMap mantiene el orden de inscripción de los participantes
        Map<Participante, Posiciones> mapa = new LinkedHashMap<>();
        for (Participante p : participantes) {
            mapa.put(p, new Posiciones(p));
        }

        for (Match match : partidos) {
            if (!match.tieneResultado()) continue; // partido pendiente

            Participante uno = match.getParticipanteUno();
            Participante dos = match.getParticipanteDos();
            MatchResultado resultado = match.getResultado();

            double pfUno = getScoreUno(resultado);
            double pfDos = getScoreDos(resultado);
            DeterminarGanador outcome = resultado.getOutcome();

            Posiciones posUno = mapa.get(uno);
            Posiciones posDos = mapa.get(dos);

            // Si algún participante no está en el mapa (caso inesperado), lo ignoramos
            if (posUno == null || posDos == null) continue;

            switch (outcome) {
                case PARTICIPANTE_UNO_GANA -> {
                    posUno.agregarVictoria(pfUno, pfDos, disciplina.puntajeVictoria());
                    posDos.agregarPerdida(pfDos, pfUno, disciplina.puntajePerdida());
                }
                case PARTICIPANTE_DOS_GANA -> {
                    posUno.agregarPerdida(pfUno, pfDos, disciplina.puntajePerdida());
                    posDos.agregarVictoria(pfDos, pfUno, disciplina.puntajeVictoria());
                }
                case EMPATE -> {
                    posUno.agregarEmpate(pfUno, pfDos, disciplina.puntajeEmpate());
                    posDos.agregarEmpate(pfDos, pfUno, disciplina.puntajeEmpate());
                }
            }
        }

        return new PosicionesDatos(new ArrayList<>(mapa.values()));
    }

    // ─────────────────────────────────────────────────────────────────
    // Helpers de marcador
    // ─────────────────────────────────────────────────────────────────

    /**
     * Devuelve el marcador del participante uno.
     * Si el resultado fue construido con eventos (RegistrarResultadoDialogo)
     * usamos getMarcadorUno(); si fue directo (constructor con puntajes)
     * usamos getPuntajeUno().
     */
    private double getScoreUno(MatchResultado resultado) {
        return resultado.getEventos().isEmpty()
                ? resultado.getPuntajeUno()
                : resultado.getMarcadorUno();
    }

    /** Análogo a {@link #getScoreUno} para el participante dos. */
    private double getScoreDos(MatchResultado resultado) {
        return resultado.getEventos().isEmpty()
                ? resultado.getPuntajeDos()
                : resultado.getMarcadorDos();
    }
}