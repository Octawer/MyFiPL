package ghost;


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Panel invisible que nos sirve para pintar las imágenes fantasma de los
 * comandos y dar la impresión de que se mueven por el entorno
 * en el ámbito de las acciones del Drag and Drop
 */
public class GhostGlassPane extends JPanel {

    private AlphaComposite composite;			// modifica la opacidad del componente
    private BufferedImage dragged = null;		// imagen del componente
    private Point location = new Point(0, 0);	// coordenadas del componente

    public GhostGlassPane() {
        setOpaque(false);
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    }

    public void setImage(BufferedImage dragged) {
        this.dragged = dragged;
    }

    public void setPoint(Point location) {
        this.location = location;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     * establece la opacidad (a través del composite) y pinta la imagen
     * del comando en el panel invisible frontal, en las coordenadas oportunas
     */
    public void paintComponent(Graphics g) {
        if (dragged == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(composite);
        g2.drawImage(dragged,
                (int) (location.getX() - (dragged.getWidth(this) / 2)),
                (int) (location.getY() - (dragged.getHeight(this) / 2)),
                null);
    }
}