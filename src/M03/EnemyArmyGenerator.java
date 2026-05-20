package M03;

import java.util.ArrayList;
import java.util.TimerTask;

import M03.Unidades.UnidadesOfensivas.Cannon;
import M03.Unidades.UnidadesOfensivas.Crossbow;
import M03.Unidades.UnidadesOfensivas.Spearman;
import M03.Unidades.UnidadesOfensivas.Swordsman;

public class EnemyArmyGenerator extends TimerTask implements Variables {

	private Civilization civilization;
	private ArrayList<MilitaryUnit> enemyArmy;

	public EnemyArmyGenerator(Civilization civilization) {
		this.civilization = civilization;
		this.enemyArmy = new ArrayList<MilitaryUnit>();
	}

	public Civilization getCivilization() {
		return this.civilization;
	}

	public void setCivilization(Civilization civilization) {
		this.civilization = civilization;
	}

	public ArrayList<MilitaryUnit> getEnemyArmy() {
		return this.enemyArmy;
	}

	public void setEnemyArmy(ArrayList<MilitaryUnit> enemyArmy) {
		this.enemyArmy = enemyArmy;
	}

	public void createEnemyArmy() {

		int iron = IRON_BASE_ENEMY_ARMY;
		int wood = WOOD_BASE_ENEMY_ARMY;
		int food = FOOD_BASE_ENEMY_ARMY;

		for (int i = 0; i < this.civilization.getBattles(); i++) {
			iron = iron + iron * ENEMY_FLEET_INCREASE / 100;
			wood = wood + wood * ENEMY_FLEET_INCREASE / 100;
			food = food + food * ENEMY_FLEET_INCREASE / 100;
		}

		this.enemyArmy = new ArrayList<MilitaryUnit>();

		while (iron >= IRON_COST_SWORDSMAN
				&& wood >= WOOD_COST_SWORDSMAN
				&& food >= FOOD_COST_SWORDSMAN) {

			int tirada = (int) (Math.random() * 100);
			int elegida;

			if (tirada < 35) {
				elegida = 0;
			} else if (tirada < 60) {
				elegida = 1;
			} else if (tirada < 80) {
				elegida = 2;
			} else {
				elegida = 3;
			}

			if (iron >= IRON_COST_UNITS[elegida]
					&& wood >= WOOD_COST_UNITS[elegida]
					&& food >= FOOD_COST_UNITS[elegida]) {

				iron = iron - IRON_COST_UNITS[elegida];
				wood = wood - WOOD_COST_UNITS[elegida];
				food = food - FOOD_COST_UNITS[elegida];

				if (elegida == 0) {
					this.enemyArmy.add(new Swordsman());
				} else if (elegida == 1) {
					this.enemyArmy.add(new Spearman());
				} else if (elegida == 2) {
					this.enemyArmy.add(new Crossbow());
				} else {
					this.enemyArmy.add(new Cannon());
				}
			}
		}
	}

	public String viewThreat() {

		int swordsmen = 0;
		int spearmen = 0;
		int crossbows = 0;
		int cannons = 0;

		for (int i = 0; i < this.enemyArmy.size(); i++) {
			MilitaryUnit unidad = this.enemyArmy.get(i);
			if (unidad instanceof Swordsman) {
				swordsmen+=1;
			} else if (unidad instanceof Spearman) {
				spearmen+=1;
			} else if (unidad instanceof Crossbow) {
				crossbows+=1;
			} else if (unidad instanceof Cannon) {
				cannons+=1;
			}
		}

		String enemigos = "Ejercito enemigo:";
		enemigos += "\nSwordsman: " + swordsmen;
		enemigos += "\nSpearman: " + spearmen;
		enemigos += "\nCrossbow: " + crossbows;
		enemigos += "\nCannon: " + cannons;
		return enemigos;
	}

	public void run() {
		this.createEnemyArmy();
		System.out.println("Nuevo ejercito enemigo creado: " + this.enemyArmy.size() + " unidades.");
		Battle batalla= new Battle(civilization, enemyArmy);
		batalla.pelear();
	}

}