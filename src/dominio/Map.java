package dominio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import utils.DebugHelper;

/**
 * Mapas creados a partir de imágenes que se usan como fondo de Escenario
 * 
 * @author Octavio
 */
public class Map {
	
	private boolean openMap;		// determina si es un mapa cerrado (con negative) o abierto (cualquier imagen - sin neg)
	private String imgPath;
	private Image background;
	private Image negativeMap;
	private Dimension size;
	private Point initialPosition;
	private Point exitPosition;
	private List<MapItem> items;
	private MapItem portal;
	private String negativeDefault = "Imagenes\\Maps\\Negative_Default.png";
	private final int tileSize = 32;
	private final Point defaultInitialPosition = new Point(256,256);
	
	// asignamos nombres simbólicos (por legibilidad) a los colores de las casillas del negativeMap
	private Color TILE_START = Color.BLUE;
	private Color TILE_EXIT = Color.GREEN;
	private Color TILE_ITEM = Color.RED;
	private Color TILE_PATH = Color.WHITE;
	private Color TILE_VOID = Color.BLACK;

	
	public Map(String path){
		openMap = false;		// en principio todos son cerrados, hasta que no podamos leer el negative, momento en que se cambia a abierto
		items = new ArrayList<MapItem>();
		setBackground(path);
		setInitialPosition();
		setExit();
		setItems();
	}
	
	
	/**
	 * establece la posición inicial del mapa en función del la casilla 
	 * correspondiente del mapa negativo
	 */
	private void setInitialPosition() {
		if(negativeMap != null && !openMap){
			// obtener la primera casilla del color de la casilla de salida
			// obviamos el marco exterior de 1 tile de los mapas (32 pixels - [0-31])
			// exploramos cada casilla (tiles de 32 px) del Mapa, excluyendo un borde de 1 tile
			// (se busca el color del primer pixel (esquina superior-izq) de cada tile --- ver si cambiamos esto a otro pixel (ojo con snapPosition())
			for(int y = tileSize; y < negativeMap.getHeight(null); y = y + 32){
				for (int x = tileSize; x < negativeMap.getWidth(null); x = x + 32) {
					int color = ((BufferedImage) negativeMap).getRGB(x, y);
					Color nextColor = new Color(color);
					if(nextColor.equals(TILE_START)){
						initialPosition = new Point(x,y);
						DebugHelper.infoMessage("Start color = " + nextColor);
						DebugHelper.infoMessage("Start tile at pixel: " + initialPosition);
						return;
					}
				}
			}
			// si por casualidad no hay casilla de salida en el negativeMap (no deberia) usamos el default
			initialPosition = defaultInitialPosition;
		} else {
			// en el caso de Mapas Libres (sin negativo) se fija una posicion por defecto;
			initialPosition = defaultInitialPosition;
		}
	}
	
	/**
	 * establece la meta del mapa en función del la casilla 
	 * correspondiente del mapa negativo
	 */
	private void setExit() {
		if(negativeMap != null && !openMap){
			// obtener el primer tile del color de casilla de meta
			// obviamos el marco exterior de 1 tile de los mapas (32 pixels - [0-31])
			// exploramos cada casilla (tiles de 32 px) del Mapa, excluyendo un borde de 1 tile
			for(int y = tileSize; y < negativeMap.getHeight(null); y = y + 32){
				for (int x = tileSize; x < negativeMap.getWidth(null); x = x + 32) {
					int color = ((BufferedImage) negativeMap).getRGB(x, y);
					Color nextColor = new Color(color);
					if(nextColor.equals(TILE_EXIT)){
						exitPosition = new Point(x,y);
						portal = new MapItem();
						portal.setPosition(exitPosition);
						DebugHelper.infoMessage("Exit color = " + nextColor);
						DebugHelper.infoMessage("Exit tile at pixel: " + exitPosition);
						return;
					}
				}
			}
			// si por casualidad no hay meta en el negativeMap (no deberia) usamos el default
			//exitPosition = defaultInitialPosition;
		} else {
			// en el caso de Mapas Libres (sin negativo) se fija una posicion por defecto;
			exitPosition = new Point(256,256);
		}
	}
	
