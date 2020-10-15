package ghost;


import gui.Comando;
import gui.Escritorio;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import utils.DebugHelper;
import utils.FactoriaComandos;


/**
 * Manejador o gestor de los eventos del Drop (Paleta-Escritorio)
 */
public class GhostDropManager extends AbstractGhostDropManager {
    
    private JComponent trash;			// por si implementamos el area específica de papelera (NO USADO)
    private GhostGlassPane glassPane;

    public GhostDropManager(JComponent target) {
        super(target);
    }

    public GhostDropManager(JComponent target, GhostGlassPane gp) {
        super(target);
        glassPane = gp;
    }
    
     public GhostDropManager(JComponent target, JComponent trash, GhostGlassPane gp) {
        this(target, gp);
        this.trash = trash;
    }

    /* (non-Javadoc)
     * @see ghost.AbstractGhostDropManager#ghostDropped(ghost.GhostDropEvent)
     * 
     * aqui se encapsula toda la gestión del evento del Drop
     * se obtiene el componente del evento (Comando), se crea uno nuevo a través del método de factoría
     * se establece un rectángulo inicial para el componente
     * se registran sus oyentes y nuevos manejadores (el manejador del evento move (Escritorio-Escritorio))
     * y finalmente se coloca en el Escritorio
     */
    public void ghostDropped(GhostDropEvent e) {
        String action = e.getAction();
        Point p = getTranslatedPoint(e.getDropLocation());
        BufferedImage image = e.getImage();						// esto vale si es un ghostPictureAdapter (GPA), en caso del GCA no hay imagen (pero si componente) -> comboBox
        
        JComponent comp = e.getComando();
        
        // aqui pasariamos al nuevo el comboBox tb
        Comando com = (Comando) comp;
        Comando comando = FactoriaComandos.createCom(com.getToken(), com.getImagePath());
        
        // es necesario definir un rectangulo inicial para el comando que dejamos desde el glassPane, para poder verificar el snap
        Dimension size = comando.getPreferredSize();
        comando.setBounds(p.x - size.width / 2, p.y - size.height / 2, size.width, size.height);
        
        if (isInTarget(p)) {
            
        	// registramos los oyentes y manejadores del comando
            setCommandListeners(comando, glassPane);
            
            // colocamos el nuevo comando en el Escritorio
            if(this.component instanceof Escritorio) {
            	Escritorio escritorio = (Escritorio) this.component;
            	
            	escritorio.placeCommand(p, comando);
            }
        } else {
            DebugHelper.infoDialog(this.component, "Fuera de Escritorio");
        }
    }
    
}