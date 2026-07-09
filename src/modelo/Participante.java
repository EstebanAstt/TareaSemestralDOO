package modelo;

import java.io.Serializable;

/**
 * Clase abstracta que representa a un participante en el torneo.
 * Puede ser un jugador individual (Player) o un equipo (Team).
 */
public abstract class Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String contact; // email o teléfono

    /**
     * Constructor que representa un participante
     * @param name Nombre del participante
     * @param contact Contacto del participante
     */
    public Participante(String name, String contact) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del participante no puede estar vacío.");
        }
        this.name = name.trim();
        this.contact = (contact != null) ? contact.trim() : "";
    }

    /**
     * Getter del nombre del participante
     * @return Nombre del participante
     */
    public String getName()    { return name; }

    /**
     * Getter del contacto del participante
     * @return Contacto del participante
     */
    public String getContact() { return contact; }

    /**
     * Cambia el nombre de un participante
     * @param name Nombre nuevo
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.name = name.trim();
    }

    /**
     * Cambia el contacto de un participante
     * @param contact Contacto nuevo
     */
    public void setContact(String contact) {
        this.contact = (contact != null) ? contact.trim() : "";
    }

    @Override
    public String toString() {
        return name;
    }
}
