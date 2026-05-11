package M03;

public abstract class SpecialUnit implements MilitaryUnit, Variables {

	private int armor;
	private int initialArmor;
	private int baseDamage;
	private int experience;

	public SpecialUnit() {

	}

	public int attack() {
		int damage = this.baseDamage+ (this.experience * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT * this.baseDamage / 100);
		return damage;
	}

	public void takeDamage(int receivedDamage) {
		this.armor = this.armor - receivedDamage;
	}

	public int getActualArmor() {
		return this.armor;
	}

	public void resetArmor() {
		this.armor = this.initialArmor;
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

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

}