package modelo;

import java.time.*;

/**
 * Representa el torneo.
 * Este debe tener un nombre y una fecha establecida
 */
public class Torneo {
    String nombreTorneo;
    LocalDate fecha;

    public Torneo(String nombreTorneo, LocalDate fecha){
        this.nombreTorneo = nombreTorneo;
        this.fecha = fecha;
    }
}
