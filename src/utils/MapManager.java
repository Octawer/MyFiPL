package utils;

import java.awt.Color;

import dominio.Map;

/**
 * Clase utilitaria usada para la creación y gestión de mapas - PTE EXTENSION
 * 
 * @author Octavio
 */
public class MapManager {
	
	public static String level1 = "Imagenes\\Maps\\PFG-level-1A.png";
	public static String level2 = "Imagenes\\Maps\\PFG-level-2A.png";
	public static String level3 = "Imagenes\\Maps\\PFG-level-3A.png";
	public static String level4 = "Imagenes\\Maps\\PFG-level-4A.png";
	
	public MapManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static Map createMap(String path) {
		return new Map(path);
	}
	
	public static Map createMap(int level){
		// los creamos por niveles (p.e)
		return null;
	}

}
