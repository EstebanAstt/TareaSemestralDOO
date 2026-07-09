package UI.rol;

import UI.CrearTorneoPanel.CrearTorneoPanel;
import UI.Resources.AppWindow;

import javax.swing.*;

public class OrganizadorStrategy implements RolStrategy {

    private final AppWindow appWindow;
    public OrganizadorStrategy(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public String[] getOpciones() {
        return new String[] {
                "Ver Torneos",
                "Crear Torneo"
        };
    }

    @Override
    public void ejecutarOpcion(String opcion, JPanel panelActual) {
        switch (opcion) {
            case "Crear Torneo" -> {
                appWindow.mostrarPanel(new CrearTorneoPanel(appWindow));
                // new CrearTorneo().setVisible(true);
                System.out.println("[LOG] Abrir: Crear Torneo");
            }
            case "Ver Torneos" -> {
                // new VerTorneos(true).setVisible(true); // true = modo organizador (permite eliminar)
                System.out.println("[LOG] Abrir: Ver Torneos (modo Organizador)");
            }
            default -> System.err.println("Opción no reconocida: " + opcion);
        }
    }

    @Override
    public String getNombreRol() {
        return "Organizador";
    }

}
