package modelo;

import java.util.ArrayList;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;
import java.time.*;

/**
 * Representa el torneo
 * Este debe tener un formato, disciplina y fecha de inicio
 */
public class Torneo {
    private String nombre;
    private Disciplina disciplina;
    private TorneoFormato formato;
    private LocalDate fechaInicio;

    public Torneo(String nombre, Disciplina disciplina, TorneoFormato formato, LocalDate fechaInicio){
        if (nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El torneo debe tener un nombre");
        }
        if (disciplina == null || formato == null){
            throw new IllegalArgumentException("El torneo debe tener disciplina y un formato");
        }
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.formato = formato;
        this.fechaInicio = fechaInicio;
    }

    /** Getters */
    public String getNombreTorneo(){
        return nombre;
    }

    public Disciplina getDisciplinaTorneo(){
        return disciplina;
    }

    public TorneoFormato getFormatoTorneo(){
        return formato;
    }

    public LocalDate getFechaInicioTorneo(){
        return fechaInicio;
    }
}
