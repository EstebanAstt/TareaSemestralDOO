import modelo.*;
import modelo.enums.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MatchResultadoTest {
    private Participante participanteUno;
    private Equipo equipoUno;

    @BeforeEach
    void setUp(){
        participanteUno = new Jugador("Jugador 1");
        equipoUno = new Equipo("Equipo 1");
    }

    /** Tests de Constructores */
    @Test
    void constructorFutbolBasketValido(){
        MatchResultado partido = new MatchResultado(3,2);
        assertEquals(3.0, partido.getPuntajeUno());
        assertEquals(2.0, partido.getPuntajeDos());
        assertEquals(DeterminarGanador.PARTICIPANTE_UNO_GANA, partido.getOutcome());
    }

    @Test
    void constructorFutbolBasketPuntajeNegativo(){
        assertThrows(IllegalStateException.class, () ->
                new MatchResultado(-1, 2),
                "Se ingresó un puntaje negativo");
        assertThrows(IllegalStateException.class, () ->
                new MatchResultado(1, -2),
                "Se ingresó un puntaje negativo");
    }

    @Test
    void constructorTenisValido(){
        ArrayList<SetTenis> sets = new ArrayList<>();
        sets.add(new SetTenis(6, 3));
        sets.add(new SetTenis(4, 6));
        sets.add(new SetTenis(6, 4));

        MatchResultado partidoTenis = new MatchResultado(sets);
        assertEquals(2.0, partidoTenis.getPuntajeUno());
        assertEquals(1.0, partidoTenis.getPuntajeDos());
        assertEquals(DeterminarGanador.PARTICIPANTE_UNO_GANA, partidoTenis.getOutcome());
    }

    @Test
    void constructorTenisMenosDeDosSets(){
        ArrayList<SetTenis> sets = new ArrayList<>();
        sets.add(new SetTenis(6, 3));

        assertThrows(IllegalArgumentException.class, () ->
                new MatchResultado(sets),
                "Se ingresaron menos de dos sets de tenis");
    }

    @Test
    void constructorTenisSetsNulo(){
        assertThrows(IllegalArgumentException.class, () ->
                new MatchResultado(null),
                "No hay ningún set de tenis ingresado");
    }

    @Test
    void constructorAjedrezValido() {
        MatchResultado partidoAjedrez = new MatchResultado(0.0, 0.5, true);
        assertEquals(0.0, partidoAjedrez.getPuntajeUno());
        assertEquals(0.5, partidoAjedrez.getPuntajeDos());
        assertEquals(DeterminarGanador.PARTICIPANTE_DOS_GANA, partidoAjedrez.getOutcome());
    }

    @Test
    void constructorAjedrezPuntajeInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                new MatchResultado(0.3, 0.7, true),
                "Ambos puntajes inválidos");

        assertThrows(IllegalArgumentException.class, () ->
                new MatchResultado(0.5, 0.4, true),
                "Un puntaje inválido");

        assertThrows(IllegalArgumentException.class, () ->
                new MatchResultado(1.5, -0.5, true),
                "Ambos puntajes inválidos (Un negativo y otro mayor a 1.0)");
    }

    @Test
    void constructorVacioOutcomeEmpate() {
        /** Se prueba un partido sin ningún puntaje */
        MatchResultado partido = new MatchResultado();

        assertEquals(DeterminarGanador.EMPATE, partido.getOutcome());
        assertEquals(0, partido.getMarcadorUno());
        assertEquals(0, partido.getMarcadorDos());

        assertTrue(partido.getEventos().isEmpty(), "No existen eventos");
    }
}