	/**
	 * establece las posiciones en el mapa de los distintos MapItems, en caso de que existan,
	 * en fc de las casillas correspondientes del mapa negativo
	 */
	private void setItems() {
		if(negativeMap != null && !openMap){
			// obtener cada casilla del color de TILE_ITEM y añadimos MapItems con esas posiciones
			// obviamos el marco exterior de 1 tile de los mapas (32 pixels - [0-31])
			// exploramos cada casilla (tiles de 32 px) del Mapa, excluyendo un borde de 1 tile
			for(int y = tileSize; y < negativeMap.getHeight(null); y = y + 32){
				for (int x = tileSize; x < negativeMap.getWidth(null); x = x + 32) {
					int color = ((BufferedImage) negativeMap).getRGB(x, y);
					Color nextColor = new Color(color);
					if(nextColor.equals(TILE_ITEM)){
						MapItem item = new MapItem();
						item.setPosition(new Point(x,y));
						items.add(item);
						DebugHelper.infoMessage("item color = " + nextColor);
						DebugHelper.infoMessage("Item tile at pixel: " + exitPosition);
					}
				}
			}
		} else {
			// en el caso de Mapas Abiertos (sin negativo) no deben existir map items;
		}
		
	}

	/**
	 * establece la imagen del mapa visible y el mapa negativo para mapas abiertos o cerrados
	 */
	private void setBackground(String image) {
		File imgFile = new File(image);
		DebugHelper.debugMessage("File name:" + imgFile.getName());
		String prevPath = image.replaceAll(imgFile.getName(), "");
		DebugHelper.debugMessage("Previous path: " + prevPath);
		File negImgFile = new File(prevPath + "Negative_" + imgFile.getName());
		DebugHelper.debugMessage("Negative Map Path: " + prevPath + "Negative_" + imgFile.getName());
		// Ojo: aqui solo valen nombres de mapas que tengan su Negativo (y simples, sin "negative" en el nombre del archivo)
		try {
			background = ImageIO.read(imgFile);
			negativeMap = ImageIO.read(negImgFile);
		} catch (IOException e) {
			
			// si no podemos leer el negativeMap (porque dicho mapa no tiene asociado uno)
			// le asignamos un negativeMap por defecto (negativeDefault), entero en blanco
			// y lo establecemos a mapa abierto (por seguridad)
			
			// usamos un mapa por defecto para el caso del mapa visible en el caso improbable de que no se haya leido este
			if(background == null) {
				createMapDefault();
			}
			
			createNegativeDefault();
			openMap = true;
			e.printStackTrace();
			
		} finally {
			size = new Dimension(background.getWidth(null), background.getHeight(null));
		}
	}
	
	private void createMapDefault() {
		Color color = new Color(255,255,255);
		int mapHeight = 512;
		int mapWidth = 512;
		
		background = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = ((BufferedImage) background).createGraphics();
		g2d.setPaint (color);
		g2d.fillRect ( 0, 0, background.getWidth(null), background.getHeight(null) );	
	}

	private void createNegativeDefault() {
		Color color = new Color(255,255,255);
		int mapHeight = background.getHeight(null);
		int mapWidth = background.getWidth(null);
		
		negativeMap = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = ((BufferedImage) negativeMap).createGraphics();
		g2d.setPaint (color);
		g2d.fillRect ( 0, 0, negativeMap.getWidth(null), negativeMap.getHeight(null) );	
	}
	
	/*
	private void setBackgroundImageSize(Image img) {
		//background = img;
		
	}*/

	public boolean isOpenMap() {
		return openMap;
	}

	public String getImgPath() {
		return imgPath;
	}

	public Image getBackground() {
		return background;
	}

	public Image getNegativeMap() {
		return negativeMap;
	}

	public Dimension getSize() {
		return size;
	}

	public Point getInitialPosition() {
		return initialPosition;
	}

	public Point getExitPosition() {
		return exitPosition;
	}

	public List<MapItem> getItems() {
		return items;
	}

	public int getTileSize() {
		return tileSize;
	}

	public MapItem getPortal() {
		return portal;
	}


}
