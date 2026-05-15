package M03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import M03.GUI.*;

public class ConexionBD {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String urlDatos = "jdbc:mysql://localhost:3307/civilizations_db?serverTimezone=UTC";
    // private String urlDatos = "jdbc:mysql://localhost/civilizations_db?serverTimezone=UTC";
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
    	String[] tiposAtaque   = {"Swordsman", "Spearman", "Crossbow", "Cannon"};
	    String[] tiposDefensa  = {"ArrowTower", "Catapult", "RocketLauncherTower"};
	    String[] tiposEspecial = {"Magician", "Priest"};
	
	    try {
	        String sqlCiv = "UPDATE Civilization_stats SET " + "wood_amount=?, iron_amount=?, food_amount=?, mana_amount=?, " +
	            "magicTower_counter=?, church_counter=?, farm_counter=?, " + "smithy_counter=?, carpentry_counter=?, " +
	            "technology_defense_level=?, technology_attack_level=?, battles_counter=? " + "WHERE civilization_id=?";
	
	        PreparedStatement psCiv = conn.prepareStatement(sqlCiv);
	        psCiv.setInt(1,  civ.getWood());
	        psCiv.setInt(2,  civ.getIron());
	        psCiv.setInt(3,  civ.getFood());
	        psCiv.setInt(4,  civ.getMana());
	        psCiv.setInt(5,  civ.getMagicTower());
	        psCiv.setInt(6,  civ.getChurch());
	        psCiv.setInt(7,  civ.getFarm());
	        psCiv.setInt(8,  civ.getSmithy());
	        psCiv.setInt(9,  civ.getCarpentry());
	        psCiv.setInt(10, civ.getTechnologyDefense());
	        psCiv.setInt(11, civ.getTechnologyAtack());
	        psCiv.setInt(12, civ.getBattles());
	        psCiv.setInt(13, idCiv);
	        psCiv.executeUpdate();
	        System.out.println("Civilization_stats actualizada.");
	

	        PreparedStatement psDel;
	        psDel = conn.prepareStatement("DELETE FROM attack_units_stats  WHERE civilization_id=?");
	        psDel.setInt(1, idCiv); psDel.executeUpdate();
	        psDel = conn.prepareStatement("DELETE FROM defense_units_stats WHERE civilization_id=?");
	        psDel.setInt(1, idCiv); psDel.executeUpdate();
	        psDel = conn.prepareStatement("DELETE FROM special_units_stats WHERE civilization_id=?");
	        psDel.setInt(1, idCiv); psDel.executeUpdate();
	
	         
	        String sqlAtaque = "INSERT INTO attack_units_stats " + "(civilization_id, unit_id, type, armor, base_damage, experience, sanctified) " +
	            "VALUES (?,?,?,?,?,?,?)";
	        PreparedStatement psAtaque = conn.prepareStatement(sqlAtaque);
	
	        int unitId = 1;
	        for (int i = 0; i <= 3; i++) {
	            for (MilitaryUnit u : civ.getArmy().get(i)) {
	                AttackUnit au = (AttackUnit) u;
	                psAtaque.setInt(1, idCiv);
	                psAtaque.setInt(2, unitId++);
	                psAtaque.setString(3, tiposAtaque[i]);
	                psAtaque.setInt(4, au.getArmor());
	                psAtaque.setInt(5, au.getBaseDamage());
	                psAtaque.setInt(6, au.getExperience());
	                psAtaque.setBoolean(7, au.isSanctified());
	                psAtaque.addBatch();
	            }
	        }
	        psAtaque.executeBatch();
	        System.out.println("Unidades de ataque guardadas.");
	
	        String sqlDefensa = "INSERT INTO defense_units_stats " + "(civilization_id, unit_id, type, armor, base_damage, experience, sanctified) " +
	            "VALUES (?,?,?,?,?,?,?)";
	        PreparedStatement psDefensa = conn.prepareStatement(sqlDefensa);
	
	        unitId = 1;
	        for (int i = 4; i <= 6; i++) {
	            for (MilitaryUnit u : civ.getArmy().get(i)) {
	                DefenseUnit du = (DefenseUnit) u;
	                psDefensa.setInt(1, idCiv);
	                psDefensa.setInt(2, unitId++);
	                psDefensa.setString(3, tiposDefensa[i - 4]);
	                psDefensa.setInt(4, du.getArmor());
	                psDefensa.setInt(5, du.getBaseDamage());
	                psDefensa.setInt(6, du.getExperience());
	                psDefensa.setBoolean(7, du.isSanctified());
	                psDefensa.addBatch();
	            }
	        }
	        psDefensa.executeBatch();
	        System.out.println("Unidades de defensa guardadas.");
	        
	        String sqlEspecial = "INSERT INTO special_units_stats " + "(civilization_id, unit_id, type, armor, base_damage, experience) " +
	            "VALUES (?,?,?,?,?,?)";
	        PreparedStatement psEspecial = conn.prepareStatement(sqlEspecial);
	
	        unitId = 1;
	        for (int i = 7; i <= 8; i++) {
	            for (MilitaryUnit u : civ.getArmy().get(i)) {
	                SpecialUnit su = (SpecialUnit) u;
	                psEspecial.setInt(1, idCiv);
	                psEspecial.setInt(2, unitId++);
	                psEspecial.setString(3, tiposEspecial[i - 7]);
	                psEspecial.setInt(4, su.getArmor());
	                psEspecial.setInt(5, su.getBaseDamage());
	                psEspecial.setInt(6, su.getExperience());
	                psEspecial.addBatch();
	            }
	        }
	        psEspecial.executeBatch();
	        System.out.println("Unidades especiales guardadas.");
	
	        System.out.println("Partida guardada correctamente. (civilization_id=" + idCiv + ")");
	
	    } catch (SQLException e) {
	        System.out.println("Error al guardar la partida.");
	        e.printStackTrace();
	    }
    }
    
    public String[][] cargarPartidas(int userID) {
        String sql = "SELECT civilization_id, name FROM Civilization_stats WHERE user_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
            	count += 1;
            }

            rs = ps.executeQuery();

            String[][] partidas = new String[count][2];
            int i = 0;
            while (rs.next()) {
                partidas[i][0] = String.valueOf(rs.getInt("civilization_id")); // id como String
                partidas[i][1] = rs.getString("name");
                i += 1;
            }
            return partidas;

        } catch (SQLException e) {
            System.out.println("Error al cargar las partidas.");
            e.printStackTrace();
        }
        return new String[0][2];
    }
    
    public Civilization cargarCivilization(int idCiv) {
        Civilization civ = null;
     
        String sqlCiv =
            "SELECT wood_amount, iron_amount, food_amount, mana_amount, " +
            "magicTower_counter, church_counter, farm_counter, smithy_counter, carpentry_counter, " +
            "technology_defense_level, technology_attack_level, battles_counter " +
            "FROM Civilization_stats WHERE civilization_id = ?";
     
        try {
            PreparedStatement ps = conn.prepareStatement(sqlCiv);
            ps.setInt(1, idCiv);
            ResultSet rs = ps.executeQuery();
     
            if (!rs.next()) {
                System.out.println("No se encontró la partida con id=" + idCiv);
                return null;
            }
     
            civ = new Civilization();
            civ.setWood(rs.getInt("wood_amount"));
            civ.setIron(rs.getInt("iron_amount"));
            civ.setFood(rs.getInt("food_amount"));
            civ.setMana(rs.getInt("mana_amount"));
            civ.setMagicTower(rs.getInt("magicTower_counter"));
            civ.setChurch(rs.getInt("church_counter"));
            civ.setFarm(rs.getInt("farm_counter"));
            civ.setSmithy(rs.getInt("smithy_counter"));
            civ.setCarpentry(rs.getInt("carpentry_counter"));
            civ.setTechnologyDefense(rs.getInt("technology_defense_level"));
            civ.setTechnologyAtack(rs.getInt("technology_attack_level"));
            civ.setBattles(rs.getInt("battles_counter"));
     
            String sqlAtaque =
                "SELECT type, armor, base_damage, experience, sanctified " +
                "FROM attack_units_stats WHERE civilization_id = ? ORDER BY unit_id";
     
            PreparedStatement psA = conn.prepareStatement(sqlAtaque);
            psA.setInt(1, idCiv);
            ResultSet rsA = psA.executeQuery();
     
            while (rsA.next()) {
                String tipoUnidad    = rsA.getString("type");
                int armadura         = rsA.getInt("armor");
                int dañoBase         = rsA.getInt("base_damage");
                int experiencia      = rsA.getInt("experience");
                boolean santificado  = rsA.getBoolean("sanctified");

                AttackUnit nuevaUnidad = null;
                int indiceEjercito = -1;

                if (tipoUnidad.equals("Swordsman")) {
                    nuevaUnidad = new Swordsman(armadura, dañoBase);
                    indiceEjercito = 0;
                } else if (tipoUnidad.equals("Spearman")) {
                    nuevaUnidad = new Spearman(armadura, dañoBase);
                    indiceEjercito = 1;
                } else if (tipoUnidad.equals("Crossbow")) {
                    nuevaUnidad = new Crossbow(armadura, dañoBase);
                    indiceEjercito = 2;
                } else if (tipoUnidad.equals("Cannon")) {
                    nuevaUnidad = new Cannon(armadura, dañoBase);
                    indiceEjercito = 3;
                }

                if (nuevaUnidad != null && indiceEjercito >= 0) {
                    nuevaUnidad.setExperience(experiencia);
                    nuevaUnidad.setSanctified(santificado);
                    civ.getArmy().get(indiceEjercito).add(nuevaUnidad);
                }
            }
     
            String sqlDef =
                "SELECT type, armor, base_damage, experience, sanctified " +
                "FROM defense_units_stats WHERE civilization_id = ? ORDER BY unit_id";
     
            PreparedStatement psD = conn.prepareStatement(sqlDef);
            psD.setInt(1, idCiv);
            ResultSet rsD = psD.executeQuery();
     
            while (rsD.next()) {
                String tipoDefensa      = rsD.getString("type");
                int armadura            = rsD.getInt("armor");
                int dañoBase            = rsD.getInt("base_damage");
                int experiencia         = rsD.getInt("experience");
                boolean estaSantificado = rsD.getBoolean("sanctified");

                DefenseUnit nuevaDefensa = null;
                int indiceEjercito = -1;

                if (tipoDefensa.equals("ArrowTower")) {
                    nuevaDefensa = new ArrowTower(armadura, dañoBase);
                    indiceEjercito = 4;
                } else if (tipoDefensa.equals("Catapult")) {
                    nuevaDefensa = new Catapult(armadura, dañoBase);
                    indiceEjercito = 5;
                } else if (tipoDefensa.equals("RocketLauncherTower")) {
                    nuevaDefensa = new RocketLauncherTower(armadura, dañoBase);
                    indiceEjercito = 6;
                }

                if (nuevaDefensa != null && indiceEjercito >= 0) {
                    nuevaDefensa.setExperience(experiencia);
                    nuevaDefensa.setSanctified(estaSantificado);
                    civ.getArmy().get(indiceEjercito).add(nuevaDefensa);
                }
            }
     
            String sqlEsp =
                "SELECT type, armor, base_damage, experience " +
                "FROM special_units_stats WHERE civilization_id = ? ORDER BY unit_id";
     
            PreparedStatement psE = conn.prepareStatement(sqlEsp);
            psE.setInt(1, idCiv);
            ResultSet rsE = psE.executeQuery();
     
            while (rsE.next()) {
                String tipoUnidadEspecial = rsE.getString("type");
                int armadura              = rsE.getInt("armor");
                int dañoBase              = rsE.getInt("base_damage");
                int experiencia           = rsE.getInt("experience");

                SpecialUnit unidadEspecial = null;
                int indiceEjercito = -1;

                if (tipoUnidadEspecial.equals("Magician")) {
                    unidadEspecial = new Magician(armadura, dañoBase);
                    indiceEjercito = 7;
                } else if (tipoUnidadEspecial.equals("Priest")) {
                    unidadEspecial = new Priest(armadura, dañoBase);
                    indiceEjercito = 8;
                }

                if (unidadEspecial != null && indiceEjercito >= 0) {
                    unidadEspecial.setExperience(experiencia);
                    civ.getArmy().get(indiceEjercito).add(unidadEspecial);
                }
            }
     
            System.out.println("Partida cargada correctamente. (civilization_id=" + idCiv + ")");
     
        } catch (SQLException e) {
            System.out.println("Error al cargar la civilización.");
            e.printStackTrace();
        }
     
        return civ;
    }
    
    public void guardarEdificios(int idCiv, ArrayList<EdificioColocado> edificios) {
        try {
            PreparedStatement psDel = conn.prepareStatement(
                "DELETE FROM Civilization_buildings WHERE civilization_id = ?"
            );
            psDel.setInt(1, idCiv);
            psDel.executeUpdate();

            String sql =
                "INSERT INTO Civilization_buildings (civilization_id, building_id, type, pos_x, pos_y) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 0; i < edificios.size(); i++) {
                EdificioColocado ed = edificios.get(i);
                ps.setInt(1, idCiv);
                ps.setInt(2, i + 1);
                ps.setString(3, ed.getTipo());
                ps.setInt(4, ed.getX());
                ps.setInt(5, ed.getY());
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("Edificios colocados guardados correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al guardar los edificios.");
            e.printStackTrace();
        }
    }
    
    public String[][] cargarEdificios(int idCiv) {
        String sql =
            "SELECT type, pos_x, pos_y FROM Civilization_buildings " +
            "WHERE civilization_id = ? ORDER BY building_id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCiv);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
            	count += 1;
            }
            rs = ps.executeQuery();

            String[][] result = new String[count][3];
            int i = 0;
            while (rs.next()) {
                result[i][0] = rs.getString("type");
                result[i][1] = String.valueOf(rs.getInt("pos_x"));
                result[i][2] = String.valueOf(rs.getInt("pos_y"));
                i += 1;
            }
            return result;

        } catch (SQLException e) {
            System.out.println("Error al cargar los edificios.");
            e.printStackTrace();
        }
        return new String[0][3];
    }
    
    public void guardarBatalla(Battle batalla, int idCiv) {
    	 
        int numBatalla = batalla.getCivilization().getBattles(); // ya fue incrementado al pelear()
     
        // Nombres de unidades en el mismo orden que initialArmies[fila][columna]
        // Civ:   0=Swordsman, 1=Spearman, 2=Crossbow, 3=Cannon,
        //        4=ArrowTower, 5=Catapult, 6=RocketLauncherTower,
        //        7=Magician,   8=Priest
        // Enemy: 0=Swordsman, 1=Spearman, 2=Crossbow, 3=Cannon
        String[] tiposAtaque   = {"Swordsman", "Spearman", "Crossbow", "Cannon"};
        String[] tiposDefensa  = {"ArrowTower", "Catapult", "RocketLauncherTower"};
        String[] tiposEspecial = {"Magician", "Priest"};
     
        int[][] initialArmies            = batalla.getInitialArmies();           // [0]=civ, [1]=enemy
        int[]   actualCiv                = batalla.getActualNumberUnitsCivilization();
        int[]   actualEnemy              = batalla.getActualNumberUnitsEnemy();
        int[][] initialCostFleet         = batalla.getInitialCostFleet();        // [0]=civ [1]=enemy, [food,wood,iron]
        int[]   wasteWoodIron            = batalla.getWasteWoodIron();           // [wood, iron]
        boolean ganaCiv = batalla.getResourcesLooses()[0][3] <= batalla.getResourcesLooses()[1][3];
     
        try {
     
            // -----------------------------------------------------------------
            // 1. Battle_stats  (fila raíz de la batalla)
            // -----------------------------------------------------------------
            String sqlStats =
                "INSERT INTO Battle_stats (civilization_id, num_battle, wood_acquired, iron_acquired) " +
                "VALUES (?, ?, ?, ?)";
            PreparedStatement psStats = conn.prepareStatement(sqlStats);
            psStats.setInt(1, idCiv);
            psStats.setInt(2, numBatalla);
            // Solo se recoge el waste si la civilización gana
            psStats.setInt(3, ganaCiv ? wasteWoodIron[0] : 0);
            psStats.setInt(4, ganaCiv ? wasteWoodIron[1] : 0);
            psStats.executeUpdate();
            System.out.println("Battle_stats guardado (batalla " + numBatalla + ").");
     
            // -----------------------------------------------------------------
            // 2. Battle_log  (una fila por línea del log)
            // -----------------------------------------------------------------
            String sqlLog =
            	    "INSERT INTO Battle_log (civilization_id, num_battle, num_line, log_entry) " +
            	    "VALUES (?, ?, ?, ?)";
        	PreparedStatement psLog = conn.prepareStatement(sqlLog);

        	// 1. Obtenemos las líneas del desarrollo
        	String[] lineasDesarrollo = batalla.getBattleDevelopment().split("\n");
        	int lineCount = 0;

        	// Insertar desarrollo
        	for (int i = 0; i < lineasDesarrollo.length; i++) {
        	    if (!lineasDesarrollo[i].trim().isEmpty()) {
        	        lineCount++;
        	        psLog.setInt(1, idCiv);
        	        psLog.setInt(2, numBatalla);
        	        psLog.setInt(3, lineCount);
        	        psLog.setString(4, lineasDesarrollo[i]);
        	        psLog.addBatch();
        	    }
        	}

        	// 2. Añadir una línea separadora y el REPORTE FINAL
        	lineCount++;
        	psLog.setInt(1, idCiv);
        	psLog.setInt(2, numBatalla);
        	psLog.setInt(3, lineCount);
        	psLog.setString(4, "============================================");
        	psLog.addBatch();

        	lineCount++;
        	psLog.setInt(1, idCiv);
        	psLog.setInt(2, numBatalla);
        	psLog.setInt(3, lineCount);
        	psLog.setString(4, "RESUMEN FINAL DE LA BATALLA");
        	psLog.addBatch();

        	// 3. Obtener líneas del reporte e insertarlas
        	String[] lineasReporte = batalla.getBattleReport(batalla.getCivilization().getBattles()).split("\n");
        	for (int i = 0; i < lineasReporte.length; i++) {
        	    if (!lineasReporte[i].trim().isEmpty()) {
        	        lineCount++;
        	        psLog.setInt(1, idCiv);
        	        psLog.setInt(2, numBatalla);
        	        psLog.setInt(3, lineCount);
        	        psLog.setString(4, lineasReporte[i]);
        	        psLog.addBatch();
        	    }
        	}

        	psLog.executeBatch();
        	System.out.println("Battle_log guardado (" + lineCount + " líneas totales: Desarrollo + Reporte).");
     
            // -----------------------------------------------------------------
            // 3. Civilization_attack_stats  (Swordsman, Spearman, Crossbow, Cannon)
            // -----------------------------------------------------------------
            String sqlCivAtk =
                "INSERT INTO Civilization_attack_stats (civilization_id, num_battle, type, initial, drops) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psCivAtk = conn.prepareStatement(sqlCivAtk);
     
            for (int i = 0; i < tiposAtaque.length; i++) {   // índices 0-3
                int inicial = initialArmies[0][i];
                int bajas   = inicial - actualCiv[i];
                psCivAtk.setInt(1, idCiv);
                psCivAtk.setInt(2, numBatalla);
                psCivAtk.setString(3, tiposAtaque[i]);
                psCivAtk.setInt(4, inicial);
                psCivAtk.setInt(5, bajas);
                psCivAtk.addBatch();
            }
            psCivAtk.executeBatch();
            System.out.println("Civilization_attack_stats guardado.");
     
            // -----------------------------------------------------------------
            // 4. Civilization_defense_stats  (ArrowTower, Catapult, RocketLauncherTower)
            // -----------------------------------------------------------------
            String sqlCivDef =
                "INSERT INTO Civilization_defense_stats (civilization_id, num_battle, type, initial, drops) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psCivDef = conn.prepareStatement(sqlCivDef);
     
            for (int i = 0; i < tiposDefensa.length; i++) {   // índices 4-6
                int inicial = initialArmies[0][i + 4];
                int bajas   = inicial - actualCiv[i + 4];
                psCivDef.setInt(1, idCiv);
                psCivDef.setInt(2, numBatalla);
                psCivDef.setString(3, tiposDefensa[i]);
                psCivDef.setInt(4, inicial);
                psCivDef.setInt(5, bajas);
                psCivDef.addBatch();
            }
            psCivDef.executeBatch();
            System.out.println("Civilization_defense_stats guardado.");
     
            // -----------------------------------------------------------------
            // 5. Civilization_special_stats  (Magician, Priest)
            // -----------------------------------------------------------------
            String sqlCivEsp =
                "INSERT INTO Civilization_special_stats (civilization_id, num_battle, type, initial, drops) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psCivEsp = conn.prepareStatement(sqlCivEsp);
     
            for (int i = 0; i < tiposEspecial.length; i++) {  // índices 7-8
                int inicial = initialArmies[0][i + 7];
                int bajas   = inicial - actualCiv[i + 7];
                psCivEsp.setInt(1, idCiv);
                psCivEsp.setInt(2, numBatalla);
                psCivEsp.setString(3, tiposEspecial[i]);
                psCivEsp.setInt(4, inicial);
                psCivEsp.setInt(5, bajas);
                psCivEsp.addBatch();
            }
            psCivEsp.executeBatch();
            System.out.println("Civilization_special_stats guardado.");
     
            // -----------------------------------------------------------------
            // 6. Enemy_attack_stats  (Swordsman, Spearman, Crossbow, Cannon)
            // -----------------------------------------------------------------
            String sqlEnemyAtk =
                "INSERT INTO Enemy_attack_stats (civilization_id, num_battle, type, initial, drops) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psEnemyAtk = conn.prepareStatement(sqlEnemyAtk);
     
            for (int i = 0; i < tiposAtaque.length; i++) {   // índices 0-3
                int inicial = initialArmies[1][i];
                int bajas   = inicial - actualEnemy[i];
                psEnemyAtk.setInt(1, idCiv);
                psEnemyAtk.setInt(2, numBatalla);
                psEnemyAtk.setString(3, tiposAtaque[i]);
                psEnemyAtk.setInt(4, inicial);
                psEnemyAtk.setInt(5, bajas);
                psEnemyAtk.addBatch();
            }
            psEnemyAtk.executeBatch();
            System.out.println("Enemy_attack_stats guardado.");
     
            System.out.println(">>> Batalla " + numBatalla + " guardada completamente en la BD.");
     
        } catch (SQLException e) {
            System.out.println("Error al guardar la batalla " + numBatalla + ".");
            e.printStackTrace();
        }
    }
}
