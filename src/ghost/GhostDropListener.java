package ghost;

/**
 * interfaz de los gestores o manejadores de los eventos del drop ((drop)Paleta-Escritorio y (move)Escritorio-Escritorio)
 */
public interface GhostDropListener {
	public void ghostDropped(GhostDropEvent e);

    public void ghostMoved(GhostDropEvent evt);
}
