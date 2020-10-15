package lenguaje;

import java.io.ObjectInputStream.GetField;
import java.util.Iterator;

/**
 * Programa lógico. 
 * Implementa la interfaz Runnable, ya que cada uno va a ser ejecutado como un hilo separado
 * 
 * @author Octavio
 */
public class Programa implements Runnable {
	
	private Start start;					// cada programa debe comenzar por un comando Start (NOT-USED de momento)
	private Bloque bloque;					// bloque de sentencias lógicas
	private int ID;							// el ID de un programa representa tb su posicion (indice) en la lista del ProgramContainer
	private int currentLevel = 0;			// determina el nivel actual de anidamiento siendo 0 el inicial
	
	private boolean stopped = false;		// indicador que determina si está detenido
	private boolean executed = false;		// indicador que determina si está en ejecución

	public Programa() {
		bloque = new Bloque();
	}
	
	public Programa(Start start) {
		this.setStart(start);
		bloque = new Bloque();
	}
	
	public Programa(Start start, Bloque bloque) {
		this.setStart(start);
		this.setBloque(bloque);
	}

	public Start getStart() {
		return start;
	}

	public void setStart(Start start) {
		this.start = start;
	}

	public Bloque getBloque() {
		return bloque;
	}

	public void setBloque(Bloque bloque) {
		this.bloque = bloque;
	}
    
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public boolean isEmpty() {
		return bloque == null;
	}

	public int getLevel() {
		return currentLevel;
	}

	/**
	 * Establece el nivel de anidamiento
	 * @param nivel el nivel de anidamiento
	 */
	public void setLevel(int nivel) {
		this.currentLevel = nivel;
		System.out.println("Nivel de anidamiento actual: " + currentLevel);
	}
	
	
	/**
	 * Obtiene el ultimo bloque de sentencias que se está construyendo
	 * en el nivel de anidamiento apropiado
	 * Se usa para añadir nuevas sentencias al final de los programas
	 * en el bloque anidado (o no) apropiado
	 * 
	 * NOTA: valorar la posibilidad de usar recursividad aqui
	 * NOTA2: es posible que esto se cambie en futuras extensiones para poder añadir sentencias
	 * a mitad de un programa (como en el caso de los sensores)
	 * @return el ultimo bloque de sentencias abierto en el nivel de anidamiento apropiado
	 */
	private Bloque getCurrentBloque(){
		//Sent s = bloque.getSents().get(currentLevel);
		Sent sent = null;
		boolean found = false;
		Bloque currentBloque = bloque;
		// va recorriendo para cada nivel las sentencias y
		// si existen sentencias con bloques abiertos (sentencias de Control no cerradas con comandos FIN)
		// actualiza el bloque, procediendo iterativamente hasta el nivel de anidamiento actual
		for(int i = 0; i < currentLevel; i++) {
			Iterator<Sent> it = currentBloque.getSents().iterator();
			found = false;
			while(it.hasNext() && !found){
				Sent nextSent = it.next();
				if(nextSent instanceof Sent_if && nextSent.isOpen()){
					sent = nextSent;
					currentBloque = ((Sent_if) sent).getBloque();
					found = true;
				} else if (nextSent instanceof Sent_repeat && nextSent.isOpen()) {
					sent = nextSent;
					currentBloque = ((Sent_repeat) sent).getBloque();
					found = true;
				} 
			}
		}
		return currentBloque;
	}
	
	/**
	 * Cierra el último bloque de sentencias abierto 
	 * en el nivel de anidamiento correspondiente
	 * 
	 * @see lenguaje.Programa#getCurrentBloque()
	 */
	public void closeCurrentBlock(){
		//Sent s = bloque.getSents().get(currentLevel);
		Sent sent = null;
		boolean found = false;
		Bloque currentBloque = bloque;
		// va recorriendo para cada nivel las sentencias y
		// si existen sentencias con bloques abiertos (sentencias de Control no cerradas con comandos FIN)
		// la cierra
		for(int i = 0; i <= currentLevel; i++) {
			Iterator<Sent> it = currentBloque.getSents().iterator();
			found = false;
			while(it.hasNext() && !found){
				Sent nextSent = it.next();
				if(nextSent instanceof Sent_if && nextSent.isOpen()){
					sent = nextSent;
					currentBloque = ((Sent_if) sent).getBloque();
					found = true;
				} else if (nextSent instanceof Sent_repeat && nextSent.isOpen()) {
					sent = nextSent;
					currentBloque = ((Sent_repeat) sent).getBloque();
					found = true;
				}
			}
		}
		sent.setOpen(false);
	}

	/**
	 * Añade una sentencia lógica (comando lógico)
	 * al final del programa en el bloque anidado correspondiente
	 * 
	 * NOTA: es posible que esto se cambie en futuras extensiones para poder añadir sentencias
	 * a mitad de un programa (como en el caso de los sensores)
	 * @param s
	 */
	public void addSent(Sent s){
		Bloque b = getCurrentBloque();
		b.addSent(s);
	}
	
	/**
	 * Envia un mensaje de wakeUP a todas las sentencias lógicas
	 */
	public void wakeUpSents(){
		// recorremos todas las sentencias del programa, y a cada uno de tipo wait, le hacemos un notify()
		// sent.notify()
		// hay que obtener el monitor del objeto (sent) antes -- synchronized
		for(Sent s: bloque.getSents()){
			s.wakeUp();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * Método principal de los programas
	 * que ejecuta el programa, ejecutando secuencial y recursivamente los bloques
	 * de sentencias contenidos en el
	 */
	public void run() {
		System.out.println("-------init program (" + ID + ")----------");
		bloque.run();
		System.out.println("--------end program (" + ID + ")---------");
	}
	
	
	/**
	 * Detiene la ejecución del programa, estableciendo cada
	 * sentencia a stopped
	 */
	public void stop(){
		stopped = true;
		bloque.setStopped(stopped);
		//bloque.printStopped();
	}
	
	/**
	 * Reanuda la ejecución (habilita) del programa
	 * estableciendo cada sentencia a !stopped
	 */
	public void resume() {
		stopped = false;
		bloque.setStopped(stopped);
		//run();
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

    
}
