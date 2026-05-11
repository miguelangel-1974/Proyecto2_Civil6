package M03;

public class Cannon extends AttackUnit {

	public Cannon(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
		this.setSanctified(false);
	}

	public Cannon() {
		this.setArmor(ARMOR_CANNON);
		this.setInitialArmor(ARMOR_CANNON);
		this.setBaseDamage(BASE_DAMAGE_CANNON);
		this.setExperience(0);
		this.setSanctified(false);
	}

	public int getFoodCost() {
		return FOOD_COST_CANNON;
	}

	public int getWoodCost() {
		return WOOD_COST_CANNON;
	}

	public int getIronCost() {
		return IRON_COST_CANNON;
	}

	public int getManaCost() {
		return MANA_COST_CANNON;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_CANNON;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_CANNON;
	}


}
