package lenguaje;

import gui.Entorno;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Bloque de sentencias. 
 * Es una lista de sentencias con funcionalidades para gestionarlas
 * para ejecutarlas y detener su ejecución
 * 
 * @author Octavio
 */
public class Bloque {
	
	private List<Sent> sents;
	
	public Bloque() {
		sents = new ArrayList<Sent>();
	}
		
	public Bloque(List<Sent> sents) {
		super();
		this.setSents(sents);
	}

	public List<Sent> getSents() {
		return sents;
	}

	public void setSents(List<Sent> sents) {
		this.sents = sents;
	}
	
	public void addSent(Sent sent) {
		sents.add(sent);
	}

	/**
	 * Método de debug, que imprime la estructura sintáctica de los programas por consola 
	 */
	public void printProgram() {
		for (Sent sent : sents) {
			if(sent instanceof Sent_repeat) {
				System.out.println("--- init repeat ---");
				System.out.println(sent.toString());
				((Sent_repeat) sent).getBloque().printProgram();
				System.out.println("--- end repeat ---");
			} else if (sent instanceof Sent_if) {
				System.out.println("--- init if ---");
				System.out.println(sent.toString());
				((Sent_if) sent).getBloque().printProgram();		
				System.out.println("--- end if ---");
			} else {
				System.out.println(sent.toString());
			}
		}
		
	}
	
	
	/**
	 * Método de debug, que imprime la estructura sintáctica de los programas por consola 
	 * indicando que sentencias están paradas
	 */
	public void printStopped() {
		for (Sent sent : sents) {
			if(sent instanceof Sent_repeat) {
				System.out.println("--- init repeat ---");
				System.out.println(sent.toString() + " Stopped: " + sent.stopped);
				((Sent_repeat) sent).getBloque().printStopped();
				System.out.println("--- end repeat ---");
			} else if (sent instanceof Sent_if) {
				System.out.println("--- init if ---");
				System.out.println(sent.toString() + " Stopped: " + sent.stopped);
				((Sent_if) sent).getBloque().printStopped();		
				System.out.println("--- end if ---");
			} else {
				System.out.println(sent.toString() + " Stopped: " + sent.stopped);
			}
		}

	}
	
	/**
	 * Método principal del bloque, que ejecuta secuencial y recursivamente cada
	 * sentencia de la lista
	 */
	public void run(){
		for (Sent sent : sents) {
			if(sent instanceof Sent_repeat) {
				//sent.act();
				System.out.println("--- Init repeat ---");
				System.out.println(sent.toString());
				((Sent_repeat) sent).act();
				System.out.println("--- End repeat ---");
			} else if (sent instanceof Sent_if) {
				System.out.println("--- Init if ---");
				System.out.println(sent.toString());
				((Sent_if) sent).act();	
				System.out.println("--- End if ---");
			} else if (sent instanceof Sent_wait) {
				System.out.println(sent.toString());
				// Entorno.getInstancia().getEscenario().requestFocus();
				((Sent_wait) sent).act();	
				// System.out.println("--- end wait ---");
			} else {
				System.out.println(sent.toString());
				sent.act();
			}
		}
	}
	
	public boolean isEmpty() {
		return sents.isEmpty();
	}
	
	public void removeSent(Sent sent){
		sents.remove(sent);
	}

	/**
	 * Detiene la ejecución de los programas, estableciendo
	 * el atributo stopped de cada sentencia recursivamente (para las sentencias con bloques anidados)
	 * @param stopped el parámetro que determina si deben detenerse o no
	 */
	public void setStopped(boolean stopped) {
		for (Sent sent : sents) {
			if(stopped) {
				sent.stop();
				if(sent instanceof Sent_repeat) {
					((Sent_repeat) sent).getBloque().setStopped(true);
				} else if (sent instanceof Sent_if) {
					((Sent_if) sent).getBloque().setStopped(true);
				}
			} else {
				sent.resume();
				if(sent instanceof Sent_repeat) {
					((Sent_repeat) sent).getBloque().setStopped(false);
				} else if (sent instanceof Sent_if) {
					((Sent_if) sent).getBloque().setStopped(false);
				}
			}
		}
	}

	

}
