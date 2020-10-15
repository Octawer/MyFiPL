package lenguaje;

/**
 * clase abstracta que agrupa a los sensores lógicos
 * y que a su vez extiende la clase genérica Sentencia
 * 
 * @author Octavio
 */
public abstract class Sensor extends Sent {

	/**
	 * comprobaciones del sensor
	 * @return true si coincide con el sensor del P.Control
	 */
	public abstract boolean isTrue();
	
	public abstract String toString();

}
