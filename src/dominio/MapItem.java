package dominio;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * objetos que pueden contener los mapas cerrados, distintos del portal de salida
 * 
 * @author Octavio
 */
public class MapItem {

	private String defaultImgPath = "Imagenes\\Robots\\PFG-BluePortal.gif";
	private Point position = new Point(0,0);
	//private final Point initialPosition = new Point(200,200);
	
	private Image image;
	private Dimension itemSize;
	//private List<RobotListener> robotListeners;
	private Point pivot = new Point(0,0);			// pivote de la imagen del Robot (pixel central inferior) - CAMBIO DE COORDENADAS (para el paint del Escenario)
	
	
	
	public MapItem() {
		//robotListeners = new ArrayList<RobotListener>(); 
		setImage(defaultImgPath);
	}
	
	public MapItem(String imgPath){
		setImage(imgPath);
	}
	
	/*
	public void move(Point direction) {
		position.x += 1 * step * direction.x;
		position.y += 1 * step * direction.y;
		setPivot();
		publishRobotEvent();
	}*/
	
	
	public Point getPosition(){
		return position;
	}
	
	public void setPosition(Point p){
		// establecemos la posición para que sea el pixel sup-izq de cada tile
		// y le trasladamos a la mitad del tile
		// OJO: el comando setPosition ahora sera poco transparente (ya que si le indicas una posición le añade internamente +16x +16y
		// ver los posibles side effects
		position.x = p.x; 	// esto solo debería usarse para la pos.inicial (quizás)
		position.y = p.y;
		setPivot();
		//publishRobotEvent();
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(String img) {
		image = new ImageIcon(img).getImage();
		itemSize = new Dimension(image.getWidth(null), image.getHeight(null));
	}
	

	public Point getPivot() {
		return pivot;
	}

	private void setPivot() {
		// this.pivot = pivot;
		// CAMBIO DE COORDENADAS (para el paint del Escenario)
		// el drawImage de Graphics2D pinta desde la esquina sup-izq de la imagen,
		// de modo que hay que desplazarla en negativo para que la pinte con la sombra sobre la casilla del mapa (dentro del tile)
		this.pivot.x = position.x - (itemSize.width / 2);
		this.pivot.y = position.y - itemSize.height + (itemSize.height / 10);
	}

	public String getDefaultImgPath() {
		return defaultImgPath;
	}

}
