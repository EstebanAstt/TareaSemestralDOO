package UI;

import modelo.*;
import modelo.enums.AccionPartido;
import UI.Resources.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static UI.Resources.BaseWindow.buildButton;

import static gestion.BracketUtils.buildTextoEvento;

/**
 * Diálogo modal para registrar el resultado de un partido acción por acción.
 *
 * Muestra ambos participantes, el marcador actualizado en tiempo real,
 * botones de acción simétricos por equipo, y un historial desplazable
 * de eventos. Soporta deshacer el último evento sin perder el historial.
 *
 * <p>Uso típico:
 * <pre>
 *   MatchResultado resultado = RegistrarResultadoDialogo.mostrar(
 *           componentePadre,
 *           match,
 *           torneo.getDisciplinaTorneo().getNombreDisciplina()  // ej. "Futbol"
 *   );
 *   if (resultado != null) {
 *       match.setResultadoMatch(resultado);
 *   }
 * </pre>
 *
 * <p>El nombre de disciplina se convierte a mayúsculas internamente para
 * coincidir con las claves de {@link AccionPartido}.
 */
public class RegistrarResultadoDialogo extends JDialog {

    // ── Modelo ────────────────────────────────────────────────────────────────
    private final Match  match;
    private final String disciplina;   // siempre en mayúsculas: "FUTBOL", "BASKET", …
    private MatchResultado resultado;  // se reconstruye al deshacer

    /**
     * Historial interno para soporte de deshacer.
     * MatchResultado no expone remoción de eventos, así que lo reconstruimos.
     */
    private final List<EventoConContexto> historial = new ArrayList<>();

    // ── Vistas dinámicas ──────────────────────────────────────────────────────
    private JLabel lblMarcadorUno;
    private JLabel lblMarcadorDos;
    private DefaultListModel<String> modelEventos;
    private JList<String>            listEventos;

    // ── Estado de salida ──────────────────────────────────────────────────────
    private boolean confirmado = false;
    private final boolean permitirEmpate;

    // ── Paleta (igual a la del resto del proyecto) ────────────────────────────
    private static final Color BTN_BG     = ColorPalette.COLOR_BTN.getColor();
    private static final Color BTN_HOVER  = ColorPalette.COLOR_BTN_HOVER.getColor();
    private static final Color BTN_FG     = ColorPalette.COLOR_TEXT_LIGHT.getColor();
    private static final Color BTN_DEL    = ColorPalette.COLOR_BTN_DEL.getColor();
    private static final Color BTN_DEL_H  = ColorPalette.COLOR_BTN_DEL_HOVER.getColor();
    private static final Color BTN_WIN    = ColorPalette.COLOR_BTN_MATCH_WINNER.getColor();
    private static final Color BTN_WIN_H  = new Color(30, 130, 60);
    private static final Color BTN_GREY   = new Color(150, 150, 150);
    private static final Color BTN_GREY_H = new Color(110, 110, 110);

    // ══════════════════════════════════════════════════════════════════════════
    // Clase interna: evento + contexto para soporte de deshacer
    // ══════════════════════════════════════════════════════════════════════════

    private static class EventoConContexto {
        final EventoPartido evento;
        final boolean       esParticipanteUno;
        final String        textoHistorial;

        EventoConContexto(EventoPartido evento, boolean esParticipanteUno, String textoHistorial) {
            this.evento            = evento;
            this.esParticipanteUno = esParticipanteUno;
            this.textoHistorial    = textoHistorial;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Constructor
    // ══════════════════════════════════════════════════════════════════════════

    public RegistrarResultadoDialogo(Frame parent, Match match, String disciplina, boolean permitirEmpate) {
        super(parent, "Partido en curso", true);
        this.match        = match;
        this.disciplina   = disciplina.toUpperCase();
        this.resultado    = new MatchResultado();
        this.modelEventos = new DefaultListModel<>();
        this.permitirEmpate = permitirEmpate;

        buildUI();
        setResizable(false);
        pack();
        setMinimumSize(new Dimension(540, 420));
        setLocationRelativeTo(parent);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Construcción de la UI
    // ══════════════════════════════════════════════════════════════════════════

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        setContentPane(root);

        root.add(buildMarcadorPanel(),  BorderLayout.NORTH);
        root.add(buildAccionesPanel(),  BorderLayout.CENTER);
        root.add(buildBottomPanel(),    BorderLayout.SOUTH);
    }

    // ── Panel de marcador ─────────────────────────────────────────────────────

    private JPanel buildMarcadorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 20, 4, 20);

        Participante p1 = match.getParticipanteUno();
        Participante p2 = match.getParticipanteDos();

        // ─ Nombre equipo 1 ─
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.CENTER;
        JLabel lblNom1 = new JLabel(p1.getName(), SwingConstants.CENTER);
        lblNom1.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lblNom1, c);

