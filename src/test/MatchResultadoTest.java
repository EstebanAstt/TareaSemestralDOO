import modelo.*;
import modelo.enums.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test encargado de ver los casos del
 * resultado de un enfrentamiento
 */
public class MatchResultadoTest {
    private Participante jugadorUno;
    private Equipo equipo;

    private EventoPartido eventoGol;;
    private EventoPartido eventoTarjeta;
    private EventoPartido eventoTablas;

    @BeforeEach
    void setUp() {
        jugadorUno = new Jugador("Jugador 1");
        equipo = new Equipo("Equipo 1");

        eventoGol = new EventoPartido(AccionPartido.FUTBOL_GOL,
                jugadorUno, equipo);
        eventoTarjeta = new EventoPartido(AccionPartido.FUTBOL_AMARILLA,
                jugadorUno, equipo);
        eventoTablas = new EventoPartido(AccionPartido.AJEDREZ_TABLAS,
                jugadorUno, equipo);
    }

    /** Tests de Constructores */
    @Test
    void constructorFutbolBasketValido() {
        MatchResultado partido = new MatchResultado(3, 2);
        assertEquals(3.0, partido.getPuntajeUno());
        assertEquals(2.0, partido.getPuntajeDos());
        assertEquals(DeterminarGanador.PARTICIPANTE_UNO_GANA, partido.getOutcome());
    }

    @Test
    void constructorFutbolBasketPuntajeNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                        new MatchResultado(-1, 2),
                "Se ingresó un puntaje negativo");
        assertThrows(IllegalArgumentException.class, () ->
                        new MatchResultado(1, -2),
                "Se ingresó un puntaje negativo");
    }

    @Test
    void constructorTenisValido() {
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
    void constructorTenisMenosDeDosSets() {
        ArrayList<SetTenis> sets = new ArrayList<>();
        sets.add(new SetTenis(6, 3));

        assertThrows(IllegalArgumentException.class, () ->
                        new MatchResultado(sets),
                "Se ingresaron menos de dos sets de tenis");
    }

    @Test
    void constructorTenisSetsNulo() {
        assertThrows(NullPointerException.class, () ->
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
    void constructorAjedrezVictoria() {
        MatchResultado partidoAjedrez = new MatchResultado(1.0, 0.0, true);
        assertEquals(DeterminarGanador.PARTICIPANTE_UNO_GANA, partidoAjedrez.getOutcome());
    }

    @Test
    void constructorVacioOutcomeEmpate() {
        MatchResultado partido = new MatchResultado();

        assertEquals(DeterminarGanador.EMPATE, partido.getOutcome());
        assertEquals(0, partido.getMarcadorUno());
        assertEquals(0, partido.getMarcadorDos());

        assertTrue(partido.getEventos().isEmpty(), "No existen eventos");
    }

    /** Tests de registro de eventos */
    @Test
    void registrarEventoGolParticipanteUno() {
        MatchResultado partido = new MatchResultado();
        partido.registrarEvento(eventoGol, true);

        assertEquals(1, partido.getMarcadorUno());
        assertEquals(0, partido.getMarcadorDos());
        assertEquals(DeterminarGanador.PARTICIPANTE_UNO_GANA, partido.getOutcome());
    }

    @Test
    void registrarEventoGolParticipanteDos() {
        MatchResultado partido = new MatchResultado();
        partido.registrarEvento(eventoGol, false);

        assertEquals(0, partido.getMarcadorUno());
        assertEquals(1, partido.getMarcadorDos());
        assertEquals(DeterminarGanador.PARTICIPANTE_DOS_GANA, partido.getOutcome());
    }

    @Test
    void registrarEventoTarjetaNoModificaMarcador() {
        MatchResultado partido = new MatchResultado();
        partido.registrarEvento(eventoTarjeta, true);

        assertEquals(0, partido.getMarcadorUno());
        assertEquals(0, partido.getMarcadorDos());
        assertEquals(DeterminarGanador.EMPATE, partido.getOutcome());
    }

    @Test
    void registrarEventoTablas() {
        MatchResultado partido = new MatchResultado();
        partido.registrarEvento(eventoTablas, true);

        assertEquals(1, partido.getMarcadorUno());
        assertEquals(1, partido.getMarcadorDos());
        assertEquals(DeterminarGanador.EMPATE, partido.getOutcome());
    }

    @Test
    void registrarEventoNulo() {
        MatchResultado match = new MatchResultado();
        assertThrows(IllegalArgumentException.class, () ->
                        match.registrarEvento(null, true),
                "Se registró un evento nulo");
    }

    /** Tests de validación de puntajes de ajedrez */
    @Test
    void validarPuntajesAjedrezInvalido() {
        MatchResultado resultado = new MatchResultado();
        assertThrows(IllegalArgumentException.class, () ->
                        resultado.validarPuntajesAjedrez(0.3, 0.7),
                "Ambos puntajes inválidos");

        assertThrows(IllegalArgumentException.class, () ->
                        resultado.validarPuntajesAjedrez(0.4, 0.5),
                "Un puntaje inválido");

        assertThrows(IllegalArgumentException.class, () ->
                        resultado.validarPuntajesAjedrez(1.5, -0.5),
                "Ambos puntajes inválidos (Un negativo y otro mayor a 1.0)");
    }

    @Test
    void validarPuntajesAjedrezValido() {
        MatchResultado resultado = new MatchResultado();
        assertDoesNotThrow(() -> resultado.validarPuntajesAjedrez(0.0, 1.0));
        assertDoesNotThrow(() -> resultado.validarPuntajesAjedrez(0.5, 0.5));
        assertDoesNotThrow(() -> resultado.validarPuntajesAjedrez(1.0, 0.0));
    }
}
