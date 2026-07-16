package UI.rol;

import UI.PosicionesPanel.PosicionesVer;
import UI.Resources.AppWindow;
import javax.swing.*;

public class EspectadorStrategy implements RolStrategy {
    @Override
    public String[] getOpciones() {
        return new String[] {
                "Ver Torneos"
        };
    }

    @Override
    public void ejecutarOpcion(String opcion, JPanel actualPanel) {
        switch (opcion) {
            case "Ver Torneos" -> {
                AppWindow appWindow = (AppWindow) SwingUtilities.getWindowAncestor(actualPanel);
                gestion.TorneoGestion torneoActivo = appWindow.getTorneoActivo();

                if (torneoActivo == null) {
                    JOptionPane.showMessageDialog(
                            actualPanel,
                            "Aún no hay ningún torneo creado en el sistema para visualizar.",
                            "Sin Torneos",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    if (torneoActivo.getTorneo().getFormatoTorneo() instanceof modelo.formato.Liga) {
                        appWindow.mostrarPanel(new UI.PosicionesPanel.PosicionesVer(appWindow, torneoActivo));
                    } else {
                        appWindow.mostrarPanel(new UI.BracketPanel.BracketVer(appWindow, torneoActivo));
                    }
                }
            }
            default -> System.err.println("Opción no reconocida: " + opcion);
        }
    }

    @Override
    public String getNombreRol() {
        return "Espectador";
    }
}