package M03;

import java.util.ArrayList;

public class Battle implements Variables {

	private Civilization civilization;
	private ArrayList<MilitaryUnit> civilizationArmy;
	private ArrayList<MilitaryUnit> enemyArmy;
	private ArrayList<ArrayList<ArrayList<MilitaryUnit>>> armies;
	private String battleDevelopment;
	private int[][] initialCostFleet;
	private int initialNumberUnitsCivilization;
	private int initialNumberUnitsEnemy;
	private int[] wasteWoodIron;
	private int enemyDrops;
	private int civilizationDrops;
	private int[][] resourcesLooses;
	private int[][] initialArmies;
	private int[] actualNumberUnitsCivilization;
	private int[] actualNumberUnitsEnemy;

	public Battle(Civilization civilization, ArrayList<MilitaryUnit> enemyArmy) {

		this.civilization = civilization;

		this.armies = new ArrayList<ArrayList<ArrayList<MilitaryUnit>>>();

		ArrayList<ArrayList<MilitaryUnit>> filaCivil = new ArrayList<ArrayList<MilitaryUnit>>();
		for (int i = 0; i < 9; i++) {
			ArrayList<MilitaryUnit> grupo = new ArrayList<MilitaryUnit>();
			for (int j = 0; j < civilization.getArmy().get(i).size(); j++) {
				grupo.add(civilization.getArmy().get(i).get(j));
			}
			filaCivil.add(grupo);
		}
		this.armies.add(filaCivil);

		ArrayList<ArrayList<MilitaryUnit>> filaEnemy = new ArrayList<ArrayList<MilitaryUnit>>();
		for (int i = 0; i < 9; i++) {
			filaEnemy.add(new ArrayList<MilitaryUnit>());
		}
		for (int i = 0; i < enemyArmy.size(); i++) {
			MilitaryUnit u = enemyArmy.get(i);
			if (u instanceof Swordsman) {
				filaEnemy.get(0).add(u);
			} else if (u instanceof Spearman) {
				filaEnemy.get(1).add(u);
			} else if (u instanceof Crossbow) {
				filaEnemy.get(2).add(u);
			} else if (u instanceof Cannon) {
				filaEnemy.get(3).add(u);
			}
		}
		this.armies.add(filaEnemy);

		this.civilizationArmy = new ArrayList<MilitaryUnit>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < this.armies.get(0).get(i).size(); j++) {
				this.civilizationArmy.add(this.armies.get(0).get(i).get(j));
			}
		}

		this.enemyArmy = new ArrayList<MilitaryUnit>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < this.armies.get(1).get(i).size(); j++) {
				this.enemyArmy.add(this.armies.get(1).get(i).get(j));
			}
		}

		this.initialNumberUnitsCivilization = this.civilizationArmy.size();
		this.initialNumberUnitsEnemy = this.enemyArmy.size();

		this.wasteWoodIron = new int[2];
		this.enemyDrops = 0;
		this.civilizationDrops = 0;
		this.battleDevelopment = "";

		this.initInitialArmies();
	}

	public Civilization getCivilization() { return civilization; }
	public void setCivilization(Civilization civilization) { this.civilization = civilization; }
	public ArrayList<MilitaryUnit> getCivilizationArmy() { return civilizationArmy; }
	public void setCivilizationArmy(ArrayList<MilitaryUnit> civilizationArmy) { this.civilizationArmy = civilizationArmy; }
	public ArrayList<MilitaryUnit> getEnemyArmy() { return enemyArmy; }
	public void setEnemyArmy(ArrayList<MilitaryUnit> enemyArmy) { this.enemyArmy = enemyArmy; }
	public ArrayList<ArrayList<ArrayList<MilitaryUnit>>> getArmies() { return armies; }
	public void setArmies(ArrayList<ArrayList<ArrayList<MilitaryUnit>>> armies) { this.armies = armies; }
	public void setBattleDevelopment(String battleDevelopment) { this.battleDevelopment = battleDevelopment; }
	public int[][] getInitialCostFleet() { return initialCostFleet; }
	public void setInitialCostFleet(int[][] initialCostFleet) { this.initialCostFleet = initialCostFleet; }
	public int getInitialNumberUnitsCivilization() { return initialNumberUnitsCivilization; }
	public void setInitialNumberUnitsCivilization(int initialNumberUnitsCivilization) { this.initialNumberUnitsCivilization = initialNumberUnitsCivilization; }
	public int getInitialNumberUnitsEnemy() { return initialNumberUnitsEnemy; }
	public void setInitialNumberUnitsEnemy(int initialNumberUnitsEnemy) { this.initialNumberUnitsEnemy = initialNumberUnitsEnemy; }
	public int[] getWasteWoodIron() { return wasteWoodIron; }
	public void setWasteWoodIron(int[] wasteWoodIron) { this.wasteWoodIron = wasteWoodIron; }
	public int getEnemyDrops() { return enemyDrops; }
	public void setEnemyDrops(int enemyDrops) { this.enemyDrops = enemyDrops; }
	public int getCivilizationDrops() { return civilizationDrops; }
	public void setCivilizationDrops(int civilizationDrops) { this.civilizationDrops = civilizationDrops; }
	public int[][] getResourcesLooses() { return resourcesLooses; }
	public void setResourcesLooses(int[][] resourcesLooses) { this.resourcesLooses = resourcesLooses; }
	public int[][] getInitialArmies() { return initialArmies; }
	public void setInitialArmies(int[][] initialArmies) { this.initialArmies = initialArmies; }
	public int[] getActualNumberUnitsCivilization() { return actualNumberUnitsCivilization; }
	public void setActualNumberUnitsCivilization(int[] actualNumberUnitsCivilization) { this.actualNumberUnitsCivilization = actualNumberUnitsCivilization; }
	public int[] getActualNumberUnitsEnemy() { return actualNumberUnitsEnemy; }
	public void setActualNumberUnitsEnemy(int[] actualNumberUnitsEnemy) { this.actualNumberUnitsEnemy = actualNumberUnitsEnemy; }

	public void initInitialArmies() {
		this.initialArmies = new int[2][9];
		this.actualNumberUnitsCivilization = new int[9];
		this.actualNumberUnitsEnemy = new int[9];

		for (int i = 0; i < 9; i++) {
			this.initialArmies[0][i] = this.armies.get(0).get(i).size();
			this.initialArmies[1][i] = this.armies.get(1).get(i).size();
			this.actualNumberUnitsCivilization[i] = this.initialArmies[0][i];
			this.actualNumberUnitsEnemy[i] = this.initialArmies[1][i];
		}

		this.initialCostFleet = new int[2][3];
		int[] costCiv = this.fleetResourceCost(this.civilizationArmy);
		int[] costEnemy = this.fleetResourceCost(this.enemyArmy);
		for (int i = 0; i < 3; i++) {
			this.initialCostFleet[0][i] = costCiv[i];
			this.initialCostFleet[1][i] = costEnemy[i];
		}

		this.resourcesLooses = new int[2][4];
	}

	public int[] fleetResourceCost(ArrayList<MilitaryUnit> army) {
		int[] cost = new int[3];
		for (int i = 0; i < army.size(); i++) {
			MilitaryUnit u = army.get(i);
			cost[0] = cost[0] + u.getFoodCost();
			cost[1] = cost[1] + u.getWoodCost();
			cost[2] = cost[2] + u.getIronCost();
		}
		return cost;
	}

	public int initialFleetNumber(ArrayList<MilitaryUnit> army) {
		if (army == this.civilizationArmy) {
			return this.initialNumberUnitsCivilization;
		} else {
			return this.initialNumberUnitsEnemy;
		}
	}

	public int remainderPercentageFleet(ArrayList<MilitaryUnit> army) {
		int inicial;
		if (army == this.civilizationArmy) {
			inicial = this.initialNumberUnitsCivilization;
		} else {
			inicial = this.initialNumberUnitsEnemy;
		}
		if (inicial == 0) {
			return 0;
		}
		return army.size() * 100 / inicial;
	}

	public void resetArmyArmor() {
		for (int i = 0; i < this.civilizationArmy.size(); i++) {
			this.civilizationArmy.get(i).resetArmor();
		}
	}

	public void updateResourcesLooses() {
		int[] costActualCiv = this.fleetResourceCost(this.civilizationArmy);
		int[] costActualEnemy = this.fleetResourceCost(this.enemyArmy);

		this.resourcesLooses[0][0] = this.initialCostFleet[0][0] - costActualCiv[0];
		this.resourcesLooses[0][1] = this.initialCostFleet[0][1] - costActualCiv[1];
		this.resourcesLooses[0][2] = this.initialCostFleet[0][2] - costActualCiv[2];
		this.resourcesLooses[0][3] = this.resourcesLooses[0][2]
				+ this.resourcesLooses[0][1] / 5
				+ this.resourcesLooses[0][0] / 10;

		this.resourcesLooses[1][0] = this.initialCostFleet[1][0] - costActualEnemy[0];
		this.resourcesLooses[1][1] = this.initialCostFleet[1][1] - costActualEnemy[1];
		this.resourcesLooses[1][2] = this.initialCostFleet[1][2] - costActualEnemy[2];
		this.resourcesLooses[1][3] = this.resourcesLooses[1][2]
				+ this.resourcesLooses[1][1] / 5
				+ this.resourcesLooses[1][0] / 10;
	}

	public int getCivilizationGroupAttacker() {
		int suma = 0;
		for (int i = 0; i < 9; i++) {
			suma = suma + CHANCE_ATTACK_CIVILIZATION_UNITS[i];
		}
		int rnd = 1 + (int) (Math.random() * suma);
		int acum = 0;
		for (int i = 0; i < 9; i++) {
			acum = acum + CHANCE_ATTACK_CIVILIZATION_UNITS[i];
			if (acum >= rnd) {
				return i;
			}
		}
		return 8;
	}

	public int getEnemyGroupAttacker() {
		int suma = 0;
		for (int i = 0; i < 4; i++) {
			suma = suma + CHANCE_ATTACK_ENEMY_UNITS[i];
		}
		int rnd = 1 + (int) (Math.random() * suma);
		int acum = 0;
		for (int i = 0; i < 4; i++) {
			acum = acum + CHANCE_ATTACK_ENEMY_UNITS[i];
			if (acum >= rnd) {
				return i;
			}
		}
		return 3;
	}

	public int getGroupDefender(ArrayList<MilitaryUnit> army) {
		int[] cantidades;
		int numGrupos;
		if (army == this.civilizationArmy) {
			cantidades = this.actualNumberUnitsCivilization;
			numGrupos = 9;
		} else {
			cantidades = this.actualNumberUnitsEnemy;
			numGrupos = 4;
		}
		int suma = 0;
		for (int i = 0; i < numGrupos; i++) {
			suma = suma + cantidades[i];
		}
		if (suma == 0) {
			return -1;
		}
		int rnd = 1 + (int) (Math.random() * suma);
		int acum = 0;
		for (int i = 0; i < numGrupos; i++) {
			acum = acum + cantidades[i];
			if (acum >= rnd) {
				return i;
			}
		}
		return numGrupos - 1;
	}

	public void pelear() {

		if (this.actualNumberUnitsCivilization[8] > 0) {
			for (int i = 0; i < this.civilizationArmy.size(); i++) {
				MilitaryUnit u = this.civilizationArmy.get(i);
				if (u instanceof AttackUnit) {
					((AttackUnit) u).setSanctified(true);
				} else if (u instanceof DefenseUnit) {
					((DefenseUnit) u).setSanctified(true);
				}
			}
		}

		int bandoAtacante;
		if (Math.random() < 0.5) {
			bandoAtacante = 0;
		} else {
			bandoAtacante = 1;
		}

		while (this.remainderPercentageFleet(this.civilizationArmy) >= 20
				&& this.remainderPercentageFleet(this.enemyArmy) >= 20) {

			this.battleDevelopment = this.battleDevelopment
					+ "********************CHANGE ATTACKER********************\n";

			int grupoAtacante;
			if (bandoAtacante == 0) {
				grupoAtacante = this.getCivilizationGroupAttacker();
			} else {
				grupoAtacante = this.getEnemyGroupAttacker();
			}

			ArrayList<MilitaryUnit> grupoAt = this.armies.get(bandoAtacante).get(grupoAtacante);

			if (grupoAt.isEmpty()) {
				bandoAtacante = 1 - bandoAtacante;
				continue;
			}

			int idxAtacante = (int) (Math.random() * grupoAt.size());
			MilitaryUnit atacante = grupoAt.get(idxAtacante);

			boolean repite = true;
			while (repite) {

				int bandoDefensor = 1 - bandoAtacante;
				ArrayList<MilitaryUnit> ejercitoDefensor;
				if (bandoDefensor == 0) {
					ejercitoDefensor = this.civilizationArmy;
				} else {
					ejercitoDefensor = this.enemyArmy;
				}

				if (ejercitoDefensor.isEmpty()) {
					break;
				}

				int grupoDefensor = this.getGroupDefender(ejercitoDefensor);
				if (grupoDefensor == -1) {
					break;
				}

				ArrayList<MilitaryUnit> grupoDef = this.armies.get(bandoDefensor).get(grupoDefensor);
				if (grupoDef.isEmpty()) {
					break;
				}

				int idxDefensor = (int) (Math.random() * grupoDef.size());
				MilitaryUnit defensor = grupoDef.get(idxDefensor);

				int danyo = atacante.attack();
				defensor.takeDamage(danyo);

				String nAtaque = nombreUnidad(atacante);
				String nDef = nombreUnidad(defensor);
				String prefijo;
				if (bandoAtacante == 0) {
					prefijo = "Attacks Civilization";
				} else {
					prefijo = "Attacks army enemy";
				}
				this.battleDevelopment = this.battleDevelopment
						+ prefijo + ": " + nAtaque + " attacks " + nDef + "\n"
						+ nAtaque + " generates the damage = " + danyo + "\n"
						+ nDef + " stays with armor = " + defensor.getActualArmor() + "\n";

				if (defensor.getActualArmor() <= 0) {

					this.battleDevelopment = this.battleDevelopment
							+ "we eliminate " + nDef + "\n";

					int chanceWaste = defensor.getChanceGeneratinWaste();
					if ((int) (Math.random() * 100) < chanceWaste) {
						this.wasteWoodIron[0] = this.wasteWoodIron[0]
								+ defensor.getWoodCost() * PERCENTATGE_WASTE / 100;
						this.wasteWoodIron[1] = this.wasteWoodIron[1]
								+ defensor.getIronCost() * PERCENTATGE_WASTE / 100;
					}

					if (bandoDefensor == 0) {
						this.civilizationDrops = this.civilizationDrops + 1;
					} else {
						this.enemyDrops = this.enemyDrops + 1;
					}

					grupoDef.remove(idxDefensor);
					ejercitoDefensor.remove(defensor);

					if (bandoDefensor == 0) {
						this.actualNumberUnitsCivilization[grupoDefensor] =
								this.actualNumberUnitsCivilization[grupoDefensor] - 1;
					} else {
						this.actualNumberUnitsEnemy[grupoDefensor] =
								this.actualNumberUnitsEnemy[grupoDefensor] - 1;
					}

					if (defensor instanceof Priest && bandoDefensor == 0
							&& this.actualNumberUnitsCivilization[8] == 0) {
						for (int k = 0; k < this.civilizationArmy.size(); k++) {
							MilitaryUnit uu = this.civilizationArmy.get(k);
							if (uu instanceof AttackUnit) {
								((AttackUnit) uu).setSanctified(false);
							} else if (uu instanceof DefenseUnit) {
								((DefenseUnit) uu).setSanctified(false);
							}
						}
					}
				}

				if (this.remainderPercentageFleet(this.civilizationArmy) < 20
						|| this.remainderPercentageFleet(this.enemyArmy) < 20) {
					repite = false;
				} else {
					if ((int) (Math.random() * 100) < atacante.getChanceAttackAgain()) {
						repite = true;
					} else {
						repite = false;
					}
				}
			}

			bandoAtacante = 1 - bandoAtacante;
		}

		for (int i = 0; i < this.civilizationArmy.size(); i++) {
			MilitaryUnit u = this.civilizationArmy.get(i);
			u.setExperience(u.getExperience() + 1);
		}
		for (int i = 0; i < this.enemyArmy.size(); i++) {
			MilitaryUnit u = this.enemyArmy.get(i);
			u.setExperience(u.getExperience() + 1);
		}

		this.resetArmyArmor();
		this.updateResourcesLooses();

		for (int i = 0; i < 9; i++) {
			ArrayList<MilitaryUnit> grupo = this.civilization.getArmy().get(i);
			int j = 0;
			while (j < grupo.size()) {
				boolean estaViva = this.civilizationArmy.contains(grupo.get(j));
				if (estaViva == false) {
					grupo.remove(j);
				} else {
					j = j + 1;
				}
			}
		}

		boolean ganaCiv;
		if (this.resourcesLooses[0][3] <= this.resourcesLooses[1][3]) {
			ganaCiv = true;
		} else {
			ganaCiv = false;
		}

		if (ganaCiv) {
			this.civilization.setWood(this.civilization.getWood() + this.wasteWoodIron[0]);
			this.civilization.setIron(this.civilization.getIron() + this.wasteWoodIron[1]);
		}

		this.civilization.setBattles(this.civilization.getBattles() + 1);
	}

	public String getBattleReport(int battles) {
		String r = "";
		r = r + "BATTLE NUMBER: " + battles + "\n";
		r = r + "BATTLE STATISTICS\n";
		r = r + "Army planet           Units  Drops    Initial Army Enemy   Units  Drops\n";

		String[] nombres = {"Swordsman", "Spearman", "Crossbow", "Cannon",
				"Arrow Tower", "Catapult", "Rocket Launcher", "Magician", "Priest"};

		for (int i = 0; i < 9; i++) {
			int unidadesCiv = this.actualNumberUnitsCivilization[i];
			int dropsCiv = this.initialArmies[0][i] - unidadesCiv;

			String linea = padDerecha(nombres[i], 22)
					+ " " + padIzquierda("" + unidadesCiv, 5)
					+ " " + padIzquierda("" + dropsCiv, 6);

			if (i < 4) {
				int unidadesEn = this.actualNumberUnitsEnemy[i];
				int dropsEn = this.initialArmies[1][i] - unidadesEn;
				linea = linea + "    "
						+ padDerecha(nombres[i], 22)
						+ " " + padIzquierda("" + unidadesEn, 5)
						+ " " + padIzquierda("" + dropsEn, 6);
			}
			r = r + linea + "\n";
		}

		r = r + "**************************************************************************************\n";
		r = r + "Cost Army Civilization                 Cost Army Enemy\n";
		r = r + "Food:  " + padDerecha("" + this.initialCostFleet[0][0], 30) + " Food:  " + this.initialCostFleet[1][0] + "\n";
		r = r + "Wood:  " + padDerecha("" + this.initialCostFleet[0][1], 30) + " Wood:  " + this.initialCostFleet[1][1] + "\n";
		r = r + "Iron:  " + padDerecha("" + this.initialCostFleet[0][2], 30) + " Iron:  " + this.initialCostFleet[1][2] + "\n";
		r = r + "**************************************************************************************\n";
		r = r + "Losses Army Civilization               Losses Army Enemy\n";
		r = r + "Food:  " + padDerecha("" + this.resourcesLooses[0][0], 30) + " Food:  " + this.resourcesLooses[1][0] + "\n";
		r = r + "Wood:  " + padDerecha("" + this.resourcesLooses[0][1], 30) + " Wood:  " + this.resourcesLooses[1][1] + "\n";
		r = r + "Iron:  " + padDerecha("" + this.resourcesLooses[0][2], 30) + " Iron:  " + this.resourcesLooses[1][2] + "\n";
		r = r + "**************************************************************************************\n";
		r = r + "Waste Generated:\n";
		r = r + "Wood  " + this.wasteWoodIron[0] + "\n";
		r = r + "Iron  " + this.wasteWoodIron[1] + "\n";

		boolean ganaCiv = this.resourcesLooses[0][3] <= this.resourcesLooses[1][3];
		if (ganaCiv) {
			r = r + "Battle Winned by Civilization, We Collect Rubble\n";
		} else {
			r = r + "Battle Winned by Enemy\n";
		}
		r = r + "##########################################################################\n";

		return r;
	}

	public String getBattleDevelopment() {
		return this.battleDevelopment;
	}

	private String padDerecha(String s, int largo) {
		while (s.length() < largo) {
			s = s + " ";
		}
		return s;
	}

	private String padIzquierda(String s, int largo) {
		while (s.length() < largo) {
			s = " " + s;
		}
		return s;
	}

	private String nombreUnidad(MilitaryUnit u) {
		if (u instanceof Swordsman) return "Swordsman";
		if (u instanceof Spearman) return "Spearman";
		if (u instanceof Crossbow) return "Crosswob";
		if (u instanceof Cannon) return "Cannon";
		if (u instanceof ArrowTower) return "Arrow Tower";
		if (u instanceof Catapult) return "Catapult";
		if (u instanceof RocketLauncherTower) return "Rocket Launcher Tower";
		if (u instanceof Magician) return "Magician";
		if (u instanceof Priest) return "Priest";
		return "Unknown";
	}

}