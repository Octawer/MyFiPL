package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import utils.DebugHelper;
import utils.ProgramExecutor;

import dominio.ProgramContainer;
import dominio.PropertyEvent;
import dominio.PropertyListener;
import dominio.SensorEvent;
import dominio.SensorEventListener;

import lenguaje.Programa;


/**
 * Panel donde se pueden colocar los comandos pegados entre sí para formar programas
 * que controlan el comportamiento del Robot
 * 
 * @author Octavio
 */
public class Escritorio extends javax.swing.JPanel implements PropertyListener, SensorEventListener {
	
	private ProgramContainer pc;				// contenedor de programas lógicos encargado de la gestión lógica
	private ProgramExecutor pExecutor;			// gestor de la ejecución de los hilos de los programas
	
	public Escritorio(){
		super();
		Border border = javax.swing.BorderFactory.createLoweredBevelBorder();
		setBorder(border);
		
        setLayout(null);
        setBackground(new Color(240, 240, 240));
        
        pc = new ProgramContainer();
        pExecutor = ProgramExecutor.getExecutor();
	}
	
	 /**
	 * Coloca el comando objetivo en un punto del Escritorio
	 * en función del cual, si hay mas comandos cerca se acoplará a aquel cuya intersección
	 * entre ellos (comando origen y comando destino) tenga el mayor área,
	 * o bien si el punto no tiene comandos candidatos de snap cerca, se colocará sin acoplar
	 * @param p las coordenadas donde se suelta el comando origen
	 * @param comando el comando origen, el cual queremos colocar en el Escritorio
	 */
	public void placeCommand(Point p, Comando comando) {
		Component[] labels = getComponents();
		if(labels.length > 0) {
			// comprobamos cual intersección tiene mayor area, para elegir al comando destino
			Comando target = getTargetCommand(comando);			
			if(target != null) {
				snapLabel(p, target, comando);
			} else {
				place(p, comando);
			}
		} else {
			place(p, comando);
		}
	}

	/**
	 * Si el comando origen se puede unir al comando destino se hace el snap entre ellos
	 * y si no, se coloca el comando origen en el punto del drop
	 * @param p el punto del drop
	 * @param targetLabel el comando destino
	 * @param sourceLabel el comando origen
	 */
	public void snapLabel(Point p, Comando targetLabel, Comando sourceLabel){
    	// si se solapan y sintácticamente es válida la unión, acoplamos los comandos
    	if(overlapping(targetLabel.getBounds(), sourceLabel.getBounds()) && sourceLabel.canSnap(targetLabel)){
    		snap(targetLabel, sourceLabel);
        // si no se solapan se deposita solo el nuevo, sin unirle a otros
    	} else {
    		place(p,sourceLabel);
    	}
    }
    
    /**
     * Se elimina el comando gráfico y lógico del Escritorio
     * @param com el comando a eliminar
     */
    public void removeCommand(Comando com) {
    	remove(com);
    	pc.removeCom(com);
    }

	/**
	 * Comprobación de solapamiento de dos comandos. Si si intersección es no nula
	 * se solaparán.
	 * @param target el rectángulo de una de las etiquetas (comandos) del
	 * escritorio
	 * @param source el rectángulo de la etiqueta que estamos comparando para
	 * colocarla
	 * @return true si se solapan
	 */
	private boolean overlapping(Rectangle target, Rectangle source) {	    
		return target.intersects(source);
	}

	/**
	 * Acción gráfica del snap. Si el comando origen no está ya acoplado a otros
	 * se muestra un mensaje visual de snap, se pone a snapped su atributo,
	 * se coloca en la posición del snapped en fc. de su forma (de su tipo de comando - Sensor y Resto)
	 * se añade al panel, se pinta en la GUI
	 * y se añade su comando lógico al programa lógico correspondiente
	 * 
	 * @param targetCom el comando destino
	 * @param sourceCom el comando origen
	 */
	private void snap(Comando targetCom, Comando sourceCom) {
		if(!sourceCom.isSnapped()) {
			DebugHelper.infoDialog(this, "Snap!");

			sourceCom.setSnapped(true);

			anchorCommand(targetCom, sourceCom);
			add(sourceCom);

			//sourceCom.setBorder(new EtchedBorder());
			sourceCom.setOpaque(false);

			revalidate();
			repaint();

			// añadimos el comando lógico
			//Comando com = (Comando) sourceCom;
			pc.appendComando(targetCom,sourceCom);
		}
    }
    
    /**
     * Acción gráfica de colocación en Escritorio (no snap). 
     * 
     * NOTA: el att. placed en Comando no sirve para nada de momento (ya que se crean nuevos en el GhostMoveEvent)
     * @param p
     * @param sourceCom
     */
	private void place(Point p, Comando sourceCom) {
		DebugHelper.infoDialog(this, "Placed!");

		// se obtienen las dimensiones del rectángulo y se establecen las nuevas
		// trasladadas medio alto y medio ancho en negativo para ajustar la colocación
		Dimension size = sourceCom.getPreferredSize();
		sourceCom.setBounds(p.x - size.width / 2, p.y - size.height / 2, size.width, size.height);

		// se añade al panel
		add(sourceCom);

		//sourceCom.setBorder(new EtchedBorder());
		sourceCom.setOpaque(false);

		// se pinta
		revalidate();
		repaint();

		// añadimos el comando lógico 
		pc.addComando(sourceCom);
	}
    
    
    /**
     * Anclaje gráfico del comando origen respecto del comando destino para el caso del snap.
     * Se coloca en diferente posición para simular un acoplamiento en función de si es
     * snap por la derecha (Sensor) o por abajo (Resto)
     * 
     * @param targetCom el comando destino del snap
     * @param sourceCom el comando que se quiere acoplar (origen del snap)
     */
    private void anchorCommand(Comando targetCom, Comando sourceCom) {
    	// target.getAnchorPoint() -> debe ser un Comando mas que un JLabel
    	int targetX = targetCom.getBounds().x;
    	int targetY = targetCom.getBounds().y;
    	int targetHeight = targetCom.getBounds().height;
    	int targetWidth = targetCom.getBounds().width;

    	// quizás se podría pensar en un método menos dependiente del tamaño de los comandos
    	// ya que este solo vale para el tamaño específico de las imágenes usadas para los comandos actualmente
    	if(sourceCom.getToken() == Comando.COLOR || sourceCom.getToken() == Comando.KEY) {
    		sourceCom.setBounds(targetX + targetWidth - targetWidth / 7, targetY + 7, targetWidth, targetHeight);
    	} else {
    		sourceCom.setBounds(targetX, targetY + targetHeight - targetHeight / 2, targetWidth, targetHeight);
    	}
    	
    }
    
