package gestion;

import java.util.ArrayList;
import gestion.temporizador.AsignacionTecla;
import gestion.temporizador.ParticipanteTiempoLlegada;
import modelo.Participante;

public class TemporizadorGestion {
    private long tiempoInicio;
    private boolean estaCorriendo;

    /** Lista con los participantes con su tecla respectiva */
    private ArrayList<AsignacionTecla> participantesConTecla;

    /** Tiempos de llegada de cada participante */
    private ArrayList<ParticipanteTiempoLlegada> participantesTiemposLlegada;

    public TemporizadorGestion(){
        this.estaCorriendo = false;
        this.participantesConTecla = new ArrayList<>();
        this.participantesTiemposLlegada = new ArrayList<>();
    }

    public void empezarCarrera(){
        this.tiempoInicio = System.nanoTime();
        this.participantesTiemposLlegada.clear();
        this.estaCorriendo = true;
    }

    public void pararCarrera(){
        this.estaCorriendo = false;
    }

    public void asignarTecla(char tecla, Participante participante){
        char teclaMayuscula = Character.toUpperCase(tecla);

        // Por participante en "participantesConTecla"
        for (AsignacionTecla pa : participantesConTecla){
            if (pa.getTeclaAsignacionTecla() == teclaMayuscula){
                throw new IllegalArgumentException("Esta tecla ya fué asignada a alguien");
            }
        }
        AsignacionTecla participanteLocal = new AsignacionTecla(tecla, participante);
        participantesConTecla.add(participanteLocal);
    }

    /**
     * Revisa si el jugador ingresado llegó a la meta o no
     */
    public boolean jugadorLlegoMeta(Participante participante){
        for (ParticipanteTiempoLlegada ptl : participantesTiemposLlegada){
            Participante participanteLocal = ptl.getParticipanteTiempoLlegada();
            if (participanteLocal.equals(participante)){
                return true;
            }
        }
        return false;
    }
}
