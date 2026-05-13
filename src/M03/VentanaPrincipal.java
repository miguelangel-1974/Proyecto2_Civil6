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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VentanaPrincipal extends JFrame {
	private ConexionBD conexion;
	private int userID = -1;
	private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private Rectangle bounds = ge.getMaximumWindowBounds();
	
	VentanaPrincipal(ConexionBD conexion) {
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

class PanelJuego extends JPanel {
	 
	private static final Color C_FONDO          = new Color(30, 30, 30);
	private static final Color C_BARRA_RECURSOS = new Color(20, 20, 50);
	private static final Color C_PANEL_ACCIONES = new Color(50, 30, 80);
	private static final Color C_PANEL_ESTADO   = new Color(100, 60, 20);
	private static final Color C_TITULO_PANEL   = new Color(200, 180, 255);
	private static final Color C_TEXTO          = Color.WHITE;
	private static final Color C_TEXTO_MUTED    = new Color(200, 200, 200);
 
	private static final Color C_BTN_EDIFICIOS        = new Color(20, 140, 60);
	private static final Color C_BTN_UNID_ATAQUE      = new Color(180, 40, 40);
	private static final Color C_BTN_UNID_DEFENSA     = new Color(150, 50, 150);
	private static final Color C_BTN_UNID_ESPECIALES  = new Color(60, 100, 200);
	private static final Color C_BTN_TECNOLOGIAS      = new Color(30, 130, 160);
	private static final Color C_BTN_STATS            = new Color(20, 120, 60);
	private static final Color C_BTN_CERRAR_SESION    = new Color(160, 30, 30);
 
	private VentanaPrincipal ventana;
	private Civilization miCiv;
	private Image fondoPartida;
 
	private JLabel lblComida, lblMadera, lblHierro, lblMana, lblBatallas;
 
	private JLabel lblEspadachin, lblLancero, lblBallesta, lblCanion;
	private JLabel lblTorreLanza, lblCatapulta, lblTorreCohete;
	private JLabel lblMago, lblSacerdote;
	private JLabel lblGranjas, lblCarpinterias, lblHerrerias, lblTorresMagicas, lblIglesias;
	
	private JPanel panelLog;
 
	public PanelJuego(VentanaPrincipal ventana, Civilization civ) {
		this.ventana = ventana;
		this.miCiv = civ;
 
		setLayout(new BorderLayout());
		setBackground(C_FONDO);
 
		try {
			fondoPartida = ImageIO.read(new File("src/M03/img/fondo_partida.png"));
		} catch (IOException e) {
			System.out.println("No se ha encontrado imagen de fondo de partida.");
		}
 
		add(crearBarraRecursos(), BorderLayout.NORTH);
		add(crearPanelAcciones(), BorderLayout.WEST);
		add(crearZonaCentral(), BorderLayout.CENTER);
		add(crearPanelEstado(), BorderLayout.EAST);
 
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
        	public void run() {
        		actualizarUI();
        		repaint();
        	}
        };
        timer.scheduleAtFixedRate(task, 0, 50);
	}

	private JPanel crearBarraRecursos() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 6));
		barra.setBackground(C_BARRA_RECURSOS);
		barra.setBorder(new LineBorder(new Color(60, 60, 100), 1));
		barra.setPreferredSize(new Dimension(0, 38));
 
		Font fRecurso = new Font("Arial", Font.BOLD, 14);
 
		lblComida    = recursoLabel("Comida: 0",   fRecurso);
		lblMadera    = recursoLabel("Madera: 0",   fRecurso);
		lblHierro    = recursoLabel("Hierro: 0",   fRecurso);
		lblMana      = recursoLabel("Maná: 0",     fRecurso);
		lblBatallas  = recursoLabel("Batallas: 0", fRecurso);
 
		barra.add(lblComida);
		barra.add(lblMadera);
		barra.add(lblHierro);
		barra.add(lblMana);
		barra.add(lblBatallas);
 
		return barra;
	}
 
	private JLabel recursoLabel(String texto, Font f) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(f);
		lbl.setForeground(C_TEXTO);
		return lbl;
	}

	private JPanel crearPanelAcciones() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(C_PANEL_ACCIONES);
		panel.setPreferredSize(new Dimension(170, 0));
		panel.setBorder(new EmptyBorder(8, 8, 8, 8));
 
		JLabel titulo = new JLabel("Acciones", JLabel.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 16));
		titulo.setForeground(C_TITULO_PANEL);
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
		panel.add(titulo);
		panel.add(Box.createVerticalStrut(8));
 
		panel.add(botonAccion("Edificios",           C_BTN_EDIFICIOS,       "edificios"));
		panel.add(Box.createVerticalStrut(6));
		panel.add(botonAccion("Unidades ataque",      C_BTN_UNID_ATAQUE,     "unidades_ataque"));
		panel.add(Box.createVerticalStrut(6));
		panel.add(botonAccion("Unidades defensa",     C_BTN_UNID_DEFENSA,    "unidades_defensa"));
		panel.add(Box.createVerticalStrut(6));
		panel.add(botonAccion("Unidades especiales", C_BTN_UNID_ESPECIALES, "unidades_especiales"));
		panel.add(Box.createVerticalStrut(6));
		panel.add(botonAccion("Tecnologías",         C_BTN_TECNOLOGIAS,     "tecnologias"));
		panel.add(Box.createVerticalStrut(6));
		panel.add(botonAccion("Ver stats",           C_BTN_STATS,           "stats"));
		panel.add(Box.createVerticalGlue());
 
		panel.add(botonAccion("Cerrar sesión",       C_BTN_CERRAR_SESION,   "cerrar_sesion"));
		return panel;
	}
 
	private JButton botonAccion(String texto, Color color, String comando) {
		JButton btn = new JButton(texto);
		btn.setActionCommand(comando);
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, 13));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		btn.addActionListener(accionesListener());
		return btn;
	}
 
	private ActionListener accionesListener() {
		return e -> {
			String cmd = e.getActionCommand();
			switch (cmd) {
				case "edificios":
					abrirVentanaEdificios();
					break;
				case "unidades_ataque":
					abrirVentanaUnidades("Unidades de Ataque");
					break;
				case "unidades_defensa":
					abrirVentanaUnidades("Unidades de Defensa");
					break;
				case "unidades_especiales":
					abrirVentanaUnidades("Unidades Especiales");
					break;
				case "tecnologias":
					JOptionPane.showMessageDialog(ventana, "Árbol de tecnologías — próximamente.");
					break;
				case "stats":
					abrirVentanaStats();
					break;
				case "cerrar_sesion":
					int confirm = JOptionPane.showConfirmDialog(ventana, "¿Cerrar sesión y volver al menú principal?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						ventana.mostrarInicioSesion();
					}
					break;
			}
		};
	}

	private JPanel crearZonaCentral() {
		JPanel zona = new JPanel(new BorderLayout()) {
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (fondoPartida != null) {
	                g.drawImage(fondoPartida, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
 
		return zona;
	}

	public void agregarLog(String mensaje) {
		JLabel linea = new JLabel("• " + mensaje);
		linea.setFont(new Font("Monospaced", Font.PLAIN, 13));
		linea.setForeground(C_TEXTO_MUTED);
		linea.setAlignmentX(Component.LEFT_ALIGNMENT);
		linea.setBorder(new EmptyBorder(2, 10, 2, 10));
		panelLog.add(linea);
		panelLog.revalidate();
	}

	private JPanel crearPanelEstado() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(C_PANEL_ESTADO);
		panel.setPreferredSize(new Dimension(180, 0));
		panel.setBorder(new EmptyBorder(8, 8, 8, 8));
 
		Font fTitulo = new Font("Arial", Font.BOLD, 15);
		Font fItem   = new Font("Arial", Font.PLAIN, 13);

		JLabel titulo = new JLabel("Ejército", JLabel.CENTER);
		titulo.setFont(fTitulo);
		titulo.setForeground(new Color(255, 210, 120));
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		panel.add(titulo);
		panel.add(Box.createVerticalStrut(6));

		lblEspadachin  = estadoLabel("Espadachín: 0",  fItem);
		lblLancero     = estadoLabel("Lancero: 0",     fItem);
		lblBallesta    = estadoLabel("Ballesta: 0",    fItem);
		lblCanion      = estadoLabel("Cañón: 0",       fItem);
		panel.add(lblEspadachin);
		panel.add(lblLancero);
		panel.add(lblBallesta);
		panel.add(lblCanion);
 
		panel.add(Box.createVerticalStrut(6));

		lblTorreLanza  = estadoLabel("Torre lanza: 0",  fItem);
		lblCatapulta   = estadoLabel("Catapulta: 0",    fItem);
		lblTorreCohete = estadoLabel("Torre cohete: 0", fItem);
		panel.add(lblTorreLanza);
		panel.add(lblCatapulta);
		panel.add(lblTorreCohete);
 
		panel.add(Box.createVerticalStrut(6));

		lblMago      = estadoLabel("Mago: 0",      fItem);
		lblSacerdote = estadoLabel("Sacerdote: 0", fItem);
		panel.add(lblMago);
		panel.add(lblSacerdote);
 
		panel.add(Box.createVerticalStrut(8));

		JLabel subEdificios = new JLabel("Edificios:", JLabel.LEFT);
		subEdificios.setFont(new Font("Arial", Font.BOLD, 13));
		subEdificios.setForeground(new Color(255, 210, 120));
		subEdificios.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(subEdificios);
 
		lblGranjas        = estadoLabel("Granjas: 0",       fItem);
		lblCarpinterias   = estadoLabel("Carpinterías: 0",  fItem);
		lblHerrerias      = estadoLabel("Herrerías: 0",      fItem);
		lblTorresMagicas  = estadoLabel("Torres mágicas: 0", fItem);
		lblIglesias       = estadoLabel("Iglesias: 0",       fItem);
 
		panel.add(lblGranjas);
		panel.add(lblCarpinterias);
		panel.add(lblHerrerias);
		panel.add(lblTorresMagicas);
		panel.add(lblIglesias);
 
		return panel;
	}
 
	private JLabel estadoLabel(String texto, Font f) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(f);
		lbl.setForeground(C_TEXTO_MUTED);
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		return lbl;
	}

	private void actualizarUI() {
		// Barra de recursos
		lblComida.setText("Comida: "   + miCiv.getFood());
		lblMadera.setText("Madera: "   + miCiv.getWood());
		lblHierro.setText("Hierro: "   + miCiv.getIron());
		lblMana.setText(  "Maná: "     + miCiv.getMana());

		lblEspadachin.setText("Espadachín: "  + miCiv.getArmy().get(0).size());
		lblLancero.setText(   "Lancero: "     + miCiv.getArmy().get(1).size());
		lblBallesta.setText(  "Ballesta: "    + miCiv.getArmy().get(2).size());
		lblCanion.setText(    "Cañón: "       + miCiv.getArmy().get(3).size());

		lblTorreLanza.setText( "Torre lanza: "  + miCiv.getArmy().get(4).size());
		lblCatapulta.setText(  "Catapulta: "    + miCiv.getArmy().get(5).size());
		lblTorreCohete.setText("Torre cohete: " + miCiv.getArmy().get(6).size());
 
		lblMago.setText(      "Mago: "      + miCiv.getArmy().get(7).size());
		lblSacerdote.setText( "Sacerdote: " + miCiv.getArmy().get(8).size());

		lblGranjas.setText(       "Granjas: "        + miCiv.getFarm());
		lblCarpinterias.setText(  "Carpinterías: "   + miCiv.getCarpentry());
		lblHerrerias.setText(     "Herrerías: "       + miCiv.getSmithy());
		lblTorresMagicas.setText( "Torres mágicas: " + miCiv.getMagicTower());
		lblIglesias.setText(      "Iglesias: "        + miCiv.getChurch());
 
		repaint();
	}

	private void abrirVentanaEdificios() {
		JFrame v = ventanaSecundaria("Construcción de Edificios", 400, 300);
		JPanel contenido = new JPanel(new GridLayout(0, 1, 4, 4));
		contenido.setBackground(new Color(40, 40, 40));
		contenido.setBorder(new EmptyBorder(12, 12, 12, 12));
 
		contenido.add(botonConstruir("Granja", miCiv));
		contenido.add(botonConstruir("Carpintería", miCiv));
		contenido.add(botonConstruir("Herrería", miCiv));
		contenido.add(botonConstruir("Torre mágica", miCiv));
		contenido.add(botonConstruir("Iglesia", miCiv));
 
		v.add(contenido);
		v.setVisible(true);
	}
	
	private JButton botonConstruir(String nombre, Civilization miCiv) {
		JButton btn = new JButton(nombre);
		btn.setBackground(new Color(30, 100, 50));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.PLAIN, 13));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	try {
		            if (nombre.equals("Granja"))  {
		            	miCiv.newFarm();
		            }
		            if (nombre.equals("Carpintería"))  {
		            	miCiv.newCarpentry();
		            }
		            if (nombre.equals("Herrería"))  {
		            	miCiv.newSmithy();
		            }
		            if (nombre.equals("Torre mágica"))  {
		            	miCiv.newMagicTower();
		            }
		            if (nombre.equals("Iglesia"))  {
		            	miCiv.setFood(miCiv.getFood()+10000);
	            		miCiv.setWood(miCiv.getWood()+10000);
	            		miCiv.setMana(miCiv.getMana()+10000);
	            		miCiv.setIron(miCiv.getIron()+10000);
	            		// miCiv.newChurch();
		            }
		            actualizarUI();
		            repaint();
	        	} catch (ResourceException ex) {
	                JOptionPane.showMessageDialog(ventana, "No hay recursos suficientes para construir: " + nombre);
	            }
	        }
	    });
		return btn;
		
	}
 
	private void abrirVentanaUnidades(String titulo) {
		JFrame v = ventanaSecundaria(titulo, 420, 320);
		JPanel contenido = new JPanel(new GridLayout(0, 1, 4, 4));
		contenido.setBackground(new Color(40, 40, 40));
		contenido.setBorder(new EmptyBorder(12, 12, 12, 12));
 
		boolean esAtaque   = titulo.contains("Ataque");
		boolean esDefensa  = titulo.contains("Defensa");
		boolean esEspecial = titulo.contains("Especial");
 
		if (esAtaque) {
			contenido.add(botonReclutar(v, "Espadachín",  "Fuerza 10",  20, 10, 5,  0));
			contenido.add(botonReclutar(v, "Lancero",     "Fuerza 15",  15, 20, 10, 0));
			contenido.add(botonReclutar(v, "Ballesta",    "Fuerza 20",  10, 15, 20, 0));
			contenido.add(botonReclutar(v, "Cañón",       "Fuerza 50",  30, 40, 60, 0));
		} else if (esDefensa) {
			contenido.add(botonReclutar(v, "Torre lanza",  "Defensa 20", 0,  30, 20, 0));
			contenido.add(botonReclutar(v, "Catapulta",    "Defensa 30", 0,  40, 35, 0));
			contenido.add(botonReclutar(v, "Torre cohete", "Defensa 50", 0,  50, 70, 20));
		} else if (esEspecial) {
			contenido.add(botonReclutar(v, "Mago",      "Magia 25",  30, 0,  0,  50));
			contenido.add(botonReclutar(v, "Sacerdote", "Curación 15", 20, 0, 0, 30));
		}
 
		v.add(contenido);
		v.setVisible(true);
	}
 
	private JButton botonReclutar(JFrame padre, String nombre, String descripcion,
			int costoComida, int costoMadera, int costoHierro, int costoMana) {
		JButton btn = new JButton(nombre + "  —  " + descripcion
				+ "  [C:" + costoComida + " M:" + costoMadera
				+ " H:" + costoHierro + " Ma:" + costoMana + "]");
		btn.setBackground(new Color(120, 30, 30));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.PLAIN, 13));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
