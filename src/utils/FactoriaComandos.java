package utils;

import gui.*;

/**
 * Clase Factoría que encapsula la creación de Comandos
 * 
 * @author Octavio
 */
public class FactoriaComandos {
	
	private static FactoriaComandos instancia;				// ver si es necesario hacer un Singleton de esta factoria, o simplemente acceder con statics

	public FactoriaComandos() {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized FactoriaComandos getInstancia() {
		if (instancia == null) {
			instancia = new FactoriaComandos();
		}
		return instancia;
	}

	/**
	 * Método estático que encapsula la creación de los diferentes tipos de comando
	 * en función del tipo de comando y de la imagen del mismo
	 * 
	 * @param type el tipo de comando (enteros de la clase Comando)
	 * @param imagePath la ruta de la imagen del Comando
	 * @return el Comando del tipo adecuado
	 */
	public static Comando createCom(int type, String imagePath) {
		Comando com = null;
		switch (type) {
		case Comando.START:
			com = new ComandoStart(type, imagePath);
			break;
		case Comando.REPEAT:
			com = new ComandoRepeat(type, imagePath);
			break;
		case Comando.WAIT:
			com = new ComandoWait(type, imagePath);
			break;
		case Comando.IF:
			com = new ComandoIf(type, imagePath);
			break;
		case Comando.END_REPEAT:
			com = new ComandoEndRepeat(type, imagePath);
			break;
		case Comando.END_IF:
			com = new ComandoEndIf(type, imagePath);
			break;
		case Comando.COLOR:
			com = new ComandoColor(type, imagePath);
			break;
		case Comando.KEY:
			com = new ComandoKey(type, imagePath);
			break;
		case Comando.SETPOSITION:
			com = new ComandoPosition(type, imagePath);
			break;
		case Comando.SETSPEED:
			com = new ComandoSpeed(type, imagePath);
			break;
		case Comando.MOVE:
			com = new ComandoMove(type, imagePath);
			break;
		default:		
			// estan cubiertos todos los casos en principio (ver pruebas)
			// com = new Comando(type, imagePath);
			break;
		}
		// deberia encargarse de crear tb el comando lógico asociado a cada comando gráfico (atributo logicalCom)
		// esto por encapsulamiento está dentro de la lógica de creación (dentro del propio constructor del Comando)
		return com;
	}

}
