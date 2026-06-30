package modelo.formato;

import modelo.Equipo;

public class Liga implements TorneoFormato{
    @Override
    public void setTorneoFormato(Equipo equipoTorneo){
        System.out.println("Formato Generado: Liga Simple");
    }

    @Override
    public String getNombreFormato(){
        return "Liga";
    }
}
