package UI.Resources;

import UI.MenuMainPanel;

import javax.swing.*;

public class AppWindow extends JFrame {

    public AppWindow() {
        super("Planificador de Torneos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        mostrarPanel(new MenuMainPanel(this)); // vista inicial
    }

    /**
     * Reemplaza el contenido actual de la ventana por un nuevo panel.
     * Esto es lo que reemplaza tus dispose() + new JFrame().
     */
    public void mostrarPanel(JPanel nuevoPanel) {
        setContentPane(nuevoPanel);
        revalidate(); // recalcula el layout
        repaint();    // repinta
    }
}
