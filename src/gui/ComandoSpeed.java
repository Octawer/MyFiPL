package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import lenguaje.SetSpeed;

/**
 * Comando gráfico de velocidad
 * 
 * @author Octavio
 */
public class ComandoSpeed extends Comando {
	
	private int speed;
	private NumberFormat speedFormat;
	private JFormattedTextField box;

	public ComandoSpeed(int token) {
		super(token);
	}

	public ComandoSpeed(int token, String imgPath) {
		super(token, imgPath);
		setSpeed(5);
		setBox();	
		setLogicalCom(new SetSpeed(speed));
		System.out.println("Speed = " + speed);
		setToolTipText("Modifica la velocidad de movimiento del Robot");
		setDownSlot(true);
	}
	
	protected void setBox() {
		speedFormat = NumberFormat.getNumberInstance();
		box = new JFormattedTextField(speedFormat);
		box.setValue(new Integer(speed));
		box.setColumns(2);
		box.addPropertyChangeListener("value", this);
    	box.setBounds(90,20,30,25);
    	add(box);
	}
	
	
    /* (non-Javadoc)
     * @see gui.Comando#propertyChange(java.beans.PropertyChangeEvent)
     * 
     * cuando el campo del box cambia se cambia el atributo speed dentro de los limites [0-10]
     * la comprobación de límites se delega en el comando lógico
     */
    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        if (source == box) {
            int inputSpeed = ((Number) box.getValue()).intValue();
            speed = inputSpeed;
            ((SetSpeed) logicalCom).setSpeed(speed);
        }
    }

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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
