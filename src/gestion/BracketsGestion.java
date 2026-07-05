package gestion;

import java.util.ArrayList;

import modelo.Equipo;
import modelo.Match;
import modelo.formato.TorneoFormato;

public class BracketsGestion {
    private TorneoFormato formato;
    private ArrayList<Match> partidos;

    /** Lista de observadores del bracket, avisan actualizaciones */
    private ArrayList<EstadoBracketsGestion> observadoresBracket;

    public BracketsGestion(TorneoFormato formato){
        this.formato = formato;
        this.partidos = new ArrayList<>();
        this.observadoresBracket = new ArrayList<>();

        /** Se referencia que la gestión del bracket está aquí */
        this.formato.setBracketsGestion(this);
    }

    /** Abajo hay métodos de observadores */

    public void agregarObservadorBracket(EstadoBracketsGestion observador){
        if (observador == null){
            throw new IllegalArgumentException("El observador no puede ser nulo");
        }
        if (observadoresBracket.contains(observador)){
            throw new IllegalArgumentException("Éste observador ya está en la lista de observadores");
        }
        observadoresBracket.add(observador);
    }

    public void eliminarObservadorBracket(EstadoBracketsGestion observador){
        observadoresBracket.remove(observador);
    }

    public void notificarBracketGenerado(){
        // El ciclo for representa "por observador local en observadoresBracket"
        for (EstadoBracketsGestion observadorLocal : observadoresBracket){
            observadorLocal.onBracketGenerado(partidos);
        }
    }

    public void notificarMatchActualizado(Match partido){
        for (EstadoBracketsGestion observadorLocal : observadoresBracket){
            observadorLocal.onMatchActualizado(partido);
        }
    }

    /** Genera un bracket a partir del formato, notifica a los observadores */
    public ArrayList<Match> generarBracket(Equipo equipo){
        if (equipo.getEquipoSize() < formato.getMinimoParticipantes()){
            throw new IllegalArgumentException("No se alcanzó la cantidad mínima de participantes");
        }
        this.partidos = formato.generarMatches(equipo);
        notificarBracketGenerado();
        return partidos;
    }

    /** Registra el resultado de un partido ingresado, notifica a los observadores */
    public void registrarResultado(Match partido){
        if (!partidos.contains(partido)){
            throw new IllegalArgumentException("El partido no pertenece al bracket");
        }
        formato.actualizarBracket(partido, this);
        notificarMatchActualizado(partido);
    }

    /** Getters */
    public ArrayList<Match> getPartidosBracketsGestion(){
        return partidos;
    }

    public TorneoFormato getFormatoBracketsGestion(){
        return formato;
    }

    public int getCantidadPartidosEsperados(Equipo equipo){
        return formato.getCantidadMatches(equipo);
    }
}
