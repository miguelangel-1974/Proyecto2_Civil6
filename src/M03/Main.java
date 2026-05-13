package M03;

import M03.GUI.VentanaPrincipal;

public class Main {
	
	public static void main(String[] args) {
		ConexionBD conexion = new ConexionBD();
		conexion.connect();
		
		VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(conexion);
	}
}
