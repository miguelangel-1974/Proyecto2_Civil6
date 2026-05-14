package M03.GUI;

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

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import M03.ConexionBD;

class PanelInicioSesion extends JPanel implements ActionListener {
	private VentanaPrincipal ventana;
	private ConexionBD conexion;
	
	private Image logo;
	private JButton botonIniciarSesion, botonCrearUsuario, botonSalir;
	
	public PanelInicioSesion(VentanaPrincipal ventana, ConexionBD conexion) {
		this.ventana = ventana;
		this.conexion = conexion;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		establecerFondo();
		inicializarBotones();
	}
	
	public void establecerFondo() {
		try {
			logo = ImageIO.read(new File("src/M03/img/logo.jpg"));
	    } catch (IOException e) {
	    	System.out.println("No se ha encontrado imagen");
	    }
	}
	
	public void inicializarBotones() {
		botonIniciarSesion = new JButton("Iniciar Sesion");
		botonCrearUsuario = new JButton("Crear Usuario");
		botonSalir = new JButton("Salir");
		
		botonIniciarSesion.setMaximumSize(new Dimension(300, 60));
		botonIniciarSesion.setPreferredSize(new Dimension(300, 60));
		botonIniciarSesion.setBackground(Color.GREEN);
		botonIniciarSesion.setFont(new Font("Arial", Font.BOLD, 22)); 
		botonIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonIniciarSesion.addActionListener(this);
		
		botonCrearUsuario.setMaximumSize(new Dimension(300, 60));
		botonCrearUsuario.setPreferredSize(new Dimension(300, 60));
		botonCrearUsuario.setBackground(Color.CYAN);
		botonCrearUsuario.setFont(new Font("Arial", Font.BOLD, 22)); 
		botonCrearUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonCrearUsuario.addActionListener(this);
		
		botonSalir.setMaximumSize(new Dimension(300, 60));
		botonSalir.setPreferredSize(new Dimension(300, 60));
		botonSalir.setBackground(Color.PINK);
		botonSalir.setFont(new Font("Arial", Font.BOLD, 22)); 
		botonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonSalir.addActionListener(this);
		
		add(Box.createVerticalGlue());
		add(botonIniciarSesion);
		add(Box.createVerticalStrut(15));
		add(botonCrearUsuario);
		add(Box.createVerticalStrut(15));
		add(botonSalir);
		add(Box.createVerticalGlue());
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (logo != null) {
            g.drawImage(logo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Iniciar Sesion")) {
			ventanaInicioSesion();
		} else if (e.getActionCommand().equals("Crear Usuario")) {
			ventanaCrearUsuario();
		} else if (e.getActionCommand().equals("Salir")) {
			System.exit(0);
		}
	}
	
	public void ventanaInicioSesion() {
		JFrame nuevaVentana = new JFrame();
	    nuevaVentana.setBounds(500, 500, 250, 180);
	    nuevaVentana.setResizable(false);
	    nuevaVentana.setLocationRelativeTo(this);
	    nuevaVentana.setLayout(new FlowLayout());
		
		JLabel usuario = new JLabel("Usuario: ");
		JTextField inputUsuario = new JTextField(10);
		JLabel contraseña = new JLabel("Contraseña: ");
		JPasswordField inputContraseña = new JPasswordField(10);
		JButton botonEntrar = new JButton("Entrar");
		
		botonEntrar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String user = inputUsuario.getText();
	            String pass = new String(inputContraseña.getPassword());
	            
	            int userID = conexion.login(user, pass);

	            if (userID != -1) {
	                nuevaVentana.dispose();
	                ventana.loginExitoso(userID);
	            } else {
	            	JOptionPane.showMessageDialog(nuevaVentana, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
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
	
	public void ventanaCrearUsuario() {
		JFrame nuevaVentana = new JFrame();
	    nuevaVentana.setBounds(500, 500, 250, 180);
	    nuevaVentana.setResizable(false);
	    nuevaVentana.setLocationRelativeTo(this);
	    nuevaVentana.setLayout(new FlowLayout());
		
		JLabel usuario = new JLabel("Usuario: ");
		JTextField inputUsuario = new JTextField(10);
		JLabel contraseña = new JLabel("Contraseña: ");
		JPasswordField inputContraseña = new JPasswordField(10);
		
		JButton botonEntrar = new JButton("Entrar");
		
		botonEntrar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String user = inputUsuario.getText();
	            String pass = new String(inputContraseña.getPassword());
	            boolean newUser = false;
	            
	            newUser = conexion.createUser(user, pass);
	            
	            if (newUser == false) {
	            	JOptionPane.showMessageDialog(nuevaVentana, "Usuario ya en uso.\nPrueba otro usuario.");
	            } else {
	            	JOptionPane.showMessageDialog(nuevaVentana, "Usuario creado correctamente.\nYa puedes iniciar sesión.");
	            	nuevaVentana.dispose();
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