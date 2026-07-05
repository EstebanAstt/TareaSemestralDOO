
package UI;

import modelo.Equipo;
import modelo.Jugador;
import modelo.Participante;

import javax.swing.*;
import java.awt.*;

/**
 * Clase utilitaria que centraliza los diálogos modales de CrearTorneoPanel.
 * Evita duplicar la lógica de formulario nombre+contacto en tres métodos distintos.
 *
 * Cada método estático construye y muestra el diálogo correspondiente,
 * devolviendo el objeto creado o null si el usuario canceló.
 */
public class Dialogo {

    // Constructor privado: no se instancia, solo se usan los métodos estáticos
    private Dialogo() {}

    // ─────────────────────────────────────────────────────────────────
    // Formulario base reutilizado por todos los diálogos
    // ─────────────────────────────────────────────────────────────────

    /**
     * Muestra un formulario modal con campos nombre y contacto.
     * @param parent    componente padre para centrar el diálogo
     * @param titulo    título de la ventana del diálogo
     * @param etiqueta  texto de la primera fila ("Nombre del equipo:", etc.)
     * @return String[]{nombre, contacto} si el usuario confirmó, null si canceló
     */
    private static String[] mostrarFormulario(Component parent, String titulo, String etiqueta) {
        JTextField campoNombre   = new JTextField(20);
        JTextField campoContacto = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel(etiqueta));               form.add(campoNombre);
        form.add(new JLabel("Contacto (opcional):")); form.add(campoContacto);

        int res = JOptionPane.showConfirmDialog(parent, form, titulo, JOptionPane.OK_CANCEL_OPTION);

        if (res != JOptionPane.OK_OPTION) return null;

        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "El nombre no puede estar vacío.");
            return null;
        }
        return new String[]{ nombre, campoContacto.getText().trim() };
    }

    // ─────────────────────────────────────────────────────────────────
    // Diálogos públicos
    // ─────────────────────────────────────────────────────────────────

    /**
     * Diálogo para crear un Equipo.
     * @return el Equipo creado, o null si el usuario canceló
     */
    public static Equipo mostrarDialogoEquipo(Component parent) {
        String[] datos = mostrarFormulario(parent, "Añadir Equipo", "Nombre del equipo:");
        if (datos == null) return null;
        return new Equipo(datos[0], datos[1]);
    }

    /**
     * Diálogo para crear un Jugador, ya sea independiente o dentro de un equipo.
     * Si equipoPadre es null, el jugador es independiente.
     * Si equipoPadre no es null, el jugador se añade directamente al equipo.
     * @return el Jugador creado, o null si el usuario canceló
     */
    public static Jugador mostrarDialogoJugador(Component parent, Equipo equipoPadre) {
        String titulo   = equipoPadre != null ? "Añadir Jugador a " + equipoPadre.getName() : "Añadir Jugador";
        String[] datos  = mostrarFormulario(parent, titulo, "Nombre del jugador:");
        if (datos == null) return null;

        Jugador jug = new Jugador(datos[0], datos[1]);
        if (equipoPadre != null) equipoPadre.addjugador(jug);
        return jug;
    }

    /**
     * Diálogo para gestionar los jugadores de un equipo ya existente.
     * Muestra la lista de jugadores con opciones de añadir y eliminar.
     * Actualiza el listModel de la pantalla padre al modificar el equipo.
     *
     * @param parent      componente padre
     * @param equipo      equipo a gestionar
     * @param idxEquipo   índice del equipo en el listModel para actualizar su etiqueta
     * @param listModel   modelo de la lista principal de participantes
     */
    public static void mostrarDialogoJugadoresEquipo(Component parent, Equipo equipo,
                                                     int idxEquipo, DefaultListModel<String> listModel) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "Jugadores de: " + equipo.getName(), true
        );
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));

        // ── Lista de jugadores ─────────────────────────────────────────
        DefaultListModel<String> jugadoresModel = new DefaultListModel<>();
        equipo.getJugadores().forEach(j -> jugadoresModel.addElement(j.getName()));

        JList<String> listaJugadores = new JList<>(jugadoresModel);
        listaJugadores.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listaJugadores.setFixedCellHeight(28);

        JScrollPane scroll = new JScrollPane(listaJugadores);
        scroll.setBorder(BorderFactory.createTitledBorder("Jugadores"));

        // ── Botones ────────────────────────────────────────────────────
        JButton btnAnadir   = new JButton("+ Añadir jugador");
        JButton btnEliminar = new JButton("X Eliminar seleccionado");
        JButton btnCerrar   = new JButton("Cerrar");

        btnEliminar.setForeground(Color.RED);

        btnAnadir.addActionListener(e -> {
            Jugador jug = mostrarDialogoJugador(dialog, equipo);
            if (jug != null) {
                jugadoresModel.addElement(jug.getName());
                actualizarEtiquetaEquipo(listModel, idxEquipo, equipo);
            }
        });

        btnEliminar.addActionListener(e -> {
            int idx = listaJugadores.getSelectedIndex();
            if (idx >= 0) {
                Participante jugador = equipo.getJugadores().get(idx);
                equipo.removePlayer(jugador);
                jugadoresModel.remove(idx);
                actualizarEtiquetaEquipo(listModel, idxEquipo, equipo);
            }
        });

        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botones.add(btnAnadir);
        botones.add(btnEliminar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(botones,   BorderLayout.CENTER);
        sur.add(btnCerrar, BorderLayout.SOUTH);
        sur.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(sur,    BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── Helper privado ─────────────────────────────────────────────────
    private static void actualizarEtiquetaEquipo(DefaultListModel<String> listModel,
                                                 int idx, Equipo equipo) {
        listModel.set(idx, equipo.getName() + " (" + equipo.getEquipoSize() + " jugadores)");
    }
}
