package modelo.formato;

import modelo.Equipo;

public class PartidoUnico implements TorneoFormato {
    @Override
    public void setTorneoFormato(Equipo equipoTorneo){
        System.out.println("Formato Generado: Partido Único");
    }

    @Override
    public String getNombreFormato(){
        return "Partido Único";
    }
}
