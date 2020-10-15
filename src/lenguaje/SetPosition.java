package lenguaje;

import gui.Entorno;
import gui.Escenario;

import java.awt.Point;
import java.beans.PropertyChangeEvent;

import dominio.Robot;

/**
 * Sentencia lógica de posición.
 * 
 * @author Octavio
 */
public class SetPosition extends Actuador {
	
	private Point coord;								// las coordenadas de la posición en el Escenario
	private final Point origen = new Point(0,0);		// el origen de coords - NOT USED
	
	public SetPosition() {
		setCoord(origen);
	}
	
	public SetPosition(Point point) {
		setCoord(point);
	}

	public Point getCoord() {
		return coord;
	}

	public void setCoord(Point coord) {
		this.coord = coord;
	}
	
	/* (non-Javadoc)
	 * @see lenguaje.Sent#act()
	 * 
	 * Acción semántica del comando lógico. 
	 * Establece la posición del robot en el Escenario
	 */
	@Override
	public void act() {
		if(!stopped){
			Entorno.getInstancia().getEscenario().setRobotPosition(coord);
			pause(Entorno.getInstancia().getEscenario().getRobotSpeed());
		}
	}
	
	@Override
	public String toString() {
		if(coord != null) {
			return "SetPosition " + coord;
		} else {
			return "SetPosition " + origen;
		}
		
	}


}
