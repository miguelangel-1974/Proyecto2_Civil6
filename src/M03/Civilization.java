package M03;

import java.util.ArrayList;

public class Civilization implements Variables{

	private int technologyDefense;
	private int technologyAtack;

	private int wood;
	private int iron;
	private int food;
	private int mana;

	private int magicTower;
	private int church;
	private int farm;
	private int smithy;
	private int carpentry;

	private int battles;

	private ArrayList<ArrayList<MilitaryUnit>> army;

	public Civilization() {
		this.technologyDefense = 0;
		this.technologyAtack = 0;

		this.wood = 0;
		this.iron = 0;
		this.food = 0;
		this.mana = 0;

		this.magicTower = 0;
		this.church = 0;
		this.farm = 0;
		this.smithy = 0;
		this.carpentry = 0;

		this.battles = 0;

		this.army = new ArrayList<ArrayList<MilitaryUnit>>();
		for (int i = 0; i < 9; i++) {
			this.army.add(new ArrayList<MilitaryUnit>());
		}
	}

	public int getTechnologyDefense() {
		return technologyDefense;
	}

	public void setTechnologyDefense(int technologyDefense) {
		this.technologyDefense = technologyDefense;
	}

	public int getTechnologyAtack() {
		return technologyAtack;
	}

	public void setTechnologyAtack(int technologyAtack) {
		this.technologyAtack = technologyAtack;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getMagicTower() {
		return magicTower;
	}

	public void setMagicTower(int magicTower) {
		this.magicTower = magicTower;
	}

	public int getChurch() {
		return church;
	}

	public void setChurch(int church) {
		this.church = church;
	}

	public int getFarm() {
		return farm;
	}

	public void setFarm(int farm) {
		this.farm = farm;
	}

	public int getSmithy() {
		return smithy;
	}

	public void setSmithy(int smithy) {
		this.smithy = smithy;
	}

	public int getCarpentry() {
		return carpentry;
	}

	public void setCarpentry(int carpentry) {
		this.carpentry = carpentry;
	}

	public int getBattles() {
		return battles;
	}

	public void setBattles(int battles) {
		this.battles = battles;
	}

	public ArrayList<ArrayList<MilitaryUnit>> getArmy() {
		return army;
	}

	public void setArmy(ArrayList<ArrayList<MilitaryUnit>> army) {
		this.army = army;
	}

	public void newChurch() throws ResourceException {
		if(this.food < FOOD_COST_CHURCH || this.wood < WOOD_COST_CHURCH || this.iron < IRON_COST_CHURCH || this.mana < MANA_COST_CHURCH) {
			throw new ResourceException("No hay recursos suficientes para construir una CHURCH.");
		}

			
		this.food= this.food-FOOD_COST_CHURCH;
		this.wood= this.wood-WOOD_COST_CHURCH;
		this.iron=this.iron-IRON_COST_CHURCH;
		this.mana=this.mana-MANA_COST_CHURCH;
		this.church+=1;
		System.out.println("CHURCH construida. Total de churchs: "+this.church);
	
	}

	public void newMagicTower() throws ResourceException {
		if (this.food < FOOD_COST_MAGICTOWER || this.wood < WOOD_COST_MAGICTOWER  || this.iron < IRON_COST_MAGICTOWER ) {
			throw new ResourceException("No hay recursos suficientes para construir una TORRE MAGICA.");
		}
 
		this.food = this.food- FOOD_COST_MAGICTOWER ;
		this.wood = this.wood - WOOD_COST_MAGICTOWER ;
		this.iron = this.iron- IRON_COST_MAGICTOWER ;
		this.magicTower+=1;
 
		System.out.println("magic tower construida. Total granjas: " + this.magicTower);
	}

	public void newFarm() throws ResourceException {
		if (this.food < FOOD_COST_FARM || this.wood < WOOD_COST_FARM || this.iron < IRON_COST_FARM) {
			throw new ResourceException("No hay recursos suficientes para construir una granja.");
		}
 
		this.food = this.food- FOOD_COST_FARM;
		this.wood = this.wood - WOOD_COST_FARM;
		this.iron = this.iron- IRON_COST_FARM;
		this.farm+=1;
 
		System.out.println("Granja construida. Total granjas: " + this.farm);
	}

	public void newCarpentry() throws ResourceException {
		if (this.food < FOOD_COST_CARPENTRY || this.wood < WOOD_COST_CARPENTRY || this.iron < IRON_COST_CARPENTRY) {
			throw new ResourceException("No hay recursos suficientes para construir una  CARPentry");
		}
 
		this.food = this.food- FOOD_COST_CARPENTRY;
		this.wood = this.wood - WOOD_COST_CARPENTRY;
		this.iron = this.iron- IRON_COST_CARPENTRY;
		this.carpentry+=1;
 
		System.out.println("carpintry construida. Total CARPINTERY: " + this.carpentry);
	}

	public void newSmithy() throws ResourceException {
		if (this.food < FOOD_COST_SMITHY || this.wood < WOOD_COST_SMITHY || this.iron < IRON_COST_SMITHY) {
			throw new ResourceException("No hay recursos suficientes para construir una SMITHY");
		}
 
		this.food = this.food- FOOD_COST_SMITHY;
		this.wood = this.wood - WOOD_COST_SMITHY;
		this.iron = this.iron- IRON_COST_SMITHY;
		this.smithy+=1;
 
		System.out.println("smithy construida. Total CARPINTERY: " + this.smithy);
	}

	public void upgradeTechnologyDefense() throws ResourceException {
 
		int coste = UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST + this.technologyDefense * UPGRADE_PLUS_DEFENSE_TECHNOLOGY_IRON_COST;
 
		if (this.iron < coste) {
			throw new ResourceException("No hay hierro suficiente para mejorar la tecnologia de defensa. Coste: " + coste);
		}
 
		this.iron = this.iron - coste;
		this.technologyDefense+=1;
 
		System.out.println("Tecnologia de defensa mejorada. Nuevo nivel: " + this.technologyDefense + " (coste: " + coste + " hierro)");
	}

	public void upgradeTechnologyAttack() throws ResourceException {
 
		int coste = UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST + this.technologyAtack * UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
 
		if (this.iron < coste) {
			throw new ResourceException("No hay hierro suficiente para mejorar la tecnologia de ataque. Coste: " + coste);
		}
 
		this.iron = this.iron - coste;
		this.technologyAtack+=1;
 
		System.out.println("Tecnologia de ataque mejorada. Nuevo nivel: " + this.technologyAtack + " (coste: " + coste + " hierro)");
	}

	public void newSwordsman(int n) throws ResourceException {
		int armor = ARMOR_SWORDSMAN+ (this.technologyDefense * PLUS_ARMOR_SWORDSMAN_BY_TECHNOLOGY * ARMOR_SWORDSMAN / 100);
	    int damage = BASE_DAMAGE_SWORDSMAN+ (this.technologyAtack * PLUS_ATTACK_SWORDSMAN_BY_TECHNOLOGY * BASE_DAMAGE_SWORDSMAN / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_SWORDSMAN
	            && this.wood >= WOOD_COST_SWORDSMAN
	            && this.food >= FOOD_COST_SWORDSMAN) {

	        this.iron = this.iron - IRON_COST_SWORDSMAN;
	        this.wood = this.wood - WOOD_COST_SWORDSMAN;
	        this.food = this.food - FOOD_COST_SWORDSMAN;
	        

	        this.army.get(0).add(new Swordsman(armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " espadachines.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " espadachines, solo se crearon " + creados + ".");
	    }
	
	} 

	public void newSpearman(int n) throws ResourceException {
		int armor = ARMOR_SPEARMAN+ (this.technologyDefense * PLUS_ARMOR_SPEARMAN_BY_TECHNOLOGY * ARMOR_SPEARMAN / 100);
	    int damage = BASE_DAMAGE_SPEARMAN+ (this.technologyAtack * PLUS_ATTACK_SPEARMAN_BY_TECHNOLOGY * BASE_DAMAGE_SPEARMAN / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_SPEARMAN
	            && this.wood >= WOOD_COST_SPEARMAN
	            && this.food >= FOOD_COST_SPEARMAN) {

	        this.iron = this.iron - IRON_COST_SPEARMAN;
	        this.wood = this.wood - WOOD_COST_SPEARMAN;
	        this.food = this.food - FOOD_COST_SPEARMAN;
	        

	        this.army.get(1).add(new Spearman(armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " lanceros.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " lanceros, solo se crearon " + creados + ".");
	    }

	}

	public void newCrossbow(int n) throws ResourceException {
		int armor = ARMOR_CROSSBOW+ (this.technologyDefense * PLUS_ARMOR_CROSSBOW_BY_TECHNOLOGY * ARMOR_CROSSBOW / 100);
	    int damage = BASE_DAMAGE_CROSSBOW+ (this.technologyAtack * PLUS_ATTACK_CROSSBOW_BY_TECHNOLOGY * BASE_DAMAGE_CROSSBOW / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_CROSSBOW
	            && this.wood >= WOOD_COST_CROSSBOW
	            && this.food >= FOOD_COST_CROSSBOW) {

	        this.iron = this.iron - IRON_COST_CROSSBOW;
	        this.wood = this.wood - WOOD_COST_CROSSBOW;
	        this.food = this.food - FOOD_COST_CROSSBOW;
	        

	        this.army.get(2).add(new Crossbow(armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " ballesta.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " ballesta, solo se crearon " + creados + ".");
	    }

	}

	public void newCannon(int n) throws ResourceException {
		int armor = ARMOR_CANNON+ (this.technologyDefense * PLUS_ARMOR_CANNON_BY_TECHNOLOGY * ARMOR_CANNON / 100);
	    int damage = BASE_DAMAGE_CANNON+ (this.technologyAtack * PLUS_ATTACK_CANNON_BY_TECHNOLOGY * BASE_DAMAGE_CANNON / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_CANNON
	            && this.wood >= WOOD_COST_CANNON
	            && this.food >= FOOD_COST_CANNON) {

	        this.iron = this.iron - IRON_COST_CANNON;
	        this.wood = this.wood - WOOD_COST_CANNON;
	        this.food = this.food - FOOD_COST_CANNON;
	        

	        this.army.get(3).add(new Cannon(armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " cañones.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " cañones, solo se crearon " + creados + ".");
	    }

	}

	public void newArrowTower(int n) throws ResourceException {
		int armor = ARMOR_ARROWTOWER+ (this.technologyDefense * PLUS_ARMOR_ARROWTOWER_BY_TECHNOLOGY * ARMOR_ARROWTOWER / 100);
	    int damage = BASE_DAMAGE_ARROWTOWER+ (this.technologyAtack * PLUS_ATTACK_ARROWTOWER_BY_TECHNOLOGY * BASE_DAMAGE_ARROWTOWER / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_ARROWTOWER
	            && this.wood >= WOOD_COST_ARROWTOWER
	            && this.food >= FOOD_COST_ARROWTOWER) {

	        this.iron = this.iron - IRON_COST_ARROWTOWER;
	        this.wood = this.wood - WOOD_COST_ARROWTOWER;
	        this.food = this.food - FOOD_COST_ARROWTOWER;
	        

	        this.army.get(4).add(new ArrowTower(armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " torres arqueras.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " torres arqueras, solo se crearon " + creados + ".");
	    }

	}

	public void newCatapult(int n) throws ResourceException {
		int armor = ARMOR_CATAPULT+ (this.technologyDefense * PLUS_ARMOR_CATAPULT_BY_TECHNOLOGY * ARMOR_CATAPULT / 100);
	    int damage = BASE_DAMAGE_CATAPULT+ (this.technologyAtack * PLUS_ATTACK_CATAPULT_BY_TECHNOLOGY * BASE_DAMAGE_CATAPULT / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_CATAPULT
	            && this.wood >= WOOD_COST_CATAPULT
	            && this.food >= FOOD_COST_CATAPULT) {

	        this.iron = this.iron - IRON_COST_CATAPULT;
	        this.wood = this.wood - WOOD_COST_CATAPULT;
	        this.food = this.food - FOOD_COST_CATAPULT;
	        

	        this.army.get(5).add(new Catapult(armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " catapultas.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " catapultas, solo se crearon " + creados + ".");
	    }

	}

	public void newRocketLauncher(int n) throws ResourceException {
		int armor = ARMOR_ROCKETLAUNCHERTOWER+ (this.technologyDefense * PLUS_ARMOR_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY * ARMOR_ROCKETLAUNCHERTOWER / 100);
	    int damage = BASE_DAMAGE_ROCKETLAUNCHERTOWER+ (this.technologyAtack * PLUS_ATTACK_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY * BASE_DAMAGE_ROCKETLAUNCHERTOWER / 100);

	    int creados = 0;

	    while (creados < n
	            && this.iron >= IRON_COST_ROCKETLAUNCHERTOWER
	            && this.wood >= WOOD_COST_ROCKETLAUNCHERTOWER
	            && this.food >= FOOD_COST_ROCKETLAUNCHERTOWER) {

	        this.iron = this.iron - IRON_COST_ROCKETLAUNCHERTOWER;
	        this.wood = this.wood - WOOD_COST_ROCKETLAUNCHERTOWER;
	        this.food = this.food - FOOD_COST_ROCKETLAUNCHERTOWER;
	        

	        this.army.get(6).add(new RocketLauncherTower (armor, damage));

	        creados+=1;
	    }

	    if (creados > 0) {
	        System.out.println("Se han reclutado " + creados + " torres de cohetes.");
	    }

	    if (creados < n) {
	        throw new ResourceException("Recursos insuficientes. Querias " + n + " torres de cohetes, solo se crearon " + creados + ".");
	    }

	}

	    public void newMagician(int n) throws ResourceException, BuildingException {

        if (this.magicTower < 1) {
            throw new BuildingException("No tienes Torres de mago para crear magos.");
        }
    
        int armor = 0;
        int damage = 0;
    
        int creados = 0;
    
        while (creados < n
                && this.iron >= IRON_COST_MAGICIAN
                && this.wood >= WOOD_COST_MAGICIAN
                && this.food >= FOOD_COST_MAGICIAN
                && this.mana >= MANA_COST_MAGICIAN) {
    
            this.iron = this.iron - IRON_COST_MAGICIAN;
            this.wood = this.wood - WOOD_COST_MAGICIAN;
            this.food = this.food - FOOD_COST_MAGICIAN;
            this.mana = this.mana - MANA_COST_MAGICIAN;
    
            this.army.get(7).add(new Magician(armor, damage));
    
            creados+=1;
        }
    
        if (creados > 0) {
            System.out.println("Se han reclutado " + creados + " magos.");
        }
    
        if (creados < n) {
            throw new ResourceException("Recursos insuficientes. Querias " + n + " magos, solo se crearon " + creados + ".");
        }
    }
	public void newPriest(int n) throws ResourceException, BuildingException {
		if (church<1) {
			throw new BuildingException("No tienes Iglesias para crear sacerdotes");
		}
		 int armor = 0;
		    int damage = 0;

		    int creados = 0;

		    while (creados < n
		            && this.iron >= IRON_COST_PRIEST
		            && this.wood >= WOOD_COST_PRIEST
		            && this.food >= FOOD_COST_PRIEST
		            && this.mana >= MANA_COST_PRIEST) {

		        this.iron = this.iron - IRON_COST_PRIEST;
		        this.wood = this.wood - WOOD_COST_PRIEST;
		        this.food = this.food - FOOD_COST_PRIEST;
		        this.mana = this.mana - MANA_COST_PRIEST;

		        this.army.get(8).add(new Priest(armor, damage));

		        creados+=1;
		    }

		    if (creados > 0) {
		        System.out.println("Se han reclutado " + creados + " sacerdotes.");
		    }

		    if (creados < n) {
		        throw new ResourceException("Recursos insuficientes. Querias " + n + " sacerdotes, solo se crearon " + creados + ".");
		    }
	}

	public void printStats() {
		System.out.println("=========== CIVILIZATION STATS ===========");
		System.out.println("\nRecursos:");
		System.out.println("  Madera: " + this.wood);
		System.out.println("  Hierro: " + this.iron);
		System.out.println("  Comida: " + this.food);
		System.out.println("  Mana:   " + this.mana);
		System.out.println("\nEdificios:");
		System.out.println("  Granjas:        " + this.farm);
		System.out.println("  Carpinterias:   " + this.carpentry);
		System.out.println("  Herrerias:      " + this.smithy);
		System.out.println("  Iglesias:       " + this.church);
		System.out.println("  Torres magicas: " + this.magicTower);
		System.out.println("\nTecnologias:");
		System.out.println("  Defensa: nivel " + this.technologyDefense);
		System.out.println("  Ataque:  nivel " + this.technologyAtack);
		System.out.println("\nEjercito:");
		System.out.println("  Espadachines:        " + this.army.get(0).size());
		System.out.println("  Lanceros:            " + this.army.get(1).size());
		System.out.println("  Ballesteros:         " + this.army.get(2).size());
		System.out.println("  Canones:             " + this.army.get(3).size());
		System.out.println("  Torres de flechas:   " + this.army.get(4).size());
		System.out.println("  Catapultas:          " + this.army.get(5).size());
		System.out.println("  Torres lanzacohetes: " + this.army.get(6).size());
		System.out.println("  Magos:               " + this.army.get(7).size());
		System.out.println("  Sacerdotes:          " + this.army.get(8).size());

		System.out.println("\nBatallas libradas: " + this.battles);
		System.out.println("==========================================");
	}

}
