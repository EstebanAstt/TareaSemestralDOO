package modelo.enums;

import modelo.disciplina.*;

/**
 * Catálogo de formatos de torneo disponibles en el sistema.
 *
 * Centraliza por cada formato su nombre y su instanciación,
 * evitando switches o ifs dispersos en el código cliente.
 *
 * Implementa el patrón Factory Method a través de crearFormato(),
 * permitiendo obtener el TorneoFormato correspondiente a partir
 * del nombre seleccionado por el usuario en la interfaz.
 *
 * Se usa en conjunto con DisciplinaEnum: cada disciplina define
 * qué formatos acepta, y FormatoEnum los instancia cuando se crea el torneo.
 */
public enum DisciplinaEnum {

    FUTBOL("Fútbol", true,
            new String[]{"Liga", "Partido Único", "Ida y Vuelta"}) {
        @Override public Disciplina crearDisciplina() { return new Futbol(); }
    },
    BASKET("Basket", true,
            new String[]{"Liga", "Partido Único"}) {
        @Override public Disciplina crearDisciplina() { return new Basket(); }
    },
    TENIS("Tenis", false,
            new String[]{"Partido Único", "Liga"}) {
        @Override public Disciplina crearDisciplina() { return new Tenis(); }
    },
    AJEDREZ("Ajedrez", false,
            new String[]{"Liga", "Partido Único"}) {
        @Override public Disciplina crearDisciplina() { return new Ajedrez(); }
    },
    CARRERA("Carrera", false,
            new String[]{"Carrera"}) {
        @Override public Disciplina crearDisciplina() { return new Carrera(); }
    };

    private final String nombre;
    private final boolean usaEquipos;    // true = equipos, false = jugadores
    private final String[] formatos;

    DisciplinaEnum(String nombre, boolean usaEquipos, String[] formatos) {
        this.nombre     = nombre;
        this.usaEquipos = usaEquipos;
        this.formatos   = formatos;
    }

    public abstract Disciplina crearDisciplina();

    public String getNombre()      { return nombre; }
    public boolean usaEquipos()    { return usaEquipos; }
    public String[] getFormatos()  { return formatos; }

    /** Devuelve solo los nombres para buildOptionGroup() */
    public static String[] getNombres() {
        return java.util.Arrays.stream(values())
                .map(DisciplinaEnum::getNombre)
                .toArray(String[]::new);
    }
}