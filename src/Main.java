import UI.MenuMainPanel;
import UI.Resources.AppWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppWindow().setVisible(true));
    }
}