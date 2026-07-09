package modelo;

/**
 * Acumula las estadísticas de un participante dentro de una liga.
 * Cada instancia representa una fila en la tabla de posiciones.
 */
public class Posiciones {

    private final Participante participante;

    private int    pj;   // partidos jugados
    private int    pg;   // ganados
    private int    pe;   // empatados
    private int    pp;   // perdidos
    private double pf;   // puntos/goles a favor
    private double pc;   // puntos/goles en contra
    private double pts;  // puntos de liga acumulados (según disciplina)

    public Posiciones(Participante participante) {
        if (participante == null) {
            throw new IllegalArgumentException("El participante no puede ser nulo.");
        }
        this.participante = participante;
    }

    // ─────────────────────────────────────────────────────────────────
    // Acumuladores
    // ─────────────────────────────────────────────────────────────────

    public void agregarVictoria(double pfPartido, double pcPartido, double ptsVictoria) {
        pj++;  pg++;
        pf  += pfPartido;
        pc  += pcPartido;
        pts += ptsVictoria;
    }

    public void agregarEmpate(double pfPartido, double pcPartido, double ptsEmpate) {
        pj++;  pe++;
        pf  += pfPartido;
        pc  += pcPartido;
        pts += ptsEmpate;
    }

    public void agregarPerdida(double pfPartido, double pcPartido, double ptsPerdida) {
        pj++;  pp++;
        pf  += pfPartido;
        pc  += pcPartido;
        pts += ptsPerdida;
    }

    // ─────────────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────────────

    public Participante getParticipante() { return participante; }
    public int    getPj()                 { return pj; }
    public int    getPg()                 { return pg; }
    public int    getPe()                 { return pe; }
    public int    getPp()                 { return pp; }
    public double getPf()                 { return pf; }
    public double getPc()                 { return pc; }
    public double getPts()                { return pts; }

    /** Diferencia de puntos/goles (a favor menos en contra). */
    public double getDiferencia()         { return pf - pc; }
}
