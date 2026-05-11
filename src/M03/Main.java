package M03;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		ConexionBD conexion = new ConexionBD();
		conexion.connect();
		
		VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(conexion);
	}

}