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

    /**
     * Constructor con todos los atributos de un torneo
     * @param nombre Nombre del torneo
     * @param disciplina Disciplina del torneo
     * @param formato Formato del torneo
     * @param fechaInicio Fecha de inicio del torneo
     */
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

    /**
     * Inscribe un participante individual
     * @param participante Participante el cual se quiere ingresar
     */
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

    /**
     * Elimina un participante individual
     * @param participante Participante el cual se quiere eliminar
     */
    public void eliminarParticipanteIndividual(Participante participante){
        if (estado == EstadoTorneo.TORNEO_EN_PROCESO){
            throw new IllegalStateException("No se puede inscribir a un torneo que ya está en proceso");
        }
        if (estado == EstadoTorneo.TORNEO_FINALIZADO){
            throw new IllegalStateException("No se puede inscribir a un torneo que finalizó");
        }
        listaParticipantes.remove(participante);
    }

    /**
     * Getter del nombre del torneo
     * @return String del nombre del torneo
     */
    public String getNombreTorneo(){
        return nombre;
    }

    /**
     * Getter de la disciplina del torneo
     * @return Variable Disciplina que describe la disciplina seleccionada
     */
    public Disciplina getDisciplinaTorneo(){
        return disciplina;
    }

    /**
     * Getter del formato del torneo
     * @return Variable TorneoFormato que describe el formato seleccionado
     */
    public TorneoFormato getFormatoTorneo(){
        return formato;
    }

    public LocalDate getFechaInicioTorneo(){
        return fechaInicio;
    }

    /**
     * Retorna la lista de participantes del torneo
     * @return ArrayList de participantes
     */
    public ArrayList<Participante> getParticipantes(){
        return listaParticipantes;
    }

    public ArrayList<Match> getMatches(){
        return listaMatches;
    }
    }
}
