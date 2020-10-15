package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.EventListenerList;

import lenguaje.ColorSensor;
import lenguaje.Sensor;

/**
 * Comando gráfico sensor de colores
 * Tiene un comboBox con una lista de colores predefinidos, y 
 * la posibilidad de seleccionar cualquier color del espectro
 * mediante un JColorChooser
 * 
 * @author Octavio
 */
public class ComandoColor extends Comando {
	
	private Color commandColor;
	Color colors[] = { Color.black, Color.blue, Color.cyan, Color.darkGray,
	        Color.gray, Color.green, Color.lightGray, Color.magenta,
	        Color.orange, Color.pink, Color.red, Color.white, Color.yellow };
	private JComboBox colorBox;

	public ComandoColor(int token) {
		super(token);
	}

	public ComandoColor(int token, String imgPath) {
		super(token, imgPath);
		setBox();
		setColor();
		setLogicalCom(new ColorSensor(commandColor));
		setToolTipText("Indica una condición de color (el color del fondo donde este el Robot)");
	}

	/**
	 * establece el color de la lista desplegable
	 */
	private void setColor() {
		commandColor = (Color) colorBox.getSelectedItem();
		System.out.println("Color = " + commandColor.toString());
		
	}
	
	/**
	 * crea el ComboBox del comando
	 */
	protected void setBox(){
		colorBox = new JComboBox(colors);
		colorBox.setMaximumRowCount(5);
		colorBox.setEditable(true);
		colorBox.setPreferredSize(new Dimension(50, 30));
		colorBox.setBounds(75,12,45,25);
		colorBox.setRenderer(new ColorCellRenderer());
		Color color = (Color) colorBox.getSelectedItem();
	    ComboBoxEditor editor = new ColorComboBoxEditor(color);
	    colorBox.setEditor(editor);
	    colorBox.addActionListener(this);
		add(colorBox);
	}

	public Color getColor(){
		return commandColor;
	}
	
	
	/* (non-Javadoc)
	 * @see gui.Comando#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * cuando se selecciona un color de la lista desplegable
	 * se establece dicho color como el color actual y se actualiza con ese color
	 * su comando lógico
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setColor();		// obtiene el color del selectedItem del box y actualiza el att commandColor
		((ColorSensor) logicalCom).setColor(commandColor);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean canSnap(Comando target) {
		if(target instanceof ComandoRepeat || target instanceof ComandoWait || target instanceof ComandoIf){
			if(target.hasRightSlot()) {
				target.setRightSlot(false); 		// ver si es responsabilidad de este método el poner a false los slots libres del target
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Clase encargada de pintar los elementos de la lista desplegable 
	 * como colores
	 */
	class ColorCellRenderer implements ListCellRenderer {
	    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	    // width doesn't matter as combobox will size
	    private final Dimension preferredSize = new Dimension(0, 20);

	    public Component getListCellRendererComponent(JList list, Object value,
	        int index, boolean isSelected, boolean cellHasFocus) {
	      JLabel renderer = (JLabel) defaultRenderer
	          .getListCellRendererComponent(list, value, index,
	              isSelected, cellHasFocus);
	      if (value instanceof Color) {
	        renderer.setBackground((Color) value);
	        renderer.setText("");
	      }
	      renderer.setPreferredSize(preferredSize);
	      return renderer;
	    }
	  }
	
	/**
	 * Clase encargada de implementar la mecánica de seleccion de
	 * color pinchando en el caja del comboBox mediante un JColorChooser
	 */
	class ColorComboBoxEditor implements ComboBoxEditor {
		  final protected JButton editor;

		  transient protected EventListenerList listenerList = new EventListenerList();

		  public ColorComboBoxEditor(Color initialColor) {
		    editor = new JButton("");
		    editor.setBackground(initialColor);
		    ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        Color currentBackground = editor.getBackground();
		        Color color = JColorChooser.showDialog(editor, "Color Chooser",
		            currentBackground);
		        if ((color != null) && (currentBackground != color)) {
		          editor.setBackground(color);
		          fireActionEvent(color);
		        }
		      }
		    };
		    editor.addActionListener(actionListener);
		  }

		  public void addActionListener(ActionListener l) {
		    listenerList.add(ActionListener.class, l);
		  }

		  public Component getEditorComponent() {
		    return editor;
		  }

		  public Object getItem() {
		    return editor.getBackground();
		  }

		  public void removeActionListener(ActionListener l) {
		    listenerList.remove(ActionListener.class, l);
		  }

		  public void selectAll() {
		    // ignore
		  }

		  public void setItem(Object newValue) {
		    if (newValue instanceof Color) {
		      Color color = (Color) newValue;
		      editor.setBackground(color);
		    } else {
		      // Try to decode
		      try {
		        Color color = Color.decode(newValue.toString());
		        editor.setBackground(color);
		      } catch (NumberFormatException e) {
		        // ignore - value unchanged
		      }
		    }
		  }

		  protected void fireActionEvent(Color color) {
		    Object listeners[] = listenerList.getListenerList();
		    for (int i = listeners.length - 2; i >= 0; i -= 2) {
		      if (listeners[i] == ActionListener.class) {
		        ActionEvent actionEvent = new ActionEvent(editor,
		            ActionEvent.ACTION_PERFORMED, color.toString());
		        ((ActionListener) listeners[i + 1])
		            .actionPerformed(actionEvent);
		      }
		    }
		  }
		}
	

}
