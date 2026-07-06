package gestion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Equipo;
import modelo.Match;
import modelo.Participante;
import modelo.Torneo;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;

public class TorneoGestion {

    private Torneo torneo;
    private List<JornadaGestion> jornadas;
    private BracketsGestion bracketsGestion;

    // ─────────────────────────────────────────────────────────────────
    // Crear torneo
    // ─────────────────────────────────────────────────────────────────

    public Torneo crearTorneo(String nombre, Disciplina disciplina,
                              TorneoFormato formato, LocalDate fechaInicio) {
        try {
            this.torneo = new Torneo(nombre, disciplina, formato, fechaInicio);
        } catch (Exception e) {
            throw new IllegalArgumentException("Faltó ingresar un atributo");
        }
        return this.torneo;
    }

    // ─────────────────────────────────────────────────────────────────
    // Participantes
    // ─────────────────────────────────────────────────────────────────

    public void inscribirParticipanteGestion(Participante participante) {
        verificarTorneoActivo();
        if (torneo.getParticipantes().contains(participante)) {
            throw new IllegalArgumentException("Este participante ya está en el torneo");
        }
        torneo.inscribirParticipanteIndividual(participante);
    }

    /**
     * Inscribe el equipo como unidad, no como jugadores sueltos.
     * Así el bracket puede mostrar "Real Madrid vs Barcelona"
     * en vez de "Vinicius vs Carvajal".
     */
    public void inscribirEquipoGestion(Equipo equipo) {
        verificarTorneoActivo();
        if (torneo.getParticipantes().contains(equipo)) {
            throw new IllegalArgumentException("Este equipo ya está en el torneo");
        }
        torneo.inscribirParticipanteIndividual(equipo);
    }

    public void eliminarParticipanteGestion(Participante participante) {
        verificarTorneoActivo();
        torneo.eliminarParticipanteIndividual(participante);
    }

    public void eliminarEquipoGestion(Equipo equipo) {
        verificarTorneoActivo();
        torneo.eliminarParticipanteIndividual(equipo);
    }

    // ─────────────────────────────────────────────────────────────────
    // Generación de calendario / bracket
    // ─────────────────────────────────────────────────────────────────

    /**
     * Genera el calendario de liga usando GeneradorLigaGestion.
     * Mantiene la estructura de jornadas para mostrarlas en el panel.
     */
    public List<JornadaGestion> generarCalendarioLiga() {
        verificarTorneoActivo();
        if (torneo.getParticipantes().size() < 2) {
            throw new IllegalStateException("Se necesitan al menos 2 participantes.");
        }
        this.jornadas = GeneradorLigaGestion.generar(torneo.getParticipantes());
        return jornadas;
    }

    /**
     * Genera el bracket de eliminatoria directa.
     * Construye un Equipo temporal como contenedor de participantes
     * para compatibilidad con BracketsGestion.
     */
    public ArrayList<Match> generarBracketEliminatoria() {
        verificarTorneoActivo();
        if (torneo.getParticipantes().size() < 2) {
            throw new IllegalStateException("Se necesitan al menos 2 participantes.");
        }

        // Equipo temporal: solo sirve como contenedor para el generador
        Equipo contenedor = new Equipo("__bracket__");
        torneo.getParticipantes().forEach(contenedor::addjugador);

        this.bracketsGestion = new BracketsGestion(torneo.getFormatoTorneo());
        return bracketsGestion.generarBracket(contenedor);
    }

    // ─────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────

    public void verificarTorneoActivo() {
        if (torneo == null) {
            throw new IllegalStateException("No existe un torneo activo");
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────────────

    public Torneo              getTorneo()           { return torneo; }
    public List<JornadaGestion> getJornadas()        { return jornadas; }
    public BracketsGestion     getBracketsGestion()  { return bracketsGestion; }
}
