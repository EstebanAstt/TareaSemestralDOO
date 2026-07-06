package UI;

import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.BracketsGestion;
import gestion.TorneoGestion;
import modelo.Match;
import modelo.Participante;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel que muestra el bracket de eliminatoria directa.
 *
 * Organiza los partidos por ronda (octavos, cuartos, semis, final)
 * y muestra cada enfrentamiento como un par de botones apilados,
 * uno por participante. Al hacer clic en un par se podrá registrar
 * el resultado (funcionalidad futura).
 *
 * Usa el patrón Observer a través de BracketsGestion: cuando se
 * registre un resultado, el panel se actualiza automáticamente.
 */
public class BracketVer extends BaseWindow implements gestion.EstadoBracketsGestion {

    // ── Colores específicos del bracket ────────────────────────────────
    private static final Color COLOR_MATCH       = new Color(30, 100, 140, 255);
    private static final Color COLOR_MATCH_HOVER = new Color(20,  70, 100, 255);
    private static final Color COLOR_GANADOR     = new Color(40, 160,  80, 255);
    private static final Color COLOR_PENDIENTE   = new Color(150, 150, 150, 200);

    private final TorneoGestion torneoGestion;
    private final BracketsGestion bracketsGestion;

    // Panel central que se reconstruye al actualizar el bracket
    private JPanel panelRondas;

    public BracketVer(AppWindow appWindow, TorneoGestion torneoGestion) {
        super(appWindow);
        this.torneoGestion   = torneoGestion;
        this.bracketsGestion = torneoGestion.getBracketsGestion();
        loadBackgroundImage("MenuBracketVer.png");
        initUI();

        // Registrar este panel como observador (patrón Observer)
        // BracketVer implementa EstadoBracketsGestion, entonces se pasa a sí mismo
        bracketsGestion.agregarObservadorBracket(this);
    }

    // ─────────────────────────────────────────────────────────────────
    // initUI
    // ─────────────────────────────────────────────────────────────────
    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        JPanel fondo = createBackgroundPanel();
        fondo.setLayout(new BorderLayout());

        // ── Cabecera: nombre y tipo del torneo ─────────────────────────
        fondo.add(buildCabecera(), BorderLayout.NORTH);

        // ── Centro: rondas del bracket con scroll ──────────────────────
        panelRondas = new JPanel();
        panelRondas.setOpaque(false);
        construirRondas();

        JScrollPane scroll = new JScrollPane(panelRondas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        fondo.add(scroll, BorderLayout.CENTER);

        // ── Barra inferior ─────────────────────────────────────────────
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

        String nombreTorneo  = torneoGestion.getTorneo().getNombreTorneo();
        String nombreFormato = torneoGestion.getTorneo().getFormatoTorneo().getNombreFormato();

        panel.add(buildTitulo(nombreTorneo));
        panel.add(Box.createVerticalStrut(4));

        JLabel lblFormato = buildEtiqueta("Tipo: " + nombreFormato);
        lblFormato.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblFormato);

