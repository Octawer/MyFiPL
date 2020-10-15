package gui;

import ghost.GhostComponentAdapter;
import ghost.GhostMotionAdapter;

import java.awt.Color;
import java.awt.Shape;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import utils.FactoriaComandos;




/**
 * Panel que contiene, dividido en pestañas por tipos, todos los comandos
 * gráficos que se pueden usar en el lenguaje de programación diseñado
 * 
 * @author Octavio
 */
public class PaletaComandos extends javax.swing.JTabbedPane {
	
	private javax.swing.JPanel paletaControlTab;		// pestaña de control
	private javax.swing.JPanel paletaMoveTab; 			// pestaña de movimiento
	private javax.swing.JPanel paletaSensorTab;			// pestaña de sensores
	
	// oyentes de eventos				
	private GhostMotionAdapter gma;
	private GhostComponentAdapter gca;
	
	private List<Comando> comandos;						// lista de comandos
	private int maxNumComandos = 11;					// POSIBILIDAD DE VARIAR EL NUMERO MAXIMO DE COMANDOS (USER-DEFINED, etc...)
	private Comando comActual;							// NOT-USED
	
	private String[] commandNames = {"Start.png","Repeat.png","Wait.png","IfThen.png","EndRepeat.png","EndIf.png","Color.png","Key.png","Move.png","SetPosition.png","SetSpeed.png"};
	private int[] commandTypes = {Comando.START,Comando.REPEAT,Comando.WAIT,Comando.IF,Comando.END_REPEAT,Comando.END_IF,Comando.COLOR,Comando.KEY,Comando.MOVE,Comando.SETPOSITION,Comando.SETSPEED};
	
	public PaletaComandos() {
		super();
	}
	
	public PaletaComandos(GhostMotionAdapter gma){
		super();
		//this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		setMotionAdapter(gma);

		initComandos();
		setLayout();
	}
	
	
	public PaletaComandos(GhostComponentAdapter componentAdapter, GhostMotionAdapter ghostMotionAdapter) {
		super();
		//this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		setComponentAdapter(componentAdapter);
		setMotionAdapter(ghostMotionAdapter);

		initComandos();
		setLayout();
	}

	
	/**
	 * Inicialización de los comandos de la paleta
	 * crea todos los comandos apoyándose en el método de factoría, y el los arrays de nombres y tipos de comandos
	 * cada vez que crea uno le registra los oyentes de click / release (GhostComponentAdapter) y drag (GhostMotionAdapter)
	 * y lo añade a la lista
	 */
	private void initComandos() {
		comandos = new ArrayList<Comando>(); 
        for (int i = 0; i < maxNumComandos; i++) {
			//Comando com = new Comando(commandTypes[i], "Imagenes\\Comandos\\" + commandNames[i]);
			Comando com = FactoriaComandos.createCom(commandTypes[i], "Imagenes\\Comandos\\" + commandNames[i]);
			com.addMouseListener(gca);
		    com.addMouseMotionListener(gma);
		    comandos.add(com);
		}    
	}
	
	
	/**
	 * Establece el layout de la paleta, diferenciando por pestañas los comandos
	 */
	private void setLayout() {
		 paletaMoveTab = new javax.swing.JPanel();
		 paletaControlTab = new javax.swing.JPanel();
		 paletaSensorTab = new javax.swing.JPanel();
		 Border border = javax.swing.BorderFactory.createRaisedBevelBorder();
		 paletaMoveTab.setBorder(border);
		 paletaControlTab.setBorder(border);
		 paletaSensorTab.setBorder(border);

		javax.swing.GroupLayout paletaControlTabLayout = new javax.swing.GroupLayout(paletaControlTab);
		paletaControlTab.setLayout(paletaControlTabLayout);
		paletaControlTabLayout.setHorizontalGroup(
				paletaControlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(paletaControlTabLayout.createSequentialGroup()
						.addGap(28, 28, 28)
						.addGroup(paletaControlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(comandos.get(5))
								.addComponent(comandos.get(4))
								.addComponent(comandos.get(3))
								.addComponent(comandos.get(2))
								.addComponent(comandos.get(1))
								.addComponent(comandos.get(0)))
								.addContainerGap(40, Short.MAX_VALUE))
				);
		paletaControlTabLayout.setVerticalGroup(
				paletaControlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(paletaControlTabLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(comandos.get(0))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(1))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(2))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(3))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(4))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(5))
						.addContainerGap(288, Short.MAX_VALUE))
				);

		addTab("Control", paletaControlTab);
		
		javax.swing.GroupLayout paletaMoveTabLayout = new javax.swing.GroupLayout(paletaMoveTab);
		paletaMoveTab.setLayout(paletaMoveTabLayout);
		paletaMoveTabLayout.setHorizontalGroup(
				paletaMoveTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(paletaMoveTabLayout.createSequentialGroup()
						.addGap(28, 28, 28)
						.addGroup(paletaMoveTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(comandos.get(10))
								.addComponent(comandos.get(9))
								.addComponent(comandos.get(8)))
								.addContainerGap(40, Short.MAX_VALUE))
				);
		paletaMoveTabLayout.setVerticalGroup(
				paletaMoveTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(paletaMoveTabLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(comandos.get(8))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(9))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(10))
						.addContainerGap(288, Short.MAX_VALUE))
				);

		addTab("Move", paletaMoveTab);

		javax.swing.GroupLayout paletaSensorTabLayout = new javax.swing.GroupLayout(paletaSensorTab);
		paletaSensorTab.setLayout(paletaSensorTabLayout);
		paletaSensorTabLayout.setHorizontalGroup(
				paletaSensorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(paletaSensorTabLayout.createSequentialGroup()
						.addGap(28, 28, 28)
						.addGroup(paletaSensorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(comandos.get(7))
								.addComponent(comandos.get(6)))
								.addContainerGap(40, Short.MAX_VALUE))
				);
		paletaSensorTabLayout.setVerticalGroup(
				paletaSensorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(paletaSensorTabLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(comandos.get(6))
						.addGap(18, 18, 18)
						.addComponent(comandos.get(7))
						.addContainerGap(288, Short.MAX_VALUE))
				);

		addTab("Sensor", paletaSensorTab);
	}

	public void setMotionAdapter(GhostMotionAdapter gma) {
		this.gma = gma;
	}

	public void setComponentAdapter(GhostComponentAdapter gca) {
		this.gca = gca;
	}

}
