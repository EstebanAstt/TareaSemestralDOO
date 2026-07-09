package gestion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modelo.Equipo;
import modelo.Match;
import modelo.formato.TorneoFormato;

/**
 * Clase encargada de generar un bracket a partir de un formato
 * Implementa el patrón de diseño "Observer"
 */
public class BracketsGestion {
    private TorneoFormato formato;
    private ArrayList<Match> partidos;

    /** Lista de observadores del bracket, avisan actualizaciones */
    private ArrayList<EstadoBracketsGestion> observadoresBracket;

    /**
     * Constructor que guarda el formato ingresado y crea ArrayLists
     * @param formato Formato del torneo
     */
    public BracketsGestion(TorneoFormato formato){
        this.formato = formato;
        this.partidos = new ArrayList<>();
        this.observadoresBracket = new ArrayList<>();

        /** Se referencia que la gestión del bracket está aquí */
        this.formato.setBracketsGestion(this);
    }

    /**
     * Agrega un observador a la lista de observadores
     * @param observador Observador que se quiere referenciar
     */
    public void agregarObservadorBracket(EstadoBracketsGestion observador){
        if (observador == null){
            throw new IllegalArgumentException("El observador no puede ser nulo");
        }
        if (observadoresBracket.contains(observador)){
            throw new IllegalArgumentException("Éste observador ya está en la lista de observadores");
        }
        observadoresBracket.add(observador);
    }

    /**
     * Elimina un observador de la lista de observadores
     * @param observador Observador que se quiere eliminar
     */
    public void eliminarObservadorBracket(EstadoBracketsGestion observador){
        observadoresBracket.remove(observador);
    }

    /**
     * Notifica que el bracket fue generado
     */
    public void notificarBracketGenerado(){
        /** El ciclo for representa "por observador local en observadoresBracket" */
        for (EstadoBracketsGestion observadorLocal : observadoresBracket){
            observadorLocal.onBracketGenerado(partidos);
        }
    }

    /**
     * Notifica que se actualizó una partida
     * @param partido Partido actualizado
     */
    public void notificarMatchActualizado(Match partido){
        for (EstadoBracketsGestion observadorLocal : observadoresBracket){
            observadorLocal.onMatchActualizado(partido);
        }
    }

    /**
     * Genera el bracket a partir del formato descrito en el constructor
     * @param equipo Equipo el cual posteriormente se crea el bracket
     * @return
     */
    public ArrayList<Match> generarBracket(Equipo equipo){
        if (equipo.getEquipoSize() < formato.getMinimoParticipantes()){
            throw new IllegalArgumentException("No se alcanzó la cantidad mínima de participantes");
        }
        this.partidos = formato.generarMatches(equipo);
        notificarBracketGenerado();
        return partidos;
    }

    /**
     * Registra el resultado actualizando el bracket y notificando su actualización
     * @param partido Partido el cual se quiere registrar el resultado
     */
    public void registrarResultado(Match partido){
        if (!partidos.contains(partido)){
            throw new IllegalArgumentException("El partido no pertenece al bracket");
        }
        formato.actualizarBracket(partido, this);
        notificarMatchActualizado(partido);
    }


    /**
     * Retorna el ArrayList de los partidos
     * @return Los partidos
     */
    public ArrayList<Match> getPartidosBracketsGestion(){
        return partidos;
    }

    /**
     * Retorna el formato
     * @return El formato definido en el constructor
     */
    public TorneoFormato getFormatoBracketsGestion(){
        return formato;
    }

    /**
     * Retorna la cantidad de partidos esperados
     * @param equipo Equipo el cual se utiliza para getCantidadMatches
     * @return La cantidad de partidos esperados definidos por el formato
     */
    public int getCantidadPartidosEsperados(Equipo equipo){
        return formato.getCantidadMatches(equipo);
    }

    /**
     * Agrupa los partidos por ronda y filtra aquellas fases donde
     * aún no hay ningún participante definido.
     *
     * @return Un mapa ordenado con el número de ronda y sus partidos correspondientes.
     */
    public Map<Integer, List<Match>> obtenerRondasVisibles() {
        Map<Integer, List<Match>> porRonda = new LinkedHashMap<>();

        // Se Agrupan todos los partidos por su número de ronda
        for (Match m : getPartidosBracketsGestion()) {
            porRonda.computeIfAbsent(m.getRonda(), k -> new ArrayList<>()).add(m);
        }

        // Se filtra dejando solo las rondas que tengan al menos un participante
        Map<Integer, List<Match>> rondasVisibles = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Match>> entry : porRonda.entrySet()) {
            boolean tieneParticipantes = entry.getValue().stream()
                    .anyMatch(m -> m.getParticipanteUno() != null || m.getParticipanteDos() != null);

            if (tieneParticipantes) {
                rondasVisibles.put(entry.getKey(), entry.getValue());
            }
        }

        return rondasVisibles;
    }
}
