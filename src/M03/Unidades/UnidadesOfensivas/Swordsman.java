package M03.Unidades.UnidadesOfensivas;

public class Swordsman extends AttackUnit {

	public Swordsman(int armor, int baseDamage) {
		this.setArmor(armor);
		this.setInitialArmor(armor);
		this.setBaseDamage(baseDamage);
		this.setExperience(0);
		this.setSanctified(false);
	}

	public Swordsman() {
		this.setArmor(ARMOR_SWORDSMAN);
		this.setInitialArmor(ARMOR_SWORDSMAN);
		this.setBaseDamage(BASE_DAMAGE_SWORDSMAN);
		this.setExperience(0);
		this.setSanctified(false);
	}

	public int getFoodCost() {
		return FOOD_COST_SWORDSMAN;
	}

	public int getWoodCost() {
		return WOOD_COST_SWORDSMAN;
	}

	public int getIronCost() {
		return IRON_COST_SWORDSMAN;
	}

	public int getManaCost() {
		return MANA_COST_SWORDSMAN;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATNG_WASTE_SWORDSMAN;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_SWORDSMAN;
	}

}