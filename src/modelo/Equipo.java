package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un equipo en el torneo.
 * Usado en disciplinas como Fútbol o Baloncesto.
 * Un equipo puede tener una lista de jugadores (opcional).
 */
public class Equipo extends Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Jugador> jugadores;

    public Equipo(String name, String contact) {
        super(name, contact);
        this.jugadores = new ArrayList<>();
    }

    public Equipo(String nombre) {
        super(nombre, "");
        this.jugadores = new ArrayList<>();
    }

    /**
     * Añade un jugador al equipo.
     */
    public void addjugador(Jugador jugador) {
        if (jugador == null) throw new IllegalArgumentException("El jugador no puede ser null.");
        if (!jugadores.contains(jugador)) {
            jugadores.add(jugador);
        }
    }

    public void removePlayer(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public int getEquipoSize() {
        return jugadores.size();
    }
}
