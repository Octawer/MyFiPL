package gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import lenguaje.Sent_if;

/**
 * Comando gráfico condicional
 * 
 * @author Octavio
 */
public class ComandoIf extends Comando {

	public ComandoIf(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoIf(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setLogicalCom(new Sent_if());
		setToolTipText("Se ejecutan una vez y en orden todos los comandos hasta Fin Si, si se cumple la condición");
		setDownSlot(true);
		setRightSlot(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

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

}
