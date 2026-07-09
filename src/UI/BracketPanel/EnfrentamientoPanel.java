package UI.BracketPanel;

import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import modelo.Match;
import modelo.Participante;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Es el par de botones que indican un enfrentamiento directo
 * y cuando se hace click se abre un dialogo para poder determinar
 * el resultado de ese enfrentamiento
 */
public class EnfrentamientoPanel extends JPanel {

    public EnfrentamientoPanel(Match match, BaseWindow windowContext, Consumer<Match> onMatchClick) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        Participante p1 = match.getParticipanteUno();
        Participante p2 = match.getParticipanteDos();
        Participante ganador = match.getGanadorMatch();

        // ── Determinar colores según estado ───────────────────────────────
        Color colorP1, colorP2;

        if (match.isIdaVuelta() && match.tieneIda() && !match.tieneVuelta()) {
            int g1 = match.getMarcadorGlobalUno();
            int g2 = match.getMarcadorGlobalDos();
            colorP1 = (g1 >= g2) ? ColorPalette.COLOR_BTN_PENDING_MATCH.getColor() : ColorPalette.COLOR_BTN.getColor();
            colorP2 = (g2 >= g1) ? ColorPalette.COLOR_BTN_PENDING_MATCH.getColor() : ColorPalette.COLOR_BTN.getColor();
        } else if (ganador != null) {
            colorP1 = p1 != null && p1.equals(ganador) ? ColorPalette.COLOR_BTN_MATCH_WINNER.getColor() : ColorPalette.COLOR_BTN.getColor();
            colorP2 = p2 != null && p2.equals(ganador) ? ColorPalette.COLOR_BTN_MATCH_WINNER.getColor() : ColorPalette.COLOR_BTN.getColor();
        } else {
            colorP1 = ColorPalette.COLOR_BTN.getColor();
            colorP2 = ColorPalette.COLOR_BTN.getColor();
        }

        // ── Texto de los botones ──────────────────────────────────────────
        String textoP1 = p1 != null ? p1.getName() : "BYE";
        String textoP2 = p2 != null ? p2.getName() : "BYE";

        if (match.isIdaVuelta() && match.tieneIda()) {
            textoP1 += "  " + match.getMarcadorGlobalUno();
            textoP2 += "  " + match.getMarcadorGlobalDos();
        }

        // Usamos el windowContext (BaseWindow) para heredar el estilo de los botones
        JButton btnP1 = windowContext.buildButtonTruncado(textoP1, colorP1, colorP1.darker());
        JButton btnP2 = windowContext.buildButtonTruncado(textoP2, colorP2, colorP2.darker());

        btnP1.addActionListener(e -> onMatchClick.accept(match));
        btnP2.addActionListener(e -> onMatchClick.accept(match));

        add(btnP1);
        add(Box.createVerticalStrut(2));
        add(btnP2);
    }
}
