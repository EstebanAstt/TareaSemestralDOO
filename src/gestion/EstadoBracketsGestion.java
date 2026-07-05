package gestion;
import java.util.ArrayList;
import modelo.Match;

/**
 * Interfaz referenciada del patrón de diseño "Observer"
 * Sirve para la notificación de cambios en el bracket
 * dependiendo del formato del Torneo (Liga, Ida y Vuelta y Partido Único)
 */
public interface EstadoBracketsGestion {
    /** Avisa cuando se generó o regeneró un bracket inicial */
    void onBracketGenerado(ArrayList<Match> partidos);

    /** Avisa cuando se actualiza un match en específico */
    void onMatchActualizado(Match partido);
}
