package gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * Comando gráfico finalizador de un bloque Repeat
 * 
 * @author Octavio
 */
public class ComandoEndRepeat extends Comando {

	public ComandoEndRepeat(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoEndRepeat(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setToolTipText("Marca en final de un bloque Repetir");
		setDownSlot(true);
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
		if(target instanceof ComandoColor || target instanceof ComandoKey || target instanceof ComandoStart){
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
