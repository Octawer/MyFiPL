package lenguaje;

/**
 * clase genérica de Sentencia lógica
 * 
 * @author Octavio
 */
public abstract class Sent {
	
	protected boolean stopped = false;	// determina si está detenida
	protected boolean open;				// determina si tiene un bloque abierto, solo se usa para sentencias con bloques (Repeat e If)

	/**
	 * Acciones semánticas de las sentencias lógicas
	 */
	public abstract void act();
	
	@Override
	public abstract String toString();
	
	
	/**
	 * para despertar a una sentencia Wait
	 */
	public void wakeUp() {
		
	}			
	
	
	/**
	 * funcionalidad stop del botón stop del P.Control
	 */
	public void stop() {
		stopped = true;
	}
	
	/**
	 * funcionalidad resume del botón stop del P.Control
	 */
	public void resume(){
		stopped = false;
	}
	
	/**
	 * Retardo entre acciones, para permitir que las acciones en
	 * los bucles sean visibles para el usuario
	 * @param millis delay entre acciones
	 */
	protected void pause(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Determina si tiene un bloque abierto (para Repeat e If)
	 * @return true si tiene un bloque abierto
	 */
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	/**
	 * Añade un sensor lógico si se trata de una sentencia de Control
	 * @param s el sensor lógico
	 */
	public void addSensor(Sensor s){
		if(this instanceof Sent_repeat && ((Sent_repeat) this).getSensor() == null) {
			((Sent_repeat)this).setSensor(s);
		} else if(this instanceof Sent_if && ((Sent_if) this).getSensor() == null) {
			((Sent_if)this).setSensor(s);
		} else if(this instanceof Sent_wait && ((Sent_wait) this).getSensor() == null) {
			((Sent_wait)this).setSensor(s);
		}
	}

}
