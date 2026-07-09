import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import gestion.*;
import modelo.disciplina.*;
import modelo.formato.*;
import modelo.*;
import java.time.LocalDate;
import java.util.ArrayList;import java.util.List;

/**
 * Test encargado de ver los casos posibles en las
 * acciones de un Organizador de un torneo
 */
class TorneoGestionTest {
    private TorneoGestion torneoGestion;

    /** Atributos del torneo */
    private String nombre;
    private Disciplina disciplina;
    private TorneoFormato formato;
    private LocalDate fechaInicio;

    /** Jugadores y un equipo */
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

    /** Torneo exitoso */
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

    /** Tests de creación de torneo inválido */
    @Test
    void crearTorneoInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                        torneoGestion.crearTorneo(null, disciplina, formato, fechaInicio),
                "El torneo debe tener nombre");

        assertThrows(IllegalArgumentException.class, () ->
                        torneoGestion.crearTorneo(nombre, null, formato, fechaInicio),
                "El torneo debe tener disciplina");

        assertThrows(IllegalArgumentException.class, () ->
                        torneoGestion.crearTorneo(nombre, disciplina, null, fechaInicio),
                "El torneo debe tener formato");
    }

    @Test
    void formatoTorneoInvalido(){
        TorneoFormato formatoInvalido = new PartidoUnico();
        Torneo torneoFormatoInvalido = torneoGestion.crearTorneo(nombre,
                disciplina, formatoInvalido, fechaInicio);

        assertFalse(torneoFormatoInvalido.getDisciplinaTorneo().validarFormato(formatoInvalido),
                "El formato ingresado no es válido");
    }

    /** Tests de inscripciones duplicadas */
    @Test
    void inscribirParticipanteGestionTorneoDuplicado(){
        torneoGestion.crearTorneo("Torneo Ajedrez Nuevo", disciplina,
                formato, fechaInicio);
        torneoGestion.inscribirParticipanteGestion(participante1);

        assertThrows(IllegalArgumentException.class, () ->
            torneoGestion.inscribirParticipanteGestion(participante1),
                "Se inscribió el mismo participante dos veces");
    }

    @Test
    void inscribirEquipoGestionTorneoDuplicado(){
        torneoGestion.crearTorneo("Torneo Ajedrez Nuevo", disciplina,
                formato, fechaInicio);
        torneoGestion.inscribirEquipoGestion(equipo);

        assertThrows(IllegalArgumentException.class, () ->
                torneoGestion.inscribirEquipoGestion(equipo),
                "Se inscibió el mismo equipo dos veces");
    }

    /** Tests de inscripción y eliminación sin torneo activo */
    @Test
    void inscribirParticipanteGestionSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                        torneoGestion.inscribirParticipanteGestion(participante1),
                "Se inscribió un participante en un torneo que no existe");
    }

    @Test
    void inscribirEquipoGestionSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                        torneoGestion.inscribirEquipoGestion(equipo),
                "Se inscibió un equipo a un torneo que no existe");
    }

    @Test
    void eliminarParticipanteGestionSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                torneoGestion.eliminarParticipanteGestion(participante1),
                "Se eliminó un participante a un torneo que no existe");
    }

    @Test
    void eliminarEquipoGestionSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                torneoGestion.eliminarEquipoGestion(equipo),
                "Se eliminó un equipo a un torneo que no existe");
    }

    /** Tests de generación de calendario */
    @Test
    void generarCalendarioLigaExitoso() {
        torneoGestion.crearTorneo(nombre, disciplina, formato, fechaInicio);

        for (int p = 0; p <= 4; p++) {
            torneoGestion.inscribirParticipanteGestion(new Jugador("Jugador" + p));
        }

        List<JornadaGestion> jornadas = torneoGestion.generarCalendarioLiga();
        assertNotNull(jornadas);
        assertFalse(jornadas.isEmpty());
        assertEquals(5, jornadas.size());
    }

    @Test
    void generarCalendarioLigaMenosDeDosParticipantes(){
        torneoGestion.crearTorneo(nombre, disciplina, formato, fechaInicio);
        torneoGestion.inscribirParticipanteGestion(participante1);

        assertThrows(IllegalStateException.class, () ->
                torneoGestion.generarCalendarioLiga(),
                "Hay menos de dos participantes en la liga");
    }

    @Test
    void generarCalendarioLigaSinTorneo() {
        assertThrows(IllegalStateException.class, () ->
                torneoGestion.generarCalendarioLiga(),
                "Se intentó crear un calendario sin un torneo");
    }

    /** Tests de bracket de eliminatoria */
    @Test
    void generarBracketEliminatoriaExitoso(){
        torneoGestion.crearTorneo(nombre, disciplina, formato, fechaInicio);

        for (int p = 0; p < 4; p++){
            torneoGestion.inscribirParticipanteGestion(new Jugador("Jugador" + p));
        }
        ArrayList<Match> bracket = torneoGestion.generarBracketEliminatoria();
        assertNotNull(bracket);

        // Arreglar este test
        assertEquals(0, bracket.size());
    }

    @Test
    void generarBracketEliminatoriaMenosDeDosParticipantes(){
        torneoGestion.crearTorneo(nombre, disciplina, formato, fechaInicio);
        torneoGestion.inscribirParticipanteGestion(participante1);

        assertThrows(IllegalStateException.class, () ->
                torneoGestion.generarBracketEliminatoria(),
                "Se intentó crear un bracket con menos de dos jugadores");
    }

    @Test
    void generarBracketEliminatoriaSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                torneoGestion.generarBracketEliminatoria(),
                "Se intentó crear un bracket en un torneo que no existe");
    }

    /** Tests de verificación de torneo activo */
    @Test
    void verificarTorneoActivoSinTorneo(){
        assertThrows(IllegalStateException.class, () ->
                        torneoGestion.verificarTorneoActivo(),
                "No hay torneo activo");
    }

    @Test
    void verificarTorneoActivoExitoso(){
        torneoGestion.crearTorneo(nombre, disciplina, formato, fechaInicio);

        assertDoesNotThrow(() -> torneoGestion.verificarTorneoActivo(),
                "Existe un torneo");
    }
}
