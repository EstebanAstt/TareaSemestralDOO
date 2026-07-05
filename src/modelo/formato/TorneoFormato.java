package modelo.formato;

import java.util.ArrayList;
import gestion.BracketsGestion;
import modelo.Equipo;
import modelo.Match;

public interface TorneoFormato {

    /** Retorna la cantidad mínima de participantes del formato */
    int getMinimoParticipantes();

    /** Genera enfrentamientos a partir de un equipo ingresado */
    ArrayList<Match> generarMatches(Equipo equipoTorneo);

    String getNombreFormato();

    /** Retorna la cantidad de enfrentamientos dependiendo la cantidad de jugadores */
    int getCantidadMatches(Equipo equipoTorneo);

    /** Se determina la gestión del formato con una variable BracketsGestion */
    void setBracketsGestion(BracketsGestion bracketsGestion);

    /** Actualiza el bracket al recibir un partido, este es distinto dependiendo
     *  del formato */
    void actualizarBracket(Match match, BracketsGestion bracketsGestion);
}
