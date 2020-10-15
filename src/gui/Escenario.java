package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.Border;

import utils.DebugHelper;
import utils.MapManager;

import dominio.Map;
import dominio.MapItem;
import dominio.PropertyEvent;
import dominio.PropertyListener;
import dominio.Robot;
import dominio.RobotListener;
import dominio.SensorEvent;
import dominio.SensorEventListener;

/**
 * Panel donde se visualizan las acciones del Robot sobre el mapa actual
 * 
 * @author Octavio
 */
public class Escenario extends javax.swing.JPanel implements PropertyListener, RobotListener {
	
	private Robot robot;										// el Robot del Escenario
	private String fondo = "Imagenes\\Maps\\PFG-Void.png";		// mapa inicial por defecto
	private Color positionColor;								// color en la posición del Robot
	private List<SensorEventListener> sensorEventListeners;		// oyentes de eventos sensores
	private List<PropertyListener> propertyListeners;			// oyentes de eventos de acciones y propiedades de botones del P.Control
	private final int tileSize = 32;							// tamaño de un tile en pixels
	private Map map;											// mapa actual del Escenario
	
	// quizás seria mejor (desde el p.vista de la cohesión) que el Escenario tuviera un att al estilo del programContainer para gestionar la mecánica NO-GRAFICA ?

	public Escenario() {
		super();
		Border border = javax.swing.BorderFactory.createLoweredBevelBorder();
		setBorder(border);

		 // registramos el oyente de sensor de teclado
		 addKeyListener(new TAdapter());
		 setFocusable(true);
		 
		 // registramos el oyente de sensores de ratón
		 addMouseListener(new MouseOverAdapter());
		 
		 // creamos el mapa
		 map = MapManager.createMap(fondo);
		 // ajustamos el tamaño del panel segun el mapa
		 setBackgroundImageSize();
		 
		 // creamos e inicializamos el Robot
		 robot = new Robot();
		 robot.setStep(tileSize);
		 robot.setPosition(snapPosition(map.getInitialPosition()));
		 robot.addRobotListener(this);
		 
		 sensorEventListeners = new ArrayList<SensorEventListener>();
		 propertyListeners = new ArrayList<PropertyListener>(); 
		 
		 setRobotPositionColor();
	}


	/**
	 * Movemos el Robot en la dirección indicada si el movimiento es legal, 
	 * es decir, si el siguiente paso le lleva a una casilla válida
	 * En caso contrario, se muestra una animación de explosión, un mensaje
	 * de movimiento ilegal, se paran todos los programas
	 * y se resetea el Robot
	 * @param direction la dirección del movimiento
	 */
	public void moveRobot(Point direction){
		if(legalMovement(direction)) {
			Point currentPos = robot.getPosition();		// Ojo con la verificacion de los limites del mapa (insideMap(nextPivot)) 
			int nextX = currentPos.x + (1 * robot.getStep() * direction.x);
			int nextY = currentPos.y + (1 * robot.getStep() * direction.y);
			Point nextPos = new Point(nextX, nextY);
			int color = ((BufferedImage) map.getNegativeMap()).getRGB(nextPos.x, nextPos.y);
			Color tileColor = new Color(color);
			// si llegamos a la salida cambiamos el gif al warp
			// y mostramos un mensaje de felicitación
			if(tileColor.equals(Color.GREEN)) {
				SensorEvent se = new SensorEvent(SensorEvent.STOP_SENSOR_EVENT);
				publishSensorEvent(se);
				// guardamos el path de la imagen anterior
				String prevImg = robot.getDefaultImgPath();
				// cambiar a gif de warp para simular la llegada al portal
				setRobotImage("Imagenes\\Robots\\PFG-LoopWarp.gif");
				DebugHelper.sceneDialog(this, "Enhorabuena!!!");
				DebugHelper.debugMessage("Warp finished");
				// restauramos la imagen original y volvemos a la posicion inicial
				publishPropertyEvent(new PropertyEvent(PropertyEvent.ROBOT, prevImg));		// mensaje a si mismo para pintar el robot original en la pos inicial
				DebugHelper.debugMessage("Resettle finished");
			} else {
				robot.move(direction);
			}
		} else {
			// comprobamos que el Panel de Control no esté en estado stopped, para evitar cambiar de estado
			// varias veces en movimientos mútliples (cuando haya varios programas corriendo a la vez)
			if(Entorno.getInstancia().getPanelControl().getStoppedState() == false) {
				// si el robot se topa con una pared o algun obstáculo se para y vuelve al inicio (como si se pulsara el Stop del P.Control)
				// avisamos al P.Control para que cambie el icono del Stop y envie un mensaje de Stop al Escritorio
				SensorEvent se = new SensorEvent(SensorEvent.STOP_SENSOR_EVENT);
				publishSensorEvent(se);
				// guardamos el path de la imagen anterior
				String prevImg = robot.getDefaultImgPath();
				// cambiar a gif de explosión para simular la muerte del Robot
				setRobotImage("Imagenes\\Robots\\PFG-LoopExplosion.gif");		// si usamos la explosion simple (sin loop) solo ocurre una vez aunque se cargue varias ...
				DebugHelper.sceneDialog(this, "Movimiento Ilegal");
				DebugHelper.debugMessage("Explosion finished");
				// restauramos la imagen original y volvemos a la posicion inicial
				publishPropertyEvent(new PropertyEvent(PropertyEvent.ROBOT, prevImg));		// mensaje a si mismo para pintar el robot original en la pos inicial
				DebugHelper.debugMessage("Resettle finished");
			}
		}
	}
	
