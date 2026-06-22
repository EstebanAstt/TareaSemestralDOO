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

    public Participante(String name, String contact) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del participante no puede estar vacío.");
        }
        this.name = name.trim();
        this.contact = (contact != null) ? contact.trim() : "";
    }

    public String getName()    { return name; }
    public String getContact() { return contact; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.name = name.trim();
    }

    public void setContact(String contact) {
        this.contact = (contact != null) ? contact.trim() : "";
    }

    @Override
    public String toString() {
        return name;
    }
}
