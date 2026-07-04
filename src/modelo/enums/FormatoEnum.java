package modelo.enums;

import modelo.formato.IdaVuelta;
import modelo.formato.Liga;
import modelo.formato.PartidoUnico;
import modelo.formato.TorneoFormato;
/**
 * Catálogo de disciplinas deportivas disponibles en el sistema.
 *
 * Centraliza por cada disciplina:
 * - Su nombre para mostrar en la interfaz
 * - Si el torneo se juega con equipos o jugadores individuales
 * - Los formatos de torneo compatibles con esa disciplina
 *
 * Implementa el patrón Factory Method a través de crearDisciplina(),
 * permitiendo instanciar la disciplina correspondiente sin acoplar
 * el código cliente a las clases concretas (Futbol, Basket, etc.).
 */
public enum FormatoEnum {

    LIGA("Liga") {
        @Override public TorneoFormato crearFormato() { return new Liga(); }
    },
    PARTIDO_UNICO("Partido Único") {
        @Override public TorneoFormato crearFormato() { return new PartidoUnico(); }
    },
    IDA_Y_VUELTA("Ida y Vuelta") {
        @Override public TorneoFormato crearFormato() { return new IdaVuelta(); }
    };

    private final String nombre;

    FormatoEnum(String nombre) { this.nombre = nombre; }

    public abstract TorneoFormato crearFormato();
    public String getNombre() { return nombre; }

    public static FormatoEnum desdeNombre(String nombre) {
        for (FormatoEnum f : values()) {
            if (f.nombre.equals(nombre)) return f;
        }
        throw new IllegalArgumentException("Formato no encontrado: " + nombre);
    }
}