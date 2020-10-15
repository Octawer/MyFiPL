package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import lenguaje.Move;

/**
 * Comando gráfico de movimiento
 * 
 * @author Octavio
 */
public class ComandoMove extends Comando {
	
	private Point directionVector;
	private JComboBox box;

	public ComandoMove(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoMove(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setBox();
		setDirection();
		setLogicalCom(new Move(directionVector));
		setToolTipText("Mueve al Robot una posición en la dirección indicada");
		setDownSlot(true);
	}
	
	/**
	 * Establece la dirección de movimiento (como vector unitario bidimiensional)
	 * en función de la descripción de las imágenes de las flechas
	 * incluidas en su comboBox
	 */
	private void setDirection(){
		// TODO Auto-generated constructor stub
		ImageIcon icon = (ImageIcon) box.getSelectedItem();
		String name = icon.getDescription();
		switch (name) {
		case "UpArrow.png":		// Up
			directionVector = new Point(0,-1);
			break;
		case "RightArrow.png":		// Right
			directionVector = new Point(1,0);
			break;
		case "DownArrow.png":		// Down
			directionVector = new Point(0,1);
			break;
		case "LeftArrow.png":		// Left
			directionVector = new Point(-1,0);
			break;
		default:
			// no hay mas posibilidades de descriptores
			directionVector = new Point(0,0);
			break;
		}
		System.out.println("Direccion = " + directionVector.toString());
	}
	
	protected void setBox() {
		ImageIcon[] icons = new ImageIcon[4];
    	for (int i = 0; i < icons.length; i++) {
    		ImageIcon icon = new ImageIcon("Imagenes\\Arrows\\" + arrows[i]);
    		icon.setDescription(arrows[i]);			// establecemos la descripción (numbre de archivo) para recuperar la dirección cuando sea necesario
			icons[i] = icon;
		}
    	box = new JComboBox<ImageIcon>();
		//box.setEditable(true);
		box.setModel(new javax.swing.DefaultComboBoxModel<ImageIcon>(icons));		// hay que proporcionarle un editor? (tutorial java)
    	box.setBounds(80,20,50,30);
    	box.addActionListener(this);
    	add(box);
	}
	
	/* (non-Javadoc)
	 * @see gui.Comando#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * cuando se selecciona una de las flechas de la lista desplegable del comboBox
	 * se establece la dirección vectorial del movimiento
	 * y se actualiza el comando lógico asociado
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setDirection();
		((Move) logicalCom).setDirection(directionVector);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
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

	public Point getDirectionVector() {
		return directionVector;
	}

	public void setDirectionVector(Point directionVector) {
		this.directionVector = directionVector;
	}


}
