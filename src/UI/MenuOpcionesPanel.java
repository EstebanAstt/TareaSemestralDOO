package UI;

import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import UI.rol.RolStrategy;

import javax.swing.*;

/**
 * Es el Menu donde se indica que acciones puede hacer el espectador u organizador,
 * tienen interfaces similares
 */
public class MenuOpcionesPanel extends BaseWindow {
    private final RolStrategy strategy;

    private static final int BTN_WIDTH = 280;
    private static final int BTN_HEIGHT = 50;
    private static final int BTN_X = 50;
    private static final int BTN_Y = 420;
    private static final int BTN_MARGIN_Y = 80;


    private static final int BTN_BACK_WIDTH = 120;
    private static final int BTN_BACK_HEIGHT = 40;
    private static final int BTN_BACK_X = 40;
    private static final int BTN_BACK_Y = 650-90;


    public MenuOpcionesPanel(AppWindow appWindow, RolStrategy strategy) {
        super(appWindow);
        this.strategy = strategy;
        loadBackgroundImage("MenuOpciones" + strategy.getNombreRol() + ".png");
        initUI();
    }

    @Override
    protected void initUI() {
        // Panel principal con imagen de fondo
        JPanel mainPanel = createBackgroundPanel();
        mainPanel.setLayout(null);

        String[] opciones = strategy.getOpciones();


        for (int i = 0; i < opciones.length; i++) {
            String opcion = opciones[i];
            JButton btn = buildButton(
                    opcion,
                    ColorPalette.COLOR_BTN.getColor(),
                    ColorPalette.COLOR_BTN_HOVER.getColor(),
                    ColorPalette.COLOR_TEXT_DARK.getColor()
            );
            btn.setBounds(BTN_X, BTN_Y + BTN_MARGIN_Y*i, BTN_WIDTH, BTN_HEIGHT);
            btn.addActionListener(e -> strategy.ejecutarOpcion(opcion, this));
            mainPanel.add(btn);
        }

        // Botón Volver (esquina inferior izquierda)
        JButton btnVolver = buildButton(
                "Volver",
                ColorPalette.COLOR_BTN_BACK.getColor(),
                ColorPalette.COLOR_BTN_BACK_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnVolver.setBounds(BTN_BACK_X, BTN_BACK_Y, BTN_BACK_WIDTH, BTN_BACK_HEIGHT);
        btnVolver.addActionListener(e -> appWindow.mostrarPanel(new MenuMainPanel(appWindow)));
        mainPanel.add(btnVolver);

        setLayout(new java.awt.BorderLayout());
        add(mainPanel, java.awt.BorderLayout.CENTER);
    }
}
