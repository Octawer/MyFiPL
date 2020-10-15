package gui;

import ghost.GhostComponentAdapter;
import ghost.GhostMotionAdapter;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JSeparator;


/**
 * Clase que engloba los paneles del Escritorio y la Paleta de comandos
 * Realmente solo encapsula la lógica de creación de ambos, y se podría prescindir de ella,
 * usándoles por separado en el Entorno.
 * 
 * @author Octavio
 */
public class PanelProgramacion extends javax.swing.JPanel {
	
	// en esta solucion usa como atributos sus dos areas diferenciadas (dos JPanel)
	private Escritorio escritorio;
	private PaletaComandos paletaComandos;

	public PanelProgramacion(GhostMotionAdapter gma){
		escritorio = new Escritorio();
		paletaComandos = new PaletaComandos(gma);
	}
	
	public PanelProgramacion(GhostComponentAdapter componentAdapter, GhostMotionAdapter ghostMotionAdapter) {
		escritorio = new Escritorio();
		paletaComandos = new PaletaComandos(componentAdapter,ghostMotionAdapter);
	}

	public Escritorio getEscritorio() {
		return escritorio;
	}

	public void setEscritorio(Escritorio escritorio) {
		this.escritorio = escritorio;
	}

	public PaletaComandos getPaletaComandos() {
		return paletaComandos;
	}

	public void setPaletaComandos(PaletaComandos paletaComandos) {
		this.paletaComandos = paletaComandos;
	}

	
}
