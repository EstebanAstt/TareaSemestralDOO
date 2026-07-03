package gestion;

import java.time.LocalDate;
import modelo.Torneo;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;

public class TorneoGestion {
    private Torneo torneo;

    public TorneoGestion(){
    }
    
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

    /*
    public void registrarEquipos(Equipo equipoIngresado){
        if (equipoIngresado == null) {
            throw new IllegalArgumentException();
        }
        if (!listaEquipos.contains(equipoIngresado)) {
            listaEquipos.add(equipoIngresado);
        }
    }
    */

    /** Aquí luego se crean métodos que replican las opciones del organizador
     *  Primero desarrollar la clase Torneo */
}
