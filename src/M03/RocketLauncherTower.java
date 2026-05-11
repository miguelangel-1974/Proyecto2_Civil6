package M03;

public class RocketLauncherTower extends DefenseUnit {

	public RocketLauncherTower(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
		this.setSanctified(false);
	}
	
	public int getFoodCost() {
		return FOOD_COST_ROCKETLAUNCHERTOWER;
	}

	public int getWoodCost() {
		return WOOD_COST_ROCKETLAUNCHERTOWER;
	}

	public int getIronCost() {
		return IRON_COST_ROCKETLAUNCHERTOWER;
	}

	public int getManaCost() {
		return MANA_COST_ROCKETLAUNCHERTOWER;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_ROCKETLAUNCHERTOWER;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_ROCKETLAUNCHERTOWER;
	}
}
