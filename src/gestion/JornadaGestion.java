package gestion;


import modelo.Match;
import java.util.List;

/**
 * Representa una jornada dentro de un torneo de liga.
 * Contiene todos los partidos que se juegan en esa fecha.
 */
public class JornadaGestion {

    private final int numero;
    private final List<Match> partidos;

    public JornadaGestion(int numero, List<Match> partidos) {
        this.numero   = numero;
        this.partidos = partidos;
    }

    public int getNumero()            { return numero; }
    public List<Match> getPartidos()  { return partidos; }
    public int getCantidadPartidos()  { return partidos.size(); }
}
