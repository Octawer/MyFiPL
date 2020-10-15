package ghost;

import gui.Escritorio;
import gui.PaletaComandos;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import utils.DebugHelper;

/**
 * extiende el oyente de eventos del mouse y se encarga de crear la imagen ghost del componente
 * pintando el componente original el en glassPane. 
 * Se encarga de los pasos iniciales y finales del drag and drop (click inicial, y liberación del click)
 */
public class GhostComponentAdapter extends GhostDropAdapter
{
    public GhostComponentAdapter(GhostGlassPane glassPane, String action) {
        super(glassPane, action);
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     * cuando ocurre el evento del mouse del click inicial (primera accion del drag n drop)
     * obtiene el componente donde ocurrió y lo pinta en el glassPane (haciendole visible)
     */
    public void mousePressed(MouseEvent e)
    {
        Component c = e.getComponent();

        BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        c.paint(g);		// se pide al componente original que se pinte en el contexto gráfico obtenido (mismas dimensiones que el original)

        glassPane.setVisible(true);

        Point p = (Point) e.getPoint().clone();
        SwingUtilities.convertPointToScreen(p, c);
        SwingUtilities.convertPointFromScreen(p, glassPane);

        glassPane.setPoint(p);
        glassPane.setImage(image);		// se pasa la imagen del componente original al glassPane
        glassPane.repaint();			// se pinta el glassPane
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     * cuando ocurre el evento del mouse de liberar el click se obtienen las coordenadas del
     * componente objetivo (y el propio componente), se hace invisible el glassPane, y se lanza un evento de drop
     * para que el manejador registrado se encargue de el
     */
    public void mouseReleased(MouseEvent e)
    {
        Component c = e.getComponent();

        BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Point p = (Point) e.getPoint().clone();
        SwingUtilities.convertPointToScreen(p, c);

        Point eventPoint = (Point) p.clone();
        SwingUtilities.convertPointFromScreen(p, glassPane);

        glassPane.setPoint(p);
        glassPane.setVisible(false);		// se oculta el glassPane
        glassPane.setImage(null);

        Component parent = c.getParent();
        // DebugHelper.infoDialog(parent, "Parent = " + parent.getBounds());
        // si viene del Escritorio se lanza un move, si no (Paleta) se lanza un drop
        if(parent instanceof Escritorio) {
        	// se lanza el evento del move para que el manejador registrado se ocupe de el
        	fireGhostMoveEvent(new GhostDropEvent(action, eventPoint, image, (JComponent) c));
        } else {
        	// se lanza el evento del drop para que el manejador registrado se ocupe de el
        	fireGhostDropEvent(new GhostDropEvent(action, eventPoint, image, (JComponent) c));
        }
    }
}