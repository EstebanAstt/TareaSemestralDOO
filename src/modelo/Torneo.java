package modelo;

import java.time.*;
import java.util.ArrayList;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;

/**
 * Representa el torneo.
 * Este debe tener una disciplina y formato concreto.
 * Se añaden los equipos en una lista inicial.
 */
public class Torneo {
    String nombreTorneo;
    Disciplina disciplina;
    TorneoFormato formato;
    LocalDate fecha;
    ArrayList<Equipo> listaEquipos;

    public Torneo(String nombreTorneo, LocalDate fecha){
        this.nombreTorneo = nombreTorneo;
        this.fecha = fecha;
    }

    public void setDisciplina(Disciplina disciplina){
        this.disciplina = disciplina;
    }

    public void setFormato(TorneoFormato formato){
        this.formato = formato;
    }

    public void registrarEquipos(Equipo equipoIngresado){
        if (equipoIngresado == null) {
            throw new IllegalArgumentException();
        }

        if (!listaEquipos.contains(equipoIngresado)) {
            listaEquipos.add(equipoIngresado);
        }
    }
}
