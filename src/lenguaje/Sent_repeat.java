package lenguaje;

import gui.Entorno;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import dominio.Robot;

/**
 * Sentencia repeat lógica
 * 
 * @author Octavio
 */
public class Sent_repeat extends Sent {
	
	private Sensor sensor;					// sensor de la condición del bucle
	private Bloque bloque;					// bloque de sentencias de bucle
	
	public Sent_repeat() {
		super();
		bloque = new Bloque();
	}


	public Sent_repeat(Sensor sensor) {
		super();
		this.sensor = sensor;
		bloque = new Bloque();
	}


	public Sent_repeat(Sensor sensor, Bloque bloque) {
		super();
		this.sensor = sensor;
		this.bloque = bloque;
	}
	
	/* (non-Javadoc)
	 * @see lenguaje.Sent#act()
	 * 
	 * Acción semántica del repeat.
	 * Mientras no esté parada la sentencia y el sensor sea false
	 * se ejecutan repetidamente las acciones de su bloque de sentencias
	 */
	@Override
	public void act() {
		while (!stopped && !sensor.isTrue()) {
			bloque.run();
			//pause(Entorno.getInstancia().getEscenario().getRobotSpeed());			// ya hay pausas despues de cada movimiento (esto es innecesario ahora ...)

		} 
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Sent_Repeat [" + sensor + "]";
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Bloque getBloque() {
		return bloque;
	}

	public void setBloque(Bloque bloque) {
		this.bloque = bloque;
	}
	
	/* (non-Javadoc)
	 * @see lenguaje.Sent#wakeUp()
	 * 
	 * Reenvía el mensaje wakeUP a las sentencias de su bloque
	 */
	@Override
	public void wakeUp() {
		// TODO Auto-generated method stub
		for(Sent s: bloque.getSents()){
			s.wakeUp();
		}
	}

}
