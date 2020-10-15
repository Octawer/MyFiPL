package ghost;

import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
//import sun.security.jca.JCAUtil;

/**
 * evento de drop (tb usado para representar eventos de move ya que son similares)
 */
public class GhostDropEvent {
	private Point point;			// coordenadas del drop
	private String action;			// cadena informativa de la accion
	private BufferedImage image;	// imagen del comando
	private JComponent comando;		// comando (objeto entero, no solo la imagen)

	public GhostDropEvent(String action, Point point) {
		this.action = action;
		this.point = point;
	}

	public GhostDropEvent(String action, Point point, BufferedImage image) {
		this(action, point);
		this.image = image;
	}

	public GhostDropEvent(String action, Point point, BufferedImage image, JComponent com) {
		this(action, point, image);
		this.comando = com;
	}

	public String getAction() {
		return action;
	}

	public Point getDropLocation() {
		return point;
	}

	public BufferedImage getImage() {
		return image;
	}

	public JComponent getComando() {
		return comando;
	}
}
