package M03.Unidades.UnidadesOfensivas;

public class Spearman extends AttackUnit {

	public Spearman(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
		this.setSanctified(false);
	}

	public Spearman() {
		this.setArmor(ARMOR_SPEARMAN);
		this.setInitialArmor(ARMOR_SPEARMAN);
		this.setBaseDamage(BASE_DAMAGE_SPEARMAN);
		this.setExperience(0);
		this.setSanctified(false);
	}


	public int getFoodCost() {
		return FOOD_COST_SPEARMAN;
	}

	public int getWoodCost() {
		return WOOD_COST_SPEARMAN;
	}

	public int getIronCost() {
		return IRON_COST_SPEARMAN;
	}

	public int getManaCost() {
		return MANA_COST_SPEARMAN;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_SPEARMAN;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_SPEARMAN;
	}
	
}
