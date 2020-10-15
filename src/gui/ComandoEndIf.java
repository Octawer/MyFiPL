package gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * Comando gráfico finalizador de un bloque If
 * 
 * @author Octavio
 */
public class ComandoEndIf extends Comando {

	public ComandoEndIf(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoEndIf(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setToolTipText("Marca el final de un bloque Si");
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
