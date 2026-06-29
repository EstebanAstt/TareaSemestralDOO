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

    // Cada ventana construye su propio metodo de inicializacion
    protected abstract void initUI();
}
