package UI.PosicionesPanel;

import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.JornadaGestion;
import modelo.Match;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class JornadasListadoPanel extends JPanel {

    public JornadasListadoPanel(BaseWindow parent, List<JornadaGestion> jornadas,
                                String discNombre, boolean permiteEmpate, Runnable onResultadoRegistrado) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Título de la sección
        JLabel lbl = new JLabel("Jornadas");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lbl);
        add(Box.createVerticalStrut(8));

        if (jornadas == null || jornadas.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay jornadas generadas todavía.");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(lblVacio);
            return;
        }

        // Crear cada contenedor de Jornada
        for (JornadaGestion jornada : jornadas) {
            JPanel panelJornada = crearPanelContenedorJornada(jornada.getNumero());

            // Meter los partidos individuales dentro de la jornada
            for (Match match : jornada.getPartidos()) {
                panelJornada.add(new FilaPartidoPanel(parent, match, discNombre, permiteEmpate, onResultadoRegistrado));
                panelJornada.add(Box.createVerticalStrut(4));
            }

            add(panelJornada);
            add(Box.createVerticalStrut(10)); // Espacio entre jornadas
        }
    }

    private JPanel crearPanelContenedorJornada(int numeroJornada) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(ColorPalette.COLOR_BG_LIGHT.getColor());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.COLOR_LINEBORDER_LIGHT.getColor()),
                new EmptyBorder(10, 16, 10, 16)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lblNum = new JLabel("Jornada " + numeroJornada);
        lblNum.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNum.setForeground(ColorPalette.COLOR_BTN.getColor());
        lblNum.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNum);
        panel.add(Box.createVerticalStrut(6));

        return panel;
    }
}
