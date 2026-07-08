package modelo.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AccionPartido {

    // ── Fútbol ─────────────────────────────────────────────────────
    FUTBOL_GOL          ("Gol",           1,  "FUTBOL", false),
    FUTBOL_AUTOGOL      ("Autogol",       1,  "FUTBOL", true),  // suma al rival
    FUTBOL_AMARILLA     ("T. Amarilla",   0,  "FUTBOL", false),  // no afecta marcador
    FUTBOL_ROJA         ("T. Roja",       0,  "FUTBOL", false),

    // ── Basket ─────────────────────────────────────────────────────
    BASKET_DOBLE        ("Doble",         2,  "BASKET", false),
    BASKET_TRIPLE       ("Triple",        3,  "BASKET", false),
    BASKET_UNO          ("Tiro libre",    1,  "BASKET", false),

    // ── Tenis ──────────────────────────────────────────────────────
    TENIS_SET_GANADOR    ("Set Ganador",    1,  "TENIS", false),

    // ── Ajedrez ────────────────────────────────────────────────────
    AJEDREZ_VICTORIA    ("Victoria",      2,  "AJEDREZ", false),
    AJEDREZ_TABLAS      ("Tablas",        1,  "AJEDREZ", false);

    private final String nombre;
    private final int puntos;       // cuánto suma al marcador del que la ejecuta
    private final String disciplina;
    private final boolean sumaAlRival;

    AccionPartido(String nombre, int puntos, String disciplina, boolean sumaAlRival) {
        this.nombre     = nombre;
        this.puntos     = puntos;
        this.disciplina = disciplina;
        this.sumaAlRival = sumaAlRival;
    }

    public String getNombre()     { return nombre; }
    public int    getPuntos()     { return puntos; }
    public String getDisciplina() { return disciplina; }

    public boolean isSumaAlRival() {
        return sumaAlRival;
    }

    /** Devuelve las acciones disponibles para una disciplina
     * @param disciplina nombre de la disciplina en mayusculas
     * */
    public static List<AccionPartido> getAccionesPara(String disciplina) {
        return Arrays.stream(values())
                .filter(a -> a.disciplina.equals(disciplina))
                .collect(Collectors.toList());
    }
}
