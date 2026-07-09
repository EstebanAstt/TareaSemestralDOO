package UI.PosicionesPanel;


import UI.Resources.ColorPalette;
import modelo.Posiciones;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import static UI.PosicionesPanel.PosicionesVer.fmt;

/**
 * TablaPosicionesPanel se encarga de construir y mostrar la
 * tabla
 */
public class TablaPosicionesPanel extends JPanel {

    /**
     *
     * @param tabla
     * @param muestraEmpate
     */
    public TablaPosicionesPanel(List<Posiciones> tabla, boolean muestraEmpate) {
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Título de la sección
        JLabel lbl = new JLabel("Clasificación");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(lbl, BorderLayout.NORTH);

        // Se construye la tabla real
        JTable jTable = buildJTable(tabla, muestraEmpate);

        // Ajustamos la altura para que no tenga un scroll interno innecesario
        int alturaViewport = jTable.getRowHeight() * Math.max(1, tabla.size()) + 2;
        jTable.setPreferredScrollableViewportSize(new Dimension(800, alturaViewport));

        JScrollPane sp = new JScrollPane(jTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createLineBorder(ColorPalette.COLOR_LINEBORDER_LIGHT.getColor()));

        add(sp, BorderLayout.CENTER);
    }

    private JTable buildJTable(List<Posiciones> tabla, boolean muestraEmpate) {
        String[] columnas = muestraEmpate
                ? new String[]{"#", "Participante", "PJ", "PG", "PE", "PP", "GF", "GC", "DIF", "PTS"}
                : new String[]{"#", "Participante", "PJ", "PG", "PP", "GF", "GC", "DIF", "PTS"};
        int cols = columnas.length;

        Object[][] filas = new Object[tabla.size()][cols];
        for (int i = 0; i < tabla.size(); i++) {
            Posiciones pos = tabla.get(i);
            double dif = pos.getDiferencia();
            String difStr = (dif >= 0 ? "+" : "") + fmt(dif);

            if (muestraEmpate) {
                filas[i] = new Object[]{
                        i + 1, pos.getParticipante().getName(),
                        pos.getPj(), pos.getPg(), pos.getPe(), pos.getPp(),
                        fmt(pos.getPf()), fmt(pos.getPc()), difStr, fmt(pos.getPts())
                };
            } else {
                filas[i] = new Object[]{
                        i + 1, pos.getParticipante().getName(),
                        pos.getPj(), pos.getPg(), pos.getPp(),
                        fmt(pos.getPf()), fmt(pos.getPc()), difStr, fmt(pos.getPts())
                };
            }
        }

        DefaultTableModel model = new DefaultTableModel(filas, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable jTable = new JTable(model);
        jTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jTable.setRowHeight(32);
        jTable.setShowGrid(false);
        jTable.setIntercellSpacing(new Dimension(0, 0));
        jTable.setBackground(Color.WHITE);
        jTable.setForeground(ColorPalette.COLOR_TEXT_DARK.getColor());
        jTable.setSelectionBackground(ColorPalette.COLOR_BTN.getColor());
        jTable.setSelectionForeground(Color.WHITE);
        jTable.setFocusable(false);

        // Estilos de la cabecera
        JTableHeader header = jTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(ColorPalette.COLOR_BTN.getColor());
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Renderer para filas alternadas
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean selected, boolean focused, int row, int col) {
                super.getTableCellRendererComponent(t, value, selected, focused, row, col);
                setHorizontalAlignment(col == 1 ? JLabel.LEFT : JLabel.CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!selected) {
                    setBackground(row % 2 == 0 ? ColorPalette.COLOR_BG_LIGHT.getColor() : ColorPalette.COLOR_BG_LIGHT.getColor().darker());
                    setForeground(ColorPalette.COLOR_TEXT_DARK.getColor());
                }
                setFont(col == cols - 1 ? new Font("SansSerif", Font.BOLD, 14) : new Font("SansSerif", Font.PLAIN, 14));
                return this;
            }
        };
        for (int c = 0; c < cols; c++) {
            jTable.getColumnModel().getColumn(c).setCellRenderer(renderer);
        }

        jTable.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable.getColumnModel().getColumn(1).setMinWidth(160);

        return jTable;
    }
}
