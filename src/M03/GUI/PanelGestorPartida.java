package M03.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import M03.Civilization;
import M03.ConexionBD;
import M03.ResourceGenerator;
import M03.Variables;

class PanelGestorPartida extends JPanel implements Variables {
	private Image logo;
	
	public PanelGestorPartida(VentanaPrincipal ventana, ConexionBD conexion, int userID) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		try {
			logo = ImageIO.read(new File("src/M03/img/logo.jpg"));
	    } catch (IOException e) {
	    	System.out.println("No se ha encontrado imagen de fondo.");
	    }

		JButton botonCrearPartida = new JButton("Crear Partida");
		JButton botonContinuarPartida = new JButton("Continuar Partida");

		botonCrearPartida.setMaximumSize(new Dimension(300, 60));
		botonCrearPartida.setPreferredSize(new Dimension(300, 60));
		botonCrearPartida.setBackground(Color.ORANGE);
		botonCrearPartida.setFont(new Font("Arial", Font.BOLD, 22)); 
		botonCrearPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		botonContinuarPartida.setMaximumSize(new Dimension(300, 60));
		botonContinuarPartida.setPreferredSize(new Dimension(300, 60));
		botonContinuarPartida.setBackground(Color.YELLOW);
		botonContinuarPartida.setFont(new Font("Arial", Font.BOLD, 22)); 
		botonContinuarPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		botonCrearPartida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(ventana, "Nombre de la civilización:");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    int idCiv = conexion.crearNuevaPartida(userID, nombre);
                    
                    if (idCiv != -1) {
                        Civilization miCiv = new Civilization();

                        Timer timerReloj = new Timer();
                        ResourceGenerator generador = new ResourceGenerator(miCiv);
                        timerReloj.scheduleAtFixedRate(generador, RESOURCES_GENERATOR_TIME, RESOURCES_GENERATOR_TIME);
                        
                        ventana.cambiarPanel(new PanelJuego(ventana, miCiv));
                    } else {
                        JOptionPane.showMessageDialog(ventana, "Error al crear partida.");
                    }
                }
            }
        });
		
		botonContinuarPartida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ventana, "Buscando partida guardada...");
            }
        });
		
		add(Box.createVerticalGlue());
		add(botonCrearPartida);
		add(Box.createVerticalStrut(20));
		add(botonContinuarPartida);
		add(Box.createVerticalGlue());
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (logo != null) {
            g.drawImage(logo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}