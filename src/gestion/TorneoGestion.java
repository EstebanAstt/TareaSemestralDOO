package gestion;

import modelo.Equipo;
import modelo.Torneo;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;

import java.util.ArrayList;

public class TorneoGestion {
    Torneo torneo;
    Disciplina disciplina;
    TorneoFormato formato;
    ArrayList<Equipo> listaEquipos;

    /** Recibe un torneo para después añadirle sus atributos */
    public TorneoGestion(Torneo torneo){
        this.torneo = torneo;
    }

    public void setDisciplina(Disciplina disciplina){
        this.disciplina = disciplina;
    }

    public void setFormatoTorneo(TorneoFormato formato){
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
