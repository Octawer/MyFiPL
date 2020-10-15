package gui;

import java.awt.Color;
import java.awt.Frame;

import ghost.GhostComponentAdapter;
import ghost.GhostDropListener;
import ghost.GhostDropManager;
import ghost.GhostGlassPane;
import ghost.GhostMotionAdapter;
import utils.*;

/**
 * Clase principal de la aplicación
 * Crea el entorno gráfico, creando cada uno de los subpaneles necesarios
 * y registrando los oyentes y manejadores de eventos oportunos
 * 
 * @author Octavio
 */
public class Entorno extends javax.swing.JFrame {
	
	// usamos los 3 paneles por separado como atributos del entorno
	
	private PanelProgramacion panelProgramacion;
	private PanelControl panelControl;
	private Escenario escenario;
	private static Entorno instancia;				// única instancia del Entorno (instancia del singleton)
	
	public Entorno() {
		iniciarEntorno();
		//setExtendedState(Frame.MAXIMIZED_BOTH);  
	}
	
	
	/**
	 * Acciones de inicialización del entorno
	 */
	private void iniciarEntorno() {
		// creamos el glassPane y los oyentes y manejadores de eventos del Drag and Drop
		GhostGlassPane glassPane = new GhostGlassPane();
        setGlassPane(glassPane);
        GhostComponentAdapter componentAdapter = new GhostComponentAdapter(glassPane, "accion vacia");
        GhostMotionAdapter ghostMotionAdapter = new GhostMotionAdapter(glassPane);
        
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MyFiPL");
        
        // creamos las diferentes áreas del entorno
        panelProgramacion = new PanelProgramacion(componentAdapter,ghostMotionAdapter);
		panelControl = new PanelControl();
		escenario = new Escenario();
		
		// añadimos los oyentes de los diferentes eventos de control y sensores
		panelControl.addPropertyListener(escenario);
		panelControl.addPropertyListener(panelProgramacion.getEscritorio());
		escenario.addPropertyListener(panelProgramacion.getEscritorio());
		escenario.addPropertyListener(escenario);			// se usa para la explosión (ver si modificamos esto)
		escenario.addSensorEventListener(panelControl);
		escenario.addSensorEventListener(panelProgramacion.getEscritorio());
		
		// ajustamos el layout del entorno y sus componentes
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(escenario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelProgramacion.getPaletaComandos(), javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelProgramacion.getEscritorio(), javax.swing.GroupLayout.PREFERRED_SIZE, 527, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelProgramacion.getEscritorio(), javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .addComponent(panelProgramacion.getPaletaComandos())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(escenario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        // registramos el manejador de eventos de drop en el Escritorio
        GhostDropListener listener = new GhostDropManager(panelProgramacion.getEscritorio(), glassPane);
        componentAdapter.addGhostDropListener(listener);

        pack();
	}
	
	/**
	 * Establece el aspecto visual del entorno
	 */
	private static void setLookAndFeel() {
		/* Set the Nimbus look and feel */
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
		
		// change nimbus colors
		//javax.swing.UIManager.put("nimbusBase", new Color(255,255,0));
		//javax.swing.UIManager.put("nimbusBlueGrey", new Color(0,255,0));
		//javax.swing.UIManager.put("control", new Color(0,255,255));
		
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        	//javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Entorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Entorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Entorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Entorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		
	}

	/**
	 * Método del singleton, usado para acceder a la única instancia del Entorno
	 * por parte de componentes inferiores en la jerarquía
	 * @return la única instancia del Entorno
	 */
	public static synchronized Entorno getInstancia() {
		if(instancia == null){
			// en teoria no deberia ocurrir, ya que el unico de este método es posterior a la creación de la GUI
			// pero se mantiene por seguridad
			// en cualquier caso si llegara a ocurrir faltaria establecer el layout,  los listeners y el resto de acciones posteriores a la mera creacion ...
			instancia = new Entorno();
		}
		return instancia;
	}


	public PanelProgramacion getPanelProgramacion() {
		return panelProgramacion;
	}


	public PanelControl getPanelControl() {
		return panelControl;
	}


	public Escenario getEscenario() {
		return escenario;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		setLookAndFeel();
		
		java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            instancia = new Entorno();
	            instancia.setVisible(true);
	        }
	    });
	}

}
