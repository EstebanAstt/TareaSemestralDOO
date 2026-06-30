package UI.rol;

import javax.swing.*;

public interface RolStrategy {
    String[] getOpciones();           // qué botones mostrar
    void ejecutarOpcion(String opcion, JFrame ventanaActual); // qué hacer al presionar cada uno
    String getNombreRol();

}
