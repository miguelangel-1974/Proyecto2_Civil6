package M03;

import java.util.ArrayList;

public class Civilization {

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

	}

	public void newMagicTower() throws ResourceException {

	}

	public void newFarm() throws ResourceException {

	}

	public void newCarpentry() throws ResourceException {

	}

	public void newSmithy() throws ResourceException {

	}

	public void upgradeTechnologyDefense() throws ResourceException {

	}

	public void upgradeTechnologyAttack() throws ResourceException {

	}

	public void newSwordsman(int n) throws ResourceException {

	}

	public void newSpearman(int n) throws ResourceException {

	}

	public void newCrossbow(int n) throws ResourceException {

	}

	public void newCannon(int n) throws ResourceException {

	}

	public void newArrowTower(int n) throws ResourceException {

	}

	public void newCatapult(int n) throws ResourceException {

	}

	public void newRocketLauncher(int n) throws ResourceException {

	}

	public void newMagician(int n) throws ResourceException, BuildingException {

	}

	public void newPriest(int n) throws ResourceException, BuildingException {

	}

	public void printStats() {

	}

}
