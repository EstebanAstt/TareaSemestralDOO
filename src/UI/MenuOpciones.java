package UI;

import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import UI.rol.RolStrategy;

import javax.swing.*;

public class MenuOpciones extends BaseWindow {
    private RolStrategy strategy;

    public MenuOpciones(RolStrategy strategy) {
        super("Menú Principal");
        this.strategy = strategy;
        loadBackgroundImage("MenuOpciones" + strategy.getNombreRol() + ".png");
        initUI();
    }

    @Override
    protected void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con imagen de fondo, posicionamiento absoluto
        JPanel mainPanel = createBackgroundPanel();
        mainPanel.setLayout(null);

        String[] opciones = strategy.getOpciones();

        // ── Botones centrales (uno por cada opción de la estrategia) ───
        int btnW = 280, btnH = 55, gap = 20;
        int totalH = (btnH * opciones.length) + (gap * (opciones.length - 1));
        int startY = (650 - totalH) / 2;
        int x = (1100 - btnW) / 2;

        for (int i = 0; i < opciones.length; i++) {
            String opcion = opciones[i];
            JButton btn = buildButton(
                    opcion,
                    ColorPalette.COLOR_BTN.getColor(),
                    ColorPalette.COLOR_BTN_HOVER.getColor(),
                    ColorPalette.COLOR_TEXT_DARK.getColor()
            );
            btn.setBounds(x, startY + i * (btnH + gap), btnW, btnH);
            btn.addActionListener(e -> strategy.ejecutarOpcion(opcion, this));
            mainPanel.add(btn);
        }

        // ── Botón Volver (esquina inferior izquierda) ──────────────────
        JButton btnVolver = buildButton(
                "← Volver",
                ColorPalette.COLOR_BTN_BACK.getColor(),
                ColorPalette.COLOR_BTN_BACK_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnVolver.setBounds(40, 650 - 90, 160, 45);
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuMain().setVisible(true);
        });
        mainPanel.add(btnVolver);

        setContentPane(mainPanel);
    }
}
