package gestion;

import modelo.Participante;
import modelo.enums.AccionPartido;

import java.awt.*;

/**
 * Métodos utilitarios para la visualización de brackets.
 * No dependen de ningún componente Swing, son lógica pura.
 */
public class BracketUtils {

    private BracketUtils() {}

    /**
     * Devuelve el nombre de la fase según la cantidad de partidos en esa ronda.
     */
    public static String getNombreFase(int cantidadPartidos) {
        return switch (cantidadPartidos) {
            case 1  -> "Final";
            case 2  -> "Semifinal";
            case 4  -> "Cuartos de Final";
            case 8  -> "Octavos de Final";
            case 16 -> "Dieciseisavos";
            default -> "Ronda de " + (cantidadPartidos * 2);
        };
    }

    /**
     * Trunca el texto si no cabe en el ancho disponible.
     */
    public static String truncar(String texto, FontMetrics fm, int maxAncho) {
        if (fm.stringWidth(texto) <= maxAncho) return texto;
        while (texto.length() > 0 && fm.stringWidth(texto + "...") > maxAncho) {
            texto = texto.substring(0, texto.length() - 1);
        }
        return texto + "...";
    }

    /**
     * Genera el texto para el historial de eventos.
     * Ejemplos: "1. [+1] Gol — Equipo A (Jugador X)"
     *           "2. [Autogol] Autogol — Equipo B"
     *           "3. [Sanción] T. Amarilla — Equipo A (Jugador Y)"
     */
    public static String buildTextoEvento(AccionPartido accion, Participante autor,
                                    Participante equipo, int num) {
        StringBuilder sb = new StringBuilder();
        sb.append(num).append(". ");

        if (accion.isSumaAlRival())       sb.append("[Autogol] ");
        else if (accion.getPuntos() == 0) sb.append("[Sanción] ");
        else                              sb.append("[+").append(accion.getPuntos()).append("] ");

        sb.append(accion.getNombre())
                .append("  —  ")
                .append(equipo.getName());

        if (!autor.equals(equipo)) {
            sb.append(" (").append(autor.getName()).append(")");
        }

        return sb.toString();
    }
}
