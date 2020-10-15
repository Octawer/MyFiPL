package ghost;


import gui.Comando;
import gui.Escritorio;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import utils.DebugHelper;
import utils.FactoriaComandos;

/**
 *  Manejador o gestor de los eventos del Move (Escritorio-Escritorio)
 */
class GhostMoveManager extends AbstractGhostDropManager {

    private GhostGlassPane glassPane;

    public GhostMoveManager(JComponent target) {
        super(target);
    }

    public GhostMoveManager(JComponent target, GhostGlassPane gp) {
        super(target);
        glassPane = gp;
    }

    /* (non-Javadoc)
     * @see ghost.AbstractGhostDropManager#ghostMoved(ghost.GhostDropEvent)
     * 
     * aqui se encapsula toda la gestión del evento del Move
     * se obtiene el componente del evento (Comando), y si el Comando no está bloqueado (snapped o es Start)
     * se crea uno nuevo a través del método de factoría
     * se establece un rectángulo inicial para el comando
     * se elimina el comando antiguo del Escritorio (cambio de posición gráfica)
     * se registran sus oyentes y nuevos manejadores (el manejador del evento move (Escritorio-Escritorio))
     * y finalmente se coloca en el Escritorio (o en caso de que caiga fuera de los límites se elimina - 
     * es decir, no se coloca uno nuevo, ya que se eliminó el antiguo antes -)
     */
    public void ghostMoved(GhostDropEvent e) {
        String action = e.getAction();
        Point p = getTranslatedPoint(e.getDropLocation());
        BufferedImage image = e.getImage();

        Comando com = (Comando) e.getComando();
        if(!com.isSnapped() && com.getToken() != Comando.START) {
        	Comando newCom = FactoriaComandos.createCom(com.getToken(), com.getImagePath());
        	
        	// es necesario definir un rectangulo inicial para el comando que dejamos desde el glassPane, para poder verificar el snap
        	Dimension size = newCom.getPreferredSize();
        	newCom.setBounds(p.x - size.width / 2, p.y - size.height / 2, size.width, size.height);

        	Escritorio escritorio = (Escritorio) this.component;
        	escritorio.remove(com);
        	setCommandListeners(newCom, glassPane);					// OJO: estamos creando un nuevo oyente (de move) cada vez q movemos el comando (ver si usamos el mismo siempre)

        	if (!isInTarget(p)) {
        		DebugHelper.infoDialog(this.component, "Comando Eliminado");

        	} else {
        		// colocamos el nuevo comando en el Escritorio
        		escritorio.placeCommand(p, newCom);
        	}
        } else {
        	DebugHelper.infoDialog(this.component, "Comando Bloqueado!");
        }
    }
}
