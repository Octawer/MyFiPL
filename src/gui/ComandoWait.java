package gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import lenguaje.Sent_wait;

/**
 * Comando gráfico de Esperar hasta que se cumpla una condición
 * 
 * @author Octavio
 */
public class ComandoWait extends Comando {

	public ComandoWait(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoWait(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setLogicalCom(new Sent_wait());
		setToolTipText("El programa se pone en espera hasta que se cumpla la condición");
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
