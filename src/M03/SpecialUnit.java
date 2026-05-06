package M03;

public abstract class SpecialUnit implements MilitaryUnit, Variables {

	private int armor;
	private int initialArmor;
	private int baseDamage;
	private int experience;

	public SpecialUnit() {

	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getInitialArmor() {
		return initialArmor;
	}

	public void setInitialArmor(int initialArmor) {
		this.initialArmor = initialArmor;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public int attack() {
		return 0;
	}

	public void takeDamage(int receivedDamage) {

	}

	public int getActualArmor() {
		return 0;
	}

	public void resetArmor() {

	}

	public void setExperience(int n) {
		this.experience = n;
	}

	public int getExperience() {
		return experience;
	}

	public abstract int getFoodCost();

	public abstract int getWoodCost();

	public abstract int getIronCost();

	public abstract int getManaCost();

	public abstract int getChanceGeneratinWaste();

	public abstract int getChanceAttackAgain();

}
