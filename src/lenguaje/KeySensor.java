package lenguaje;

import gui.Entorno;

/**
 * Sensor lógico de teclado
 * 
 * @author Octavio
 */
public class KeySensor extends Sensor {
	
	private Character sensorKey;			// la tecla del sensor

	public KeySensor() {
		sensorKey = null;
	}
	
	public KeySensor(char c){
		sensorKey = new Character(c);
	}

	/* (non-Javadoc)
	 * @see lenguaje.Sensor#isTrue()
	 * 
	 * método principal del comando lógico, que determina si su sensor de tecla
	 * coincide con el del Panel de Control
	 */
	@Override
	public boolean isTrue() {
		return sensorKey.charValue() == Entorno.getInstancia().getPanelControl().getLastKey();
	}

	public Character getSensorKey() {
		return sensorKey;
	}

	public void setSensorKey(Character sensorKey) {
		this.sensorKey = sensorKey;
	}
	
	public String toString(){ 
		return "Tecla: " + sensorKey.toString();
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
