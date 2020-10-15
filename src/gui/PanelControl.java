package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import utils.DebugHelper;

import dominio.PropertyEvent;
import dominio.PropertyListener;
import dominio.SensorEvent;
import dominio.SensorEventListener;

/**
 * Panel desde donde se puede controlar la ejecución de los programas, y visualizar varios
 * indicadores con información diversa
 * 
 * @author Octavio
 */
public class PanelControl extends javax.swing.JPanel implements ActionListener, SensorEventListener {
	
	private JButton startButton, stopButton, resetButton, loadRobot, loadBackground, clearButton;			// botones
	private JFileChooser fc;																				
	private List<PropertyListener> propertyListeners;
	private JLabel currentColorLabel, lastKeyLabel, mouseColorLabel, mouseCoordsLabel;						// indicadores
	// ver si cambiamos la init impaciente
	private char lastKey = 'a';																				// ultima tecla	
	private Color currentColor = new Color(255,255,255);													// color actual
	
	// iconos de los botones e indicadores
	private final Icon stopIcon = new ImageIcon("Imagenes\\Icons\\stop_icon.png");
	private final Icon resumeIcon = new ImageIcon("Imagenes\\Icons\\resume_icon.png");
	private final Icon startIcon = new ImageIcon("Imagenes\\Icons\\start_icon.png");
	private final Icon resetIcon = new ImageIcon("Imagenes\\Icons\\reset_icon.png");
	private final Icon clearIcon = new ImageIcon("Imagenes\\Icons\\clear_icon.png");
	private final Icon loadRobotIcon = new ImageIcon("Imagenes\\Icons\\loadRobot_icon.png");
	private final Icon loadMapIcon = new ImageIcon("Imagenes\\Icons\\loadMap_icon.png");
	private boolean stoppedState = false;

	
	public PanelControl(){
		super();
		
		propertyListeners = new ArrayList<PropertyListener>(); 
		
		setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		setLayout(new GridLayout(0, 5, 0, 0));
		
		fc = new JFileChooser();	
		
		loadRobot = new javax.swing.JButton();
		loadRobot.setIcon(loadRobotIcon);
		loadRobot.setToolTipText("cargar imagen de Robot desde archivo");
		loadRobot.addActionListener(this);
		
		add(loadRobot);
		
		startButton = new javax.swing.JButton();
		startButton.setIcon(startIcon); // NOI18N
		startButton.setToolTipText("ejecutar programa");
		startButton.addActionListener(this);
		
		add(startButton);
		
		
		stopButton = new javax.swing.JButton();
		stopButton.setToolTipText("detener ejecución de programa");
		stopButton.setIcon(stopIcon); // NOI18N
		stopButton.addActionListener(this);
		
		add(stopButton);
		
		resetButton = new javax.swing.JButton();
		resetButton.setToolTipText("reiniciar posiciones iniciales");
		resetButton.setIcon(resetIcon); // NOI18N
		resetButton.addActionListener(this);
		
		add(resetButton);
		
		clearButton =new javax.swing.JButton();
		clearButton.setToolTipText("limpiar Escritorio");
		clearButton.setIcon(clearIcon); // NOI18N
		clearButton.addActionListener(this);
		
		add(clearButton);
		
		loadBackground = new javax.swing.JButton();
		loadBackground.setToolTipText("cargar imagen de escenario desde archivo");
		loadBackground.setIcon(loadMapIcon);
		loadBackground.addActionListener(this);
		
		add(loadBackground);
		
		lastKeyLabel = new JLabel("Tecla: " + lastKey);
		Border border = BorderFactory.createLoweredBevelBorder();
		//border.getBorderInsets(c)
		lastKeyLabel.setBorder(border);
		lastKeyLabel.setToolTipText("Ultima tecla presionada: " + lastKey);
		lastKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastKeyLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lastKeyLabel.setForeground(new Color(0, 0, 0));
		lastKeyLabel.setBackground(Color.LIGHT_GRAY);
		lastKeyLabel.setOpaque(true);
		
		add(lastKeyLabel);
		
		currentColorLabel = new JLabel("Robot");
		currentColorLabel.setBorder(border);
		currentColorLabel.setToolTipText("Color actual: (" + currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue() + ")");
		currentColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentColorLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentColorLabel.setBackground(currentColor);
		currentColorLabel.setOpaque(true);
		
		add(currentColorLabel);
		
		mouseCoordsLabel = new JLabel("Coords");
		mouseCoordsLabel.setBorder(border);
		mouseCoordsLabel.setToolTipText("Coords del ratón: (" + currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue() + ")");
		mouseCoordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mouseCoordsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		mouseCoordsLabel.setBackground(Color.LIGHT_GRAY);
		mouseCoordsLabel.setOpaque(true);
		
		add(mouseCoordsLabel);
		
		
		mouseColorLabel = new JLabel("Mouse");
		mouseColorLabel.setBorder(border);
		mouseColorLabel.setToolTipText("Color del ratón: (" + currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue() + ")");
		mouseColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mouseColorLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		mouseColorLabel.setBackground(currentColor);
		mouseColorLabel.setOpaque(true);
		
		add(mouseColorLabel);
	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * Cuando se pulsa alguno de los botones se realizan las acciones y se envían
	 * los eventos pertinentes, de acuerdo a cada uno de ellos
	 */
	public void actionPerformed(ActionEvent e) {
		 
		// acción de cargar el robot al presionar el boton loadRobot
        if (e.getSource() == loadRobot) {
            int returnVal = fc.showOpenDialog(this.getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File file = fc.getSelectedFile();
                String imgPath = file.getAbsolutePath();
				// debemos pasar la info del path de la imagen al escenario
            	publishPropertyEvent(new PropertyEvent(PropertyEvent.ROBOT,imgPath));
            	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
            } else {
            	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
            }
        // acción de cargar el fondo del escenario al presionar el boton loadBackground
        } else if (e.getSource() == loadBackground) {
            int returnVal = fc.showOpenDialog(this.getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File file = fc.getSelectedFile();
                String imgPath = file.getAbsolutePath();
            	// debemos pasar la info del path de la imagen al escenario
                publishPropertyEvent(new PropertyEvent(PropertyEvent.BACKGROUND,imgPath));
                Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
            } else {
            	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
            }
        } else if (e.getSource() == startButton) {
        	publishPropertyEvent(new PropertyEvent(PropertyEvent.START, "start pressed"));
        	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
        } else if (e.getSource() == stopButton) {
        	// cambiamos la imagen del boton de pause a play y viceversa,
        	// cambiamos el estado del panel de stopped a !stopped y viceversa
        	// y enviamos el mensaje de STOP al Escritorio
        	switchStopButton();
        	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
        } else if (e.getSource() == resetButton) {
        	publishPropertyEvent(new PropertyEvent(PropertyEvent.RESET, "reset pressed"));
        	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
        } else if (e.getSource() == clearButton) {
        	publishPropertyEvent(new PropertyEvent(PropertyEvent.CLEAR, "clear pressed"));
        	Entorno.getInstancia().getEscenario().requestFocusInWindow();		// despues de pulsar el boton transferimos el focus al Escenario
        }
    }
	
	public void addPropertyListener(PropertyListener pl) {
		propertyListeners.add(pl);
	}
	
	public void publishPropertyEvent(PropertyEvent pev) {
		for (PropertyListener pl : propertyListeners) {
			pl.onPropertyEvent(pev);
		}
	}

	private void setLastKey(char c){
		lastKey = c;
		lastKeyLabel.setText("Tecla: " + lastKey);
		lastKeyLabel.setToolTipText("Ultima tecla presionada: " + lastKey);
	}
	
	private void setCurrentColor(Color color){
		currentColor = color;
		currentColorLabel.setBackground(currentColor);
		currentColorLabel.setToolTipText("Color actual: (" + currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue() + ")");
	}
	
	/**
	 * Actualiza los sensores del mouse (posición + color)
	 * @param p la posición del mouse
	 * @param c el color del pixel en la posición del mouse
	 */
	private void setMouseSensors(Point p, Color c){
		mouseColorLabel.setBackground(c);
		mouseColorLabel.setToolTipText("Color en click: (" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
		mouseCoordsLabel.setText("(" + p.x + "," + p.y + ")");
		mouseCoordsLabel.setToolTipText("Coords del click: (" + p.x + "," + p.y + ")");
	}
	
	/**
	 * Cambia el estado del Panel de Control (de modo Stop a modo Resume y viceversa)
	 * cambia el icono del boton de Stop consecuentemente
	 * e informa al Escritorio del evento de parada
	 */
	private void switchStopButton(){
		if(!stoppedState) {
			stoppedState = true;
    		stopButton.setIcon(resumeIcon);
    	} else {
    		stoppedState = false;
    		stopButton.setIcon(stopIcon);
    	}
		PropertyEvent ev = new PropertyEvent(PropertyEvent.STOP, stoppedState);
		publishPropertyEvent(ev);
	}

	/* (non-Javadoc)
	 * @see dominio.SensorEventListener#onSensorEvent(dominio.SensorEvent)
	 * 
	 * Cuando oye un evento Sensor modifica sus indicadores en consecuencia
	 */
	@Override
	public void onSensorEvent(SensorEvent e) {
		int type = e.getType();
		switch (type) {
		case SensorEvent.COLOR_SENSOR_EVENT:
			if(e.getColor() != null){
				setCurrentColor(e.getColor());
			}
			break;
		case SensorEvent.KEY_SENSOR_EVENT:
			if(e.getKey() != 0){
				setLastKey(e.getKey());
			}
			break;
		case SensorEvent.COORDS_SENSOR_EVENT:
			if(e.getCoords() != null && e.getColor() != null){
				setMouseSensors(e.getCoords(), e.getColor());
			}
			break;
		// cuando el Robot hace un movimiento ilegal se cambia el estado a stopped y el icono del boton Stop
		case SensorEvent.STOP_SENSOR_EVENT:			
			switchStopButton();
			break;
		default:
			// no puede ocurrir, en principio
			DebugHelper.debugMessage("Evento Sensor de tipo NO VALIDO");
			break;
		}
		
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public char getLastKey() {
		return lastKey;
	}

	public boolean getStoppedState() {
		return stoppedState;
	}

	public void setStoppedState(boolean stoppedState) {
		this.stoppedState = stoppedState;
	}
	
	

}
