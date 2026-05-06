package M03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // 1. Configuramos los parámetros del servidor y la BD
    // Nota: Usamos localhost si el programa corre en el mismo servidor que la BD.
    // Si corre desde tu casa, cambia localhost por ieticloudpro.ieti.cat
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://ieticloudpro.ieti.cat:3306/civilizations_db?serverTimezone=UTC";
    private final String USER = "admincivil6"; // El usuario que creamos para Java
    private final String PASSWORD = "Civil6Admin"; // La contraseña que le pusiste

    private Connection connection;

    public ConexionBD() {
        // Constructor vacío
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        // Cargar el driver
        Class.forName(DRIVER);
        
        // Crear la conexión (Teoría: DriverManager.getConnection)
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        
        System.out.println("Conexión a Civilizations_DB establecida con éxito.");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión cerrada.");
        }
    }
}