        return panel;
    }

    // ─────────────────────────────────────────────────────────────────
    // Construcción de rondas
    // ─────────────────────────────────────────────────────────────────

    /**
     * Agrupa los matches por ronda y construye una columna por cada una.
     * La ronda 1 es la primera (octavos/cuartos según el tamaño),
     * la última es la final.
     */
    private void construirRondas() {
        panelRondas.removeAll();

        ArrayList<Match> partidos = bracketsGestion.getPartidosBracketsGestion();

        // Agrupar por ronda manteniendo el orden
        Map<Integer, List<Match>> porRonda = new LinkedHashMap<>();
        for (Match m : partidos) {
            porRonda.computeIfAbsent(m.getRonda(), k -> new ArrayList<>()).add(m);
        }

        int totalRondas = porRonda.size();

        // Layout horizontal: una columna por ronda
        panelRondas.setLayout(new GridLayout(1, totalRondas, 30, 0));
        panelRondas.setBorder(new EmptyBorder(10, 40, 10, 40));

        int numRonda = 1;
        for (Map.Entry<Integer, List<Match>> entry : porRonda.entrySet()) {
            String nombreFase = getNombreFase(entry.getValue().size());
            panelRondas.add(buildColumnaRonda(nombreFase, entry.getValue(), numRonda == totalRondas));
            numRonda++;
        }

        panelRondas.revalidate();
        panelRondas.repaint();
    }

    /**
     * Construye una columna vertical con el nombre de la fase
     * y los pares de botones de cada enfrentamiento.
     */
    private JPanel buildColumnaRonda(String nombreFase, List<Match> partidos, boolean esUltimaRonda) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setOpaque(false);

        // Etiqueta de la fase
        JLabel lblFase = buildEtiqueta(nombreFase);
        lblFase.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFase.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFase.setForeground(new Color(60, 60, 60));
        columna.add(lblFase);
        columna.add(Box.createVerticalStrut(12));

        // Par de botones por cada partido
        for (Match match : partidos) {
            columna.add(buildParticipantes(match));
            columna.add(Box.createVerticalStrut(20));
        }

        columna.add(Box.createVerticalGlue());
        return columna;
    }

    /**
     * Construye el par de botones (participante 1 arriba, participante 2 abajo)
     * para un enfrentamiento. Si hay ganador, lo resalta en verde.
     */
    private JPanel buildParticipantes(Match match) {
        JPanel par = new JPanel();
        par.setLayout(new BoxLayout(par, BoxLayout.Y_AXIS));
        par.setOpaque(false);
        par.setAlignmentX(Component.CENTER_ALIGNMENT);

        Participante p1     = match.getParticipanteUno();
        Participante p2     = match.getParticipanteDos();
        Participante ganador = match.getGanadorMatch();

        JButton btnP1 = buildBotonParticipante(
                p1 != null ? p1.getName() : "BYE",
                p1 != null && p1.equals(ganador) ? COLOR_GANADOR : COLOR_MATCH,
                p1 != null && p1.equals(ganador) ? COLOR_GANADOR.darker() : COLOR_MATCH_HOVER
        );

        JButton btnP2 = buildBotonParticipante(
                p2 != null ? p2.getName() : "BYE",
                p2 != null && p2.equals(ganador) ? COLOR_GANADOR : COLOR_MATCH,
                p2 != null && p2.equals(ganador) ? COLOR_GANADOR.darker() : COLOR_MATCH_HOVER
        );

        // Al hacer clic en el par → futuro: registrar resultado
        btnP1.addActionListener(e -> onClickMatch(match));
        btnP2.addActionListener(e -> onClickMatch(match));

        par.add(btnP1);
        par.add(Box.createVerticalStrut(2)); // separador mínimo entre los dos botones
        par.add(btnP2);

        return par;
    }

    /**
     * Botón individual de participante dentro del bracket.
     */
    private JButton buildBotonParticipante(String nombre, Color bg, Color hover) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getModel().isRollover() ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                // Truncar nombre si es muy largo
                String display = truncar(nombre, fm, getWidth() - 16);
                g2.drawString(display, 10, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────
    // Barra inferior
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildBarraInferior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(10, 40, 20, 40));

        JButton btnVolver = buildButton(
                " Volver",
                ColorPalette.COLOR_BTN_BACK.getColor(),
                ColorPalette.COLOR_BTN_BACK_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnVolver.setPreferredSize(new Dimension(160, 45));
        btnVolver.addActionListener(e ->
                appWindow.mostrarPanel(new MenuMainPanel(appWindow))
        );

        barra.add(btnVolver, BorderLayout.WEST);
        return barra;
    }

    // ─────────────────────────────────────────────────────────────────
    // Lógica
    // ─────────────────────────────────────────────────────────────────

    /** Llamado cuando el usuario hace clic en un enfrentamiento. */
    private void onClickMatch(Match match) {
        if (match.tieneResultado()) {
            JOptionPane.showMessageDialog(this,
                    "Ganador: " + match.getGanadorMatch().getName(),
                    "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // TODO: abrir diálogo para registrar resultado
            JOptionPane.showMessageDialog(this,
                    match.getParticipanteUno().getName() + " vs " + match.getParticipanteDos().getName(),
                    "Partido pendiente", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Implementación de EstadoBracketsGestion (patrón Observer)
    // ─────────────────────────────────────────────────────────────────

    @Override
    public void onBracketGenerado(ArrayList<Match> partidos) {
        SwingUtilities.invokeLater(this::refrescarBracket);
    }

    @Override
    public void onMatchActualizado(Match partido) {
        SwingUtilities.invokeLater(this::refrescarBracket);
    }

    /** Reconstruye el panel de rondas con los datos actuales. */
    private void refrescarBracket() {
        construirRondas();
    }

    // ─────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────

    /** Devuelve el nombre de la fase según la cantidad de partidos en esa ronda. */
    private String getNombreFase(int cantidadPartidos) {
        return switch (cantidadPartidos) {
            case 1  -> "Final";
            case 2  -> "Semifinal";
            case 4  -> "Cuartos de Final";
            case 8  -> "Octavos de Final";
            case 16 -> "Dieciseisavos";
            default -> "Ronda de " + (cantidadPartidos * 2);
        };
    }

    /** Trunca el texto si no cabe en el ancho disponible del botón. */
    private String truncar(String texto, FontMetrics fm, int maxAncho) {
        if (fm.stringWidth(texto) <= maxAncho) return texto;
        String puntos = "...";
        while (texto.length() > 0 && fm.stringWidth(texto + puntos) > maxAncho) {
            texto = texto.substring(0, texto.length() - 1);
        }
        return texto + puntos;
    }
}
