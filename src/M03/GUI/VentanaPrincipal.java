package M03.GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import M03.ConexionBD;

public class VentanaPrincipal extends JFrame {
	private ConexionBD conexion;
	private int userID = -1;
	private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private Rectangle bounds = ge.getMaximumWindowBounds();
	
	public VentanaPrincipal(ConexionBD conexion) {
		this.conexion = conexion;
		
		setBounds(bounds);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Civilizations");
        setResizable(true);
        setLocationRelativeTo(null);
        
        mostrarInicioSesion();
        
        setVisible(true);
	}

	public int getUserID() {
		return userID;
	}

	public void cambiarPanel(JPanel nuevoPanel) {
		getContentPane().removeAll(); // Borramos lo que haya
		add(nuevoPanel);              // Ponemos el nuevo panel
		revalidate();                 // Actualizamos la estructura
		repaint();                    // Redibujamos
	}
	
	public void mostrarInicioSesion() {
		cambiarPanel(new PanelInicioSesion(this, conexion));
	}
	
	public void loginExitoso(int userID) {
		this.userID = userID;
		cambiarPanel(new PanelGestorPartida(this, conexion, this.userID));
	}
}
