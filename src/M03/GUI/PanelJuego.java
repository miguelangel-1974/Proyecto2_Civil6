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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
        	public void run() {
        		actualizarUI();
        	}
        };
        TimerTask guardarPartidaAutomatico = new TimerTask() {
        	public void run() {
        		conexion.guardarPartida(miCiv, idCiv, userID);
				conexion.guardarEdificios(idCiv, edificiosColocados);
        		System.out.println("Se ha guardado la partida");
        	}
        };
        TimerTask batallaAutomatica = new TimerTask() {
        	public void run() {
        		generarEnemigos.createEnemyArmy();
        		Battle batalla = new Battle(miCiv, generarEnemigos.getEnemyArmy());
        		generarEnemigos.viewThreat();
        		batalla.pelear();
        		System.out.println(batalla.getBattleReport(miCiv.getBattles()));
        	}
        };
        timer.scheduleAtFixedRate(task, 0, 50);
        timer.scheduleAtFixedRate(generador, RESOURCES_GENERATOR_TIME, RESOURCES_GENERATOR_TIME);
        timer.scheduleAtFixedRate(guardarPartidaAutomatico, AUTO_SAVE_TIME, AUTO_SAVE_TIME);
        timer.scheduleAtFixedRate(batallaAutomatica, AUTO_GENERATE_BATTLE, AUTO_GENERATE_BATTLE);
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
	        if (nombre.equals("Granja")) {
	        	miCiv.newFarm();
	        	edificiosColocados.add(new EdificioColocado(imgGranja, x, y, 200, 100, "Granja"));
	        }
	        if (nombre.equals("Carpintería")) {
	        	miCiv.newCarpentry();
	        	edificiosColocados.add(new EdificioColocado(imgCarpinteria, x, y, 180, 80, "Carpintería"));
	        }
	        if (nombre.equals("Herrería")) {
	        	miCiv.newSmithy();
	        	edificiosColocados.add(new EdificioColocado(imgHerreria, x, y, 120, 120, "Herrería"));
	        }
	        if (nombre.equals("Torre mágica")) {
	        	miCiv.newMagicTower();
	        	edificiosColocados.add(new EdificioColocado(imgTorre_magica, x, y, 50, 100, "Torre mágica"));
	        }
	        if (nombre.equals("Iglesia")) {
	        	miCiv.newChurch();
	        	edificiosColocados.add(new EdificioColocado(imgIglesia, x, y, 160, 100, "Iglesia"));
	        }
	    } catch (ResourceException ex) {
	        JOptionPane.showMessageDialog(ventana, "Sin recursos suficientes para: " + nombre);
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
		
		repaint();
	}

	private void abrirVentanaEdificios() {
		JFrame ventana = ventanaSecundaria("Construcción de Edificios", 400, 300);
		JPanel contenido = new JPanel(new GridLayout(0, 1, 4, 4));
		contenido.setBackground(new Color(40, 40, 40));
		contenido.setBorder(new EmptyBorder(12, 12, 12, 12));
 
		contenido.add(crearBotonConVentana("Granja", new Color(30, 100, 50), ventana));
		contenido.add(crearBotonConVentana("Carpintería", new Color(30, 100, 50), ventana));
		contenido.add(crearBotonConVentana("Herrería", new Color(30, 100, 50), ventana));
		contenido.add(crearBotonConVentana("Torre mágica", new Color(30, 100, 50), ventana));
		contenido.add(crearBotonConVentana("Iglesia", new Color(30, 100, 50), ventana));
 
		ventana.add(contenido);
		ventana.setVisible(true);
	}
	
	private void abrirVentanaUnidades(String titulo) {
		JFrame ventana = ventanaSecundaria(titulo, 420, 320);
		JPanel contenido = new JPanel(new GridLayout(0, 1, 4, 4));
		contenido.setBackground(new Color(40, 40, 40));
		contenido.setBorder(new EmptyBorder(12, 12, 12, 12));
 
		boolean esAtaque   = titulo.contains("Ataque");
		boolean esDefensa  = titulo.contains("Defensa");
		boolean esEspecial = titulo.contains("Especial");
		boolean esTecnologia = titulo.contains("Tecnologías");
 
		if (esAtaque) {
			contenido.add(crearBotonConVentana("Espadachín",  Color.RED, ventana));
			contenido.add(crearBotonConVentana("Lancero", Color.RED, ventana));
			contenido.add(crearBotonConVentana("Ballesta", Color.RED, ventana));
			contenido.add(crearBotonConVentana("Cañón", Color.RED, ventana));
		} else if (esDefensa) {
			contenido.add(crearBotonConVentana("Torre lanza", Color.BLUE, ventana));
			contenido.add(crearBotonConVentana("Catapulta", Color.BLUE, ventana));
			contenido.add(crearBotonConVentana("Torre cohete", Color.BLUE, ventana));
		} else if (esEspecial) {
			contenido.add(crearBotonConVentana("Mago", Color.PINK, ventana));
			contenido.add(crearBotonConVentana("Sacerdote", Color.PINK, ventana));
		} else if (esTecnologia) {
			contenido.add(crearBotonConVentana("Mejorar Tecnologia Ataque", Color.RED, ventana));
			contenido.add(crearBotonConVentana("Mejorar Tecnologia Defensa", Color.GREEN, ventana));
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
			
			
            if (e.getActionCommand().equals("Granja") || e.getActionCommand().equals("Carpintería") || e.getActionCommand().equals("Herrería") || 
            		e.getActionCommand().equals("Torre mágica") || e.getActionCommand().equals("Iglesia"))  {
            	edificioEnCurso = e.getActionCommand();
            	setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }

            if (e.getActionCommand().equals("Dar Items"))  {
            	miCiv.setFood(miCiv.getFood()+1000000);
        		miCiv.setWood(miCiv.getWood()+1000000);
        		miCiv.setMana(miCiv.getMana()+1000000);
        		miCiv.setIron(miCiv.getIron()+1000000);
            }
            
            if (e.getActionCommand().equals("Espadachín")) {
            	miCiv.newSwordsman(10);
            }
            if (e.getActionCommand().equals("Lancero")) {
            	miCiv.newSpearman(10);
            }
            if (e.getActionCommand().equals("Ballesta")) {
            	miCiv.newCrossbow(10);
            }
            if (e.getActionCommand().equals("Cañón")) {
            	miCiv.newCannon(10);
            }
            if (e.getActionCommand().equals("Torre lanza")) {
            	miCiv.newArrowTower(10);
            }
            if (e.getActionCommand().equals("Catapulta")) {
            	miCiv.newCatapult(10);
            }
            if (e.getActionCommand().equals("Torre cohete")) {
            	miCiv.newRocketLauncher(10);
            }
            if (e.getActionCommand().equals("Mago")) {
            	miCiv.newMagician(10);
            }
            if (e.getActionCommand().equals("Sacerdote")) {
				miCiv.newPriest(10);
            }
            
            if (e.getActionCommand().equals("Mejorar Tecnologia Ataque")) {
            	miCiv.upgradeTechnologyAttack();
            }
            if (e.getActionCommand().equals("Mejorar Tecnologia Defensa")) {
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