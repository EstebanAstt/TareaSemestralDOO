package UI.PosicionesPanel;


import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import UI.RegistrarResultadoDialogo; // Asumo que está en UI
import modelo.Match;
import modelo.MatchResultado;
import modelo.Participante;

import javax.swing.*;
import java.awt.*;

import static UI.PosicionesPanel.PosicionesVer.fmt;

/**
 * Panel que se encarga de dibujar una fila de partido
 * de la forma:
 * EQUIPO A |-------| EQUIPO B
 * y abrir RegistrarResultadoDialogo al presionar el boton central
 */
public class FilaPartidoPanel extends JPanel {

    public FilaPartidoPanel(BaseWindow parent, Match match, String discNombre, boolean permiteEmpate, Runnable onResultadoRegistrado) {
        setLayout(new GridBagLayout());
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 6, 2, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Participante p1 = match.getParticipanteUno();
        Participante p2 = match.getParticipanteDos();
        String nomP1 = p1 != null ? p1.getName() : "?";
        String nomP2 = p2 != null ? p2.getName() : "?";

        // ── Participante 1 ──────────────────────────────────────────
        JLabel lblP1 = new JLabel(nomP1, JLabel.RIGHT);
        lblP1.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblP1.setForeground(new Color(30, 30, 30));

        // ── Botón Central (vs / Marcador) ───────────────────────────
        JButton btnCentro;
        if (match.tieneResultado()) {
            MatchResultado res = match.getResultado();
            double scoreUno = getScoreUno(res);
            double scoreDos = getScoreDos(res);
            String marcador = fmt(scoreUno) + " — " + fmt(scoreDos);
            Participante ganador = match.getGanadorMatch();

            Color bgResultado = (ganador == null)
                    ? ColorPalette.COLOR_BTN_DEACTIVATED.getColor() // Empate
                    : new Color(50, 160, 80);                       // Ganador

            // Usamos el método buildButton del padre (BaseWindow)
            btnCentro = parent.buildButton(marcador, bgResultado, bgResultado.darker(),
                    ColorPalette.COLOR_TEXT_LIGHT.getColor());
                    btnCentro.setForeground(Color.WHITE);

            // Resaltar ganador 1
            if (ganador != null && ganador.equals(p1)) {
                lblP1.setFont(new Font("SansSerif", Font.BOLD, 13));
                lblP1.setForeground(new Color(30, 130, 60));
            }
        } else {
            // Partido pendiente
            btnCentro = parent.buildButton("vs",
                    ColorPalette.COLOR_BTN.getColor(),
                    ColorPalette.COLOR_BTN_HOVER.getColor(),
                    ColorPalette.COLOR_TEXT_LIGHT.getColor());

            btnCentro.addActionListener(e -> {
                if (match.tieneResultado()) return;

                // Abrimos el diálogo usando 'parent' como la ventana contenedora
                MatchResultado resultado = RegistrarResultadoDialogo.mostrar(
                        parent, match, discNombre, permiteEmpate);

                if (resultado != null) {
                    match.setResultadoMatch(resultado);
                    // se ejecuta el Callback para avisarle a PosicionesVer que se refresque
                    onResultadoRegistrado.run();
                }
            });
        }
        btnCentro.setPreferredSize(new Dimension(100, 30));

        // ── Participante 2 ──────────────────────────────────────────
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

        // ── Añadir componentes al GridBagLayout ──────────────────────
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.EAST;
        add(lblP1, gbc);
        gbc.gridx = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.CENTER;
        add(btnCentro, gbc);
        gbc.gridx = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        add(lblP2, gbc);
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
}
