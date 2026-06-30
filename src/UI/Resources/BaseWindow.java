package UI.Resources;

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

    public BaseWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    // se llama con el nombre del archivo, lo busca en Resources
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
    protected JButton buildButton(String text, Color bgColor, Color hoverColor, Color textColor) {
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
    // Cada ventana construye su propio metodo de inicializacion
    protected abstract void initUI();
}
