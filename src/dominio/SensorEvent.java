package dominio;

import java.awt.Color;
import java.awt.Point;

/**
 * eventos Sensores
 * 
 * @author Octavio
 */
public class SensorEvent {
	
	private char key;
	private Color color;
	private Point coords;
	private int type;
	public static final int KEY_SENSOR_EVENT = 0;			// evento tecla presionada
	public static final int COLOR_SENSOR_EVENT = 1;			// evento color del Robot
	public static final int COORDS_SENSOR_EVENT = 2;		// evento de coordenadas del Mouse (posicion + color)
	public static final int STOP_SENSOR_EVENT = 3;			// evento de parada del Robot por limites de mapa (NO POR PULSACIÓN DEL BOTON STOP)

	public SensorEvent() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public SensorEvent(int type){
		this.setType(type);
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Point getCoords() {
		return coords;
	}

	public void setCoords(Point coords) {
		this.coords = coords;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
