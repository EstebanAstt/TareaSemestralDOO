package gestion;

import java.time.LocalDate;
import modelo.Equipo;
import modelo.Participante;
import modelo.Torneo;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;

public class TorneoGestion {
    private Torneo torneo;

    public Torneo crearTorneo(String nombre, Disciplina disciplina,
                              TorneoFormato formato, LocalDate fechaInicio){
        try {
            this.torneo = new Torneo(nombre, disciplina, formato, fechaInicio);
        } catch (Exception e) {
            throw new IllegalArgumentException("Faltó ingresar un atributo");
        }
        return this.torneo;
    }

    /** Verifica que la variable torneo no sea nula */
    public void verificarTorneoActivo(){
        if (torneo == null) {
            throw new IllegalStateException("No existe un torneo activo");
        }
    }

    /** Aquí luego se crean métodos que replican las opciones del organizador */
    public void inscribirParticipanteGestion(Participante participante){
        verificarTorneoActivo();
        if (torneo.getParticipantes().contains(participante)){
            throw new IllegalArgumentException("Este participante ya está en el torneo");
        }
        torneo.inscribirParticipanteIndividual(participante);
    }

    public void inscribirEquipoGestion(Equipo equipo){
        verificarTorneoActivo();

        /** Hasta ahora se agregan los participantes independientemente del
         *  equipo al que pertenecen, después se puede agregar una manera
         *  de tener los equipos por separado y agregarlos al torneo */

        for (int p = 0; p < equipo.getEquipoSize(); p++){
            Participante participanteLocal = equipo.getJugadorIndividual(p);

            if (torneo.getParticipantes().contains(participanteLocal)){
                throw new IllegalArgumentException("Este participante ya está en el torneo");
            }
            torneo.inscribirParticipanteIndividual(participanteLocal);
        }
    }

    public void eliminarParticipanteGestion(Participante participante){
        verificarTorneoActivo();
        torneo.eliminarParticipanteIndividual(participante);
    }

    public void eliminarEquipoGestion(Equipo equipo){
        verificarTorneoActivo();

        /** Misma lógica que inscribirEquipoGestion */
        for (int p = 0; p < equipo.getEquipoSize(); p++){
            Participante participanteLocal = equipo.getJugadorIndividual(p);
            torneo.eliminarParticipanteIndividual(participanteLocal);
        }
    }
}
