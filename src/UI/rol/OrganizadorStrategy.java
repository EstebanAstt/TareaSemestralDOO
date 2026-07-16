package UI.rol;

import UI.CrearTorneoPanel.CrearTorneoPanel;
import UI.Resources.AppWindow;
import gestion.TorneoGestion;

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
                "Crear Torneo",
                "Finalizar Torneo"
        };
    }

    @Override
    public void ejecutarOpcion(String opcion, JPanel panelActual) {
        switch (opcion) {
            case "Crear Torneo" -> {
                appWindow.mostrarPanel(new CrearTorneoPanel(appWindow));
                System.out.println("[LOG] Abrir: Crear Torneo");
            }
            case "Ver Torneos" -> {
                gestion.TorneoGestion torneoActivo = appWindow.getTorneoActivo();

                if (torneoActivo == null) {
                    JOptionPane.showMessageDialog(
                            panelActual,
                            "Aún no hay ningún torneo activo en este momento para visualizar.",
                            "Operación Denegada",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    if (torneoActivo.getTorneo().getFormatoTorneo() instanceof modelo.formato.Liga) {
                        appWindow.mostrarPanel(new UI.PosicionesPanel.PosicionesVer(appWindow, torneoActivo));
                    } else {
                        appWindow.mostrarPanel(new UI.BracketPanel.BracketVer(appWindow, torneoActivo));
                    }
                }
            }
            case "Finalizar Torneo" -> {
                // 1. Validación de seguridad: ¿Existe un torneo en la memoria?
                if (appWindow.getTorneoActivo() == null) {
                    JOptionPane.showMessageDialog(
                            panelActual,
                            "No hay ningún torneo activo en este momento para finalizar.",
                            "Operación Denegada",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return; // Cortamos la ejecución
                }

                // 2. Lógica visual de confirmación
                int confirmacion = JOptionPane.showConfirmDialog(
                        panelActual,
                        "¿Estás seguro que deseas finalizar el torneo en curso?\nEsta acción es irreversible.",
                        "Confirmar Finalización",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        // 3. PUENTE A LA LÓGICA NUMÉRICA Y DE ESTADOS
                        appWindow.getTorneoActivo().finalizarTorneo();

                        JOptionPane.showMessageDialog(
                                panelActual,
                                "¡El torneo ha finalizado con éxito!\nEl estado cambió a FINALIZADO.",
                                "Operación Exitosa",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        System.out.println("[LOG] Torneo Finalizado correctamente");

                        // Desconectamos el torneo de la ventana principal para reiniciar el ciclo
                        appWindow.setTorneoActivo(null);

                    } catch (IllegalStateException ex) {
                        // Atrapa la excepción si tu validador encuentra partidos pendientes
                        JOptionPane.showMessageDialog(
                                panelActual,
                                ex.getMessage(),
                                "Operación Denegada",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
            default -> System.err.println("Opción no reconocida: " + opcion);
        }
    }

    @Override
    public String getNombreRol() {
        return "Organizador";
    }

}