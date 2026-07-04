package UI;


import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import UI.rol.EspectadorStrategy;
import UI.rol.OrganizadorStrategy;
import UI.rol.RolStrategy;
import UI.rol.RolUsuario;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de inicio del sistema de torneos.
 *
 * Muestra la imagen de fondo y dos botones para ingresar
 * como Organizador o como Espectador.
 *
 * Patrón Strategy: al seleccionar un rol, se instancia
 * la estrategia de acceso correspondiente (OrganizerStrategy
 * o SpectatorStrategy) que determina qué pantallas y
 * operaciones estarán disponibles en el resto de la sesión.
 */
public class MenuMainPanel extends BaseWindow {

    //tamano y posicion de los botones
    private static final int BTN_WIDTH = 280;
    private static final int BTN_HEIGHT = 50;
    private static final int BTN_X = 50;
    private static final int BTN_Y = 420;
    private static final int BTN_MARGIN_Y = 80;


    //rol que elige el usuario antes de ingresar de lleno a la aplicacion, en un principio es null
    private RolUsuario selectedRole = null;

    public MenuMainPanel(AppWindow appWindow) {
        super(appWindow);
        //super("Planificador de Torneos");
        loadBackgroundImage("MenuSeleccion.png");
        initUI();
    }

    // ── Construcción de la UI ──────────────────────────────────────────
    @Override
    protected void initUI() {

        // Panel principal con imagen de fondo personalizada
        JPanel mainPanel = createBackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        // Panel izquierdo: botones
        JPanel leftPanel = buildLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        //setContentPane(mainPanel);
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel buildLeftPanel() {
        // Panel con posicionamiento absoluto
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(400, 0));

        JButton btnOrganizador = buildButton("Organizador", ColorPalette.COLOR_BTN.getColor(), ColorPalette.COLOR_BTN_HOVER.getColor(), ColorPalette.COLOR_TEXT_DARK.getColor() );
        JButton btnEspectador = buildButton("Espectador", ColorPalette.COLOR_BTN.getColor(), ColorPalette.COLOR_BTN_HOVER.getColor(), ColorPalette.COLOR_TEXT_DARK.getColor() );


        btnOrganizador.setBounds(BTN_X, BTN_Y, BTN_WIDTH, BTN_HEIGHT);
        btnEspectador.setBounds(BTN_X, BTN_Y+BTN_MARGIN_Y, BTN_WIDTH, BTN_HEIGHT);

        btnOrganizador.addActionListener(e -> handleRoleSelection(RolUsuario.ORGANIZADOR));
        btnEspectador.addActionListener(e -> handleRoleSelection(RolUsuario.ESPECTADOR));

        panel.add(btnOrganizador);
        panel.add(btnEspectador);

        return panel;
    }
    // ── Lógica de selección de rol ─────────────────────────────────────
    /**
     * Maneja la selección de rol del usuario.
     * Aquí es donde se aplicará el patrón Strategy:
     * cada rol instanciará su propia estrategia de acceso.
     */
    private void handleRoleSelection(RolUsuario role) {
        RolStrategy strategy = (role == RolUsuario.ORGANIZADOR)
                ? new OrganizadorStrategy(appWindow)
                : new EspectadorStrategy();
        //aca se abre el Menu opciones segun si eres organizador o espectador
        appWindow.mostrarPanel(new MenuOpcionesPanel(appWindow, strategy));
    }

}
