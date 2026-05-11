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
}
