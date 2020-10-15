package lenguaje;

import java.beans.PropertyChangeEvent;

import gui.Entorno;
import gui.Escenario;

/**
 * Sentencia lógica de velocidad
 * 
 * @author Octavio
 */
public class SetSpeed extends Actuador {
	
	private int delay;
	private final int minSpeed = 1;
	private final int maxSpeed = 10;
	private final int defaultDelay = 500;
	private final int maxDelay = 1100;
	
	public SetSpeed() {
		setSpeed(defaultDelay);
	}
	
	public SetSpeed(int speed) {
		setSpeed(speed);
	}

	public int getSpeed() {
		return delay;
	}

	/**
	 * Establece la velocidad (retardo entre acciones) del Robot
	 * @param speed la velocidad pasada desde el comando gráfico
	 */
	public void setSpeed(int speed) {
		if(speed >= minSpeed && speed <= maxSpeed) {
			// los valores que los alumnos introducen van de [1-10]
			// siendo 1 la velocidad mas baja y 10 la mas alta (mas intuitivo para ellos que el delay)
			// de forma que un valor de "speed" de 1 = 1000 ms de delay
			// y un valor de "speed" de 10 = 100 ms de delay
			this.delay = maxDelay - speed * 100;								
		} else {
			this.delay = defaultDelay;
		}
		System.out.println("Speed = " + this.delay);
	}
	
	/* (non-Javadoc)
	 * @see lenguaje.Sent#act()
	 * 
	 * Acción semántica del comando lógico.
	 * Modifica la velocidad del robot (retardo entre acciones)
	 */
	@Override
	public void act() {
		if(!stopped){
			Entorno.getInstancia().getEscenario().setRobotSpeed(delay);
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SetSpeed " + delay;
	}


}
