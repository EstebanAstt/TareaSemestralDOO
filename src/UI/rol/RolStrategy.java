package UI.rol;

import javax.swing.*;

public interface RolStrategy {
    String[] getOpciones();           // qué botones mostrar
    void ejecutarOpcion(String opcion, JPanel actualPanel); // qué hacer al presionar cada uno
    String getNombreRol();

}
