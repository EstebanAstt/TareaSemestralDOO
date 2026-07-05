package gestion;

import modelo.Match;
import modelo.Participante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Genera el calendario de una liga usando el algoritmo del polígono.
 *
 * Para N participantes (N par):
 *   - N-1 jornadas
 *   - N/2 partidos por jornada
 *
 * Para N impar: se añade un "bye" (participante ficticio) para
 * hacer N par, y los partidos contra el bye se descartan.
 */
public class GeneradorLigaGestion {

    private GeneradorLigaGestion() {}

    public static List<JornadaGestion> generar(List<Participante> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos 2 participantes.");
        }

        // Trabajamos sobre una copia para no modificar la original
        List<Participante> lista = new ArrayList<>(participantes);

        // Si N es impar, añadimos un "bye" para cuadrar el algoritmo
        boolean impar = lista.size() % 2 != 0;
        if (impar) lista.add(null); // null representa el "bye"

        int n = lista.size();
        int numJornadas = n - 1;
        int numPartidos = n / 2;

        List<JornadaGestion> jornadas = new ArrayList<>();

        for (int jornada = 0; jornada < numJornadas; jornada++) {
            List<Match> partidos = new ArrayList<>();

            for (int i = 0; i < numPartidos; i++) {
                Participante local    = lista.get(i);
                Participante visitante = lista.get(n - 1 - i);

                // Descartamos partidos contra el "bye"
                if (local != null && visitante != null) {
                    partidos.add(new Match(local, visitante, jornada + 1));
                }
            }

            jornadas.add(new JornadaGestion(jornada + 1, partidos));

            // Rotar: el primer elemento es fijo, rotamos el resto
            rotar(lista);
        }

        return jornadas;
    }

    /**
     * Rota la lista manteniendo el primer elemento fijo.
     * [A, B, C, D] → [A, D, B, C]
     */
    private static void rotar(List<Participante> lista) {
        // Guardamos el último
        Participante ultimo = lista.get(lista.size() - 1);
        // Desplazamos hacia la derecha desde la posición 1
        for (int i = lista.size() - 1; i > 1; i--) {
            lista.set(i, lista.get(i - 1));
        }
        // El último pasa a la posición 1
        lista.set(1, ultimo);
    }
}