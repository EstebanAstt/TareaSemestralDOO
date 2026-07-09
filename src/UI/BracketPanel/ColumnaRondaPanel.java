package UI.BracketPanel;

import UI.Resources.BaseWindow;
import modelo.Match;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * es la columna que guarda todos los enfrentamientos de
 * una ronda, en otras palabras, guarda los enfrentamientos
 * de 8° de final, 4° de final, semifinal, etc.
 */
public class ColumnaRondaPanel extends JPanel {

    public ColumnaRondaPanel(String nombreFase, List<Match> partidos, BaseWindow windowContext, Consumer<Match> onMatchClick) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Etiqueta de la fase
        JLabel lblFase = windowContext.buildEtiqueta(nombreFase);
        lblFase.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFase.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFase.setForeground(new Color(60, 60, 60));

        add(lblFase);
        add(Box.createVerticalStrut(12));

        // Por cada partido en esta fase, agregamos un EnfrentamientoPanel
        for (Match match : partidos) {
            add(new EnfrentamientoPanel(match, windowContext, onMatchClick));
            add(Box.createVerticalStrut(20));
        }

        add(Box.createVerticalGlue());
    }
}
