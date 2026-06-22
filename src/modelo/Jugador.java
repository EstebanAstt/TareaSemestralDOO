package modelo;

import java.io.Serializable;

/**
 * Representa a un jugador individual en el torneo.
 * Usado en disciplinas como Tenis, Ajedrez o Carrera.
 */
public class Jugador extends Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    public Jugador(String name, String contact) {
        super(name, contact);
    }

    public Jugador(String name) {
        super(name, "");
    }
}