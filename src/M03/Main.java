package M03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Timer;


public class Main {

	public static void main(String[] args) {
		{
			Civilization civilization = new Civilization();
	 
			Timer timer = new Timer(true);
			ResourceGenerator resourceGenerator = new ResourceGenerator(civilization);
			timer.scheduleAtFixedRate(resourceGenerator, 10000, 10000);
	 
			Scanner scanner = new Scanner(System.in);
			int opcion = 0;
	 
			while (opcion != 9) {
				System.out.println("\n====== ");
				System.out.println("1. Construir Granja");
				System.out.println("2. Construir Carpinteria");
				System.out.println("3. Construir Herreria");
				System.out.println("4. Construir Iglesia");
				System.out.println("5. Construir Torre Magica");
				System.out.println("6. Mejorar tecnologia de Defensa");
				System.out.println("7. print");
				System.out.println("8. crear espadachin");
				System.out.println("9. Salir");
				System.out.print("Opcion: ");
	 
				opcion = scanner.nextInt();
	 
				try {
					if (opcion == 1) {
						civilization.newFarm();
					} else if (opcion == 2) {
						civilization.newCarpentry();
					} else if (opcion == 3) {
						civilization.newSmithy();
					} else if (opcion == 4) {
						civilization.newChurch();
					} else if (opcion == 5) {
						civilization.newMagicTower();
					} else if (opcion == 6) {
						civilization.upgradeTechnologyDefense();
					} else if (opcion == 7) {
						civilization.printStats();
					} else if (opcion == 8) {
						civilization.newSwordsman(1);
						
					} else if (opcion == 9) {
						System.out.println("Saliendo...");
					} else {
						System.out.println("Opcion no valida.");
					}
				} catch (ResourceException e) {
					System.out.println("Error: " + e.getMessage());
				}
			}
	 
			timer.cancel();
			scanner.close();
		}  
	
	

}
}
