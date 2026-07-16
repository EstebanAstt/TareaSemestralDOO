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
import modelo.EstadoTorneo;
/**
 * Define las principales acciones de un Organizador de un torneo
 */
public class TorneoGestion {

    private Torneo torneo;
    private List<JornadaGestion> jornadas;
    private BracketsGestion bracketsGestion;

    // ─────────────────────────────────────────────────────────────────
    // Crear torneo
    // ─────────────────────────────────────────────────────────────────

    /**
     * Crea el torneo
     * @param nombre Nombre del torneo
     * @param disciplina Disciplina del torneo
     * @param formato Formato del torneo
     * @param fechaInicio Fecha de inicio
     * @return El torneo creado
     */
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

    /**
     * Inscribe un participante individual
     * @param participante Participante al cual se quiere inscribir
     */
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
     * @param equipo Equipo al cual se quiere inscribir
     */
    public void inscribirEquipoGestion(Equipo equipo) {
        verificarTorneoActivo();
        if (torneo.getParticipantes().contains(equipo)) {
            throw new IllegalArgumentException("Este equipo ya está en el torneo");
        }
        torneo.inscribirParticipanteIndividual(equipo);
    }

    /**
     * Elimina un participante individual
     * @param participante Participante al cual se quiere eliminar
     */
    public void eliminarParticipanteGestion(Participante participante) {
        verificarTorneoActivo();
        torneo.eliminarParticipanteIndividual(participante);
    }

    /**
     * Elimina un equipo
     * @param equipo Equipo al cual se quiere eliminar
     */
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
     * @return La lista de jornadas del calendario
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
     * @return Un ArrayList de partidos que representan un bracket
     */
    public ArrayList<Match> generarBracketEliminatoria() {
        verificarTorneoActivo();
        if (torneo.getParticipantes().size() < 2) {
            throw new IllegalStateException("Se necesitan al menos 2 participantes.");
        }

        /** Equipo temporal: solo sirve como contenedor para el generador */
        Equipo contenedor = new Equipo("__bracket__");
        torneo.getParticipantes().forEach(contenedor::addjugador);

        this.bracketsGestion = new BracketsGestion(torneo.getFormatoTorneo());
        return bracketsGestion.generarBracket(contenedor);
    }
    // ─────────────────────────────────────────────────────────────────
    // Finalización del torneo
    // ─────────────────────────────────────────────────────────────────

    /**
     * Finaliza el torneo en curso.
     * Aplica una validación lógica contando los partidos para evitar
     * que se cierre un torneo con encuentros pendientes.
     */
    public void finalizarTorneo() {
        verificarTorneoActivo();

        List<Match> todosLosPartidos = obtenerTodosPartidos();
        int partidosPendientes = 0;

        // Validacion numerica: verificamos cada partido
        for (Match partido : todosLosPartidos) {
            if (!partido.tieneResultado()) {
                partidosPendientes++;
            }
        }

        // Si el contador es mayor a 0, bloqueamos la accion
        if (partidosPendientes > 0) {
            throw new IllegalStateException("Operación denegada: Quedan " + partidosPendientes + " partidos pendientes sin resultado.");
        }

        // Cambiamos el estado oficial del torneo a finalizado
        torneo.setEstado(EstadoTorneo.TORNEO_FINALIZADO);

        // Cortamos las referencias numericas y de objetos para limpiar la gestion
        this.torneo = null;
        this.jornadas = null;
        this.bracketsGestion = null;
    }
    // ─────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────

    /**
     * Verifica si el torneo está en curso
     */
    public void verificarTorneoActivo() {
        if (torneo == null) {
            throw new IllegalStateException("No existe un torneo activo");
        }
    }

    /** Aplana todas las jornadas en una lista plana de matches. */
    public List<Match> obtenerTodosPartidos() {
        List<Match> todos = new ArrayList<>();
        List<JornadaGestion> jornadas = this.getJornadas();
        if (jornadas != null) {
            for (JornadaGestion j : jornadas) {
                todos.addAll(j.getPartidos());
            }
        }
        return todos;
    }

    // ─────────────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────────────

    public Torneo              getTorneo()           { return torneo; }
    public List<JornadaGestion> getJornadas()        { return jornadas; }
    public BracketsGestion     getBracketsGestion()  { return bracketsGestion; }
}
