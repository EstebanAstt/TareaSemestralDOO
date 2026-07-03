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

    /** Variables locales de participantes (jugadores y equipos)
     *  y de matches de un torneo
     *  listaParticipantes incluye a jugadores como a equipos */
    private ArrayList<Participante> listaParticipantes;
    private ArrayList<Match> listaMatches;

    /** Variable enum para describir el estado actual del torneo */
    private EstadoTorneo estado;

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
        this.listaParticipantes = new ArrayList<>();
        this.listaMatches = new ArrayList<>();
        this.estado = EstadoTorneo.TORNEO_CREADO;
    }

    /** Métodos de organizador, estos se verán referenciados en TorneoGestion */

    public void inscribirParticipanteIndividual(Participante participante){
        if (estado == EstadoTorneo.TORNEO_EN_PROCESO){
            throw new IllegalStateException("No se puede inscribir a un torneo que ya está en proceso");
        }
        if (estado == EstadoTorneo.TORNEO_FINALIZADO){
            throw new IllegalStateException("No se puede inscribir a un torneo que finalizó");
        }
        if (listaParticipantes.contains(participante)){
            throw new IllegalArgumentException("Este participante ya está en el torneo");
        }
        listaParticipantes.add(participante);
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

    public ArrayList<Participante> getParticipantes(){
        return listaParticipantes;
    }

    public ArrayList<Match> getMatches(){
        return listaMatches;
    }
}
