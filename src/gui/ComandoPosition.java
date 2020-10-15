package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import lenguaje.Move;
import lenguaje.SetPosition;

/**
 * Comando gráfico de posición
 * 
 * @author Octavio
 */
public class ComandoPosition extends Comando {
	
	private Point position;
	private NumberFormat positionFormat;
	private JFormattedTextField boxX, boxY;

	public ComandoPosition(int token) {
		super(token);
	}

	public ComandoPosition(int token, String imgPath) {
		super(token, imgPath);
		setPosition(new Point(0,0));
		setBox();
		setLogicalCom(new SetPosition(position));
		System.out.println("Position = " + position.toString());
		setToolTipText("Posiciona al Robot en las coordenadas indicadas");
		setDownSlot(true);
	}
	
	protected void setBox() {
		positionFormat = NumberFormat.getNumberInstance();
		boxX = new JFormattedTextField(positionFormat);
		boxX.setValue(position.getX());
		//boxX.setColumns(2);
		boxX.addPropertyChangeListener("value", this);
    	boxX.setBounds(70,22,31,24);
    	add(boxX);
    	
		boxY = new JFormattedTextField(positionFormat);
		boxY.setValue(position.getY());
		//boxY.setColumns(2);
		boxY.addPropertyChangeListener("value", this);
    	boxY.setBounds(100,22,31,24);
    	add(boxY);
	}
	
	
    /* (non-Javadoc)
     * @see gui.Comando#propertyChange(java.beans.PropertyChangeEvent)
     * 
     * Cuando el campo del box cambia se cambia el atributo posicion dentro de los limites (tamaño del fondo de escenario)
	 * en principio se cambia la comprobación de los límites en tiempo de ejecución mediante el Escenario
     */
    public void propertyChange(PropertyChangeEvent e) {
    	Object source = e.getSource();
        if (source == boxX) {
            int inputX = ((Number) boxX.getValue()).intValue();
            position.setLocation(inputX, position.getY());
            // establecemos la posicion del comando lógico asociado para que cambie en el programa logico
    		((SetPosition) logicalCom).setCoord(position);
        } else if (source == boxY) {
        	int inputY = ((Number) boxY.getValue()).intValue();
        	position.setLocation(position.getX(), inputY);
            // establecemos la posicion del comando lógico asociado para que cambie en el programa logico
            ((SetPosition) logicalCom).setCoord(position);
        }
        System.out.println("Position = " + position.toString());
    }

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canSnap(Comando target) {
		if(target instanceof ComandoColor || target instanceof ComandoKey){
			return false;
		} else {
			if(target.hasDownSlot()) {
				target.setDownSlot(false); 		// ver si es responsabilidad de este método el poner a false los slots libres del target
				return true;
			} else {
				return false;
			}
		}
	}


}