        // ─ Marcador equipo 1 ─
        c.gridx = 0; c.gridy = 1;
        lblMarcadorUno = buildScoreLabel();
        panel.add(lblMarcadorUno, c);

        // ─ VS ─
        c.gridx = 1; c.gridy = 0; c.gridheight = 2;
        JLabel lblVs = new JLabel("VS", SwingConstants.CENTER);
        lblVs.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblVs.setForeground(new Color(130, 130, 130));
        panel.add(lblVs, c);

        // ─ Nombre equipo 2 ─
        c.gridheight = 1; c.gridx = 2; c.gridy = 0;
        JLabel lblNom2 = new JLabel(p2.getName(), SwingConstants.CENTER);
        lblNom2.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lblNom2, c);

        // ─ Marcador equipo 2 ─
        c.gridx = 2; c.gridy = 1;
        lblMarcadorDos = buildScoreLabel();
        panel.add(lblMarcadorDos, c);

        return panel;
    }

    /** Etiqueta de marcador grande con borde redondeado, igual a la imagen. */
    private JLabel buildScoreLabel() {
        JLabel lbl = new JLabel("0", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(30, 30, 30));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(new Font("SansSerif", Font.BOLD, 52));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setOpaque(false);
        lbl.setPreferredSize(new Dimension(110, 90));
        return lbl;
    }

    // ── Panel de acciones ─────────────────────────────────────────────────────

    /**
     * Construye una grilla de 3 columnas:
     * [botón equipo 1] [nombre acción] [botón equipo 2]
     * Una fila por cada AccionPartido disponible en la disciplina.
     */
    private JPanel buildAccionesPanel() {
        List<AccionPartido> acciones = AccionPartido.getAccionesPara(disciplina);

        JPanel grid = new JPanel(new GridLayout(acciones.size(), 3, 10, 10));
        grid.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Acciones"),
                BorderFactory.createEmptyBorder(6, 10, 8, 10)
        ));

        for (AccionPartido accion : acciones) {
            grid.add(buildBotonAccion(accion, match.getParticipanteUno(), true));
            grid.add(buildEtiquetaAccion(accion));
            grid.add(buildBotonAccion(accion, match.getParticipanteDos(), false));
        }
        return grid;
    }

    /** Etiqueta central con el nombre de la acción */
    private JLabel buildEtiquetaAccion(AccionPartido accion) {
        JLabel lbl = new JLabel(accion.getNombre(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                FontMetrics fm = g2.getFontMetrics(getFont());
                int textW = fm.stringWidth(getText());
                int x = (getWidth() - textW) / 2;
                int y = getHeight() - 4;
                g2.setColor(getForeground());
                g2.drawLine(x, y, x + textW, y);
                g2.dispose();
            }
        };
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(new Color(40, 40, 40));
        return lbl;
    }

    /** Botón de acción: rectángulo redondeado de color primario */
    private JButton buildBotonAccion(AccionPartido accion, Participante participante, boolean esUno) {
        JButton btn = buildButton("+", BTN_BG, BTN_HOVER, BTN_FG);
        btn.setPreferredSize(new Dimension(130, 38));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        // Tooltip para ayudar a identificar a qué equipo corresponde
        btn.setToolTipText(participante.getName() + " -> " + accion.getNombre());
        btn.addActionListener(e -> onAccion(accion, participante, esUno));
        return btn;
    }

    // ── Panel inferior: historial + botones ───────────────────────────────────

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        // Historial de eventos
        listEventos = new JList<>(modelEventos);
        listEventos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        listEventos.setFixedCellHeight(22);
        listEventos.setSelectionModel(new DefaultListSelectionModel() {
            @Override public boolean isSelectedIndex(int index) { return false; }
        });

        JScrollPane scroll = new JScrollPane(listEventos);
        scroll.setPreferredSize(new Dimension(0, 130));
        scroll.setBorder(BorderFactory.createTitledBorder("Historial de eventos"));
        panel.add(scroll, BorderLayout.CENTER);

        // Botones
        JButton btnDeshacer  = buildButton("Deshacer", BTN_DEL,  BTN_DEL_H, BTN_FG);
        JButton btnCancelar  = buildButton("Cancelar",   BTN_GREY, BTN_GREY_H, BTN_FG);
        JButton btnConfirmar = buildButton("Confirmar resultado", BTN_WIN, BTN_WIN_H, BTN_FG);

        btnDeshacer .setPreferredSize(new Dimension(140, 36));
        btnCancelar .setPreferredSize(new Dimension(120, 36));
        btnConfirmar.setPreferredSize(new Dimension(210, 36));

        btnDeshacer .addActionListener(e -> onDeshacer());
        btnCancelar .addActionListener(e -> dispose());
        btnConfirmar.addActionListener(e -> confirmar());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        btnRow.add(btnDeshacer);
        btnRow.add(Box.createHorizontalStrut(16));
        btnRow.add(btnCancelar);
        btnRow.add(btnConfirmar);

        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Lógica de negocio
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Invocado al pulsar un botón de acción.
     * Si el participante es un Equipo con jugadores inscriptos, pregunta quién ejecutó.
     */
    private void onAccion(AccionPartido accion, Participante participante, boolean esUno) {
        Participante autor = resolverAutor(accion, participante);
        if (autor == null) return; // el usuario canceló la selección de jugador

        EventoPartido evento  = new EventoPartido(accion, autor, participante);
        String        texto   = buildTextoEvento(accion, autor, participante, historial.size() + 1);

        historial.add(new EventoConContexto(evento, esUno, texto));
        resultado.registrarEvento(evento, esUno);

        actualizarMarcadores();
        modelEventos.addElement(texto);
        scrollToLast();
    }

    /**
     * Elimina el último evento del historial y reconstruye el MatchResultado.
     * Necesario porque MatchResultado no expone remoción de eventos.
     */
    private void onDeshacer() {
        if (historial.isEmpty()) return;

        historial.remove(historial.size() - 1);
        modelEventos.remove(modelEventos.size() - 1);

        // Reconstruir desde cero reproduciendo los eventos restantes
        resultado = new MatchResultado();
        historial.forEach(ec -> resultado.registrarEvento(ec.evento, ec.esParticipanteUno));

        actualizarMarcadores();
    }

    /**
     * Determina el autor del evento.
     *
     * <ul>
     *   <li>Participante individual (Jugador) → él mismo.</li>
     *   <li>Equipo sin jugadores cargados     → el propio Equipo.</li>
     *   <li>Equipo con jugadores              → muestra un selector de jugador.</li>
     * </ul>
     *
     * @return el Participante autor, o null si el usuario canceló el selector.
     */
    private Participante resolverAutor(AccionPartido accion, Participante participante) {
        if (!(participante instanceof Equipo)) return participante;

        Equipo equipo = (Equipo) participante;
        if (equipo.getEquipoSize() == 0) return equipo;

        List<Participante> jugadores = equipo.getJugadores();
        String[] nombres = jugadores.stream()
                .map(Participante::getName)
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(
                this,
                "¿Quién realizó la acción?",
                accion.getNombre() + "  —  " + equipo.getName(),
                JOptionPane.PLAIN_MESSAGE,
                null, nombres, nombres[0]
        );
        if (sel == null) return null;

        return jugadores.stream()
                .filter(j -> j.getName().equals(sel))
                .findFirst()
                .orElse(equipo);
    }

    /**
     * Sirve para actualizar los marcadores en vivo
     */
    private void actualizarMarcadores() {
        lblMarcadorUno.setText(String.valueOf(resultado.getMarcadorUno()));
        lblMarcadorDos.setText(String.valueOf(resultado.getMarcadorDos()));
    }

    /** Desplaza automáticamente el historial hasta el último evento. */
    private void scrollToLast() {
        if (!modelEventos.isEmpty()) {
            SwingUtilities.invokeLater(() ->
                    listEventos.ensureIndexIsVisible(modelEventos.size() - 1)
            );
        }
    }
    /**
     * @return el {@link MatchResultado} acumulado si el usuario confirmó;
     *         {@code null} si canceló o cerró el diálogo.
     */
    public MatchResultado getResultado() {
        return confirmado ? resultado : null;
    }

    private void confirmar() {
        if (!permitirEmpate && resultado.esEmpate()) {
            JOptionPane.showMessageDialog(this,
                    "Este formato no permite empates. Registra una acción más para definir un ganador.",
                    "Empate no permitido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        confirmado = true;
        dispose();
    }

    /**
     * Método de conveniencia estático.
     * Crea y muestra el diálogo de forma modal, bloqueando hasta que el usuario
     * confirme o cancele.
     *
     * @param parent     componente padre (para centrar y obtener el Frame)
     * @param match      partido cuyo resultado se va a registrar
     * @param disciplina nombre de la disciplina tal como lo devuelve
     *                   {@link modelo.disciplina.Disciplina#getNombreDisciplina()}
     *                   (p. ej. {@code "Futbol"}, {@code "Basket"})
     * @return el {@link MatchResultado} si se confirmó; {@code null} si se canceló
     */
    public static MatchResultado mostrar(Component parent, Match match, String disciplina, boolean permitirEmpate) {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(parent);
        RegistrarResultadoDialogo dlg = new RegistrarResultadoDialogo(frame, match, disciplina, permitirEmpate);
        dlg.setVisible(true);
        return dlg.getResultado();
    }
}