import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import gestion.TorneoGestion;
import modelo.Torneo;
import modelo.disciplina.Disciplina;
import modelo.formato.TorneoFormato;
import java.time.LocalDate;

class TorneoGestionTest {
    private Torneo torneo;
    private TorneoGestion torneoGestion;
    private String nombre;
    private Disciplina disciplina;
    private TorneoFormato formato;
    private LocalDate fechaInicio;

    @BeforeEach
    void setUp(){
    }

    @Test
    void TorneoInvalido(){
        assertThrows(IllegalArgumentException.class, ()->
            torneoGestion.crearTorneo(nombre, disciplina, formato, fechaInicio),
            "Debería lanzar IllegalArgumentException");
    }
}
