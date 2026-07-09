package modelo;

import modelo.disciplina.CriterioDesempate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contenedor de la tabla de posiciones de una liga.
 * Ordena las filas según la lista de criterios de desempate
 * definida por cada disciplina.
 */
public class PosicionesDatos {

    private final List<Posiciones> tabla;

    public PosicionesDatos(List<Posiciones> tabla) {
        this.tabla = new ArrayList<>(tabla);
    }

    /**
     * Devuelve una copia de la tabla ordenada según los criterios.
     * Los criterios se aplican en cascada: si dos participantes empatan
     * en el primero, se usa el segundo, y así sucesivamente.
     *
     * @param criterios lista de criterios en orden de importancia
     * @return lista ordenada (mayor a menor)
     */
    public List<Posiciones> getTablaOrdenada(List<CriterioDesempate> criterios) {
        List<Posiciones> ordenada = new ArrayList<>(tabla);
        ordenada.sort(buildComparator(criterios));
        return ordenada;
    }

    /** Devuelve la tabla en el orden de inserción (sin ordenar). */
    public List<Posiciones> getTabla() { return tabla; }

    // ─────────────────────────────────────────────────────────────────
    // Comparadores
    // ─────────────────────────────────────────────────────────────────

    private Comparator<Posiciones> buildComparator(List<CriterioDesempate> criterios) {
        Comparator<Posiciones> comp = null;
        for (CriterioDesempate criterio : criterios) {
            Comparator<Posiciones> c = buildSingleComparator(criterio);
            comp = (comp == null) ? c : comp.thenComparing(c);
        }
        // Fallback si la lista viene vacía
        return comp != null ? comp : Comparator.comparingDouble(Posiciones::getPts).reversed();
    }

    private Comparator<Posiciones> buildSingleComparator(CriterioDesempate criterio) {
        return switch (criterio) {
            case PUNTOS            -> Comparator.comparingDouble(Posiciones::getPts).reversed();
            case DIFERENCIA_PUNTOS -> Comparator.comparingDouble(Posiciones::getDiferencia).reversed();
            case PUNTOS_A_FAVOR    -> Comparator.comparingDouble(Posiciones::getPf).reversed();
            // RESULTADO_EMPATADOS (enfrentamiento directo) requiere consultar matches cruzados
            // entre los participantes empatados — implementación futura.
            default                -> Comparator.comparingDouble(Posiciones::getPts).reversed();
        };
    }
}