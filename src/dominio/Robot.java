package dominio;

import gui.Escritorio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import lenguaje.Programa;

/**
 * el Robot cuyas acciones se van a programar y visualizar mediante la aplicación
 * 
 * @author Octavio
 */
public class Robot {

	private String defaultImgPath = "Imagenes\\Robots\\PFG-Beholder.gif";
	private Point position = new Point(0,0);
	
	private int speed;								// speed se ha cambiado para que reduzca el delay de las acciones en vez aumentar el desplazamiento en pixels
	private Image image;
	private Dimension robotSize;
	private List<RobotListener> robotListeners;     // los oyentes de los eventos del Robot
	private Point pivot = new Point(0,0);			// pivote de la imagen del Robot (pixel central inferior) - CAMBIO DE COORDENADAS (para el paint del Escenario)
	private int step;								// numero de pixels que debe avanzar el robot con cada paso (suponemos tiles de 32 px)
	
	
	public Robot() {
		robotListeners = new ArrayList<RobotListener>(); 
		setImage(defaultImgPath);
		//setPosition(initialPosition);
		//setPivot(position);
		speed = 500;
	}
	
	/**
	 * mueve al Robot 1 posición en la dirección indicada
	 * @param direction la dirección del movimiento
	 */
	public void move(Point direction) {
		position.x += 1 * step * direction.x;
		position.y += 1 * step * direction.y;
		// se establece la posición para pintarlo correctamente en pantalla
		setPivot();
		// se lanza un evento de Robot
		publishRobotEvent();
	}
	
	
	public Point getPosition(){
		return position;
	}
	
	/**
	 * establece la posición del Robot en un punto p
	 * @param p las coordenadas donde se posiciona al Robot
	 */
	public void setPosition(Point p){
		position.x = p.x; 
		position.y = p.y;
		// se establece la posición para pintarlo correctamente en pantalla
		setPivot();
		// se lanza un evento de Robot
		publishRobotEvent();
	}
	
	public Image getImage() {
		return image;
	}

	/**
	 * se establece la imagen y el tamaño del Robot a partir de una ruta
	 * @param img la ruta de la imagen
	 */
	public void setImage(String img) {
		image = new ImageIcon(img).getImage();
		robotSize = new Dimension(image.getWidth(null), image.getHeight(null));
	}
	

	public int getSpeed() {
		return speed;
	}

	/**
	 * se establece la velocidad del Robot
	 * (En realidad el parámetro speed representa el delay de las acciones del Robot)
	 * @param speed la velocidad del robot (el delay)
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
		// se envía un evento de Robot a todos los oyentes registrados
		publishRobotEvent();
	}
	
	/**
	 * registra oyentes del Robot
	 * @param rl el oyente a registrar
	 */
	public void addRobotListener(RobotListener rl) {
		robotListeners.add(rl);
	}
	
	/**
	 * envía un evento de Robot a todos los oyentes registrados
	 */
	public void publishRobotEvent() {
		for (RobotListener rl : robotListeners) {
			rl.onRobotEvent();
		}
	}

	public Point getPivot() {
		return pivot;
	}

	/**
	 * establece el punto correcto para pintar el Robot en pantalla
	 */
	private void setPivot() {
		// this.pivot = pivot;
		// CAMBIO DE COORDENADAS (para el paint del Escenario)
		// el drawImage de Graphics2D pinta desde la esquina sup-izq de la imagen,
		// de modo que hay que desplazarla en negativo para que la pinte con la sombra sobre la casilla del mapa (dentro del tile)
		this.pivot.x = position.x - (robotSize.width / 2);
		this.pivot.y = position.y - robotSize.height + (robotSize.height / 10);
	}
	
	public int getStep(){
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getDefaultImgPath() {
		return defaultImgPath;
	}
	

}
