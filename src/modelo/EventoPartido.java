package modelo;

import modelo.enums.AccionPartido;

/**
 * Representa un evento ocurrido durante un partido.
 * Guarda qué acción fue, quién la ejecutó y a qué equipo pertenece.
 *
 * Para disciplinas individuales (tenis, ajedrez), equipoAfectado
 * es el mismo participante que ejecutó la acción.
 */
public class EventoPartido {

    private final AccionPartido accion;
    private final Participante  autor;         // jugador que ejecutó la acción
    private final Participante  equipoOrigen;  // participante/equipo al que pertenece el autor

    public EventoPartido(AccionPartido accion, Participante autor, Participante equipoOrigen) {
        if (accion == null) throw new IllegalArgumentException("La acción no puede ser nula.");
        if (autor  == null) throw new IllegalArgumentException("El autor no puede ser nulo.");
        this.accion       = accion;
        this.autor        = autor;
        this.equipoOrigen = equipoOrigen;
    }

    public AccionPartido getAccion()       { return accion; }
    public Participante  getAutor()        { return autor; }
    public Participante  getEquipoOrigen() { return equipoOrigen; }
}