    // POSIBLE EXTENSIÓN PARA SNAP INTERMEDIO / DELETE INDIVIDUALIZAD
    // quizás se puedan mover hacia abajo para posibilitar el snap entre medias de un programa
    // OJO: solo mover los del mismo programa (no todos los del Escritorio)
    // y solo para comandos NO-SENSORES
    // VER COMO SE PODRIA HACER ESTO
    private void moveDownCommands(){
    	
    }
    
    
    /**
     * El que tenga una area de intersección mayor con el comando que se desea acoplar gana
     * y se convierte en el comando objetivo del snap
     * @param source el comando origen, o comando que se desea acoplar a otro
     */
    private Comando getTargetCommand(Comando source) {
    	Component[] labels = getComponents();
    	Comando c = null;
    	int area = 0;
	    if(labels.length > 0) {
	    	for (Component com : labels) {
	    		if(com.getBounds().intersects(source.getBounds())){
	    			Rectangle r = com.getBounds().intersection(source.getBounds());
	    			int newArea = r.getSize().width * r.getSize().height;
	    			if(newArea > area){
	    				area = newArea;
	    				c = (Comando) com;
	    			}
	    		}
	    	}
	    	return c;
	    } else {
	    	// no existe ningun comando todavia en el escritorio
	    	return null;
	    }
    }
    
	
	/**
	 * Método de debug para imprimir todos los comandos gráficos del Escritorio
	 */
	public void printComponents() {
		Component[] labels = getComponents();
        for (Component label : labels) {
            System.out.println(label);
        }
	}

	public ProgramContainer getPc() {
		return pc;
	}

	public void setPc(ProgramContainer pc) {
		this.pc = pc;
	}

	/* (non-Javadoc)
	 * @see dominio.PropertyListener#onPropertyEvent(dominio.PropertyEvent)
	 * 
	 * Cuando oye un evento de propiedad de los botones del P.Control realiza las acciones pertinentes
	 */
	@Override
	public void onPropertyEvent(PropertyEvent pev) {
		// tipo 2: START presionado en P.Control
		int type = pev.getType();
		boolean stopped = pev.getOption();
		if(type == PropertyEvent.START) {
			for (Programa p : pc.getPrograms()) {
				// valorar el uso de la guarda "stopped" del P.Control, en vez del estado de cada programa p ... 
				if(!p.isStopped()) {
					DebugHelper.debugMessage("Programa (" + p.getID() + "): start button event received (init)");
					// p.run();
					pExecutor.execute(p);
				} 
			}

		// tipo 3: STOP presionado en P.Control
		} else if (type == PropertyEvent.STOP) {
			// debería poner TODOS o NINGUNO a Stop / Resume
			for (Programa p : pc.getPrograms()) {
				if(stopped) {
					DebugHelper.debugMessage("Programa (" + p.getID() + "): stop button event received");
					pExecutor.stop(p);
				} else {
					DebugHelper.debugMessage("Programa (" + p.getID() + "): stop button event received (resume)");
					pExecutor.resume(p);
					//publishStopResumeEvent(1);
				}
			}
		// tipo 4: RESET presionado en P.Control
		} else if (type == PropertyEvent.RESET) {
			if(pc.allPaused()) {
				pExecutor.reset();
			}
		// tipo 5: CLEAR presionado en P.Control
		} else if (type == PropertyEvent.CLEAR) {
			DebugHelper.infoDialog(this, "Borrando todos los programas");
			removeAll();
			repaint();
			pc.clearPrograms();
			// limpiar comandos gráficos
			// limpiar programas lógicos
			// limpiar comandos lógicos sueltos (no deberian existir en principio)
		}
	}

	/* (non-Javadoc)
	 * @see dominio.SensorEventListener#onSensorEvent(dominio.SensorEvent)
	 * 
	 * Cuando recibe un evento Sensor envía un mensaje de despertar a cada programa
	 * y cada uno de ellos comprueba si los sensores coinciden
	 * para despertar a los programas en caso afirmativo 
	 */
	@Override
	public void onSensorEvent(SensorEvent e) {
		// para cada programa del ProgramContainer notificarle del cambio del sensor (notify())
		// para despertar a los hilos que esperan
		// DebugHelper.debugMessage("Escritorio: Evento Sensor recibido, notificando a los programas para despertarles");
		if(e.getType() == SensorEvent.COLOR_SENSOR_EVENT || e.getType() == SensorEvent.KEY_SENSOR_EVENT) {
			for (Programa p : pc.getPrograms()) {
				// DebugHelper.debugMessage("Programa (" + p.getID() + ") evento sensor recibido: WAKING UP SENTS");
				p.wakeUpSents();
			}
		}
	}
	
}
