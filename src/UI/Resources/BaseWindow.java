package UI.Resources;

import gestion.BracketUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.List;

/**
 * BaseWindow es una plantilla para hacer lo basico para una ventana, un fondo y botones, en un futuro se puede anadir
 * mejoras como botones de seleccion unica, seleccion multiple.
 */
public abstract class BaseWindow extends JPanel {

    protected BufferedImage backgroundImage;
    protected AppWindow appWindow;

    /**
     *
     * @param appWindow es el JFrame que todos los JPanel tienen en comun
     */
    public BaseWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    /**
     * sirve para cargar la imagen de fondo del Frame
     * @param fileName es el nombre de la imagen que esta en
     *                 la carpeta de recursos (Recources)
     */
    protected void loadBackgroundImage(String fileName) {
        try {
            backgroundImage = ImageIO.read(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResource(fileName)
                    )
            );
        } catch (IOException | NullPointerException e) {
            System.err.println("No se pudo cargar imagen '" + fileName + "': " + e.getMessage());
        }
    }

    /**
     *
     * @return paintComponent de la Imagen de fondo, es decir
     * se carga el Jpanel con la imagen de fondo
     */
    protected JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                if (backgroundImage != null) {
                    double panelRatio = (double) getWidth() / getHeight();
                    double imgRatio   = (double) backgroundImage.getWidth() / backgroundImage.getHeight();
                    int drawW, drawH, drawX, drawY;
                    if (panelRatio > imgRatio) {
                        drawW = getWidth();
                        drawH = (int)(getWidth() / imgRatio);
                    } else {
                        drawH = getHeight();
                        drawW = (int)(getHeight() * imgRatio);
                    }
                    drawX = (getWidth()  - drawW) / 2;
                    drawY = (getHeight() - drawH) / 2;
                    g2.drawImage(backgroundImage, drawX, drawY, drawW, drawH, null);
                } else {
                    g2.setColor(new Color(255, 255, 255));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
            }
        };
    }

    /**
     *
     *
     * BuildButton es un metodo para poder construir lo basico de un boton
     * con esquinas redondeadas
     * @param text el texto que va a contener el boton
     * @param bgColor el color del boton
     * @param hoverColor el color cuando el cursor esta sobre el boton
     * @param textColor el color del texto que va a contener el boton
     * @return el boton armado
     */
    public static JButton buildButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isRollover() ? hoverColor : bgColor;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.setColor(textColor);
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth()  - fm.stringWidth(text)) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, textX, textY);

                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Construye un panel de búsqueda reutilizable: campo de texto + lista filtrable.
     *
     *
     * @param items     lista completa de elementos a mostrar/filtrar
     * @param onSelect  qué hacer cuando el usuario hace doble clic / selecciona un elemento
     */
    protected <T extends SearchableItem> JPanel buildSearchPanel(
            List<T> items, Consumer<T> onSelect) {

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // ── Campo de búsqueda ──────────────────────────────────────────
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.putClientProperty("JTextField.placeholderText", "Buscar...");

        // ── Lista de resultados ────────────────────────────────────────
        DefaultListModel<T> listModel = new DefaultListModel<>();
        items.forEach(listModel::addElement);

        JList<T> resultList = new JList<>(listModel);
        resultList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SearchableItem item) {
                    setText(item.getDisplayLabel());
                }
                return this;
            }
        });

        // aca se filtra mientras se escribe
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                String query = searchField.getText().toLowerCase().trim();
                listModel.clear();
                for (T item : items) {
                    if (item.getSearchableText().toLowerCase().contains(query)) {
                        listModel.addElement(item);
                    }
                }
            }
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        // ── Selección (doble clic abre el detalle) ─────────────────────
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    T selected = resultList.getSelectedValue();
                    if (selected != null && onSelect != null) {
                        onSelect.accept(selected);
                    }
                }
            }
        });

        panel.add(searchField, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultList), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Grupo de botones de selección única (como radio buttons pero con estilo propio).
     * Al seleccionar uno, los demás se deseleccionan automáticamente.
     *
     * @param opciones      etiquetas de cada opción
     * @param onSelect      qué hacer cuando se selecciona una opción (recibe el índice)
     * @return panel con los botones ya agrupados
     */
    protected JPanel buildOptionGroup(String[] opciones, Consumer<Integer> onSelect) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        boolean[] activos = new boolean[opciones.length]; // estado compartido

        JButton[] botones = new JButton[opciones.length];

        for (int i = 0; i < opciones.length; i++) {
            final int idx = i;
            final String texto = opciones[i];

            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    Color bg;
                    if (activos[idx])                    bg = ColorPalette.COLOR_BTN.getColor();
                    else if (getModel().isRollover())    bg = ColorPalette.COLOR_BTN_HOVER.getColor();
                    else                                 bg = ColorPalette.COLOR_BTN_DEACTIVATED.getColor();

                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                    if (activos[idx]) {
                        g2.setColor(ColorPalette.COLOR_BTN.getColor().darker());
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
                    }

                    g2.setColor(activos[idx] ? ColorPalette.COLOR_TEXT_LIGHT.getColor() : ColorPalette.COLOR_TEXT_DARK.getColor());
                    g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(texto,
                            (getWidth()  - fm.stringWidth(texto)) / 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                    g2.dispose();
                }
            };

            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(130, 40));

            btn.addActionListener(e -> {
                // Limpiar todos, activar solo el ultimo seleccionado ( o el primer seleccionado)
                for (int j = 0; j < activos.length; j++) activos[j] = false;
                activos[idx] = true;
                // Repintar todos para que reflejen el cambio
                for (JButton b : botones) b.repaint();

                if (onSelect != null) onSelect.accept(idx);
            });

            botones[i] = btn;
            panel.add(btn);
        }

        return panel;
    }

    /**
     * Sirve para hacer un titulo para el JFrame
     * @param texto El titulo que se quiere Ingresar
     * @return el JLabel del Titulo
     */
    protected JLabel buildTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }



    protected JLabel buildEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(new Color(50, 50, 50));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /**
     * Sirve para poder hacer un campo de texto rellenable
     * @param placeholder
     * @return
     */
    protected JTextField buildCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.putClientProperty("JTextField.placeholderText", placeholder);
        campo.setMaximumSize(new Dimension(500, 36));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.COLOR_TXT_FIELD.getColor()),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return campo;
    }

    /**
     * Sirve Para Hacer un boton con texto demasiado largo,
     * la principal diferencia con BuildButton es que
     * el texto comienza a pintarse desde la izquierda y
     * tiene dimensiones predeterminadas
     * @param nombre texto que contiene el boton
     * @param bg color del boton
     * @param hover color del boton cuando el cursor esta sobre el
     * @return el boton truncado
     */
    protected JButton buildButtonTruncado(String nombre, Color bg, Color hover) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getModel().isRollover() ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String display = BracketUtils.truncar(nombre, fm, getWidth() - 16);
                g2.drawString(display, 10, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        btn.setMaximumSize(new Dimension(160, 38));
        return btn;
    }

    /**
     * initUnit es el metodo de inicializacion estandar para todos los JPanel a crear
     */
    protected abstract void initUI();
}
