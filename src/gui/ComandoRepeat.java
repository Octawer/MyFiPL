package gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import lenguaje.Sent_repeat;

/**
 * Comando gráfico de inicio de un Bloque Repeat
 * 
 * @author Octavio
 */
public class ComandoRepeat extends Comando {

	public ComandoRepeat(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoRepeat(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setLogicalCom(new Sent_repeat());
		setToolTipText("Repite continuamente y en orden los comandos hasta Fin Repetir, hasta que se cumpla la condición indicada");
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
