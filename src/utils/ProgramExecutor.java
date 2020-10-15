package utils;

import gui.Entorno;

import java.util.concurrent.Executor;

import lenguaje.Programa;

/**
 * gestor de ejecución de los hilos de cada programa
 * 
 * @author Octavio
 */
public class ProgramExecutor implements Executor {
	
	private static ProgramExecutor executor = new ProgramExecutor();

	private ProgramExecutor() {
		// TODO Auto-generated constructor stub
	}
	
	public static ProgramExecutor getExecutor(){
		return executor;
	}

	@Override
	public void execute(Runnable r) {
		// TODO Auto-generated method stub
		new Thread(r).start();
		if(r instanceof Programa) {
			((Programa) r).setExecuted(true);
		}
	}
	
	/**
	 * detiene la ejecución de un Programa
	 * @param r el programa a parar (instancia de Runnable)
	 */
	public void stop(Runnable r) {
		// ver una forma de finalizar los hilos
		if(r instanceof Programa) {
			((Programa) r).stop();
		}
		// ((Thread) r).interrupt();
	}
	
	/**
	 * reanuda un Programa
	 * @param r el programa a parar (instancia de Runnable)
	 */
	public void resume(Runnable r) {
		if(r instanceof Programa) {
			((Programa) r).resume();
		}
	}
	
	/**
	 * resetea el Robot
	 */
	public void reset(){
		Entorno.getInstancia().getEscenario().resetRobot();
	}


}
