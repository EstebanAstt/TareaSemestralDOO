package UI;

import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.TorneoGestion;
import modelo.Equipo;
import modelo.Jugador;
import modelo.Participante;
import modelo.Torneo;
import modelo.enums.DisciplinaEnum;
import modelo.enums.FormatoEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CrearTorneoPanel extends BaseWindow {

    // ── Estado interno ─────────────────────────────────────────────────
    private int disciplinaIdx  = -1;
    private int formatoIdx     = -1;
    private boolean usaEquipos = true;
    private final List<Participante> participantes = new ArrayList<>();

    // ── Componentes dinámicos ──────────────────────────────────────────
    private JPanel panelFormatos;
    private DefaultListModel<String> listModel;
    private JLabel labelTipoParticipante;

    // ── Campos de texto ────────────────────────────────────────────────
    private JTextField campoNombre;
    private JTextField campoFecha;

    public CrearTorneoPanel(AppWindow appWindow) {
        super(appWindow);
        loadBackgroundImage("MenuCrearTorneo.png");
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        JPanel fondo = createBackgroundPanel();
        fondo.setLayout(new BorderLayout());

        JPanel formulario = buildFormulario();
        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        fondo.add(scroll, BorderLayout.CENTER);
        fondo.add(buildBarraInferior(), BorderLayout.SOUTH);

        add(fondo, BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────────────────────────
    // Formulario principal
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 60, 20, 60));

        panel.add(buildTitulo("Crear Torneo"));
        panel.add(Box.createVerticalStrut(24));

        panel.add(buildEtiqueta("Nombre del torneo"));
        panel.add(Box.createVerticalStrut(6));
        campoNombre = buildCampoTexto("Ej: Liga de Verano 2025");
        panel.add(campoNombre);
        panel.add(Box.createVerticalStrut(20));

        panel.add(buildEtiqueta("Disciplina"));
        panel.add(Box.createVerticalStrut(6));
        JPanel grupoDisciplina = buildOptionGroup(DisciplinaEnum.getNombres(), idx -> {
            DisciplinaEnum disc = DisciplinaEnum.values()[idx];
            disciplinaIdx = idx;
            formatoIdx    = -1;
            usaEquipos    = disc.usaEquipos();
            actualizarFormatos(disc);
            actualizarTipoParticipante();
        });
        grupoDisciplina.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(grupoDisciplina);
        panel.add(Box.createVerticalStrut(20));

        panel.add(buildEtiqueta("Formato"));
        panel.add(Box.createVerticalStrut(6));
        panelFormatos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFormatos.setOpaque(false);
        panelFormatos.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel placeholder = new JLabel("Selecciona primero una disciplina");
        placeholder.setForeground(ColorPalette.COLOR_BG_LIGHT.getColor());
        placeholder.setFont(new Font("SansSerif", Font.ITALIC, 13));
        panelFormatos.add(placeholder);
        panel.add(panelFormatos);
        panel.add(Box.createVerticalStrut(20));

        panel.add(buildEtiqueta("Fecha de inicio (dd/mm/aaaa)"));
        panel.add(Box.createVerticalStrut(6));
        campoFecha = buildCampoTexto("Ej: 01/01/2000");
        panel.add(campoFecha);
        panel.add(Box.createVerticalStrut(24));

        labelTipoParticipante = buildEtiqueta("Participantes");
        panel.add(labelTipoParticipante);
        panel.add(Box.createVerticalStrut(8));
        panel.add(buildBotonesParticipantes());
        panel.add(Box.createVerticalStrut(10));
        panel.add(buildListaParticipantes());
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    // ─────────────────────────────────────────────────────────────────
    // Sección de participantes
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildBotonesParticipantes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnAnadir = buildButton(
                "+ Añadir",
                ColorPalette.COLOR_BTN.getColor(),
                ColorPalette.COLOR_BTN_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnAnadir.setPreferredSize(new Dimension(130, 38));
        btnAnadir.addActionListener(e -> mostrarDialogoAnadir());

        JButton btnCargar = buildButton(
                "Cargar .txt",
                ColorPalette.COLOR_BTN_DEACTIVATED.getColor(),
                ColorPalette.COLOR_BTN_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_DARK.getColor()
        );
        btnCargar.setPreferredSize(new Dimension(150, 38));
        btnCargar.addActionListener(e -> cargarDesdeArchivo());

        panel.add(btnAnadir);
        panel.add(btnCargar);
        return panel;
    }

    private JPanel buildListaParticipantes() {
        JPanel panelParticipantes = new JPanel(new BorderLayout(0, 6));
        panelParticipantes.setOpaque(false);
        panelParticipantes.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelParticipantes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        listModel = new DefaultListModel<>();
        JList<String> lista = new JList<>(listModel);
        lista.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lista.setBackground(ColorPalette.COLOR_BG_LIGHT.getColor());
        lista.setFixedCellHeight(28);

        lista.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = lista.getSelectedIndex();
                    if (idx >= 0 && participantes.get(idx) instanceof Equipo equipo) {
                        mostrarDialogoJugadoresEquipo(equipo, idx);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(new Dimension(500, 150));
        scroll.setBorder(BorderFactory.createLineBorder(ColorPalette.COLOR_BG_LIGHT.getColor()));

        JButton btnEliminar = buildButton(
                "X Eliminar seleccionado",
                ColorPalette.COLOR_BTN_DEL.getColor(),
                ColorPalette.COLOR_BTN_DEL_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnEliminar.setPreferredSize(new Dimension(220, 34));
        btnEliminar.setMaximumSize(new Dimension(220, 34));
        btnEliminar.addActionListener(e -> {
            int idx = lista.getSelectedIndex();
            if (idx >= 0) {
                participantes.remove(idx);
                listModel.remove(idx);
            }
        });

        panelParticipantes.add(scroll, BorderLayout.CENTER);
        panelParticipantes.add(btnEliminar, BorderLayout.SOUTH);
        return panelParticipantes;
    }

    // ─────────────────────────────────────────────────────────────────
    // Barra inferior
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildBarraInferior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(10, 60, 20, 60));

        JButton btnVolver = buildButton(
                "Volver",
                ColorPalette.COLOR_BTN_BACK.getColor(),
                ColorPalette.COLOR_BTN_BACK_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnVolver.setPreferredSize(new Dimension(160, 45));
        btnVolver.addActionListener(e -> appWindow.mostrarPanel(new MenuOpcionesPanel(
                appWindow,
                new UI.rol.OrganizadorStrategy(appWindow)
        )));

        JButton btnCrear = buildButton(
                "Crear Torneo",
                ColorPalette.COLOR_BTN.getColor(),
                ColorPalette.COLOR_BTN_HOVER.getColor(),
                ColorPalette.COLOR_TEXT_LIGHT.getColor()
        );
        btnCrear.setPreferredSize(new Dimension(200, 45));
        btnCrear.addActionListener(e -> crearTorneo());

        barra.add(btnVolver, BorderLayout.WEST);
        barra.add(btnCrear,  BorderLayout.EAST);
        return barra;
    }

    // ─────────────────────────────────────────────────────────────────
    // Lógica dinámica
    // ─────────────────────────────────────────────────────────────────
    private void actualizarFormatos(DisciplinaEnum disc) {
        panelFormatos.removeAll();

        if (disc == DisciplinaEnum.CARRERA) {
            JLabel lbl = new JLabel("Formato de carrera cronometrada (automático)");
            lbl.setForeground(ColorPalette.COLOR_BG_LIGHT.getColor());
            lbl.setFont(new Font("SansSerif", Font.ITALIC, 13));
            panelFormatos.add(lbl);
            formatoIdx = 0;
        } else {
            JPanel grupo = buildOptionGroup(disc.getFormatos(), idx -> formatoIdx = idx);
            panelFormatos.add(grupo);
        }

        panelFormatos.revalidate();
        panelFormatos.repaint();
    }

    private void actualizarTipoParticipante() {
        labelTipoParticipante.setText(usaEquipos ? "Equipos inscritos" : "Jugadores inscritos");
        participantes.clear();
        listModel.clear();
    }

    // ─────────────────────────────────────────────────────────────────
    // Añadir participante manualmente
    // ─────────────────────────────────────────────────────────────────
    private void mostrarDialogoAnadir() {
        if (disciplinaIdx < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una disciplina primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (usaEquipos) mostrarDialogoEquipo();
        else            mostrarDialogoJugador(null);
    }

    private void mostrarDialogoEquipo() {
        JTextField campoNombreEq = new JTextField(20);
        JTextField campoContacto = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre del equipo:"));   form.add(campoNombreEq);
        form.add(new JLabel("Contacto (opcional):")); form.add(campoContacto);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Añadir Equipo", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            String nombre = campoNombreEq.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            Equipo eq = new Equipo(nombre, campoContacto.getText().trim());
            participantes.add(eq);
            listModel.addElement(nombre);
        }
    }

    /**
     *
     * @param equipoPadre
     */
    private void mostrarDialogoJugador(Equipo equipoPadre) {
        JTextField campoNombreJug = new JTextField(20);
        JTextField campoContacto  = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre del jugador:")); form.add(campoNombreJug);
        form.add(new JLabel("Contacto (opcional):")); form.add(campoContacto);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Añadir Jugador", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            String nombre = campoNombreJug.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            Jugador jug = new Jugador(nombre, campoContacto.getText().trim());
            if (equipoPadre != null) equipoPadre.addjugador(jug);
            else {
                participantes.add(jug);
                listModel.addElement(nombre);
            }
        }
    }

    /**
     * Version preliminar de cargar un equipo o participantes desde un archivo.txt, sirve para
     * cuando se quiera revisar el estado del programa rapidamente solo se tenga que cargar los
     * datos, pero la idea es que tambien haya una forma mas manual de carg.ar equipos
     */
    private void cargarDesdeArchivo() {
        if (disciplinaIdx < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una disciplina primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (.txt)", "txt"));
        chooser.setDialogTitle("Seleccionar archivo de participantes");

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String primeraLinea = reader.readLine();
            if (primeraLinea == null) return;

            if (usaEquipos) {
                Equipo equipo = new Equipo(primeraLinea.trim());
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.isBlank()) continue;
                    String[] partes = linea.split(",", 2);
                    equipo.addjugador(new Jugador(
                            partes[0].trim(),
                            partes.length > 1 ? partes[1].trim() : ""
                    ));
                }
                participantes.add(equipo);
                listModel.addElement(equipo.getName() + " (" + equipo.getEquipoSize() + " jugadores)");
            } else {
                String linea = primeraLinea;
                do {
                    if (linea.isBlank()) continue;
                    String[] partes = linea.split(",", 2);
                    Jugador jug = new Jugador(
                            partes[0].trim(),
                            partes.length > 1 ? partes[1].trim() : ""
                    );
                    participantes.add(jug);
                    listModel.addElement(jug.getName());
                } while ((linea = reader.readLine()) != null);
            }

            JOptionPane.showMessageDialog(this,
                    "Participantes cargados desde: " + archivo.getName(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al leer el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aca es donde se crea el torneo, primero verifica que se ingresen datos permitidos,
     * es decir, nombre disciplina formato, fecha y cantidad de participantes, cuando
     * verifica que los datos son validos, crea un nuevo torneo y POR EL MOMENTO avisa al
     * usuario que se creo un torneo, AHORA, hay que guardar ese torneo en una lista de
     * torneos probablemente (para luego poder buscarlos), y entrar a otra interfaz para
     * comenzar a jugar el torneo
     */
    private void crearTorneo() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El torneo debe tener un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (disciplinaIdx < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una disciplina.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (formatoIdx < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un formato.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (participantes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Añade al menos un participante.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha = null;
        String textoFecha = campoFecha.getText().trim();
        if (!textoFecha.isEmpty()) {
            try {
                fecha = LocalDate.parse(textoFecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Formato de fecha inválido. Usa dd/mm/aaaa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            DisciplinaEnum disc    = DisciplinaEnum.values()[disciplinaIdx];
            FormatoEnum    formato = FormatoEnum.desdeNombre(disc.getFormatos()[formatoIdx]);

            TorneoGestion gestion = new TorneoGestion();
            Torneo torneo = gestion.crearTorneo(nombre, disc.crearDisciplina(), formato.crearFormato(), fecha);
            participantes.forEach(p -> {
                if (p instanceof Equipo eq) gestion.inscribirEquipoGestion(eq);
                else                        gestion.inscribirParticipanteGestion(p);
            });
            // TODO: torneo.addParticipantes(participantes);

            JOptionPane.showMessageDialog(this,
                    "Torneo \"" + nombre + "\" creado exitosamente.\n" +
                            "Disciplina: "     + disc.getNombre() + "\n" +
                            "Formato: "        + formato.getNombre() + "\n" +
                            "Participantes: "  + participantes.size(),
                    "Torneo creado", JOptionPane.INFORMATION_MESSAGE);

            appWindow.mostrarPanel(new MenuOpcionesPanel(
                    appWindow,
                    new UI.rol.OrganizadorStrategy(appWindow)
            ));

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void mostrarDialogoJugadoresEquipo(Equipo equipo, int idxEquipo) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Jugadores de: " + equipo.getName(), true); // true = modal
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // ── Lista de jugadores del equipo ──────────────────────────────
        DefaultListModel<String> jugadoresModel = new DefaultListModel<>();
        equipo.getJugadores().forEach(j -> jugadoresModel.addElement(j.getName()));

        JList<String> listaJugadores = new JList<>(jugadoresModel);
        listaJugadores.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listaJugadores.setFixedCellHeight(28);

        JScrollPane scroll = new JScrollPane(listaJugadores);
        scroll.setBorder(BorderFactory.createTitledBorder("Jugadores"));

        // ── Botones añadir / eliminar ──────────────────────────────────
        JButton btnAnadir = new JButton("+ Añadir jugador");
        btnAnadir.addActionListener(e -> {
            JTextField campoNombre   = new JTextField(15);
            JTextField campoContacto = new JTextField(15);

            JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
            form.add(new JLabel("Nombre:"));            form.add(campoNombre);
            form.add(new JLabel("Contacto (opcional):")); form.add(campoContacto);

            int res = JOptionPane.showConfirmDialog(dialog, form,
                    "Añadir Jugador", JOptionPane.OK_CANCEL_OPTION);

            if (res == JOptionPane.OK_OPTION) {
                String nombre = campoNombre.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "El nombre no puede estar vacío.");
                    return;
                }
                Jugador jug = new Jugador(nombre, campoContacto.getText().trim());
                equipo.addjugador(jug);
                jugadoresModel.addElement(nombre);
                // Actualizar el label del equipo en la lista principal
                listModel.set(idxEquipo,
                        equipo.getName() + " (" + equipo.getEquipoSize() + " jugadores)");
            }
        });

        JButton btnEliminar = new JButton("X Eliminar seleccionado");
        btnEliminar.setForeground(Color.RED);
        btnEliminar.addActionListener(e -> {
            int idx = listaJugadores.getSelectedIndex();
            if (idx >= 0) {
                Participante jugador = equipo.getJugadores().get(idx);
                equipo.removePlayer(jugador);
                jugadoresModel.remove(idx);
                listModel.set(idxEquipo,
                        equipo.getName() + " (" + equipo.getEquipoSize() + " jugadores)");
            }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botones.add(btnAnadir);
        botones.add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(botones,   BorderLayout.CENTER);
        sur.add(btnCerrar, BorderLayout.SOUTH);
        sur.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(sur,    BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
