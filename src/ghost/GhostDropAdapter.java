package ghost;

import java.awt.event.MouseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * oyente de los eventos del mouse relacionados con el click inicial del comando
 * y el drag, el cual informa mediante eventos a los gestores / manejadores del drop (PALETA-ESCRITORIO) o move (ESCRITORIO-ESCRITORIO)
 */
public class GhostDropAdapter extends MouseAdapter {
    protected GhostGlassPane glassPane;
    protected String action;

    private List listeners;

    public GhostDropAdapter(GhostGlassPane glassPane, String action) {
        this.glassPane = glassPane;
        this.action = action;
        this.listeners = new ArrayList();
    }

    public void addGhostDropListener(GhostDropListener listener) {
        if (listener != null)
            listeners.add(listener);
    }

    public void removeGhostDropListener(GhostDropListener listener) {
        if (listener != null)
            listeners.remove(listener);
    }

    /**
     * lanza un evento de drop a todos los oyentes registrados
     * @param evt el evento del drop
     */
    protected void fireGhostDropEvent(GhostDropEvent evt) {
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
        	((GhostDropListener) it.next()).ghostDropped(evt);
        }
    }
    
    /**
     * lanza un evento de move a todos los oyentes registrados
     * @param evt el evento del move
     */
    protected void fireGhostMoveEvent(GhostDropEvent evt) {
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
        	((GhostDropListener) it.next()).ghostMoved(evt);
        }
    }
}