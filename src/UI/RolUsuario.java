package UI;


/**
 * Define el rol del usuario que ingresa al sistema.
 * Usado como base del patrón Strategy para determinar
 * qué operaciones y vistas están disponibles.
 */
public enum RolUsuario {
    ORGANIZADOR("Organizador"),
    ESPECTADOR("Espectador");

    private final String displayName;

    RolUsuario(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