	/**
	 * Comprueba si el siguiente movimiento es válido
	 * @param direction la dirección del movimiento
	 * @return true si el siguiente movimiento es legal
	 */
	private boolean legalMovement(Point direction) {
		Point currentPos = robot.getPosition();		// Ojo con la verificacion de los limites del mapa (insideMap(nextPivot)) 
		int nextX = currentPos.x + (1 * robot.getStep() * direction.x);
		int nextY = currentPos.y + (1 * robot.getStep() * direction.y);
		Point nextPos = new Point(nextX, nextY);
		int color = ((BufferedImage) map.getNegativeMap()).getRGB(nextPos.x, nextPos.y);
		Color nextColor = new Color(color);
		
		DebugHelper.debugMessage("Next Color = " + nextColor);
		DebugHelper.debugMessage("Next Position = " + nextPos);
		DebugHelper.debugMessage("Bounds = " + map.getSize());
		
		// si esta dentro de los limites del mapa y dentro del camino valido (no negro) del negativeMap --> OK
		return insideMap(nextPos) && validTile(nextColor) ;
	}
	
	/**
	 * Comprueba si el punto esta dentro de los límites del mapa
	 * @param next el punto a comprobar
	 * @return true si está dentro de los límites
	 */
	private boolean insideMap(Point next){
		// dejamos un margen de un tile (32x32) en el borde del escenario
		if(next.x >= tileSize && next.x < map.getSize().width - tileSize && next.y >= tileSize && next.y < map.getSize().height - tileSize) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Comprobamos si el color de una casilla,
	 * (de un pixel en realidad, pero toda la casilla comparte color en mapas cerrados),
	 * es un color válido
	 * @param color el color a comprobar
	 * @return true si es un color válido
	 */
	private boolean validTile(Color color){
		if(color.equals(Color.BLACK)){
			return false;
		} else {
			return true;
		}
	}

	
	/**
	 * establecemos la posición para que sea el pixel sup-izq de cada tile 
	 * y le trasladamos a la mitad del tile
	 * quizás no sea necesario este método privado, pero se mantiene por si es necesaria
	 * la posibilidad de posicionarle libremente en cualquier posicion del mapa
	 * @param p el punto a ajustar a la mitad del tile correspondiente
	 * @return el punto trasladado
	 */
	private Point snapPosition(Point p){
		
		Point snapPoint = new Point(0,0);
		// acercamos el Robot al punto inicial del tile donde haya caido (esquina sup-izq del tile)
		snapPoint.x = p.x - p.x % tileSize;
		snapPoint.y = p.y - p.y % tileSize;
		// y lo movemos al centro del tile (+16x, +16y)
		snapPoint.x += tileSize / 2;
		snapPoint.y += tileSize / 2;
		
		DebugHelper.infoMessage("snapped to tile midPoint: " + snapPoint);
		
		return snapPoint;
	}
	
	/**
	 * Establecemos la posición del Robot si es un punto válido
	 * @param p el punto de la nueva posición
	 */
	public void setRobotPosition(Point p) {
		// comprobamos que está dentro de los límites
		if(insideMap(p)) {
			// si está dentro del mapa, comprobamos si cae en un tile válido
			int color = ((BufferedImage) map.getNegativeMap()).getRGB(p.x, p.y);
			Color posColor = new Color(color);
			if(validTile(posColor)){
				// si esta dentro de un tile válido, lo posicionamos en el centro
				robot.setPosition(snapPosition(p));
			} else {
				DebugHelper.sceneDialog(this, "Posición no válida: " + p);	
			}
		} else {
			DebugHelper.sceneDialog(this, "Fuera de Mapa:" + map.getSize());
		}
	}


	public void setRobotSpeed(int speed){
		robot.setSpeed(speed);
	}
	
	public int getRobotSpeed() {
		return robot.getSpeed();
	}
	
	/**
	 * establece la imagen del Robot y la pinta en pantalla
	 * @param imgPath la ruta de la imagen
	 */
	public void setRobotImage(String imgPath) {
		// se establece la nueva imagen del robot
		robot.setImage(imgPath);
		// se pintan en pantalla los nuevos graficos
		revalidate();
		repaint();
	}


	/**
	 * Coloca al robot en la posición inicial
	 * NOTA: quizás se puedan resetear otros atributos del Robot, aparte de la posición
	 */
	public void resetRobot(){
		robot.setPosition(snapPosition(map.getInitialPosition()));
		
		DebugHelper.debugMessage("Position of robot: " + robot.getPosition());
		DebugHelper.debugMessage("Initial Position of robot: " + map.getInitialPosition());
	}
	
	/**
	 * Ajusta el panel en función del tamaño del mapa actual
	 */
	private void setBackgroundImageSize() {
		//background = img;
		Dimension size = map.getSize();
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		// System.out.println("Background bounds = " + size);
		setLayout(null);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 * 
	 * Pinta en el Escenario el mapa, el robot, y el portal de salida (y otros MapItems)
	 * en caso de que sean mapas cerrados
	 */
	public void paint(Graphics g) {
		 super.paint(g);

		 Graphics2D g2d = (Graphics2D)g;
		 
		 if(map.isOpenMap()) {
			 g2d.drawImage(map.getBackground(), 0, 0, this);        
			 g2d.drawImage(robot.getImage(), robot.getPivot().x, robot.getPivot().y, this);
		 } else {
			 g2d.drawImage(map.getBackground(), 0, 0, this);        
			 g2d.drawImage(robot.getImage(), robot.getPivot().x, robot.getPivot().y, this);
			 g2d.drawImage(map.getPortal().getImage(), map.getPortal().getPivot().x + 16, map.getPortal().getPivot().y + 16, this);		// hay que mejorar el snapPosition
		 }
		 Toolkit.getDefaultToolkit().sync();
		 g.dispose();
	}
	
	
	/**
	 * Establece el color de la posición actual del Robot cuando recibe un evento de Robot
	 * y publica un evento sensor de color, para que el Panel de Control actualice su indicador
	 */
	private void setRobotPositionColor(){
		int color = ((BufferedImage) map.getBackground()).getRGB(robot.getPosition().x + tileSize / 2, robot.getPosition().y + tileSize / 2);
		positionColor = new Color(color);
		SensorEvent e = new SensorEvent(SensorEvent.COLOR_SENSOR_EVENT);
		e.setColor(positionColor);
		publishSensorEvent(e);
	}

	public void addSensorEventListener(SensorEventListener sel) {
		sensorEventListeners.add(sel);
	}

	public void publishSensorEvent(SensorEvent e) {
		for (SensorEventListener sel : sensorEventListeners) {
			sel.onSensorEvent(e);
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

	/* (non-Javadoc)
	 * @see dominio.PropertyListener#onPropertyEvent(dominio.PropertyEvent)
	 * 
	 * Cuando recibe un evento de propiedad del P.Control de carga de Robot o Mapa
	 * actualiza el Robot o el Mapa consecuentemente
	 */
	@Override
	public void onPropertyEvent(PropertyEvent pev) {
		int type = pev.getType();
		String valor = pev.getValor();
		if(type == PropertyEvent.BACKGROUND) {
			// si se pulsa el boton de cargar escenario del P.Control se establece un nuevo fondo
			map = MapManager.createMap(valor);
			setBackgroundImageSize();
			robot.setPosition(snapPosition(map.getInitialPosition()));
			
			// se pintan en pantalla los nuevos graficos
			revalidate();
			repaint();
		} else if (type == PropertyEvent.ROBOT) {
			// si se pulsa el boton de cargar robot del P.Control se establece una nueva imagen de robot
			robot.setImage(valor);
			// se reposiciona el robot (ya que puede variar el tamaño de la imagen -- ver si establecemos restricciones a esto)
			robot.setPosition(snapPosition(map.getInitialPosition()));
			// se pintan en pantalla los nuevos graficos
			revalidate();
			repaint();
		} 
	}


	/* (non-Javadoc)
	 * @see dominio.RobotListener#onRobotEvent()
	 * 
	 * Cuando le llega un evento de Robot (cambio de posición)
	 * establece el nuevo color actual y actualiza los gráficos
	 */
	@Override
	public void onRobotEvent() {
		setRobotPositionColor();
		DebugHelper.infoMessage("Pivote: " + robot.getPivot());
		revalidate();
		repaint();
	}
	
	
	/**
	 * @author Octavio
	 * clase para gestionar el movimiento del cursor dentro del escenario
	 */
	private class MouseOverAdapter extends MouseAdapter {
		
	    
	    /* (non-Javadoc)
	     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	     * 
	     * Cuando recibe un click en el Escenario, 
	     * obtiene las coordenadas y el color de ese pixel
	     * y envía un evento Sensor para que se actualice el P.Control
	     */
	    public void mouseClicked(MouseEvent e) {
	    	// pipeta para el colorChooser del comandoColor
	    	int color = ((BufferedImage) map.getBackground()).getRGB(e.getX(), e.getY());
	    	Color sceneColor = new Color(color);
	    	DebugHelper.infoMessage("Mouse clicked on Scene: color = " + sceneColor);
	    	DebugHelper.infoMessage("Mouse on scene: coords = (" + e.getX() + "," + e.getY() + ")");
	    	SensorEvent se = new SensorEvent(SensorEvent.COORDS_SENSOR_EVENT);
	    	se.setColor(sceneColor);
	    	se.setCoords(new Point(e.getX(), e.getY()));
	    	publishSensorEvent(se);
	    }
	}
	
	/**
	 * @author Octavio
	 * clase para gestionar los eventos de teclado en el Escenario
	 */
	private class TAdapter extends KeyAdapter {
	
		/* (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
		 * 
		 * cuando se pulsa una tecla (y este panel tiene el focus) 
		 * se envía un evento de Sensor al P.Control
		 */
		public void keyPressed(KeyEvent e){
			SensorEvent se = new SensorEvent(SensorEvent.KEY_SENSOR_EVENT);
			se.setKey(e.getKeyChar());
			publishSensorEvent(se);
		}
	}

}
