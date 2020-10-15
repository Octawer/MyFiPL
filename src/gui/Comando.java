package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import lenguaje.Sent;

/**
 * cada uno de los Comandos que representan los tokens del lenguaje de programación gráfico
 * Esta representado por un JPanel con una imagen de fondo y opcionalmente 
 * un ComboBox o JTextField para los comandos editables
 * 
 * @author Octavio
 */
public abstract class Comando extends JPanel implements ActionListener, PropertyChangeListener {
	
	// tipo de comando, enum?,  { Start, If, Repeat, Wait, Color, Key, SetPosition, SetSpeed, Move }
	// elemento de gui (extension de jlabel ?? o jLabel directamente ??)
		// diferentes formas de labels
	
	
	// en principio se extenderá esta clase con los tipos de comandos donde cada uno 
	// definirá su método act según la semántica del lenguaje
	public final static int START = 0;
	public final static int IF = 1;
	public final static int REPEAT = 2;
	public final static int WAIT = 3;
	public final static int COLOR = 4;
	public final static int KEY = 5;
	public final static int SETPOSITION = 6;
	public final static int SETSPEED = 7;
	public final static int MOVE = 8;
	public final static int END_IF = 9;
	public final static int END_REPEAT = 10;
	
	
	protected String[] arrows = {"UpArrow.png", "RightArrow.png","DownArrow.png","LeftArrow.png"};
	protected String[] colors = {"Red.png","Green.png","Blue.png"};
	
	// por ejemplo ...
	protected int token;			// tipo de comando
	protected Point anchorPoint;	// punto de anclaje para el snap
	
	protected String imgPath;		// ruta de la imagen
	protected Image image;			// imagen del comando
	//protected JComponent box;
	
	protected boolean snapped;		// marcador que indica si esta acoplado a otros en el Escritorio
	protected boolean placed;		// marcador que indica si esta colocado en el Escritorio
	protected boolean downSlot;		// determina si el comando está libre en su hueco inferior para el snap
	protected boolean rightSlot;	// determina si el comando está libre en su hueco derecho para el snap
	
	protected int ID;				// ID de comando para relacionarle con un programa
	
	protected Sent logicalCom;		// comando lógico asociado a cada comando gráfico
	
	
	public Comando(int token) {
		super();
		this.token = token;
		setLayout(null);
	}
	
	public Comando(int token, String imgPath) {
		this(token);
		setImagePath(imgPath);
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public Point getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(Point anchorPoint) {
		this.anchorPoint = anchorPoint;
	}
	
	public Image getImage() {
		return image;
	}
	
	public String getImagePath() {
		return imgPath;
	}
	
	public void setImagePath(String imgPath) {
		this.imgPath = imgPath;
		File imgFile = new File(imgPath);
		try {
			image = ImageIO.read(imgFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setImageSize(image);
	}
	
	/**
	 * establece el tamaño del panel contenedor de la imagen
	 * en función de esta
	 * @param img la imagen del comando
	 */
	private void setImageSize(Image img) {
		Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setOpaque(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.drawImage(image, 0, 0, null);
		
		 Graphics2D g2d = (Graphics2D)g;
		 // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		 g2d.drawImage(image, 0, 0, null);  

		 //Toolkit.getDefaultToolkit().sync();
		 //g.dispose();
	}

	public boolean isSnapped() {
		return snapped;
	}

	public void setSnapped(boolean snapped) {
		this.snapped = snapped;
	}

	public boolean isPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}

	public boolean hasDownSlot() {
		return downSlot;
	}

	public void setDownSlot(boolean downSlot) {
		this.downSlot = downSlot;
	}

	public boolean hasRightSlot() {
		return rightSlot;
	}

	public void setRightSlot(boolean rightSlot) {
		this.rightSlot = rightSlot;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Sent getLogicalCom() {
		return logicalCom;
	}

	public void setLogicalCom(Sent logicalCom) {
		this.logicalCom = logicalCom;
	}

	@Override
	public abstract void actionPerformed(ActionEvent e);

	@Override
	public abstract void propertyChange(PropertyChangeEvent evt);
	
	
	/**
	 * aqui se hacen las comprobaciones sintácticas, según la gramática propuesta 
	 * que determinan a que otros comandos se puede acoplar
	 * se extiende por cada uno de los hijos (tipos de comando gráfico)
	 * @param target el comando objetivo del snap
	 * @return true si se puede acoplar a ese tipo de comando
	 */
	public abstract boolean canSnap(Comando target);

}
