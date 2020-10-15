package gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * Comando gráfico de Inicio
 * No crea comando lógico, solo sirve como token de inicio
 * de los programas
 * 
 * @author Octavio
 */
public class ComandoStart extends Comando {

	public ComandoStart(int token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public ComandoStart(int token, String imgPath) {
		super(token, imgPath);
		// TODO Auto-generated constructor stub
		setToolTipText("Siempre debe ser el primer comando de un programa");
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
		return false;
	}

}
