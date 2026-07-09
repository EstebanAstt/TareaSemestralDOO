package UI;

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

    // ─────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────

    public PosicionesVer(AppWindow appWindow, TorneoGestion torneoGestion) {
        super(appWindow);
        this.torneoGestion     = torneoGestion;
        this.posicionesGestion = new PosicionesGestion();
        loadBackgroundImage("MenuPosicionesVer.png");
        initUI();
    }

    // ─────────────────────────────────────────────────────────────────
    // initUI
    // ─────────────────────────────────────────────────────────────────

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

        fondo.add(buildBarraInferior(), BorderLayout.SOUTH);

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

    // ─────────────────────────────────────────────────────────────────
    // Contenido: recalcula tabla y jornadas
    // ─────────────────────────────────────────────────────────────────

    private void construirContenido() {
        panelCentro.removeAll();
        panelCentro.add(buildSeccionClasificacion());
        panelCentro.add(Box.createVerticalStrut(24));
        panelCentro.add(buildSeccionJornadas());
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    // ── Tabla de posiciones ───────────────────────────────────────────

    private JPanel buildSeccionClasificacion() {
        JPanel sec = new JPanel(new BorderLayout(0, 8));
        sec.setOpaque(false);
        sec.setAlignmentX(Component.LEFT_ALIGNMENT);
        sec.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lbl = buildEtiqueta("Clasificación");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        sec.add(lbl, BorderLayout.NORTH);

        Disciplina         disciplina    = torneoGestion.getTorneo().getDisciplinaTorneo();
        List<Participante> participantes = torneoGestion.getTorneo().getParticipantes();
        List<Match>        partidos      = obtenerTodosPartidos();

        PosicionesDatos datos = posicionesGestion.calcularPosiciones(
                partidos, participantes, disciplina);
        List<Posiciones> tabla = datos.getTablaOrdenada(disciplina.ordenCriteriosDesempate());

        boolean muestraEmpate = disciplina.permiteEmpate();
        JTable jTable = buildJTable(tabla, muestraEmpate);

        // Altura justo para mostrar todas las filas sin scroll interno
        int alturaViewport = jTable.getRowHeight() * Math.max(1, tabla.size()) + 2;
        jTable.setPreferredScrollableViewportSize(new Dimension(800, alturaViewport));

        JScrollPane sp = new JScrollPane(jTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 230)));
        sec.add(sp, BorderLayout.CENTER);

        return sec;
    }

    private JTable buildJTable(List<Posiciones> tabla, boolean muestraEmpate) {
        String[] columnas = muestraEmpate
                ? new String[]{"#", "Participante", "PJ", "PG", "PE", "PP", "GF", "GC", "DIF", "PTS"}
                : new String[]{"#", "Participante", "PJ", "PG", "PP", "GF", "GC", "DIF", "PTS"};
        int cols = columnas.length;

        Object[][] filas = new Object[tabla.size()][cols];
        for (int i = 0; i < tabla.size(); i++) {
            Posiciones pos = tabla.get(i);
            double dif = pos.getDiferencia();
            String difStr = (dif >= 0 ? "+" : "") + fmt(dif);

            if (muestraEmpate) {
                filas[i] = new Object[]{
                        i + 1, pos.getParticipante().getName(),
                        pos.getPj(), pos.getPg(), pos.getPe(), pos.getPp(),
                        fmt(pos.getPf()), fmt(pos.getPc()), difStr, fmt(pos.getPts())
                };
            } else {
                filas[i] = new Object[]{
                        i + 1, pos.getParticipante().getName(),
                        pos.getPj(), pos.getPg(), pos.getPp(),
                        fmt(pos.getPf()), fmt(pos.getPc()), difStr, fmt(pos.getPts())
                };
            }
        }

        DefaultTableModel model = new DefaultTableModel(filas, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable jTable = new JTable(model);
        jTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jTable.setRowHeight(32);
        jTable.setShowGrid(false);
        jTable.setIntercellSpacing(new Dimension(0, 0));
        jTable.setBackground(Color.WHITE);
        jTable.setForeground(ColorPalette.COLOR_TEXT_DARK.getColor());
        jTable.setSelectionBackground(ColorPalette.COLOR_BTN.getColor());
        jTable.setSelectionForeground(Color.WHITE);
        jTable.setFocusable(false);

        // Cabecera
        JTableHeader header = jTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(ColorPalette.COLOR_BTN.getColor());
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);

        // Renderer con filas alternadas
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean selected, boolean focused, int row, int col) {
                super.getTableCellRendererComponent(t, value, selected, focused, row, col);
                setHorizontalAlignment(col == 1 ? JLabel.LEFT : JLabel.CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!selected) {
                    setBackground(row % 2 == 0
                            ? new Color(255, 255, 255)
                            : new Color(242, 246, 255));
                    setForeground(ColorPalette.COLOR_TEXT_DARK.getColor());
                }
                // PTS en negrita
                setFont(col == cols - 1
                        ? new Font("SansSerif", Font.BOLD, 14)
                        : new Font("SansSerif", Font.PLAIN, 14));
                return this;
            }
        };
        for (int c = 0; c < cols; c++) {
            jTable.getColumnModel().getColumn(c).setCellRenderer(renderer);
        }

        // Anchos
        jTable.getColumnModel().getColumn(0).setMaxWidth(40);  // #
        jTable.getColumnModel().getColumn(1).setMinWidth(160); // nombre

        return jTable;
    }

    // ── Sección de jornadas ───────────────────────────────────────────

    private JPanel buildSeccionJornadas() {
        JPanel sec = new JPanel();
        sec.setLayout(new BoxLayout(sec, BoxLayout.Y_AXIS));
        sec.setOpaque(false);
        sec.setAlignmentX(Component.LEFT_ALIGNMENT);
        sec.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lbl = buildEtiqueta("Jornadas");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sec.add(lbl);
        sec.add(Box.createVerticalStrut(8));

        List<JornadaGestion> jornadas = torneoGestion.getJornadas();
        if (jornadas == null || jornadas.isEmpty()) {
            JLabel lblVacio = buildEtiqueta("No hay jornadas generadas todavía.");
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            sec.add(lblVacio);
            return sec;
        }

        String  discNombre    = torneoGestion.getTorneo().getDisciplinaTorneo().getNombreDisciplina();
        boolean permiteEmpate = torneoGestion.getTorneo().getDisciplinaTorneo().permiteEmpate();

        for (JornadaGestion jornada : jornadas) {
            sec.add(buildPanelJornada(jornada, discNombre, permiteEmpate));
            sec.add(Box.createVerticalStrut(10));
        }

        return sec;
    }

    private JPanel buildPanelJornada(JornadaGestion jornada,
                                     String discNombre, boolean permiteEmpate) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 255, 255, 210));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 230)),
                new EmptyBorder(10, 16, 10, 16)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Encabezado
        JLabel lblNum = new JLabel("Jornada " + jornada.getNumero());
        lblNum.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNum.setForeground(ColorPalette.COLOR_BTN.getColor());
        lblNum.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNum);
        panel.add(Box.createVerticalStrut(6));

        for (Match match : jornada.getPartidos()) {
            panel.add(buildFilaPartido(match, discNombre, permiteEmpate));
            panel.add(Box.createVerticalStrut(4));
        }

        return panel;
    }

    private JPanel buildFilaPartido(Match match, String discNombre, boolean permiteEmpate) {
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 6, 2, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        Participante p1     = match.getParticipanteUno();
        Participante p2     = match.getParticipanteDos();
        String       nomP1  = p1 != null ? p1.getName() : "?";
        String       nomP2  = p2 != null ? p2.getName() : "?";

        // ── P1 (derecha del primer lado) ──────────────────────────────
        JLabel lblP1 = new JLabel(nomP1, JLabel.RIGHT);
        lblP1.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblP1.setForeground(new Color(30, 30, 30));

        // ── Botón central (marcador o "vs") ───────────────────────────
        JButton btnCentro;
        if (match.tieneResultado()) {
            MatchResultado res  = match.getResultado();
            double scoreUno     = getScoreUno(res);
            double scoreDos     = getScoreDos(res);
            String marcador     = fmt(scoreUno) + " — " + fmt(scoreDos);
            Participante ganador = match.getGanadorMatch();

            Color bgResultado = (ganador == null)
                    ? ColorPalette.COLOR_BTN_DEACTIVATED.getColor()  // empate
                    : new Color(50, 160, 80);                          // hay ganador

            btnCentro = buildButton(marcador, bgResultado, bgResultado.darker(),
                    ColorPalette.COLOR_TEXT_LIGHT.getColor());

            // Resaltar ganador
            if (ganador != null) {
                if (ganador.equals(p1)) {
                    lblP1.setFont(new Font("SansSerif", Font.BOLD, 13));
                    lblP1.setForeground(new Color(30, 130, 60));
                }
            }
        } else {
            // Partido pendiente → permite registrar resultado
            btnCentro = buildButton("vs",
                    ColorPalette.COLOR_BTN.getColor(),
                    ColorPalette.COLOR_BTN_HOVER.getColor(),
                    ColorPalette.COLOR_TEXT_LIGHT.getColor());
            btnCentro.addActionListener(e -> {
                if (match.tieneResultado()) return; // ya registrado
                MatchResultado resultado = RegistrarResultadoDialogo.mostrar(
                        this, match, discNombre, permiteEmpate);
                if (resultado != null) {
                    match.setResultadoMatch(resultado);
                    construirContenido(); // refrescar tabla y jornadas
                }
            });
        }
        btnCentro.setPreferredSize(new Dimension(100, 30));

        // ── P2 (izquierda del segundo lado) ───────────────────────────
        JLabel lblP2 = new JLabel(nomP2, JLabel.LEFT);
        lblP2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblP2.setForeground(new Color(30, 30, 30));

        if (match.tieneResultado()) {
            Participante ganador = match.getGanadorMatch();
            if (ganador != null && ganador.equals(p2)) {
                lblP2.setFont(new Font("SansSerif", Font.BOLD, 13));
                lblP2.setForeground(new Color(30, 130, 60));
            }
        }

        // ── Añadir al panel ───────────────────────────────────────────
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.EAST;
        fila.add(lblP1, gbc);
        gbc.gridx = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.CENTER;
        fila.add(btnCentro, gbc);
        gbc.gridx = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        fila.add(lblP2, gbc);

        return fila;
    }

    // ─────────────────────────────────────────────────────────────────
    // Barra inferior
    // ─────────────────────────────────────────────────────────────────

    private JPanel buildBarraInferior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(10, 40, 20, 40));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izquierda.setOpaque(false);

        JButton btnVolver = buildButton(" Volver",
                ColorPalette.COLOR_BTN_BACK.getColor(),
                ColorPalette.COLOR_BTN_BACK_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor());
        btnVolver.setPreferredSize(new Dimension(140, 42));
        btnVolver.addActionListener(e ->
                appWindow.mostrarPanel(new MenuMainPanel(appWindow)));

        JButton btnActualizar = buildButton("↻ Actualizar",
                ColorPalette.COLOR_BTN.getColor(),
                ColorPalette.COLOR_BTN_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor());
        btnActualizar.setPreferredSize(new Dimension(150, 42));
        btnActualizar.addActionListener(e -> construirContenido());

        izquierda.add(btnVolver);
        izquierda.add(btnActualizar);
        barra.add(izquierda, BorderLayout.WEST);
        return barra;
    }

    // ─────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────

    /** Aplana todas las jornadas en una lista plana de matches. */
    private List<Match> obtenerTodosPartidos() {
        List<Match> todos = new ArrayList<>();
        List<JornadaGestion> jornadas = torneoGestion.getJornadas();
        if (jornadas != null) {
            for (JornadaGestion j : jornadas) {
                todos.addAll(j.getPartidos());
            }
        }
        return todos;
    }

    /**
     * Extrae el marcador del participante uno de forma compatible con los
     * dos tipos de MatchResultado (constructor directo vs. por eventos).
     */
    private double getScoreUno(MatchResultado resultado) {
        return resultado.getEventos().isEmpty()
                ? resultado.getPuntajeUno()
                : resultado.getMarcadorUno();
    }

    /** Análogo a {@link #getScoreUno} para el participante dos. */
    private double getScoreDos(MatchResultado resultado) {
        return resultado.getEventos().isEmpty()
                ? resultado.getPuntajeDos()
                : resultado.getMarcadorDos();
    }

    /**
     * Formatea un double: si es entero lo muestra sin decimales (3 → "3"),
     * si tiene parte decimal lo muestra con un decimal (0.5 → "0.5").
     */
    private String fmt(double val) {
        return (val == (long) val)
                ? String.valueOf((long) val)
                : String.format("%.1f", val);
    }
}
