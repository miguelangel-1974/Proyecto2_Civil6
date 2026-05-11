package M03;

public class Magician extends SpecialUnit {

	public Magician(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
	}
	
	public int getFoodCost() {
		return FOOD_COST_MAGICIAN;
	}

	public int getWoodCost() {
		return WOOD_COST_MAGICIAN;
	}

	public int getIronCost() {
		return IRON_COST_MAGICIAN;
	}

	public int getManaCost() {
		return MANA_COST_MAGICIAN;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_MAGICIAN;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_MAGICIAN;
	}
	
}
