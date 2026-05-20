package M03.Unidades.UnidadesOfensivas;

import M03.MilitaryUnit;
import M03.Variables;

public abstract class AttackUnit implements MilitaryUnit, Variables {

	private int armor;
	private int initialArmor;
	private int baseDamage;
	private int experience;
	private boolean sanctified;

	public AttackUnit() {

	}

	public int attack() {
		int damage = this.baseDamage
				+ (this.experience * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT * this.baseDamage / 100);
		if (this.sanctified) {
			damage = damage + (PLUS_ATTACK_UNIT_SANCTIFIED * this.baseDamage / 100);
		}
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

	public boolean isSanctified() {
		return sanctified;
	}

	public void setSanctified(boolean sanctified) {
		this.sanctified = sanctified;
	}

}