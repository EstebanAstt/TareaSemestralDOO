package UI.BracketPanel;

import UI.MenuMainPanel;
import UI.RegistrarResultadoDialogo;
import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.BracketUtils;
import gestion.BracketsGestion;
import gestion.TorneoGestion;
import modelo.Match;
import modelo.MatchResultado;
import modelo.Participante;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BracketVer extends BaseWindow implements gestion.EstadoBracketsGestion {

    private final TorneoGestion torneoGestion;
    private final BracketsGestion bracketsGestion;
    private JPanel panelRondas;

    public BracketVer(AppWindow appWindow, TorneoGestion torneoGestion) {
        super(appWindow);
        this.torneoGestion = torneoGestion;
        this.bracketsGestion = torneoGestion.getBracketsGestion();
        loadBackgroundImage("MenuBracketVer.png");
        initUI();

        bracketsGestion.agregarObservadorBracket(this);
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        JPanel fondo = createBackgroundPanel();
        fondo.setLayout(new BorderLayout());

        fondo.add(buildCabecera(), BorderLayout.NORTH);

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

        fondo.add(buildBarraInferior(), BorderLayout.SOUTH);

        add(fondo, BorderLayout.CENTER);
    }

    private JPanel buildCabecera() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 40, 10, 40));

        String nombreTorneo = torneoGestion.getTorneo().getNombreTorneo();
        String nombreFormato = torneoGestion.getTorneo().getFormatoTorneo().getNombreFormato();

        panel.add(buildTitulo(nombreTorneo));
        panel.add(Box.createVerticalStrut(4));

        JLabel lblFormato = buildEtiqueta("Tipo: " + nombreFormato);
        lblFormato.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblFormato);

        return panel;
    }

    private void construirRondas() {
        panelRondas.removeAll();

        Map<Integer, List<Match>> rondasVisibles = bracketsGestion.obtenerRondasVisibles();

        panelRondas.setLayout(new GridLayout(1, rondasVisibles.size(), 30, 0));
        panelRondas.setBorder(new EmptyBorder(10, 40, 10, 40));

        for (Map.Entry<Integer, List<Match>> entry : rondasVisibles.entrySet()) {
            String nombreFase = BracketUtils.getNombreFase(entry.getValue().size());

            // Instanciamos el panel extraído pasándole los datos
            panelRondas.add(new ColumnaRondaPanel(nombreFase, entry.getValue(), this, this::onClickMatch));
        }

        panelRondas.revalidate();
        panelRondas.repaint();
    }
    /**
     * Agrupa y filtra los partidos del gestor que tienen al menos un participante.
     * Esta es lógica de negocio temporalmente ubicada en la vista.
     */
    private Map<Integer, List<Match>> agruparRondasVisibles() {
        ArrayList<Match> partidos = bracketsGestion.getPartidosBracketsGestion();
        Map<Integer, List<Match>> porRonda = new LinkedHashMap<>();

        for (Match m : partidos) {
            porRonda.computeIfAbsent(m.getRonda(), k -> new ArrayList<>()).add(m);
        }

        Map<Integer, List<Match>> rondasVisibles = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Match>> entry : porRonda.entrySet()) {
            boolean tieneParticipantes = entry.getValue().stream()
                    .anyMatch(m -> m.getParticipanteUno() != null || m.getParticipanteDos() != null);
            if (tieneParticipantes) {
                rondasVisibles.put(entry.getKey(), entry.getValue());
            }
        }
        return rondasVisibles;
    }

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
        btnVolver.addActionListener(e -> appWindow.mostrarPanel(new MenuMainPanel(appWindow)));

        barra.add(btnVolver, BorderLayout.WEST);
        return barra;
    }

    // Lógica del controlador (Diálogos de resultados)
    private void onClickMatch(Match match) {
        if (!validarPartidoDisponible(match)) return;

        String disciplina = torneoGestion.getTorneo().getDisciplinaTorneo().getNombreDisciplina();

        if (match.isIdaVuelta()) {
            manejarFormatoIdaVuelta(match, disciplina);
        } else {
            manejarFormatoUnico(match, disciplina);
        }
    }

    private boolean validarPartidoDisponible(Match match) {
        if (match.getParticipanteUno() == null || match.getParticipanteDos() == null) {
            JOptionPane.showMessageDialog(this,
                    "Este partido está esperando al ganador de una ronda anterior.",
                    "Partido no disponible", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
    /**
     *
     * @param partidos Aca se implementa EstadoBracketsGestion (Patron Observer)
     */
    @Override
    public void onBracketGenerado(ArrayList<Match> partidos) {
        SwingUtilities.invokeLater(this::refrescarBracket);
    }

    /**
     * actualiza el estado de un partido
     * @param partido
     */
    @Override
    public void onMatchActualizado(Match partido) {
        SwingUtilities.invokeLater(this::refrescarBracket);
    }
    /** Reconstruye el panel de rondas con los datos actuales. */
    private void refrescarBracket() {
        construirRondas();
    }

    private void manejarFormatoUnico(Match match, String disciplina) {
        if (match.tieneResultado()) {
            Participante ganador = match.getGanadorMatch();
            String msg = ganador != null ? "Ganador: " + ganador.getName() : "El partido terminó en empate.";
            JOptionPane.showMessageDialog(this, msg, "Resultado registrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        MatchResultado resultado = RegistrarResultadoDialogo.mostrar(this, match, disciplina, false);
        if (resultado != null) {
            match.setResultadoMatch(resultado);
            bracketsGestion.registrarResultado(match);
        }
    }

    private void manejarFormatoIdaVuelta(Match match, String disciplina) {
        if (!match.tieneIda()) {
            MatchResultado r = RegistrarResultadoDialogo.mostrar(this, match, disciplina, true);
            if (r != null) {
                match.setResultadoIda(r);
                bracketsGestion.registrarResultado(match);
            }
        } else if (!match.tieneVuelta()) {
            MatchResultado r = RegistrarResultadoDialogo.mostrar(this, match, disciplina, true);
            if (r != null) {
                match.setResultadoVuelta(r);

                // Si el global sigue en empate, forzamos penales
                if (match.getGanadorGlobal() == null) {
                    gestionarPenales(match, disciplina);
                } else {
                    bracketsGestion.registrarResultado(match);
                }
            }
        } else {
            mostrarResultadoFinal(match);
        }
    }

    private void gestionarPenales(Match match, String disciplina) {
        JOptionPane.showMessageDialog(this,
                "¡Empate global! Se jugará tanda de penales.",
                "Empate", JOptionPane.INFORMATION_MESSAGE);

        MatchResultado penales = RegistrarResultadoDialogo.mostrar(this, match, disciplina, false);
        if (penales != null) {
            match.setResultadoVuelta(penales);
            bracketsGestion.registrarResultado(match);
        }
    }

    private void mostrarResultadoFinal(Match match) {
        JOptionPane.showMessageDialog(this,
                "Global: " + match.getMarcadorGlobalUno() + " - " + match.getMarcadorGlobalDos() +
                        "\nGanador: " + match.getGanadorGlobal().getName(),
                "Resultado final", JOptionPane.INFORMATION_MESSAGE);
    }
}
