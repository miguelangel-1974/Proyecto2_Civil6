package M03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		String urlDatos = "jdbc:mysql://localhost:3307/civilizations_db?serverTimezone=UTC";
		String user = "admincivil6";
		String pass = "Civil6Admin";
		
		try {
			// Cargar driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver cargado correctamente");
			
			// Crear conexion con la BBDD
			Connection conn = DriverManager.getConnection(urlDatos, user, pass);
			System.out.println("Conxion creada correctamente");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Driver no se ha cargado correctamente!!");
		} catch (SQLException e) {
			System.out.println("Conxion no creada correctamente!!");
		}
	}

}
