package utils;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Clase utilitaria, usada para imprimir mensajes por consola
 * y para crear mensajes de diálogo como indicadores visuales de diversos eventos
 * 
 * @author Octavio
 */
public class DebugHelper {
	
	// interruptores de los diferentes tipo de mensajes
	private static boolean debug = false;
	private static boolean info = false;
	private static boolean error = false;
	private static boolean dialog = false;
	private static boolean scene = true;

	
	public static void debugMessage(String msg){
		if(debug){
			System.out.println(msg);
		}
	}
	
	public static void infoMessage(String msg){
		if(info){
			System.out.println(msg);
		}
	}
	
	public static void errorMessage(String msg){
		if(error){
			System.out.println(msg);
		}
	}
	
	public static void infoDialog(Component parent, String msg) {
		if(dialog) {
			JOptionPane.showMessageDialog(parent, msg);
		}
	}
	
	public static void sceneDialog(Component parent, String msg) {
		if(scene) {
			JOptionPane.showMessageDialog(parent, msg);
		}
	}

}
