package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Objects;

public abstract class BaseWindow extends JFrame {

    protected BufferedImage backgroundImage;

    public BaseWindow(String title) {
        super(title);
        // configuración comun a todas las ventanas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setResizable(true);
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
    // Cada ventana construye su propio metodo de inicializacion
    protected abstract void initUI();
}
