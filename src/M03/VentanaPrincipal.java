package M03;

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
import java.util.Timer;
import java.util.TimerTask;

public class VentanaPrincipal extends JFrame {
	private ConexionBD conexion;
	private int userID = -1;
	
	VentanaPrincipal(ConexionBD conexion) {
		this.conexion = conexion;
		
        setBounds(0,0,700,500);
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
		cambiarPanel(new PanelPartida(this, conexion, this.userID));
	}
}

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
		
		add(Box.createVerticalStrut(getHeight() / 2));
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

class PanelPartida extends JPanel implements Variables {
	private VentanaPrincipal ventana;
	private ConexionBD conexion;
	private int userID;
	private Image logo;
	
	public PanelPartida(VentanaPrincipal ventana, ConexionBD conexion, int userID) {
		this.ventana = ventana;
		this.conexion = conexion;
		this.userID = userID;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		try {
			logo = ImageIO.read(new File("src/M03/img/logo.jpg"));
	    } catch (IOException e) {
	    	System.out.println("No se ha encontrado imagen de fondo.");
	    }

		JButton botonCrearPartida = new JButton("Crear Partida");
		JButton botonContinuarPartida = new JButton("Continuar Partida");

		botonCrearPartida.setMaximumSize(new Dimension(200, 40));
		botonCrearPartida.setBackground(Color.ORANGE);
		botonCrearPartida.setFont(new Font("Arial", Font.BOLD, 18)); 
		botonCrearPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		botonContinuarPartida.setMaximumSize(new Dimension(200, 40));
		botonContinuarPartida.setBackground(Color.YELLOW);
		botonContinuarPartida.setFont(new Font("Arial", Font.BOLD, 18)); 
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
		
		add(Box.createVerticalStrut(160));
		add(botonCrearPartida);
		add(Box.createVerticalStrut(20));
		add(botonContinuarPartida);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (logo != null) {
            g.drawImage(logo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}

class PanelJuego extends JPanel {
    private VentanaPrincipal ventana;
    private Image fondoPartida;
    private Civilization miCiv;

    public PanelJuego(VentanaPrincipal ventana, Civilization civ) {
        this.ventana = ventana;
        this.miCiv = civ;
        
        try {
            fondoPartida = ImageIO.read(new File("src/M03/img/fondo_partida.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
        	public void run() {
        		repaint();
        	}
        };
        timer.scheduleAtFixedRate(task, 0, 50);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondoPartida != null) {
            g.drawImage(fondoPartida, 0, 0, getWidth(), getHeight(), this);
        }
        
        g.setColor(new Color(0,0,0, 150));
        g.fillRect(0, 0, getWidth(), 40);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String recursos = String.format("Madera: %d | Hierro: %d | Comida: %d | Mana: %d", 
                          miCiv.getWood(), miCiv.getIron(), miCiv.getFood(), miCiv.getMana());
        g.drawString(recursos, 20, 25);
    }
}