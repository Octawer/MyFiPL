package ghost;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

/**
 * oyente de los eventos de arrastre del mouse (acciones del drag)
 */
public class GhostMotionAdapter extends MouseMotionAdapter
{
    private GhostGlassPane glassPane;

	public GhostMotionAdapter(GhostGlassPane glassPane) {
		this.glassPane = glassPane;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionAdapter#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 * cuando se registra un evento de drag se actualizan las coordenadas en el glassPane
	 * y se pinta en la nueva posición
	 */
	public void mouseDragged(MouseEvent e)
    {
        Component c = e.getComponent();

        Point p = (Point) e.getPoint().clone();
        SwingUtilities.convertPointToScreen(p, c);
        SwingUtilities.convertPointFromScreen(p, glassPane);
        glassPane.setPoint(p);

        glassPane.repaint();
    }
}