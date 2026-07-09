package UI;

import UI.Resources.AppWindow;
import UI.Resources.BaseWindow;
import UI.Resources.ColorPalette;
import gestion.ParticipanteLoader;
import gestion.TorneoGestion;
import modelo.Equipo;
import modelo.Jugador;
import modelo.Participante;
import modelo.Torneo;
import modelo.enums.DisciplinaEnum;
import modelo.enums.FormatoEnum;
import modelo.formato.Liga;
import modelo.formato.TorneoFormato;

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

/**
 * CrearTorneoPanel es la interfaz exclusiva del Organizador que sirve para crear los torneos,
 * el Organizador puede Ponerle nombre al torneo, Elegir la disciplina y formato, la fecha de
 * inicio y participantes.
 */
public class CrearTorneoPanel extends BaseWindow {

    // Estado interno
    private int disciplinaIdx  = -1;
    private int formatoIdx     = -1;
    private boolean usaEquipos = true;
    private final List<Participante> participantes = new ArrayList<>();

    // Componentes dinamicos, cambian segun la disciplina escogida
    private JPanel panelFormatos;
    private DefaultListModel<String> listModel;
    private JLabel labelTipoParticipante;

    // Campos de texto, donde se escriben el Nombre y la fecha inicial del torneo
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

    /**
     *
     * @return la estructura principal del formulario para crear un nuevo torneo
     */
    private JPanel buildFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 60, 20, 60));
        //Titulo de la interfaz
        panel.add(buildTitulo("Crear Torneo"));
        panel.add(Box.createVerticalStrut(24));
        //Campo donde se añade el nombre del Torneo
        panel.add(buildEtiqueta("Nombre del torneo"));
        panel.add(Box.createVerticalStrut(6));
        campoNombre = buildCampoTexto("Ej: Liga de Verano 2025");
        panel.add(campoNombre);
        panel.add(Box.createVerticalStrut(20));
        //Campo donde se escoge la disciplina del torneo
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
        //Campo donde se indica el formato de la disciplina escogida
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
        //Campo donde se escoge la fecha inicial para el torneo
        panel.add(buildEtiqueta("Fecha de inicio (dd/mm/aaaa)"));
        panel.add(Box.createVerticalStrut(6));
        campoFecha = buildCampoTexto("Ej: 01/01/2000");
        panel.add(campoFecha);
        panel.add(Box.createVerticalStrut(24));
        //Campo donde se pueden ver los participantes del torneo
        labelTipoParticipante = buildEtiqueta("Participantes");
        panel.add(labelTipoParticipante);
        panel.add(Box.createVerticalStrut(8));
        panel.add(buildBotonesParticipantes());
        panel.add(Box.createVerticalStrut(10));
        panel.add(buildListaParticipantes());
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    /**
     *
     * @return Panel con los botones de Añadir y cargar un archivo txt
     */
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
                    if (validarDisciplinaSeleccionada() && participantes.get(idx) instanceof Equipo equipo) {
                        Dialogo.mostrarDialogoJugadoresEquipo(CrearTorneoPanel.this, equipo, idx, listModel);
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
            if (validarDisciplinaSeleccionada()) {
                participantes.remove(idx);
                listModel.remove(idx);
            }
        });

        panelParticipantes.add(scroll, BorderLayout.CENTER);
        panelParticipantes.add(btnEliminar, BorderLayout.SOUTH);
        return panelParticipantes;
    }

    /**
     *
     * @return Panel inferior de la interfaz, se encuentran los botones para volver y
     * para crear el torneo
     */
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

    /**
     * aca se almacena la logica dinamica, cuando se elige una disciplina se pueden escoger
     * ciertos formatos, entonces, se ingresa
     * @param disc
     * y segun la disciplina escogida se actualiza el panelFormatos
     */
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

    /**
     * Metodo que permite añadir a un participante con su nombre y contacto, ya sea un
     * equipo o jugador, tambien es usado para añadir a un jugador en un equipo ya existente
     */
    private void mostrarDialogoAnadir() {
        if (!validarDisciplinaSeleccionada()) return;
        if (usaEquipos) {
            Equipo eq = Dialogo.mostrarDialogoEquipo(this);
            if (eq != null) {
                participantes.add(eq);
                listModel.addElement(eq.getName());
            }
        } else {
            Jugador jug = Dialogo.mostrarDialogoJugador(this, null);
            if (jug != null) {
                participantes.add(jug);
                listModel.addElement(jug.getName());
            }
        }
    }
    /**
     * Metodo que permite cargar un equipo o lista de jugadores desde un archivo.txt, sirve para
     * cuando se quiera revisar el estado del programa rapidamente (solo se tiene que cargar los
     * datos)
     */
    private void cargarDesdeArchivo() {
        if (!validarDisciplinaSeleccionada()) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (.txt)", "txt"));
        chooser.setDialogTitle("Seleccionar archivo de participantes");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        try {
            if (usaEquipos) {
                Equipo equipo = ParticipanteLoader.cargarEquipo(archivo);
                participantes.add(equipo);
                listModel.addElement(equipo.getName() + " (" + equipo.getEquipoSize() + " jugadores)");
            } else {
                List<Jugador> jugadores = ParticipanteLoader.cargarJugadores(archivo);
                jugadores.forEach(jug -> {
                    participantes.add(jug);
                    listModel.addElement(jug.getName());
                });
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
        if (!validarDisciplinaSeleccionada()) return;
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
            gestion.crearTorneo(nombre, disc.crearDisciplina(), formato.crearFormato(), fecha);
            participantes.forEach(p -> {
                if (p instanceof Equipo eq) gestion.inscribirEquipoGestion(eq);
                else                        gestion.inscribirParticipanteGestion(p);
            });

            // ── Generar el bracket ANTES de abrir el panel ─────────────────
            TorneoFormato formatoTorneo = gestion.getTorneo().getFormatoTorneo();

            if (formatoTorneo instanceof Liga) {
                gestion.generarCalendarioLiga();
                appWindow.mostrarPanel(new PosicionesVer(appWindow, gestion));
            } else {
                gestion.generarBracketEliminatoria(); // esto inicializa bracketsGestion
                appWindow.setTorneoActivo(gestion);
                appWindow.mostrarPanel(new BracketVer(appWindow, gestion));
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @return variable boolean para verificar que se escogio una disciplina, se
     * preguntaba varias veces dentro del codigo a si que mejor queda
     * en su metodo void aparte
     */
    private boolean validarDisciplinaSeleccionada() {
        if (disciplinaIdx < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una disciplina primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
