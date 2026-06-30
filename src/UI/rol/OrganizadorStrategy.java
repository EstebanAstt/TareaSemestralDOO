package UI.rol;

import javax.swing.*;

public class OrganizadorStrategy implements RolStrategy {
    @Override
    public String[] getOpciones() {
        return new String[] {
                "Ver Torneos",
                "Crear Torneos"
        };
    }

    @Override
    public void ejecutarOpcion(String opcion, JFrame ventanaActual) {
        switch (opcion) {
            case "Crear Torneo" -> {
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
