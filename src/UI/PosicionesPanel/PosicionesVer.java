package UI.PosicionesPanel;

import UI.MenuMainPanel;
import UI.RegistrarResultadoDialogo;
import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.JornadaGestion;
import gestion.PosicionesGestion;
import gestion.TorneoGestion;
import modelo.Match;
import modelo.MatchResultado;
import modelo.Participante;
import modelo.Posiciones;
import modelo.PosicionesDatos;
import modelo.disciplina.Disciplina;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel que muestra la tabla de posiciones y el calendario de jornadas
 * de un torneo con formato Liga.
 *
 * <ul>
 *   <li>Sección superior: tabla de posiciones ordenada por criterios
 *       de desempate de la disciplina.</li>
 *   <li>Sección inferior: jornadas con marcadores. Hacer clic en "vs"
 *       abre {@link RegistrarResultadoDialogo} para cargar el resultado.</li>
 * </ul>
 *
 * La vista se recalcula con el botón "Actualizar" o automáticamente
 * después de registrar un resultado.
 */
public class PosicionesVer extends BaseWindow {

    private final TorneoGestion     torneoGestion;
    private final PosicionesGestion posicionesGestion;

    /** Panel scrolleable que contiene tabla + jornadas. */
    private JPanel panelCentro;


    /**
     * Contructor
     * @param appWindow JFrame principal
     * @param torneoGestion el tipo de gestion para el torneo
     */
    public PosicionesVer(AppWindow appWindow, TorneoGestion torneoGestion) {
        super(appWindow);
        this.torneoGestion     = torneoGestion;
        this.posicionesGestion = new PosicionesGestion();
        loadBackgroundImage("MenuPosicionesVer.png");
        initUI();
    }

    /**
     *  Inicializacion de la UI
     */
    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        JPanel fondo = createBackgroundPanel();
        fondo.setLayout(new BorderLayout());

        fondo.add(buildCabecera(), BorderLayout.NORTH);

        // ── Centro ────────────────────────────────────────────────────
        panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(10, 40, 10, 40));
        construirContenido();

        JScrollPane scroll = new JScrollPane(panelCentro);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        fondo.add(scroll, BorderLayout.CENTER);

        fondo.add(new BarraNavegacionPanel(appWindow, this, () -> construirContenido()), BorderLayout.SOUTH);

        add(fondo, BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────────────────────────
    // Cabecera
    // ─────────────────────────────────────────────────────────────────

    private JPanel buildCabecera() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 40, 10, 40));

        JLabel titulo = buildTitulo("Tabla de Posiciones");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createVerticalStrut(4));

        String nombre     = torneoGestion.getTorneo().getNombreTorneo();
        String disciplina = torneoGestion.getTorneo().getDisciplinaTorneo().getNombreDisciplina();
        JLabel sub = buildEtiqueta(nombre + "  ·  " + disciplina + " — Liga");
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sub);

        return panel;
    }

    /**
     * Recalcula y construye Jornadas
     */
    private void construirContenido() {
        panelCentro.removeAll();


        Disciplina disciplina = torneoGestion.getTorneo().getDisciplinaTorneo();
        List<Participante> participantes = torneoGestion.getTorneo().getParticipantes();
        List<Match> partidos = torneoGestion.obtenerTodosPartidos();
        PosicionesDatos datos = posicionesGestion.calcularPosiciones(partidos, participantes, disciplina);
        List<Posiciones> tabla = datos.getTablaOrdenada(disciplina.ordenCriteriosDesempate());

        panelCentro.add(new TablaPosicionesPanel(tabla, disciplina.permiteEmpate()));
        panelCentro.add(Box.createVerticalStrut(24));

        panelCentro.add(new JornadasListadoPanel(
                this,
                torneoGestion.getJornadas(),
                disciplina.getNombreDisciplina(),
                disciplina.permiteEmpate(),
                () -> construirContenido()
        ));

        panelCentro.revalidate();
        panelCentro.repaint();
    }
    /**
     * Formatea un double: si es entero lo muestra sin decimales ( de 3 a "3"),
     * si tiene parte decimal lo muestra con un decimal (de 0.5 a "0.5").
     */
    public static String fmt(double val) {
        return (val == (long) val)
                ? String.valueOf((long) val)
                : String.format("%.1f", val);
    }
}
