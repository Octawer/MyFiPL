package lenguaje;

import gui.Entorno;

/**
 * Sentencia lógica de espera
 * 
 * @author Octavio
 */
public class Sent_wait extends Sent {
	
	private Sensor sensor;				// el sensor de la condición de la espera
	
	public Sent_wait() {
		super();
	}
	
	public Sent_wait(Sensor sensor){
		super();
		this.sensor = sensor;
	}
	
	/* (non-Javadoc)
	 * @see lenguaje.Sent#act()
	 * 
	 * Acción semántica de la espera.
	 * Si no está parado y si el sensor es false
	 * pone en espera al hilo del que forma parte (el programa al que pertenece)
	 */
	public void act() {
		if(!stopped){
			try {
				synchronized (this) {
					if(!sensor.isTrue()) {
						this.wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Sent_wait [" + sensor + "]";
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	
	/**
	 * NOT-USED
	 */
	private void checkSensor(){
		if(sensor.isTrue()){
			this.notify();
		}
	}

	/* (non-Javadoc)
	 * @see lenguaje.Sent#wakeUp()
	 * 
	 * Mensaje de wakeUP, mediante el cual, si el sensor es true
	 * lanza un evento notify() al hilo al cual pertenece
	 * despertando el programa 
	 */
	@Override
	public void wakeUp() {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(sensor.isTrue()){
				this.notify();
			}
		}
	}

}
