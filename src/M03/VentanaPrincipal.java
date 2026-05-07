package M03;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
	private PanelInicioSesion panelIniciarSesion;
	
	VentanaPrincipal() {
        setBounds(0,0,700,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Civilizations");
        setResizable(false);
        
        panelIniciarSesion = new PanelInicioSesion();

        add(panelIniciarSesion);
        
        setVisible(true);
	}
}

class PanelInicioSesion extends JPanel implements ActionListener {
	private ConexionBD conexion = new ConexionBD();
	private Image logo;
	private JButton botonIniciarSesion, botonCrearUsuario, botonSalir;
	
	public PanelInicioSesion() {
		conexion.connect();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		establecerFondo();
		inicializarBotones();
	}
	
	public void establecerFondo() {
		try {
			logo = ImageIO.read(new File("src/M03/img/logo.jpg"));
	    } catch (IOException e) {
	    	System.out.println("No se ha encontrado imagen");
	        e.printStackTrace();
	    }
	}
	
	public void inicializarBotones() {
		botonIniciarSesion = new JButton("Iniciar Sesion");
		botonCrearUsuario = new JButton("Crear Usuario");
		botonSalir = new JButton("Salir");
		
		botonIniciarSesion.setMaximumSize(new Dimension(200, 40));
		botonIniciarSesion.setBackground(Color.GREEN);
		botonIniciarSesion.setFont(new Font("Arial", Font.BOLD, 18)); 
		botonIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonIniciarSesion.addActionListener(this);
		
		botonCrearUsuario.setMaximumSize(new Dimension(200, 40));
		botonCrearUsuario.setBackground(Color.CYAN);
		botonCrearUsuario.setFont(new Font("Arial", Font.BOLD, 18)); 
		botonCrearUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonCrearUsuario.addActionListener(this);
		
		botonSalir.setMaximumSize(new Dimension(200, 40));
		botonSalir.setBackground(Color.PINK);
		botonSalir.setFont(new Font("Arial", Font.BOLD, 18)); 
		botonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonSalir.addActionListener(this);
		
		add(Box.createVerticalStrut(140));
		add(botonIniciarSesion);
		add(Box.createVerticalStrut(15));
		add(botonCrearUsuario);
		add(Box.createVerticalStrut(15));
		add(botonSalir);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (logo != null) {
            g.drawImage(logo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Iniciar Sesion")) {
			mostrarNuevaVentana();
		}
		if (e.getActionCommand().equals("Crear Usuario")) {
			
		}
		if (e.getActionCommand().equals("Salir")) {
			
		}
	}
	
	public void mostrarNuevaVentana() {
		JFrame nuevaVentana = new JFrame();
	    nuevaVentana.setBounds(500, 500, 250, 180);
	    nuevaVentana.setTitle("Inicio de Sesion");
	    nuevaVentana.setResizable(false);
	    nuevaVentana.setLayout(new FlowLayout());
		
		JLabel usuario = new JLabel("Usuario: ");
		JTextField inputUsuario = new JTextField(10);
		JLabel contraseña = new JLabel("Contraseña: ");
		JTextField inputContraseña = new JTextField(10);
		
		JButton botonEntrar = new JButton("Entrar");
		
		botonEntrar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String user = inputUsuario.getText();
	            String pass = inputContraseña.getText();
	            
	            int userID = conexion.login(user, pass);

	            if (userID != -1) {
	                nuevaVentana.dispose();
	            } else {
	                inputUsuario.setText("");
	                inputContraseña.setText("");
	            }
	        }
	    });
		
		nuevaVentana.add(usuario);
		nuevaVentana.add(inputUsuario);
		nuevaVentana.add(contraseña);
		nuevaVentana.add(inputContraseña);
		nuevaVentana.add(botonEntrar);
		
		nuevaVentana.setVisible(true);
	}
}
