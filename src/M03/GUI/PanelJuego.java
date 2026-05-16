package M03.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import M03.Battle;
import M03.BuildingException;
import M03.Civilization;
import M03.ConexionBD;
import M03.EnemyArmyGenerator;
import M03.ResourceException;
import M03.ResourceGenerator;
import M03.Variables;

class PanelJuego extends JPanel implements ActionListener, Variables {
 
	private VentanaPrincipal ventana;
	private Civilization miCiv;
	
	private ConexionBD conexion;
	private int idCiv;
	private int userID;
	
	private Image fondoPartida;
	private Image imgGranja, imgCarpinteria, imgHerreria, imgTorre_magica, imgIglesia;
 
	private JLabel lblComida, lblMadera, lblHierro, lblMana, lblBatallas;
 
	private JLabel lblEspadachin, lblLancero, lblBallesta, lblCanion;
	private JLabel lblTorreLanza, lblCatapulta, lblTorreCohete;
	private JLabel lblMago, lblSacerdote;
	private JLabel lblGranjas, lblCarpinterias, lblHerrerias, lblTorresMagicas, lblIglesias;
	private JLabel lblTecnoAtt, lblTecnoDef;
 
	private String edificioEnCurso;
	private ArrayList<EdificioColocado> edificiosColocados;
	
	private ResourceGenerator generador;
	private EnemyArmyGenerator generarEnemigos;

	private Timer timer;
	private Timer timerGuardarParida;
	
	public PanelJuego(VentanaPrincipal ventana, Civilization civ, ConexionBD conexion, int idCiv, int userID) {
		this.ventana = ventana;
		this.miCiv = civ;
		this.conexion = conexion;
		this.idCiv = idCiv;
	    this.userID = userID;
 
		edificiosColocados = new ArrayList<>();
		
		generador = new ResourceGenerator(miCiv);
		generarEnemigos = new EnemyArmyGenerator(miCiv);
		
		setLayout(new BorderLayout());

		try {
			fondoPartida = ImageIO.read(new File("src/M03/img/fondo_partida.jpeg"));
			imgGranja = ImageIO.read(new File("src/M03/img/granja.png"));
			imgCarpinteria = ImageIO.read(new File("src/M03/img/carpinteria.png"));
			imgHerreria = ImageIO.read(new File("src/M03/img/herreria.png"));
			imgTorre_magica = ImageIO.read(new File("src/M03/img/torreMagica.png"));
			imgIglesia = ImageIO.read(new File("src/M03/img/iglesia.png"));
		} catch (IOException e) {
			System.out.println("No se ha encontrado imagen de fondo de partida.");
		}

		add(crearPanelAcciones(), BorderLayout.WEST);
		add(crearZonaCentral(), BorderLayout.CENTER);
		add(crearPanelEstado(), BorderLayout.EAST);
 
        cargarEdificiosGuardados();
        
        comenzarTimers();
	}

