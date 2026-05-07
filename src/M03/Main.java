package M03;

import java.util.Scanner;

public class Main {
	private Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		Main app = new Main();
		
		ConexionBD conexion = new ConexionBD();
		conexion.connect();
		
		int userID = -1;
		userID = app.menuPrincipal(conexion);
		
		
		if (userID != -1) {
			System.out.println("Aqui empezaria el juego");
		}
		
		System.out.println("Saliendo del programa!");
	}
	
	public int menuPrincipal(ConexionBD conexion) {
		boolean login = false;
		while (login == false) {
            int opt = 0;

            System.out.println("\n--- MENU PRINCIPAL ---\n1- Iniciar sesion\n2- Crear usuario\n3- Salir\nSelecciona una opcion: ");

            while (!sc.hasNextInt()) {
                System.out.println("Error: No has metido un numero entero.");
                System.out.print("Selecciona una opcion (1, 2 o 3): ");
                sc.nextLine();
            }
            opt = sc.nextInt();
            sc.nextLine();
            
            if (opt == 1) {
            	System.out.println("\n--- LOGIN ---");
                System.out.print("Usuario: ");
                String user = sc.nextLine();
                System.out.print("Contraseña: ");
                String pass = sc.nextLine();
                
                int userId = conexion.login(user, pass);
                if (userId != -1) {
                	login = true;
                	return userId;
                }
            } else if (opt == 2) {
            	System.out.println("\n--- CREAR USUARIO ---");
                System.out.print("Usuario: ");
                String user = sc.nextLine();
                System.out.print("Contraseña: ");
                String pass = sc.nextLine();
                
                conexion.createUser(user, pass);
            } else if (opt == 3) {
            	break;
            }
		}
		return -1;
	}
}
