package M03;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
	private PanelFondo panelFondo;
	
	VentanaPrincipal() {
		super();
        setSize(1000,1730);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Civilizations");
        panelFondo = new PanelFondo();

        setBounds(0,0,getHeight(),getWidth());
        add(panelFondo);
        setVisible(true);
	}
}

class PanelFondo extends JPanel {
	private Image fondo;
	
	public PanelFondo() {
		setLayout(new GridBagLayout());
		
		try {
			fondo = ImageIO.read(new File("src/M03/img/fondo.png"));
		} catch (IOException e) {
			System.out.println("Error: No se pudo cargar la imagen de fondo.");
			e.printStackTrace();
		}
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}