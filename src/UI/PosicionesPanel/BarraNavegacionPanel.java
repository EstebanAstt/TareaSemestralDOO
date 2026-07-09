package UI.PosicionesPanel;
import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import UI.MenuMainPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BarraNavegacionPanel extends JPanel {

    /**
     * Constructor de la barra de navegación inferior.
     *
     * @param appWindow El JFrame principal para poder cambiar de panel al volver.
     * @param styleProvider El BaseWindow padre para reutilizar el método 'buildButton'.
     * @param onActualizar El Callback que se ejecutará al presionar "Actualizar".
     */
    public BarraNavegacionPanel(AppWindow appWindow, BaseWindow styleProvider, Runnable onActualizar) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 40, 20, 40));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izquierda.setOpaque(false);

        // ── Botón Volver ─────────────────────────────────────────────
        JButton btnVolver = styleProvider.buildButton(" Volver",
                ColorPalette.COLOR_BTN_BACK.getColor(),
                ColorPalette.COLOR_BTN_BACK_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor());
        btnVolver.setPreferredSize(new Dimension(140, 42));
        btnVolver.addActionListener(e ->
                appWindow.mostrarPanel(new MenuMainPanel(appWindow)));

        // ── Botón Actualizar ─────────────────────────────────────────
        JButton btnActualizar = styleProvider.buildButton("Actualizar",
                ColorPalette.COLOR_BTN.getColor(),
                ColorPalette.COLOR_BTN_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor());
        btnActualizar.setPreferredSize(new Dimension(150, 42));
        btnActualizar.addActionListener(e -> onActualizar.run());

        // Agregar componentes
        izquierda.add(btnVolver);
        izquierda.add(btnActualizar);
        add(izquierda, BorderLayout.WEST);
    }
}
