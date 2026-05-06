package M03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/civilizations";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	private Connection connection;

	public ConexionBD() {

	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void connect() throws ClassNotFoundException, SQLException {

	}

	public void disconnect() throws SQLException {

	}

}
