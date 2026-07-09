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

    private List<Participante> jugadores;

    /**
     * Constructor que describe el nombre y contacto del equipo
     * @param name Nombre del equipo
     * @param contact Contacto del equipo
     */
    public Equipo(String name, String contact) {
        super(name, contact);
        this.jugadores = new ArrayList<>();
    }

    /**
     * Constructor que describe el nombre del equipo
     * @param nombre Nombre del equipo
     */
    public Equipo(String nombre) {
        super(nombre, "");
        this.jugadores = new ArrayList<>();
    }

    /**
     * Añade un jugador (participante) al equipo.
     * @param jugador Jugador que se quiere ingresar
     */
    public void addjugador(Participante jugador) {
        if (jugador == null) throw new IllegalArgumentException("El jugador no puede ser null.");
        if (!jugadores.contains(jugador)) {
            jugadores.add(jugador);
        }
    }

    /**
     * Elimina un jugador (participante) al equipo.
     * @param jugador Jugador que se quiere eliminar
     */
    public void removePlayer(Participante jugador) {
        jugadores.remove(jugador);
    }

    /**
     * Getter de los jugadores en total
     * @return Lista de participantes
     */
    public List<Participante> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    /**
     * Retorna el número de jugadores del equipo
     * @return Variable entera del tamaño del equipo
     */
    public int getEquipoSize() {
        return jugadores.size();
    }

    /**
     * Se obtiene un jugador del equipo a partir de su índice
     * @param indexJugador Índice del jugador
     * @return Jugador buscado
     */
    public Participante getJugadorIndividual(int indexJugador){
        return jugadores.get(indexJugador);
    }
}
