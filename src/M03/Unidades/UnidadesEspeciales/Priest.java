package M03.Unidades.UnidadesEspeciales;

public class Priest extends SpecialUnit {

	public Priest(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
	}
	
	public int getFoodCost() {
		return FOOD_COST_PRIEST;
	}

	public int getWoodCost() {
		return WOOD_COST_PRIEST;
	}

	public int getIronCost() {
		return IRON_COST_PRIEST;
	}

	public int getManaCost() {
		return MANA_COST_PRIEST;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_PRIEST;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_PRIEST;
	}
}
