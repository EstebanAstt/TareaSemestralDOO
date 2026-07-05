package gestion;

import java.util.ArrayList;
import modelo.Match;
import modelo.formato.TorneoFormato;

/** Hacer esta ahora */
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
}
