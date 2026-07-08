package modelo;

import modelo.enums.AccionPartido;

import java.util.ArrayList;
import java.util.List;

public class MatchResultado {

    /** Puntajes de tipo double que deben ser enteros en fútbol y basket */
    private double puntajeUno;
    private double puntajeDos;

    /** Lista de sets para tenis */
    private ArrayList<SetTenis> setsTenis;

    /** Enum para ver si existe un ganador o empate */
    private DeterminarGanador outcome;


    /** Lista de eventos ocurridos durante el partido */
    private final List<EventoPartido> eventos = new ArrayList<>();

    /** Marcadores calculados dinámicamente a partir de eventos */
    private int marcadorUno = 0;
    private int marcadorDos = 0;

    /**
     * Determina un ganador empate dependiendo de los puntajes ingresados
     * @param puntaje1 puntaje del primer participante
     * @param puntaje2 puntaje del segundo participante
     * @return el resultado dependiendo los puntajes ingresados
     */
    public DeterminarGanador setDeterminarGanador(double puntaje1, double puntaje2){
        if (puntaje1 > puntaje2) return DeterminarGanador.PARTICIPANTE_UNO_GANA;
        if (puntaje2 > puntaje1) return DeterminarGanador.PARTICIPANTE_DOS_GANA;
        return DeterminarGanador.EMPATE;
    }

    /** Constructor para Futbol y basket */
    public MatchResultado(int puntajeUno, int puntajeDos){
        if (puntajeUno < 0 || puntajeDos < 0){
            throw new IllegalArgumentException("Los puntajes no pueden ser negativos");
        }

        this.puntajeUno = (double)puntajeUno;
        this.puntajeDos = (double)puntajeDos;
        this.outcome = setDeterminarGanador(puntajeUno, puntajeDos);
    }

    /**
     * Constructor para partidos en curso.
     * Los marcadores se calculan a medida que se registran eventos.
     */
    public MatchResultado() {
        this.outcome = DeterminarGanador.EMPATE; // empieza 0-0
    }

    /**
     * Constructor para Tenis
     * @param setsTenis lista de sets de tenis
     */
    public MatchResultado(ArrayList<SetTenis> setsTenis){
        if (setsTenis.size() < 2){
            throw new IllegalArgumentException("Un partido de tenis debe tener por lo mínimo 2 resultados");
        }
        if (setsTenis == null){
            throw new IllegalArgumentException("Los sets de tenis no pueden ser nulos");
        }
        this.setsTenis = setsTenis;

        /** A diferencia de determinar los puntajes al crear un enfrentamiento,
         *  en el tenis se saca automáticamente dependiendo del ganador de
         *  cada set del ArrayList */
        int puntajeUno = 0;
        int puntajeDos = 0;

        for (int s = 0; s < setsTenis.size(); s++){
            SetTenis setActual = setsTenis.get(s);
            if (setActual.determinarGanadorTenis() == DeterminarGanador.PARTICIPANTE_UNO_GANA){
                puntajeUno++;
            }
            else puntajeDos++;
        }
        this.puntajeUno = puntajeUno;
        this.puntajeDos = puntajeDos;
        this.outcome = setDeterminarGanador(puntajeUno, puntajeDos);
    }

    /**
     * Verifica que los puntajes sean válidos al tener un enfrentamiento de ajedrez
     * @param puntajeUno puntaje del primer participante
     * @param puntajeDos puntake del segundo participante
     */
    public void validarPuntajesAjedrez(double puntajeUno, double puntajeDos){
        boolean puntajeUnoValido = puntajeUno == 0.0 || puntajeUno == 0.5
                || puntajeUno == 1.0;
        boolean puntajeDosValido = puntajeDos == 0.0 || puntajeDos == 0.5
                || puntajeDos == 1.0;

        /** Los puntajes deben ser válidos */
        if (!puntajeUnoValido || !puntajeDosValido){
            throw new IllegalArgumentException("Los puntajes deben ser válidos");
        }

        /** Al sumar los dos puntajes, no deben sobrepasar 1.0 */
        if (Double.compare(puntajeUno + puntajeDos, 1.0) != 0){
            throw new IllegalArgumentException("La suma de los puntajes no puede ser mayor a 1.0");
        }
    }

    /** Constructor para Ajedrez */
    public MatchResultado(double puntajeUno, double puntajeDos, boolean esAjedrez){
        if (!esAjedrez) {
            validarPuntajesAjedrez(puntajeUno, puntajeDos);
        }
        this.puntajeUno = puntajeUno;
        this.puntajeDos = puntajeDos;
        this.outcome = setDeterminarGanador(puntajeUno, puntajeDos);
    }

    /**
     * Verifica que el resultado actual es empate
     * @return variable enum si resulta en empate
     */
    public boolean esEmpate(){
        return outcome == DeterminarGanador.EMPATE;
    }

    /** Getters */
    public DeterminarGanador getOutcome(){
        return outcome;
    }

    public double getPuntajeUno() {
        return puntajeUno;
    }

    public double getPuntajeDos() {
        return puntajeDos;
    }

    public ArrayList<SetTenis> getSetsTenis(){
        return setsTenis;
    }


    /**
     * Registra un evento en el partido y actualiza el marcador.
     *
     * @param evento            el evento a registrar
     * @param esParticipanteUno true si el equipoOrigen es el participante uno del Match
     */
    public void registrarEvento(EventoPartido evento, boolean esParticipanteUno) {
        if (evento == null) throw new IllegalArgumentException("El evento no puede ser nulo.");
        eventos.add(evento);

        int puntos = evento.getAccion().getPuntos();
        if (puntos == 0) return; // tarjetas y similares no mueven el marcador

        if (evento.getAccion() == AccionPartido.AJEDREZ_TABLAS) {
            // Tablas: suma 1 a cada uno
            marcadorUno++;
            marcadorDos++;
        } else if (evento.getAccion().isSumaAlRival()) {
            // Autogol: suma al rival
            if (esParticipanteUno) marcadorDos += puntos;
            else                   marcadorUno += puntos;
        } else {
            // Caso normal
            if (esParticipanteUno) marcadorUno += puntos;
            else                   marcadorDos += puntos;
        }

        // Recalcular outcome
        this.outcome = setDeterminarGanador(marcadorUno, marcadorDos);
    }

    public List<EventoPartido> getEventos()  { return eventos; }
    public int getMarcadorUno()              { return marcadorUno; }
    public int getMarcadorDos()              { return marcadorDos; }
}
