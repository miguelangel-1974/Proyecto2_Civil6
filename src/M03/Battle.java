package M03;

import java.util.ArrayList;

public class Battle {

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

	public Battle() {

	}

	public ArrayList<MilitaryUnit> getCivilizationArmy() {
		return civilizationArmy;
	}

	public void setCivilizationArmy(ArrayList<MilitaryUnit> civilizationArmy) {
		this.civilizationArmy = civilizationArmy;
	}

	public ArrayList<MilitaryUnit> getEnemyArmy() {
		return enemyArmy;
	}

	public void setEnemyArmy(ArrayList<MilitaryUnit> enemyArmy) {
		this.enemyArmy = enemyArmy;
	}

	public ArrayList<ArrayList<ArrayList<MilitaryUnit>>> getArmies() {
		return armies;
	}

	public void setArmies(ArrayList<ArrayList<ArrayList<MilitaryUnit>>> armies) {
		this.armies = armies;
	}

	public void setBattleDevelopment(String battleDevelopment) {
		this.battleDevelopment = battleDevelopment;
	}

	public int[][] getInitialCostFleet() {
		return initialCostFleet;
	}

	public void setInitialCostFleet(int[][] initialCostFleet) {
		this.initialCostFleet = initialCostFleet;
	}

	public int getInitialNumberUnitsCivilization() {
		return initialNumberUnitsCivilization;
	}

	public void setInitialNumberUnitsCivilization(int initialNumberUnitsCivilization) {
		this.initialNumberUnitsCivilization = initialNumberUnitsCivilization;
	}

	public int getInitialNumberUnitsEnemy() {
		return initialNumberUnitsEnemy;
	}

	public void setInitialNumberUnitsEnemy(int initialNumberUnitsEnemy) {
		this.initialNumberUnitsEnemy = initialNumberUnitsEnemy;
	}

	public int[] getWasteWoodIron() {
		return wasteWoodIron;
	}

	public void setWasteWoodIron(int[] wasteWoodIron) {
		this.wasteWoodIron = wasteWoodIron;
	}

	public int getEnemyDrops() {
		return enemyDrops;
	}

	public void setEnemyDrops(int enemyDrops) {
		this.enemyDrops = enemyDrops;
	}

	public int getCivilizationDrops() {
		return civilizationDrops;
	}

	public void setCivilizationDrops(int civilizationDrops) {
		this.civilizationDrops = civilizationDrops;
	}

	public int[][] getResourcesLooses() {
		return resourcesLooses;
	}

	public void setResourcesLooses(int[][] resourcesLooses) {
		this.resourcesLooses = resourcesLooses;
	}

	public int[][] getInitialArmies() {
		return initialArmies;
	}

	public void setInitialArmies(int[][] initialArmies) {
		this.initialArmies = initialArmies;
	}

	public int[] getActualNumberUnitsCivilization() {
		return actualNumberUnitsCivilization;
	}

	public void setActualNumberUnitsCivilization(int[] actualNumberUnitsCivilization) {
		this.actualNumberUnitsCivilization = actualNumberUnitsCivilization;
	}

	public int[] getActualNumberUnitsEnemy() {
		return actualNumberUnitsEnemy;
	}

	public void setActualNumberUnitsEnemy(int[] actualNumberUnitsEnemy) {
		this.actualNumberUnitsEnemy = actualNumberUnitsEnemy;
	}

	public String getBattleReport(int battles) {
		return "";
	}

	public String getBattleDevelopment() {
		return "";
	}

	public void initInitialArmies() {

	}

	public void updateResourcesLooses() {

	}

	public int[] fleetResourceCost(ArrayList<MilitaryUnit> army) {
		return null;
	}

	public int initialFleetNumber(ArrayList<MilitaryUnit> army) {
		return 0;
	}

	public int remainderPercentageFleet(ArrayList<MilitaryUnit> army) {
		return 0;
	}

	public int getGroupDefender(ArrayList<MilitaryUnit> army) {
		return 0;
	}

	public int getCivilizationGroupAttacker() {
		return 0;
	}

	public int getEnemyGroupAttacker() {
		return 0;
	}

	public void resetArmyArmor() {

	}

}
