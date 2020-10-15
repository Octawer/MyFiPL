package lenguaje;

import gui.Entorno;

import java.awt.Color;

/**
 * Sensor lógico de colores
 * 
 * @author Octavio
 */
public class ColorSensor extends Sensor {
	
	private Color color;			// color del sensor

	public ColorSensor() {
		color = new Color(0,0,0);
	}
	
	public ColorSensor(Color color) {
		this.color = color;
	}
	
	public ColorSensor(int r, int g, int b) {
		color = new Color(r,g,b);
	}

	/* (non-Javadoc)
	 * @see lenguaje.Sensor#isTrue()
	 * 
	 * método principal, que determina si el color del comando lógico es igual
	 * al color del sensor del P.Control
	 */
	public boolean isTrue() {
		return color.equals(Entorno.getInstancia().getPanelControl().getCurrentColor());
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public String toString() {
		return "Color(R,G,B): (" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
	}


}
