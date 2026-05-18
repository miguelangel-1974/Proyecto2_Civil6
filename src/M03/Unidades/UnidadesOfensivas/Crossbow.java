package M03.Unidades.UnidadesOfensivas;

public class Crossbow extends AttackUnit {

	public Crossbow(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
		this.setSanctified(false);
	}

	public Crossbow() {
		this.setArmor(ARMOR_CROSSBOW);
		this.setInitialArmor(ARMOR_CROSSBOW);
		this.setBaseDamage(BASE_DAMAGE_CROSSBOW);
		this.setExperience(0);
		this.setSanctified(false);
	}


	public int getFoodCost() {
		return FOOD_COST_CROSSBOW;
	}

	public int getWoodCost() {
		return WOOD_COST_CROSSBOW;
	}

	public int getIronCost() {
		return IRON_COST_CROSSBOW;
	}

	public int getManaCost() {
		return MANA_COST_CROSSBOW;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_CROSSBOW;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_CROSSBOW;
	}
	
}
