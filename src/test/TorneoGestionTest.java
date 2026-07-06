import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import gestion.*;
import modelo.disciplina.*;
import modelo.formato.*;
import modelo.*;
import java.time.LocalDate;

class TorneoGestionTest {
    private TorneoGestion torneoGestion;
    private LocalDate fechaInicio;

    private String nombre;
    private Disciplina disciplina;
    private TorneoFormato formato;

    private Participante participante1;
    private Participante participante2;
    private Equipo equipo;

    @BeforeEach
    void setUp(){
        torneoGestion = new TorneoGestion();
        fechaInicio = LocalDate.now();

        participante1 = new Jugador("Pedro", "pedro@gmail.com");
        participante2 = new Jugador("Marta", "marta@gmail.com");

        equipo = new Equipo("Equipo A");
        equipo.addjugador(participante1);
        equipo.addjugador(participante2);

        /** Se crea un torneo de ajedrez de prueba, con un formato válido */
        nombre = "Torneo Ajedrez Test";
        disciplina = new Ajedrez();
        formato = new IdaVuelta();
    }

    @Test
    void TorneoInvalido(){
        assertThrows(IllegalArgumentException.class, ()->
                        torneoGestion.crearTorneo(null, disciplina, formato, fechaInicio),
                "El torneo debe tener nombre");

        assertThrows(IllegalArgumentException.class, ()->
                        torneoGestion.crearTorneo(nombre, null, formato, fechaInicio),
                "El torneo debe tener disciplina");

        assertThrows(IllegalArgumentException.class, ()->
                        torneoGestion.crearTorneo(nombre, disciplina, null, fechaInicio),
                "El torneo debe tener formato");

        assertThrows(IllegalArgumentException.class, ()->
                        torneoGestion.crearTorneo(nombre, disciplina, formato, null),
                "El torneo debe tener fecha de inicio");
    }

    @Test
    void crearTorneoExitoso(){
        Torneo torneoExitoso = torneoGestion.crearTorneo(nombre, disciplina, formato,
                fechaInicio);

        assertNotNull(torneoExitoso);
        assertEquals(nombre, torneoExitoso.getNombreTorneo());
        assertEquals(disciplina, torneoExitoso.getDisciplinaTorneo());
        assertEquals(formato, torneoExitoso.getFormatoTorneo());
        assertEquals(fechaInicio, torneoExitoso.getFechaInicioTorneo());
    }

    @Test
    void formatoTorneoInvalido(){
        TorneoFormato formatoInvalido = new PartidoUnico();
        Torneo torneoFormatoInvalido = torneoGestion.crearTorneo(nombre,
                disciplina, formatoInvalido, fechaInicio);

        /** Cambiar esto a que la clase Torneo valide el formato
         *  en su constructor */
        assertFalse(torneoFormatoInvalido.getDisciplinaTorneo().validarFormato(formatoInvalido),
                "El formato ingresado no es válido");
    }

    @Test
    void verificarTorneoActivoSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                torneoGestion.verificarTorneoActivo(),
                "No hay torneo activo");
    }

    @Test
    void inscribirParticipanteGestionSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                torneoGestion.inscribirParticipanteGestion(participante1),
                "Se inscribió un participante en un torneo que no existe");
    }

    @Test
    void inscribirParticipanteGestionTorneoDuplicado(){
        torneoGestion.crearTorneo("Torneo Ajedrez Nuevo", disciplina,
                formato, fechaInicio);
        torneoGestion.inscribirParticipanteGestion(participante1);

        assertThrows(IllegalArgumentException.class, () ->
            torneoGestion.inscribirParticipanteGestion(participante1),
                "Se inscribió el mismo participante dos veces");
    }
}
