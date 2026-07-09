import gestion.*;
import modelo.Jugador;
import modelo.Participante;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test encargado de ver los casos posibles en la
 * creación de un temporizador para una carrera
 */
public class TemporizadorGestionTest {
    private TemporizadorGestion temporizadorGestion;
    private Participante jugadorUno;
    private Participante jugadorDos;

    @BeforeEach
    void setUp(){
        temporizadorGestion = new TemporizadorGestion();
        jugadorUno = new Jugador("Jugador 1");
        jugadorDos = new Jugador("Jugador 2");
    }

    @Test
    void asignarTeclaExitoso(){
        temporizadorGestion.asignarTecla('A', jugadorUno);
        Participante encontrado = temporizadorGestion.encontrarParticipantePorTecla('a');
        assertEquals(jugadorUno, encontrado);
    }

    @Test
    void asignarTeclaDuplicado(){
        temporizadorGestion.asignarTecla('A', jugadorUno);
        assertThrows(IllegalArgumentException.class, () ->
                        temporizadorGestion.asignarTecla('A', jugadorDos),
                "Ya hay un jugador con esta tecla");
    }

    @Test
    void encontrarParticipantePorTeclaSinAsignar(){
        Participante encontrado = temporizadorGestion.encontrarParticipantePorTecla('z');
        assertNull(encontrado);
    }

    @Test
    void registrarTiempoLlegadaSinCarrera(){
        assertThrows(IllegalStateException.class, () ->
                        temporizadorGestion.registrarTiempoLlegada('A'),
                "No hay una carrera en curso");
    }

    @Test
    void registrarTiempoLlegadaSinParticipante(){
        temporizadorGestion.asignarTecla('A', jugadorUno);
        temporizadorGestion.empezarCarrera();
        temporizadorGestion.registrarTiempoLlegada('b');
        assertThrows(IllegalArgumentException.class, () ->
                temporizadorGestion.getTiempoEnMilisegundos(jugadorDos));
    }

    @Test
    void registrarTiempoLlegadaDosVeces(){
        temporizadorGestion.asignarTecla('A', jugadorUno);
        temporizadorGestion.empezarCarrera();

        temporizadorGestion.registrarTiempoLlegada('A');
        long tiempoUno = temporizadorGestion.getTiempoEnMilisegundos(jugadorUno);

        temporizadorGestion.registrarTiempoLlegada('A');
        long tiempoRepetido = temporizadorGestion.getTiempoEnMilisegundos(jugadorUno);

        /** Los tiempos deberian ser iguales */
        assertEquals(tiempoUno, tiempoRepetido);
    }
}
