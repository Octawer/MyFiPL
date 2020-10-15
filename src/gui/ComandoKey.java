package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import lenguaje.KeySensor;
import lenguaje.Sensor;

/**
 * Comando gráfico sensor de tecla
 * 
 * @author Octavio
 */
public class ComandoKey extends Comando {
	
	private char key;
	private JComboBox box;

	public ComandoKey(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoKey(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setBox();
		setKey();
		setLogicalCom(new KeySensor(key));
		setToolTipText("Indica una condición de teclado (la tecla que se deba pulsar)");
	}
	
	private void setKey(){
		// TODO Auto-generated constructor stub
		key = (char) box.getSelectedItem();
		System.out.println("Key = " + key);
	}
	
	protected void setBox() {
		box = new JComboBox<Character>();
		box.setEditable(true);
		box.setModel(new javax.swing.DefaultComboBoxModel<Character>(
				new Character[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'}));
    	box.setBounds(75,12,40,25);
    	box.addActionListener(this);
    	add(box);
	}
	
	/* (non-Javadoc)
	 * @see gui.Comando#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * cuando se selecciona una tecla del comboBox, se establece como tecla actual
	 * y se actualiza el atributo correspondiente de su comando lógico asociado
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setKey();
		((KeySensor) logicalCom).setSensorKey(key);
	}

	public char getKey() {
		return key;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canSnap(Comando target) {
		if(target instanceof ComandoRepeat || target instanceof ComandoWait || target instanceof ComandoIf){
			if(target.hasRightSlot()) {
				target.setRightSlot(false); 		// ver si es responsabilidad de este método el poner a false los slots libres del target
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
