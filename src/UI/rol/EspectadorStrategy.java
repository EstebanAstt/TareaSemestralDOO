package UI.rol;

import javax.swing.*;

public class EspectadorStrategy implements RolStrategy {
    @Override
    public String[] getOpciones() {
        return new String[] {
                "Ver Torneos"
        };
    }

    @Override
    public void ejecutarOpcion(String opcion, JFrame ventanaActual) {
        switch (opcion) {
            case "Ver Torneos" -> {
                // new VerTorneos(false).setVisible(true); // false = modo espectador (sin eliminar)
                System.out.println("[LOG] Abrir: Ver Torneos (modo Espectador)");
            }
            default -> System.err.println("Opción no reconocida: " + opcion);
        }
    }

    @Override
    public String getNombreRol() {
        return "Espectador";
    }
}
