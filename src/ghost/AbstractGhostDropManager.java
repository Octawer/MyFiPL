package ghost;

import gui.Comando;
import gui.Escritorio;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;



/**
 * gestor abstracto de las acciones del Drop dentro del DnD
 */
public abstract class AbstractGhostDropManager implements GhostDropListener {
	protected JComponent component;

	public AbstractGhostDropManager() {
		this(null);
	}
	
	public AbstractGhostDropManager(JComponent component) {
		this.component = component;
	}

	/**
	 * convierte un punto de coordenadas de pantalla a coordenadas de componente
	 * @param point el punto que queremos convertir al nuevo sistema de coordenadas
	 * @return el punto convertido al sistema de coordenadas del componente
	 */
	protected Point getTranslatedPoint(Point point) {
        Point p = (Point) point.clone();
        SwingUtilities.convertPointFromScreen(p, component);
		return p;
	}

	/**
	 * comprueba si un punto esta dentro de los límites de un componente
	 * @param point el punto que queremos comprobar
	 * @return true si está dentro del componente
	 */
	protected boolean isInTarget(Point point) {
		Rectangle bounds = component.getVisibleRect();
		return bounds.contains(point);
	}
      

	/*
	 * manejador del evento del drop (movimiento desde Paleta a Escritorio)
	 */
	public void ghostDropped(GhostDropEvent e) {
	}
        
	/*
	 * manejador del evento del move (movimiento desde Escritorio a Escritorio)
	 */
    public void ghostMoved(GhostDropEvent e) {
	}
    
    /**
     * registra los oyentes del comando para las acciones del click inicial y del arrastre con el ratón (click + drag)
     * así como el gestor (manejador) de la acción del move (drop Escritorio - Escritorio)
     * @param com el comando cuyos oyentes vamos a registrar
     * @param glassPane panel invisible que vamos a usar para simular el desplazamiento del comando en el drag
     */
    protected void setCommandListeners(Comando com, GhostGlassPane glassPane) {
    	// establecemos los oyentes para la acción de mover o eliminar la nueva JLabel creada
        GhostComponentAdapter compAdapter = new GhostComponentAdapter(glassPane, "Missed erase action");
        GhostMotionAdapter motionAdapter = new GhostMotionAdapter(glassPane);
        com.addMouseListener(compAdapter);
        com.addMouseMotionListener(motionAdapter);
        
        GhostMoveManager moveManager = new GhostMoveManager(this.component, glassPane);
        compAdapter.addGhostDropListener(moveManager);
    }
}