	private void comenzarTimers() {
		timerGuardarParida = new Timer();
		
		timerGuardarParida.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	            conexion.guardarPartida(miCiv, idCiv, userID);
	            conexion.guardarEdificios(idCiv, edificiosColocados);
	        }
	    }, AUTO_SAVE_TIME, AUTO_SAVE_TIME);
		
		timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() { 
	        	actualizarUI();
	        }
	    }, 0, 50);
		
		timer.scheduleAtFixedRate(generador, RESOURCES_GENERATOR_TIME, RESOURCES_GENERATOR_TIME);
		
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	            JOptionPane.showMessageDialog(PanelJuego.this,
	                "¡Se acerca un ejército enemigo!",
	                "Va a comenzar una batalla",
	                JOptionPane.WARNING_MESSAGE);

	            timer.cancel();

	            generarEnemigos.createEnemyArmy();
	            Battle batalla = new Battle(miCiv, generarEnemigos.getEnemyArmy());
	            generarEnemigos.viewThreat();
	            batalla.pelear();
	            
	            conexion.guardarBatalla(batalla, idCiv);
	            conexion.guardarPartida(miCiv, idCiv, userID);
	            conexion.guardarEdificios(idCiv, edificiosColocados);
	            
	            String reporte = batalla.getBattleReport(miCiv.getBattles());
	            String battleDevelopment = batalla.getBattleDevelopment();
	            
	            String contenido = "========== DESARROLLO DE LA BATALLA (LOG) ==========\n\n" 
                        + battleDevelopment 
                        + "\n\n"
                        + "========== RESUMEN FINAL Y ESTADÍSTICAS ==========\n\n" 
                        + reporte;
	            
	            JFrame ventanaBatalla = new JFrame("Resultado de la Batalla");
	            ventanaBatalla.setSize(750, 650);
	            ventanaBatalla.setLocationRelativeTo(PanelJuego.this);
	            ventanaBatalla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	            JTextArea textArea = new JTextArea(contenido);
	            textArea.setEditable(false);
	            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
	            textArea.setBackground(new Color(20, 20, 20));
	            textArea.setForeground(new Color(200, 255, 200));
	            textArea.setMargin(new Insets(15, 15, 15, 15));

	            textArea.setCaretPosition(0);
	            
	            ventanaBatalla.add(new JScrollPane(textArea));

	            ventanaBatalla.addWindowListener(new WindowAdapter() {
	                public void windowClosed(WindowEvent we) {
	                    generador = new ResourceGenerator(miCiv);
	                    comenzarTimers();
	                }
	            });

	            ventanaBatalla.setVisible(true);
	        }
	    }, AUTO_GENERATE_BATTLE, AUTO_GENERATE_BATTLE);
	}
	
	private JPanel crearPanelAcciones() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(50, 30, 80));
		panel.setPreferredSize(new Dimension(220, 0));
		panel.setBorder(new EmptyBorder(20, 10, 10, 10));
 
		JLabel tituloAcciones = new JLabel("Acciones", JLabel.CENTER);
		tituloAcciones.setFont(new Font("Arial", Font.BOLD, 22));
		tituloAcciones.setForeground(new Color(200, 180, 255));
		tituloAcciones.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel.add(tituloAcciones);
		panel.add(Box.createVerticalStrut(15));
 
		panel.add(crearBoton("Edificios", new Color(20, 140, 60)));
		panel.add(Box.createVerticalStrut(15));
		panel.add(crearBoton("Unidades ataque", new Color(180, 40, 40)));
		panel.add(Box.createVerticalStrut(15));
		panel.add(crearBoton("Unidades defensa", new Color(150, 50, 150)));
		panel.add(Box.createVerticalStrut(15));
		panel.add(crearBoton("Unidades especiales", new Color(60, 100, 200)));
		panel.add(Box.createVerticalStrut(15));
		panel.add(crearBoton("Tecnologías", new Color(30, 130, 160)));
		panel.add(Box.createVerticalStrut(15));
		panel.add(crearBoton("Dar Items", Color.BLACK));
		panel.add(Box.createVerticalGlue());
		panel.add(crearBoton("Guardar Partida", Color.GREEN));
		panel.add(Box.createVerticalStrut(15));
		panel.add(crearBoton("Cerrar sesión", new Color(160, 30, 30)));
		
		return panel;
	}

	private JPanel crearZonaCentral() {
	    JPanel zona = new JPanel(new BorderLayout()) {
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            g.drawImage(fondoPartida, 0, 0, getWidth(), getHeight(), this);
	            
	            for (EdificioColocado ed : edificiosColocados) {
	                g.drawImage(ed.getImagen(), ed.getX() - ed.getAncho()/2, ed.getY() - ed.getAlto()/2, ed.getAncho(), ed.getAlto(), this);
	            }
	            
	            if (edificioEnCurso != null) {
	                g.setColor(new Color(255, 255, 0, 80));
	                g.fillRect(0, 0, getWidth(), getHeight());
	                g.setColor(Color.YELLOW);
	                g.setFont(new Font("Arial", Font.BOLD, 22));
	                g.drawString("Haz clic donde quieres construir: " + edificioEnCurso, 20, 30);
	            }
	        }
	    };
	    
	    zona.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent ev) {
	            if (edificioEnCurso != null) {
	                colocarEdificio(edificioEnCurso, ev.getX(), ev.getY());
	            }
	        }
	    });
	    
	    return zona;
	}

	private void colocarEdificio(String nombre, int x, int y) {
	    try {
	        if (nombre.equals("Granja | C: "+FOOD_COST_FARM+" | M: "+WOOD_COST_FARM+" | H: "+IRON_COST_FARM)) {
	            miCiv.newFarm();
	            edificiosColocados.add(new EdificioColocado(imgGranja, x, y, 200, 100, "Granja"));
	        }
	        else if (nombre.equals("Carpintería | C: "+FOOD_COST_CARPENTRY+" | M: "+WOOD_COST_CARPENTRY+" | H: "+IRON_COST_CARPENTRY)) {
	            miCiv.newCarpentry();
	            edificiosColocados.add(new EdificioColocado(imgCarpinteria, x, y, 180, 80, "Carpintería"));
	        }
	        else if (nombre.equals("Herrería | C: "+FOOD_COST_SMITHY+" | M: "+WOOD_COST_SMITHY+" | H: "+IRON_COST_SMITHY)) {
	            miCiv.newSmithy();
	            edificiosColocados.add(new EdificioColocado(imgHerreria, x, y, 120, 120, "Herrería"));
	        }
	        else if (nombre.equals("Torre mágica | C: "+FOOD_COST_MAGICTOWER+" | M: "+WOOD_COST_MAGICTOWER+" | H: "+IRON_COST_MAGICTOWER)) {
	            miCiv.newMagicTower();
	            edificiosColocados.add(new EdificioColocado(imgTorre_magica, x, y, 50, 100, "Torre mágica"));
	        }
	        else if (nombre.equals("Iglesia | C: "+FOOD_COST_CHURCH+" | M: "+WOOD_COST_CHURCH+" | H: "+IRON_COST_CHURCH+" | Mana: "+MANA_COST_CHURCH)) {
	            miCiv.newChurch();
	            edificiosColocados.add(new EdificioColocado(imgIglesia, x, y, 160, 100, "Iglesia"));
	        }
	    } catch (ResourceException ex) {
	        JOptionPane.showMessageDialog(ventana, "Sin recursos suficientes.");
	    } finally {
	        edificioEnCurso = null;
	        setCursor(Cursor.getDefaultCursor());
	        actualizarUI();
	        repaint();
	    }
	}
	
	private JPanel crearPanelEstado() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(100, 60, 20));
		panel.setPreferredSize(new Dimension(220, 0));
		panel.setBorder(new EmptyBorder(20, 10, 10, 10));
 
		panel.add(Box.createVerticalStrut(8));
		
		Font fontItem = new Font("Arial", Font.PLAIN, 18);
		
		JLabel tituloMateriales = new JLabel("Materiales", JLabel.CENTER);
	
		tituloMateriales.setFont(new Font("Arial", Font.BOLD, 20));
		tituloMateriales.setForeground(new Color(255, 210, 120));
		
		panel.add(tituloMateriales);
		panel.add(Box.createVerticalStrut(6));

		lblComida = estadoLabel("Comida: 0", fontItem);
		lblMadera = estadoLabel("Madera: 0",  fontItem);
		lblHierro = estadoLabel("Hierro: 0", fontItem);
		lblMana = estadoLabel("Mana: 0", fontItem);
		lblBatallas = estadoLabel("Batallas: 0", fontItem);
 
		panel.add(lblComida);
		panel.add(lblMadera);
		panel.add(lblHierro);
		panel.add(lblMana);
		panel.add(lblBatallas);
		
		panel.add(Box.createVerticalStrut(30));
		
		JLabel tituloEdificios = new JLabel("Edificios", JLabel.CENTER);
		
		tituloEdificios.setFont(new Font("Arial", Font.BOLD, 20));
		tituloEdificios.setForeground(new Color(255, 210, 120));
		
		panel.add(tituloEdificios);
		panel.add(Box.createVerticalStrut(6));

		lblGranjas = estadoLabel("Granjas: 0", fontItem);
		lblCarpinterias = estadoLabel("Carpinterías: 0", fontItem);
		lblHerrerias = estadoLabel("Herrerías: 0", fontItem);
		lblTorresMagicas = estadoLabel("Torres mágicas: 0", fontItem);
		lblIglesias = estadoLabel("Iglesias: 0", fontItem);
 
		panel.add(lblGranjas);
		panel.add(lblCarpinterias);
		panel.add(lblHerrerias);
		panel.add(lblTorresMagicas);
		panel.add(lblIglesias);
		
		panel.add(Box.createVerticalStrut(30));
		
		JLabel tituloUnidadesAtaque = new JLabel("Unidades Ataque", JLabel.CENTER);
		
		tituloUnidadesAtaque.setFont(new Font("Arial", Font.BOLD, 20));
		tituloUnidadesAtaque.setForeground(new Color(255, 210, 120));
		
		panel.add(tituloUnidadesAtaque);
		panel.add(Box.createVerticalStrut(6));

		lblEspadachin = estadoLabel("Espadachín: 0", fontItem);
		lblLancero = estadoLabel("Lancero: 0", fontItem);
		lblBallesta = estadoLabel("Ballesta: 0", fontItem);
		lblCanion = estadoLabel("Cañón: 0", fontItem);
		
		panel.add(lblEspadachin);
		panel.add(lblLancero);
		panel.add(lblBallesta);
		panel.add(lblCanion);
		
		panel.add(Box.createVerticalStrut(30));
		
		JLabel tituloUnidadesDefensa = new JLabel("Unidades Defensa", JLabel.CENTER);
		
		tituloUnidadesDefensa.setFont(new Font("Arial", Font.BOLD, 20));
		tituloUnidadesDefensa.setForeground(new Color(255, 210, 120));
		
		panel.add(tituloUnidadesDefensa);
		panel.add(Box.createVerticalStrut(6));
		
		lblTorreLanza = estadoLabel("Torre lanza: 0", fontItem);
		lblCatapulta = estadoLabel("Catapulta: 0", fontItem);
		lblTorreCohete = estadoLabel("Torre cohete: 0", fontItem);
		
		panel.add(lblTorreLanza);
		panel.add(lblCatapulta);
		panel.add(lblTorreCohete);

		panel.add(Box.createVerticalStrut(30));
		
		JLabel tituloUnidadesEspeciales = new JLabel("Unid. Especiales", JLabel.CENTER);
		
		tituloUnidadesEspeciales.setFont(new Font("Arial", Font.BOLD, 20));
		tituloUnidadesEspeciales.setForeground(new Color(255, 210, 120));
		
		panel.add(tituloUnidadesEspeciales);
		panel.add(Box.createVerticalStrut(6));

		lblMago = estadoLabel("Mago: 0", fontItem);
		lblSacerdote = estadoLabel("Sacerdote: 0", fontItem);
		
		panel.add(lblMago);
		panel.add(lblSacerdote);
		
		panel.add(Box.createVerticalStrut(30));
		
		JLabel tituloTecnologias = new JLabel("Tecnologías", JLabel.CENTER);
		
		tituloTecnologias.setFont(new Font("Arial", Font.BOLD, 20));
		tituloTecnologias.setForeground(new Color(255, 210, 120));
		
		panel.add(tituloTecnologias);
		panel.add(Box.createVerticalStrut(6));

		lblTecnoAtt = estadoLabel("Tecnologia Ataque: 0", fontItem);
		lblTecnoDef = estadoLabel("Tecnologia Defensa: 0", fontItem);
		
		panel.add(lblTecnoAtt);
		panel.add(lblTecnoDef);
 
		return panel;
	}
 
	private JLabel estadoLabel(String texto, Font font) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(font);
		lbl.setForeground(new Color(200, 200, 200));
		return lbl;
	}

	private void actualizarUI() {
		lblComida.setText("Comida: " + miCiv.getFood());
		lblMadera.setText("Madera: " + miCiv.getWood());
		lblHierro.setText("Hierro: " + miCiv.getIron());
		lblMana.setText("Maná: " + miCiv.getMana());

		lblGranjas.setText("Granjas: " + miCiv.getFarm());
		lblCarpinterias.setText("Carpinterías: " + miCiv.getCarpentry());
		lblHerrerias.setText("Herrerías: " + miCiv.getSmithy());
		lblTorresMagicas.setText("Torres mágicas: " + miCiv.getMagicTower());
		lblIglesias.setText("Iglesias: " + miCiv.getChurch());
		
		lblEspadachin.setText("Espadachín: " + miCiv.getArmy().get(0).size());
		lblLancero.setText("Lancero: " + miCiv.getArmy().get(1).size());
		lblBallesta.setText("Ballesta: " + miCiv.getArmy().get(2).size());
		lblCanion.setText("Cañón: " + miCiv.getArmy().get(3).size());

		lblTorreLanza.setText("Torre lanza: " + miCiv.getArmy().get(4).size());
		lblCatapulta.setText("Catapulta: " + miCiv.getArmy().get(5).size());
		lblTorreCohete.setText("Torre cohete: " + miCiv.getArmy().get(6).size());
 
		lblMago.setText("Mago: " + miCiv.getArmy().get(7).size());
		lblSacerdote.setText("Sacerdote: " + miCiv.getArmy().get(8).size());
		
		lblTecnoAtt.setText("Tecnologia Ataque: " + miCiv.getTechnologyAtack());
		lblTecnoDef.setText("Tecnologia Defensa: " + miCiv.getTechnologyDefense());
		
		lblBatallas.setText("Batallas: " + miCiv.getBattles());
		
		repaint();
	}

	private void abrirVentanaEdificios() {
	    JFrame ventana = ventanaSecundaria("Construcción de Edificios", 520, 420);
	    JPanel contenido = new JPanel(new GridLayout(0, 1, 4, 4));
	    contenido.setBackground(new Color(40, 40, 40));
	    contenido.setBorder(new EmptyBorder(12, 12, 12, 12));

	    contenido.add(crearBotonConVentana("Granja | C: "+FOOD_COST_FARM+" | M: "+WOOD_COST_FARM+" | H: "+IRON_COST_FARM, new Color(30, 100, 50), ventana));
	    contenido.add(crearBotonConVentana("Carpintería | C: "+FOOD_COST_CARPENTRY+" | M: "+WOOD_COST_CARPENTRY+" | H: "+IRON_COST_CARPENTRY, new Color(30, 100, 50), ventana));
	    contenido.add(crearBotonConVentana("Herrería | C: "+FOOD_COST_SMITHY+" | M: "+WOOD_COST_SMITHY+" | H: "+IRON_COST_SMITHY, new Color(30, 100, 50), ventana));
	    contenido.add(crearBotonConVentana("Torre mágica | C: "+FOOD_COST_MAGICTOWER+" | M: "+WOOD_COST_MAGICTOWER+" | H: "+IRON_COST_MAGICTOWER, new Color(30, 100, 50), ventana));
	    contenido.add(crearBotonConVentana("Iglesia | C: "+FOOD_COST_CHURCH+" | M: "+WOOD_COST_CHURCH+" | H: "+IRON_COST_CHURCH+" | Mana: "+MANA_COST_CHURCH, new Color(30, 100, 50), ventana));

	    ventana.add(contenido);
	    ventana.setVisible(true);
	}
	
	private void abrirVentanaUnidades(String titulo) {
		JFrame ventana = ventanaSecundaria(titulo, 520, 420);
		JPanel contenido = new JPanel(new GridLayout(0, 1, 4, 4));
		contenido.setBackground(new Color(40, 40, 40));
		contenido.setBorder(new EmptyBorder(12, 12, 12, 12));
 
		boolean esAtaque   = titulo.contains("Ataque");
		boolean esDefensa  = titulo.contains("Defensa");
		boolean esEspecial = titulo.contains("Especial");
		boolean esTecnologia = titulo.contains("Tecnologías");
 
		if (esAtaque) {
		    contenido.add(crearBotonConVentana("Espadachín | C: "+FOOD_COST_SWORDSMAN+" | M: "+WOOD_COST_SWORDSMAN+" | H: "+IRON_COST_SWORDSMAN, Color.RED, ventana));
		    contenido.add(crearBotonConVentana("Lancero | C: "+FOOD_COST_SPEARMAN+" | M: "+WOOD_COST_SPEARMAN+" | H: "+IRON_COST_SPEARMAN, Color.RED, ventana));
		    contenido.add(crearBotonConVentana("Ballesta | M: "+WOOD_COST_CROSSBOW+" | H: "+IRON_COST_CROSSBOW, Color.RED, ventana));
		    contenido.add(crearBotonConVentana("Cañón | M: "+WOOD_COST_CANNON+" | H: "+IRON_COST_CANNON, Color.RED, ventana));
		} else if (esDefensa) {
		    contenido.add(crearBotonConVentana("Torre lanza | M: "+WOOD_COST_ARROWTOWER, Color.BLUE, ventana));
		    contenido.add(crearBotonConVentana("Catapulta | M: "+WOOD_COST_CATAPULT+" | H: "+IRON_COST_CATAPULT, Color.BLUE, ventana));
		    contenido.add(crearBotonConVentana("Torre cohete | M: "+WOOD_COST_ROCKETLAUNCHERTOWER+" | H: "+IRON_COST_ROCKETLAUNCHERTOWER, Color.BLUE, ventana));
		} else if (esEspecial) {
		    contenido.add(crearBotonConVentana("Mago | C: "+FOOD_COST_MAGICIAN+" | M: "+WOOD_COST_MAGICIAN+" | H: "+IRON_COST_MAGICIAN+" | Mana: "+MANA_COST_MAGICIAN, Color.PINK, ventana));
		    contenido.add(crearBotonConVentana("Sacerdote | C: "+FOOD_COST_PRIEST+" | Mana: "+MANA_COST_PRIEST, Color.PINK, ventana));
		} else if (esTecnologia) {
		    contenido.add(crearBotonConVentana("Mejorar Tecnologia Ataque | H: "+UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST, Color.RED, ventana));
		    contenido.add(crearBotonConVentana("Mejorar Tecnologia Defensa | H: "+UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST, Color.GREEN, ventana));
		}

		ventana.add(contenido);
		ventana.setVisible(true);
	}
	
	private JButton crearBotonConVentana(String texto, Color color, JFrame ventanaOrigen) {
	    JButton btn = crearBoton(texto, color);
	    btn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            ventanaOrigen.dispose();
	        }
	    });
	    return btn;
	}
	
	private JButton crearBoton(String texto, Color color) {
		JButton btn = new JButton(texto);
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, 16));
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.addActionListener(this);
		return btn;
	}
	
	private JFrame ventanaSecundaria(String titulo, int ancho, int alto) {
		JFrame ventana = new JFrame(titulo);
		ventana.setBounds(0, 0, ancho, alto);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(this);
		ventana.getContentPane().setBackground(new Color(30, 30, 30));
		ventana.setLayout(new BorderLayout());
		return ventana;
	}
	
	private void cargarEdificiosGuardados() {
	    String[][] datos = conexion.cargarEdificios(idCiv);
	    for (String[] fila : datos) {
	        String tipo = fila[0];
	        int x = Integer.parseInt(fila[1]);
	        int y = Integer.parseInt(fila[2]);

	        Image img = null;
	        int ancho = 100;
	        int alto = 100;
	        
	        if (tipo.equals("Granja")) {
	            img = imgGranja;
	            ancho = 200;
	            alto = 100;
	        } else if (tipo.equals("Carpintería")) {
	            img = imgCarpinteria;
	            ancho = 180;
	            alto = 80;
	        } else if (tipo.equals("Herrería")) {
	            img = imgHerreria;
	            ancho = 120;
	            alto = 120;
	        } else if (tipo.equals("Torre mágica")) {
	            img = imgTorre_magica;
	            ancho = 50;
	            alto = 100;
	        } else if (tipo.equals("Iglesia")) {
	            img = imgIglesia;
	            ancho = 160;
	            alto = 100;
	        }

	        if (img != null) {
	            edificiosColocados.add(new EdificioColocado(img, x, y, ancho, alto, tipo));
	        }
	    }
	    repaint();
	}
	
	public int comprarUnidadesCantidad() {
		String cantidad = JOptionPane.showInputDialog(ventana,"Cuantos quieres adquirir?");
		
		if (cantidad != null) {
			try { 
				return Integer.parseInt(cantidad);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}
 
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("Edificios"))  {
				abrirVentanaEdificios();
            }
			if (e.getActionCommand().equals("Unidades ataque"))  {
				abrirVentanaUnidades("Unidades de Ataque");
            }
			if (e.getActionCommand().equals("Unidades defensa"))  {
				abrirVentanaUnidades("Unidades de Defensa");
            }
			if (e.getActionCommand().equals("Unidades especiales"))  {
				abrirVentanaUnidades("Unidades Especiales");
            }
			if (e.getActionCommand().equals("Tecnologías"))  {
				abrirVentanaUnidades("Tecnologías");
            }
			if (e.getActionCommand().equals("Guardar Partida"))  {
				conexion.guardarPartida(miCiv, idCiv, userID);
				conexion.guardarEdificios(idCiv, edificiosColocados);
				JOptionPane.showMessageDialog(this, "¡Partida guardada!", "Guardado", JOptionPane.INFORMATION_MESSAGE);
            }
			if (e.getActionCommand().equals("Cerrar sesión"))  {
				int confirm = JOptionPane.showConfirmDialog(ventana, "¿Cerrar sesión y volver al menú principal?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					ventana.mostrarInicioSesion();
				}
            }
			
			
			if (e.getActionCommand().equals("Granja | C: "+FOOD_COST_FARM+" | M: "+WOOD_COST_FARM+" | H: "+IRON_COST_FARM) || 
				    e.getActionCommand().equals("Carpintería | C: "+FOOD_COST_CARPENTRY+" | M: "+WOOD_COST_CARPENTRY+" | H: "+IRON_COST_CARPENTRY) || 
				    e.getActionCommand().equals("Herrería | C: "+FOOD_COST_SMITHY+" | M: "+WOOD_COST_SMITHY+" | H: "+IRON_COST_SMITHY) || 
				    e.getActionCommand().equals("Torre mágica | C: "+FOOD_COST_MAGICTOWER+" | M: "+WOOD_COST_MAGICTOWER+" | H: "+IRON_COST_MAGICTOWER) || 
				    e.getActionCommand().equals("Iglesia | C: "+FOOD_COST_CHURCH+" | M: "+WOOD_COST_CHURCH+" | H: "+IRON_COST_CHURCH+" | Mana: "+MANA_COST_CHURCH)) {
				    
				    edificioEnCurso = e.getActionCommand();
				    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				}

            if (e.getActionCommand().equals("Dar Items"))  {
            	miCiv.setFood(miCiv.getFood()+1000000);
        		miCiv.setWood(miCiv.getWood()+1000000);
        		miCiv.setMana(miCiv.getMana()+1000000);
        		miCiv.setIron(miCiv.getIron()+1000000);
            }
            
            // --- UNIDADES DE ATAQUE ---
            if (e.getActionCommand().equals("Espadachín | C: "+FOOD_COST_SWORDSMAN+" | M: "+WOOD_COST_SWORDSMAN+" | H: "+IRON_COST_SWORDSMAN)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newSwordsman(cantidad);
            }
            else if (e.getActionCommand().equals("Lancero | C: "+FOOD_COST_SPEARMAN+" | M: "+WOOD_COST_SPEARMAN+" | H: "+IRON_COST_SPEARMAN)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newSpearman(cantidad);
            }
            else if (e.getActionCommand().equals("Ballesta | M: "+WOOD_COST_CROSSBOW+" | H: "+IRON_COST_CROSSBOW)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newCrossbow(cantidad);
            }
            else if (e.getActionCommand().equals("Cañón | M: "+WOOD_COST_CANNON+" | H: "+IRON_COST_CANNON)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newCannon(cantidad);
            }

            // --- UNIDADES DE DEFENSA ---
            else if (e.getActionCommand().equals("Torre lanza | M: "+WOOD_COST_ARROWTOWER)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newArrowTower(cantidad);
            }
            else if (e.getActionCommand().equals("Catapulta | M: "+WOOD_COST_CATAPULT+" | H: "+IRON_COST_CATAPULT)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newCatapult(cantidad);
            }
            else if (e.getActionCommand().equals("Torre cohete | M: "+WOOD_COST_ROCKETLAUNCHERTOWER+" | H: "+IRON_COST_ROCKETLAUNCHERTOWER)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newRocketLauncher(cantidad);
            }

            // --- UNIDADES ESPECIALES ---
            else if (e.getActionCommand().equals("Mago | C: "+FOOD_COST_MAGICIAN+" | M: "+WOOD_COST_MAGICIAN+" | H: "+IRON_COST_MAGICIAN+" | Mana: "+MANA_COST_MAGICIAN)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newMagician(cantidad);
            }
            else if (e.getActionCommand().equals("Sacerdote | C: "+FOOD_COST_PRIEST+" | Mana: "+MANA_COST_PRIEST)) {
                int cantidad = comprarUnidadesCantidad();
                miCiv.newPriest(cantidad);
            }

            // --- TECNOLOGÍA ---
            else if (e.getActionCommand().equals("Mejorar Tecnologia Ataque | H: "+UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST)) {
                miCiv.upgradeTechnologyAttack();
            }
            else if (e.getActionCommand().equals("Mejorar Tecnologia Defensa | H: "+UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST)) {
                miCiv.upgradeTechnologyDefense();
            }
            
            actualizarUI();
            repaint();
    	} catch (ResourceException ex) {
            JOptionPane.showMessageDialog(ventana, "Sin recursos suficientes");
        } catch (BuildingException ex) {
            JOptionPane.showMessageDialog(ventana, "Sin recursos suficientes");
        }
	}
}