import UI.MenuMain;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Usar el look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usa el look por defecto
        }

        SwingUtilities.invokeLater(() -> {
            MenuMain window = new MenuMain();
            window.setVisible(true);
        });
    }
}