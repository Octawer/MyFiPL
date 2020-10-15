package lenguaje;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComboBox;

import dominio.Robot;

import gui.Entorno;
import gui.Escenario;

/**
 * Comando Move lógico
 * 
 * @author Octavio
 */
public class Move extends Actuador {
	
	private Point direction;		// la dirección de movimiento del comando
	
	public Move() {
		super();
	}
	
	public Move(Point direction) {
		super();
		this.direction = direction;
	}
	
	/* (non-Javadoc)
	 * @see lenguaje.Sent#act()
	 * 
	 * Método principal del comando lógico, el cual mueve al Robot en
	 * la dirección indicada (si no está parado el programa (cada sentencia))
	 */
	@Override
	public void act() {
		if(!stopped){
			// mueve al robot en la dirección indicada
			Entorno.getInstancia().getEscenario().moveRobot(direction);
			// setImage del robot en funcion de la dirección (uno de los 4 gifs (up, down, left, right))
			pause(Entorno.getInstancia().getEscenario().getRobotSpeed());
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Move";
	}

	public Point getDirection() {
		return direction;
	}

	public void setDirection(Point direction) {
		this.direction = direction;
	}


}
