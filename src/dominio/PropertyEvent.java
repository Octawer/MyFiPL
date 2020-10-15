package dominio;

/**
 * evento de notificación de los botones del Panel de Control
 * 
 * @author Octavio
 */
public class PropertyEvent {
	
	public final static int BACKGROUND = 0;
	public final static int ROBOT = 1;
	public final static int START = 2;
	public final static int STOP = 3;
	public final static int RESET = 4;
	public final static int CLEAR = 5;
	private int type;
	private String valor;
	private boolean option;

	public PropertyEvent() {
		// TODO Auto-generated constructor stub
	}

	public PropertyEvent(int type) {
		super();
		this.type = type;
	}

	public PropertyEvent(int type, String valor) {
		super();
		this.type = type;
		this.valor = valor;
	}

	public PropertyEvent(int type, boolean option) {
		super();
		this.type = type;
		this.option = option;
	}

	public PropertyEvent(int type, String valor, boolean option) {
		super();
		this.type = type;
		this.valor = valor;
		this.option = option;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public boolean getOption() {
		return option;
	}

	public void setOption(boolean option) {
		this.option = option;
	}

}
