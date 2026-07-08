package UI;

import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.BracketUtils;
import gestion.BracketsGestion;
import gestion.TorneoGestion;
import modelo.Match;
import modelo.MatchResultado;
import modelo.Participante;
import modelo.formato.PartidoUnico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static gestion.BracketUtils.getNombreFase;
import static gestion.BracketUtils.truncar;

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

        // Agrupar por ronda manteniendo orden
        Map<Integer, List<Match>> porRonda = new LinkedHashMap<>();
        for (Match m : partidos) {
            porRonda.computeIfAbsent(m.getRonda(), k -> new ArrayList<>()).add(m);
        }

        // ── FILTRO: solo mostrar rondas donde al menos un match tiene participantes ──
        Map<Integer, List<Match>> rondasVisibles = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Match>> entry : porRonda.entrySet()) {
            boolean tieneParticipantes = entry.getValue().stream()
                    .anyMatch(m -> m.getParticipanteUno() != null || m.getParticipanteDos() != null);
            if (tieneParticipantes) {
                rondasVisibles.put(entry.getKey(), entry.getValue());
            }
        }

        int totalRondas = rondasVisibles.size();
        panelRondas.setLayout(new GridLayout(1, totalRondas, 30, 0));
        panelRondas.setBorder(new EmptyBorder(10, 40, 10, 40));

        int numRonda = 1;
        for (Map.Entry<Integer, List<Match>> entry : rondasVisibles.entrySet()) {
            String nombreFase = BracketUtils.getNombreFase(entry.getValue().size());
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

        JButton btnP1 = buildButtonTruncado(
                p1 != null ? p1.getName() : "BYE",
                p1 != null && p1.equals(ganador) ? ColorPalette.COLOR_BTN_MATCH_WINNER.getColor() : ColorPalette.COLOR_BTN_MATCH.getColor(),
                p1 != null && p1.equals(ganador) ? ColorPalette.COLOR_BTN_MATCH_WINNER.getColor().darker() : ColorPalette.COLOR_BTN_MATCH_HOVER.getColor()
        );

        JButton btnP2 = buildButtonTruncado(
                p2 != null ? p2.getName() : "BYE",
                p2 != null && p2.equals(ganador) ? ColorPalette.COLOR_BTN_MATCH_WINNER.getColor() : ColorPalette.COLOR_BTN_MATCH.getColor(),
                p2 != null && p2.equals(ganador) ? ColorPalette.COLOR_BTN_MATCH_WINNER.getColor().darker() : ColorPalette.COLOR_BTN_MATCH_HOVER.getColor()
        );

        // Al hacer clic en el par → futuro: registrar resultado
        btnP1.addActionListener(e -> onClickMatch(match));
        btnP2.addActionListener(e -> onClickMatch(match));

        par.add(btnP1);
        par.add(Box.createVerticalStrut(2)); // separador mínimo entre los dos botones
        par.add(btnP2);

        return par;
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

    private void onClickMatch(Match match) {
        if (match.getParticipanteUno() == null || match.getParticipanteDos() == null) {
            JOptionPane.showMessageDialog(this,
                    "Este partido está esperando al ganador de una ronda anterior.",
                    "Partido no disponible", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (match.tieneResultado()) {
            JOptionPane.showMessageDialog(this,
                    "Ganador: " + match.getGanadorMatch().getName(),
                    "Resultado registrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean permitirEmpate = !(torneoGestion.getTorneo()
                .getFormatoTorneo() instanceof PartidoUnico);

        // Abrir el diálogo de registro
        String disciplina = torneoGestion.getTorneo().getDisciplinaTorneo().getNombreDisciplina();
        MatchResultado resultado = RegistrarResultadoDialogo.mostrar(this, match, disciplina, permitirEmpate);

        if (resultado != null) {
            match.setResultadoMatch(resultado);
            bracketsGestion.registrarResultado(match); // notifica Observer → refresca panel
        }
    }
    /**
     *
     * @param partidos Aca se implementa EstadoBracketsGestion (Patron Observer)
     */
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
}