//		btn.addActionListener(e -> {
//			boolean ok = miCiv.reclutar(nombre, costoComida, costoMadera, costoHierro, costoMana);
//			if (ok) {
//				agregarLog("Reclutado: " + nombre);
//			} else {
//				JOptionPane.showMessageDialog(padre, "Recursos insuficientes para reclutar " + nombre + ".");
//			}
//		});
		return btn;
	}
 
	private void abrirVentanaStats() {
		JFrame v = ventanaSecundaria("Estadísticas de la Civilización", 350, 280);
		JPanel contenido = new JPanel(new GridLayout(0, 2, 8, 8));
		contenido.setBackground(new Color(30, 30, 30));
		contenido.setBorder(new EmptyBorder(16, 16, 16, 16));
 
		agregarStat(contenido, "Comida",          String.valueOf(miCiv.getFood()));
		agregarStat(contenido, "Madera",          String.valueOf(miCiv.getWood()));
		agregarStat(contenido, "Hierro",          String.valueOf(miCiv.getIron()));
		agregarStat(contenido, "Maná",            String.valueOf(miCiv.getMana()));
		agregarStat(contenido, "Espadachines",    String.valueOf(miCiv.getArmy().get(0).size()));
		agregarStat(contenido, "Lanceros",        String.valueOf(miCiv.getArmy().get(1).size()));
		agregarStat(contenido, "Granjas",         String.valueOf(miCiv.getFarm()));
		agregarStat(contenido, "Carpinterías",    String.valueOf(miCiv.getCarpentry()));
 
		v.add(contenido);
		v.setVisible(true);
	}
 
	private void agregarStat(JPanel panel, String nombre, String valor) {
		JLabel lNombre = new JLabel(nombre + ":");
		lNombre.setFont(new Font("Arial", Font.BOLD, 13));
		lNombre.setForeground(new Color(200, 200, 200));
 
		JLabel lValor = new JLabel(valor);
		lValor.setFont(new Font("Arial", Font.PLAIN, 13));
		lValor.setForeground(Color.WHITE);
 
		panel.add(lNombre);
		panel.add(lValor);
	}

	private JFrame ventanaSecundaria(String titulo, int ancho, int alto) {
		JFrame v = new JFrame(titulo);
		v.setBounds(0, 0, ancho, alto);
		v.setResizable(false);
		v.setLocationRelativeTo(this);
		v.getContentPane().setBackground(new Color(30, 30, 30));
		v.setLayout(new BorderLayout());
		return v;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (fondoPartida != null) {
			g.drawImage(fondoPartida, 0, 0, getWidth(), getHeight(), this);
		}
		if (miCiv.getFarm() >= 1) {
			g.drawImage(fondoPartida, 20, 30, 100, 100, this);
		}
	}
}