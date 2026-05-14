package M03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private String driver = "com.mysql.cj.jdbc.Driver";
    // private String urlDatos = "jdbc:mysql://localhost:3307/civilizations_db?serverTimezone=UTC";
    private String urlDatos = "jdbc:mysql://localhost/civilizations_db?serverTimezone=UTC";
    private String user = "admincivil6";
    private String pass = "Civil6Admin";

    private Connection conn;

    public ConexionBD() {
        
    }

    public Connection getConnection() {
        return conn;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public void connect() {
    	try {
    		Class.forName(driver);
    		System.out.println("Driver cargado correctamente");
    		
    		conn = DriverManager.getConnection(urlDatos, user, pass);
    		System.out.println("Conxion a BD establecida con éxito.");
    	} catch (ClassNotFoundException e) {
    		System.out.println("Driver no se ha cargado correctamente!!");
    	} catch (SQLException e) {
    		System.out.println("Conxion no creada correctamente!!");
    	}
    }
    
    public int login(String user, String password) {
    	int userID = -1;
    	
    	String sql = "SELECT user_id FROM Users WHERE username = ? AND password_hash = ?";
    	
    	try {
    		PreparedStatement ps = conn.prepareStatement(sql);
    		ps.setString(1, user);
    		ps.setString(2, password);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		if (rs.next() ) {
    			userID = rs.getInt("user_id");
    			System.out.println("Login exitoso. Bienvenido, " + user+"!");
    		} else {
    			System.out.println("Usuario o contraseña incorrectos.");
    		}
    	} catch (SQLException e) {
            System.out.println("Error al intentar realizar el login.");
            e.printStackTrace();
        }
		return userID;
    }
    
    public boolean createUser(String user, String password) {    	
    	String sql = "SELECT user_id FROM Users WHERE username = ?";
    	String insert = "INSERT INTO Users (username, password_hash) VALUES (?,?)";
    	
    	try {
    		PreparedStatement ps = conn.prepareStatement(sql);
     		ps.setString(1, user);
     		
     		ResultSet rs = ps.executeQuery();
     		if (rs.next() ) {
    			System.out.println("Usuario no disponible");
    			return false;
    		} else {
    			ps = conn.prepareStatement(insert);
    			
    			ps.setString(1, user);
    			ps.setString(2, password);
    			ps.executeUpdate();
    			System.out.println("Usuario creado correctamente!");
    			return true;
    		}
    	} catch (SQLException e) {
             System.out.println("Error al intentar realizar el login.");
             e.printStackTrace();             
        }
    	return false;
    }
    
    public int crearNuevaPartida(int userID, String nombreCiv) {
        int idGenerado = -1;
        String sql = "INSERT INTO Civilization_stats (user_id, name) VALUES (?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, userID);
            ps.setString(2, nombreCiv);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                System.out.println("Partida creada con ID: " + idGenerado);
            }
        } catch (SQLException e) {
            System.out.println("Error: El usuario ya tiene una partida activa o hubo un fallo de conexión.");
            e.printStackTrace();
        }
        return idGenerado;
    }
    
    public void guardarPartida(Civilization civ, int idCiv, int userID) {
        try {
            // 1. Actualizar recursos, edificios y tecnologías en la tabla principal
            String sqlStats = "UPDATE Civilization_stats SET " +
                    "wood_amount = ?, iron_amount = ?, food_amount = ?, mana_amount = ?, " +
                    "magicTower_counter = ?, church_counter = ?, farm_counter = ?, " +
                    "smithy_counter = ?, carpentry_counter = ?, " +
                    "technology_defense_level = ?, technology_attack_level = ?, " +
                    "battles_counter = ? WHERE civilization_id = ? AND user_id = ?";
            
            PreparedStatement ps = conn.prepareStatement(sqlStats);
            ps.setInt(1, civ.getWood());
            ps.setInt(2, civ.getIron());
            ps.setInt(3, civ.getFood());
            ps.setInt(4, civ.getMana());
            ps.setInt(5, civ.getMagicTower());
            ps.setInt(6, civ.getChurch());
            ps.setInt(7, civ.getFarm());
            ps.setInt(8, civ.getSmithy());
            ps.setInt(9, civ.getCarpentry());
            ps.setInt(10, civ.getTechnologyDefense());
            ps.setInt(11, civ.getTechnologyAtack());
            ps.setInt(12, civ.getBattles());
            ps.setInt(13, idCiv);
            ps.setInt(14, userID);
            ps.executeUpdate();

            // 2. Limpiar las tablas de unidades para volcar los objetos actuales
            String[] tablas = {"attack_units_stats", "defense_units_stats", "special_units_stats"};
            for (String tabla : tablas) {
                PreparedStatement del = conn.prepareStatement("DELETE FROM " + tabla + " WHERE civilization_id = ?");
                del.setInt(1, idCiv);
                del.executeUpdate();
            }

            // --- GUARDADO DE OBJETOS INDIVIDUALES ---

            // 3. Unidades de ATAQUE (Índices 0 a 3 en tu ArrayList getArmy())
            String insAtk = "INSERT INTO attack_units_stats (civilization_id, type, armor, base_damage, experience) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psAtk = conn.prepareStatement(insAtk);
            for (int i = 0; i <= 3; i++) {
                for (Object unidadObj : civ.getArmy().get(i)) {
                    // Casteamos a la clase base de tus unidades para usar los getters
                    M03.MilitaryUnit u = (M03.MilitaryUnit) unidadObj;
                    psAtk.setInt(1, idCiv);
                    psAtk.setString(2, u.getClass().getSimpleName());
                    u.get
                    psAtk.setInt(3, u.getActualArmor());
                    psAtk.setInt(4, u.getBaseDamage());
                    psAtk.setInt(5, u.getExperience());
                    psAtk.executeUpdate();
                }
            }

            // 4. Unidades de DEFENSA (Índices 4 a 6)
            String insDef = "INSERT INTO defense_units_stats (civilization_id, type, armor, base_damage, experience) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psDef = conn.prepareStatement(insDef);
            for (int i = 4; i <= 6; i++) {
                for (Object unidadObj : civ.getArmy().get(i)) {
                    M03.MilitaryUnit u = (M03.MilitaryUnit) unidadObj;
                    psDef.setInt(1, idCiv);
                    psDef.setString(2, u.getClass().getSimpleName());
                    psDef.setInt(3, u.getArmor());
                    psDef.setInt(4, u.getBaseDamage());
                    psDef.setInt(5, u.getExperience());
                    psDef.executeUpdate();
                }
            }

            // 5. Unidades ESPECIALES (Índices 7 a 8)
            String insSpec = "INSERT INTO special_units_stats (civilization_id, type, armor, base_damage, experience) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psSpec = conn.prepareStatement(insSpec);
            for (int i = 7; i <= 8; i++) {
                for (Object unidadObj : civ.getArmy().get(i)) {
                    M03.MilitaryUnit u = (M03.MilitaryUnit) unidadObj;
                    psSpec.setInt(1, idCiv);
                    psSpec.setString(2, u.getClass().getSimpleName());
                    psSpec.setInt(3, u.getArmor());
                    psSpec.setInt(4, u.getBaseDamage());
                    psSpec.setInt(5, u.getExperience());
                    psSpec.executeUpdate();
                }
            }

            System.out.println("Partida guardada: Objetos de unidad sincronizados para Civ " + idCiv);

        } catch (SQLException e) {
            System.err.println("Error Crítico al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
