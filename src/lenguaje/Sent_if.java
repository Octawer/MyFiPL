package lenguaje;

/**
 * Sentencia condicional lógica
 * 
 * @author Octavio
 */
public class Sent_if extends Sent {
	
	private Sensor sensor;				// sensor de la condición
	private Bloque bloque;				// bloque del If
	
	
	public Sent_if() {
		super();
		bloque = new Bloque();
	}


	public Sent_if(Sensor sensor) {
		super();
		this.sensor = sensor;
		bloque = new Bloque();
	}


	public Sent_if(Sensor sensor, Bloque bloque) {
		super();
		this.sensor = sensor;
		this.bloque = bloque;
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
	 * @see lenguaje.Sent#act()
	 * 
	 * Accion semántica de la sentencia. 
	 * Si el sensor es true (y si no está parada la sentencia)
	 * se ejecuta su bloque de sentencias
	 */
	@Override
	public void act() {
		if(!stopped){
			// deberíamos comprobar que existe en Token "IF" ?
			// o sería responsabilidad del análisis sintáctico implementado en el snap gráfico ?
			
			if(sensor.isTrue()) {
				bloque.run();
			}
		}
		
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Sent_If [" + sensor + "]";
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
