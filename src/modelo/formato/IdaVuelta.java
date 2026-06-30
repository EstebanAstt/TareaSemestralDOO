package modelo.formato;

import modelo.Equipo;

public class IdaVuelta implements TorneoFormato{
    @Override
    public void setTorneoFormato(Equipo equipoTorneo){
        System.out.println("Formato Generado: Ida y Vuelta");
    }

    @Override
    public String getNombreFormato(){
        return "Ida y Vuelta";
    }
}
