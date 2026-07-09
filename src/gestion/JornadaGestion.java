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

    /**
     * Constructor que guarda el número de jornada y los partidos
     * @param numero Número de jornada
     * @param partidos Partidos en total
     */
    public JornadaGestion(int numero, List<Match> partidos) {
        this.numero   = numero;
        this.partidos = partidos;
    }

    /**
     * Retorna el número de jornada
     * @return Número de jornada
     */
    public int getNumero()            { return numero; }

    /**
     * Getter de partidos
     * @return Los partidos
     */
    public List<Match> getPartidos()  { return partidos; }

    /**
     * Retorna la cantidad de partidos
     * @return La cantidad de partidos en la lista de partidos
     */
    public int getCantidadPartidos()  { return partidos.size(); }
}
