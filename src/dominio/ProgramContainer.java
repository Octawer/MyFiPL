package dominio;

import gui.Comando;
import gui.ComandoColor;
import gui.ComandoIf;
import gui.ComandoKey;
import gui.ComandoMove;
import gui.ComandoPosition;
import gui.ComandoRepeat;
import gui.ComandoSpeed;
import gui.ComandoWait;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import utils.DebugHelper;

import lenguaje.*;

/**
 * Contenedor y gestor de programas lógicos
 * 
 * Por el principio de separacion Vista-Modelo,
 * se quita esta responsabilidad del objeto de la GUI "Escritorio" y
 * se delega en este objeto del dominio
 * 
 * @author Octavio
 */
public class ProgramContainer {

	private List<Programa> programs;
	//private int nextID;
	
	public ProgramContainer() {
		programs = new ArrayList<Programa>();
	}

	public List<Programa> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Programa> programs) {
		this.programs = programs;
	}
	
	public void addProgram(Programa p) {
		programs.add(p);
	}
	
	
	/**
	 * crea un programa si el comando lógico es START
	 * @param com el comando gráfico cuyo comando lógico debe sre START para crear un nuevo programa
	 */
	public void addComando(Comando com) {
		if(com.getToken() == Comando.START) {
			//crear nuevo programa con un start
			Programa p = new Programa(new Start());
			// añadimos la ID del siguiente indice de la lista de programas {0 .. size}
			int id = getNextId();
			com.setID(id);   
			p.setID(id);
			programs.add(p);
			DebugHelper.debugMessage("New Program created: ID = " + p.getID());
		} 
	}

	/**
	 * añade un comando lógico al programa cuya ID corresponda con la ID del comando objetivo
	 * @param targetCom el comando objetivo cuya ID nos determina el programa donde añadir el nuevo comando
	 * @param sourceCom el nuevo comando que queremos añadir al programa
	 */
	public void appendComando(Comando targetCom, Comando sourceCom) {
		// para añadir un comando (logico) a un programa tenemos que identificar
		// a que programa pertenece el comando target (gráfico) donde se hace el snap
		// de modo que tiene que existir algun tipo de relación entre comando gráfico y lógico ...
		// quizás algun tipo de ID de programa que compartan todos los comandos que pertenezcan a el
		int programID = targetCom.getID();
		Programa program = programs.get(programID);
		sourceCom.setID(programID);
		// añadimos el comando lógico según el tipo de comando del comando gráfico
		//program.setBloque(new Bloque());
		addLogicalCommand(program, sourceCom, targetCom);
		DebugHelper.debugMessage("New Com append to Program (" + programID + ")");
	}
	
	/**
	 * obtenemos el próximo ID de programa según el tamaño de la lista de programas
	 * @return el siguiente ID de programa
	 */
	private int getNextId() {
		return programs.size();
	}

	
	/**
	 * en este método se encapsula la lógica de creación de programas lógicos, añadiendo comandos lógicos según los casos,
	 * y construyendo de este modo los programas
	 * @param p el Programa actual
	 * @param sourceCom el comando origen que queremos añadir
	 * @param targetCom el comando destino del acoplamiento, usado solo para los casos Sensores, para identificar el comando de Control objetivo
	 */
	private void addLogicalCommand(Programa p, Comando sourceCom, Comando targetCom){
		Sent sent = null;
		switch (sourceCom.getToken()) {
		case Comando.SETPOSITION:
			sent = ((ComandoPosition) sourceCom).getLogicalCom();
			p.addSent(sent);
			break;
		case Comando.SETSPEED:
			sent = ((ComandoSpeed) sourceCom).getLogicalCom();
			p.addSent(sent);
			break;
		case Comando.MOVE:
			sent = ((ComandoMove) sourceCom).getLogicalCom();
			p.addSent(sent);
			break;
		case Comando.COLOR:
			Sensor sensorColor = (Sensor) ((ComandoColor) sourceCom).getLogicalCom();
			Sent targetSent = targetCom.getLogicalCom();
			// por las restricciones del acoplamiento del canSnap() el targetSent solo debería ser del tipo Sent_repeat / Sent_if / Sent_wait
			// internamente la clase Sent se encarga de verificar a que clase pertenece y si su sensor es nulo (addSensor())
			targetSent.addSensor(sensorColor);
			// p.addSensor(sensorColor);
			//sent = new ColorSensor(c.getRed(),c.getGreen(),c.getBlue());
			break;
		case Comando.KEY:
			Sensor sensorKey = (Sensor) ((ComandoKey) sourceCom).getLogicalCom();
			Sent targetSent1 = targetCom.getLogicalCom();
			// por las restricciones del acoplamiento del canSnap() el targetSent solo debería ser del tipo Sent_repeat / Sent_if / Sent_wait
			// internamente la clase Sent se encarga de verificar a que clase pertenece y si su sensor es nulo (addSensor())
			targetSent1.addSensor(sensorKey);
			// p.addSensor(sensorKey);
			//sent = new Key();
			break;
		case Comando.REPEAT:
			sent = ((ComandoRepeat) sourceCom).getLogicalCom();
			// la marcamos como sentencia con bloque abierto
			sent.setOpen(true);
			p.addSent(sent);
			// incrementamos el nivel de anidamiento
			p.setLevel(p.getLevel() + 1);
			//System.out.println("nivel de anidamiento: " + p.getLevel());
			break;
		case Comando.END_REPEAT:
			// obtenemos la ultima sentencia con bloque abierto y la cerramos
			p.closeCurrentBlock();
			// decrementamos el nivel de anidamiento
			p.setLevel(p.getLevel() - 1);
			//System.out.println("nivel de anidamiento: " + p.getLevel());
			break;
		case Comando.IF:
			sent = ((ComandoIf) sourceCom).getLogicalCom();
			// la marcamos como sentencia con bloque abierto
			sent.setOpen(true);
			p.addSent(sent);
			// incrementamos el nivel de anidamiento
			p.setLevel(p.getLevel() + 1);
			//System.out.println("nivel de anidamiento: " + p.getLevel());
			break;
		case Comando.END_IF:
			// obtenemos la ultima sentencia con bloque abierto y la cerramos
			p.closeCurrentBlock();
			// decrementamos el nivel de anidamiento
			p.setLevel(p.getLevel() - 1);
			//System.out.println("nivel de anidamiento: " + p.getLevel());
			break;
		case Comando.WAIT:
			sent = ((ComandoWait) sourceCom).getLogicalCom();
			p.addSent(sent);
			break;
		default:
			// no pueden ocurrir mas casos en principio
			break;
		}
		//p.addSent(sent);
	}
	
	/**
	 * determina si todos los programas están parados
	 * @return true si estan todos los programas parados
	 */
	public boolean allPaused(){
		boolean allpaused = true;
		for (Programa p : programs) {
			if(!p.isStopped()){
				allpaused = false;
			}
		}
		return allpaused;
	}
	
	public void setAllPaused(){	
	}

	/**
	 * elimina un comando lógico de un programa. DE MOMENTO NO SE USA
	 * @param com el comando a eliminar
	 */
	public void removeCom(Comando com) {
		// TODO Auto-generated method stub
		int id = com.getID();
		Programa program = programs.get(id);
		if(!program.isEmpty()) {
			Bloque b = program.getBloque();
			b.removeSent(null);					// ver como hacemos para quitar una sentencia lógica (la ultima? correspondencia con comando graf (id)? etc...)
		}
		
	}
	
	/**
	 * elimina todos los programas lógicos
	 */
	public void clearPrograms(){
		programs.clear();
	}
	
}
