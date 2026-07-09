package UI.Resources;

import UI.MenuMainPanel;
import gestion.TorneoGestion;

import javax.swing.*;

/**
 * App window es el Frame principal que contiene a todos los JPanel,
 * sirve para no estar haciendo constantemente nuevos Frame y
 * abriendo/cerrando la aplicacion
 */
public class AppWindow extends JFrame {

    private TorneoGestion torneoActivo;

    /**
     * El constructor setea lo que va a llevar todas las clases, cosas como
     * el titulo de la app, su tamaño y que no se puede cambiar su tamaño
     */
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

    /**
     *
     * @param gestion sirve para indicar que se inicio un torneo en la aplicacion
     */
    public void setTorneoActivo(TorneoGestion gestion) {
        this.torneoActivo = gestion;
    }

    public TorneoGestion getTorneoActivo() {
        return torneoActivo;
    }
}
