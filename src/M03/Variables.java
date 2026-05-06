package M03;

public interface Variables {

	// resources available to create the first enemy fleet
	public final int IRON_BASE_ENEMY_ARMY = 26000;
	public final int WOOD_BASE_ENEMY_ARMY = 180000;
	public final int FOOD_BASE_ENEMY_ARMY = 70000;

	// percentage increase of resources available to create enemy fleet
	public final int ENEMY_FLEET_INCREASE = 6;

	// resources increment every minute
	public final int CIVILIZATION_IRON_GENERATED = 1500;
	public final int CIVILIZATION_WOOD_GENERATED = 5000;
	public final int CIVILIZATION_FOOD_GENERATED = 8000;

	public final int CIVILIZATION_IRON_GENERATED_PER_SMITHY = (int) (0.5 * CIVILIZATION_IRON_GENERATED);
	public final int CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY = (int) (0.5 * CIVILIZATION_WOOD_GENERATED);
	public final int CIVILIZATION_FOOD_GENERATED_PER_FARM = (int) (0.5 * CIVILIZATION_FOOD_GENERATED);
	public final int CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER = 10;

	// TECHNOLOGY COST
	public final int UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST = 2000;
	public final int UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST = 2000;
	public final int UPGRADE_PLUS_DEFENSE_TECHNOLOGY_IRON_COST = 60;
	public final int UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST = 60;

	public final int UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST = 0;
	public final int UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST = 0;
	public final int UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST = 0;
	public final int UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST = 0;

	// COST ATTACK UNITS
	public final int FOOD_COST_SWORDSMAN = 8000;
	public final int FOOD_COST_SPEARMAN = 5000;
	public final int FOOD_COST_CROSSBOW = 0;
	public final int FOOD_COST_CANNON = 0;

	public final int WOOD_COST_SWORDSMAN = 3000;
	public final int WOOD_COST_SPEARMAN = 6500;
	public final int WOOD_COST_CROSSBOW = 45000;
	public final int WOOD_COST_CANNON = 30000;

	public final int IRON_COST_SWORDSMAN = 50;
	public final int IRON_COST_SPEARMAN = 50;
	public final int IRON_COST_CROSSBOW = 7000;
	public final int IRON_COST_CANNON = 15000;

	public final int MANA_COST_SWORDSMAN = 0;
	public final int MANA_COST_SPEARMAN = 0;
	public final int MANA_COST_CROSSBOW = 0;
	public final int MANA_COST_CANNON = 0;

	// COST DEFENSES ARROWTOWER, CATAPULT, ROCKETLAUNCHERTOWER
	public final int IRON_COST_ARROWTOWER = 0;
	public final int IRON_COST_CATAPULT = 500;
	public final int IRON_COST_ROCKETLAUNCHERTOWER = 5000;

	public final int WOOD_COST_ARROWTOWER = 2000;
	public final int WOOD_COST_CATAPULT = 4000;
	public final int WOOD_COST_ROCKETLAUNCHERTOWER = 50000;

	public final int FOOD_COST_ARROWTOWER = 0;
	public final int FOOD_COST_CATAPULT = 0;
	public final int FOOD_COST_ROCKETLAUNCHERTOWER = 0;

	public final int MANA_COST_ARROWTOWER = 0;
	public final int MANA_COST_CATAPULT = 0;
	public final int MANA_COST_ROCKETLAUNCHERTOWER = 0;

	// Cost Especial units
	public final int FOOD_COST_MAGICIAN = 12000;
	public final int FOOD_COST_PRIEST = 15000;

	public final int WOOD_COST_MAGICIAN = 2000;
	public final int WOOD_COST_PRIEST = 0;

	public final int IRON_COST_MAGICIAN = 500;
	public final int IRON_COST_PRIEST = 0;

	public final int MANA_COST_MAGICIAN = 5000;
	public final int MANA_COST_PRIEST = 15000;

	// array units costs SWORDSMAN, SPEARMAN, CROSSBOW, CANNON, ARROWTOWER, CATAPULT, ROCKETLAUNCHERTOWER, MAGICIAN, PRIEST
	public final int[] WOOD_COST_UNITS = { WOOD_COST_SWORDSMAN, WOOD_COST_SPEARMAN, WOOD_COST_CROSSBOW,
			WOOD_COST_CANNON, WOOD_COST_ARROWTOWER, WOOD_COST_CATAPULT, WOOD_COST_ROCKETLAUNCHERTOWER,
			WOOD_COST_MAGICIAN, WOOD_COST_PRIEST };

	public final int[] IRON_COST_UNITS = { IRON_COST_SWORDSMAN, IRON_COST_SPEARMAN, IRON_COST_CROSSBOW,
			IRON_COST_CANNON, IRON_COST_ARROWTOWER, IRON_COST_CATAPULT, IRON_COST_ROCKETLAUNCHERTOWER,
			IRON_COST_MAGICIAN, IRON_COST_PRIEST };

	public final int[] FOOD_COST_UNITS = { FOOD_COST_SWORDSMAN, FOOD_COST_SPEARMAN, FOOD_COST_CROSSBOW,
			FOOD_COST_CANNON, FOOD_COST_ARROWTOWER, FOOD_COST_CATAPULT, FOOD_COST_ROCKETLAUNCHERTOWER,
			FOOD_COST_MAGICIAN, FOOD_COST_PRIEST };

	// Cost Buildings
	public final int FOOD_COST_FARM = 5000;
	public final int WOOD_COST_FARM = 10000;
	public final int IRON_COST_FARM = 12000;

	public final int FOOD_COST_CARPENTRY = 5000;
	public final int WOOD_COST_CARPENTRY = 10000;
	public final int IRON_COST_CARPENTRY = 12000;

	public final int FOOD_COST_SMITHY = 5000;
	public final int WOOD_COST_SMITHY = 10000;
	public final int IRON_COST_SMITHY = 12000;

	public final int FOOD_COST_CHURCH = 5000;
	public final int WOOD_COST_CHURCH = 10000;
	public final int IRON_COST_CHURCH = 12000;

	public final int FOOD_COST_MAGICTOWER = 5000;
	public final int WOOD_COST_MAGICTOWER = 10000;
	public final int IRON_COST_MAGICTOWER = 12000;

	// BASE DAMAGE ATTACK UNITS
	public final int BASE_DAMAGE_SWORDSMAN = 80;
	public final int BASE_DAMAGE_SPEARMAN = 150;
	public final int BASE_DAMAGE_CROSSBOW = 1000;
	public final int BASE_DAMAGE_CANNON = 700;

	// BASE DAMAGE DEFENSES
	public final int BASE_DAMAGE_ARROWTOWER = 80;
	public final int BASE_DAMAGE_CATAPULT = 250;
	public final int BASE_DAMAGE_ROCKETLAUNCHERTOWER = 2000;
	public final int BASE_DAMAGE_MAGICIAN = 3000;

	// ARMOR ATTACK UNITS
	public final int ARMOR_SWORDSMAN = 400;
	public final int ARMOR_SPEARMAN = 1000;
	public final int ARMOR_CROSSBOW = 6000;
	public final int ARMOR_CANNON = 8000;

	// ARMOR DEFENSES
	public final int ARMOR_ARROWTOWER = 200;
	public final int ARMOR_CATAPULT = 1200;
	public final int ARMOR_ROCKETLAUNCHERTOWER = 7000;

	// Attack Units armor increase percentage per tech level
	public final int PLUS_ARMOR_SWORDSMAN_BY_TECHNOLOGY = 5;
	public final int PLUS_ARMOR_SPEARMAN_BY_TECHNOLOGY = 5;
	public final int PLUS_ARMOR_CROSSBOW_BY_TECHNOLOGY = 5;
	public final int PLUS_ARMOR_CANNON_BY_TECHNOLOGY = 5;

	// defense armor increase percentage per tech level
	public final int PLUS_ARMOR_ARROWTOWER_BY_TECHNOLOGY = 5;
	public final int PLUS_ARMOR_CATAPULT_BY_TECHNOLOGY = 5;
	public final int PLUS_ARMOR_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY = 5;

	// attack units power increase percentage per tech level
	public final int PLUS_ATTACK_SWORDSMAN_BY_TECHNOLOGY = 5;
	public final int PLUS_ATTACK_SPEARMAN_BY_TECHNOLOGY = 5;
	public final int PLUS_ATTACK_CROSSBOW_BY_TECHNOLOGY = 5;
	public final int PLUS_ATTACK_CANNON_BY_TECHNOLOGY = 5;

	// Defense attack power increase percentage per tech level
	public final int PLUS_ATTACK_ARROWTOWER_BY_TECHNOLOGY = 5;
	public final int PLUS_ATTACK_CATAPULT_BY_TECHNOLOGY = 5;
	public final int PLUS_ATTACK_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY = 5;
	public final int PLUS_ATTACK_MAGICIAN_BY_TECHNOLOGY = 6;

	public final int PLUS_ARMOR_UNIT_PER_EXPERIENCE_POINT = 4;
	public final int PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT = 4;

	// Units plus armor/attack increase percentage when sanctified
	public final int PLUS_ARMOR_UNIT_SANCTIFIED = 7;
	public final int PLUS_ATTACK_UNIT_SANCTIFIED = 7;

	// Chance of resurrection by magician
	public final int CHANCE_MAGICIAN_RESSURECT = 2;

	// fleet probability of generating waste
	public final int CHANCE_GENERATNG_WASTE_SWORDSMAN = 55;
	public final int CHANCE_GENERATNG_WASTE_SPEARMAN = 65;
	public final int CHANCE_GENERATNG_WASTE_CROSSBOW = 80;
	public final int CHANCE_GENERATNG_WASTE_CANNON = 90;

	// Defense probability of generating waste ARROWTOWER, CATAPULT, ROCKETLAUNCHERTOWER
	public final int CHANCE_GENERATNG_WASTE_ARROWTOWER = 55;
	public final int CHANCE_GENERATNG_WASTE_CATAPULT = 65;
	public final int CHANCE_GENERATNG_WASTE_ROCKETLAUNCHERTOWER = 75;

	// especial Units
	public final int CHANCE_GENERATNG_WASTE_PRIEST = 0;
	public final int CHANCE_GENERATNG_WASTE_MAGICIAN = 0;

	// AttackUnit chance to attack again
	public final int CHANCE_ATTACK_AGAIN_SWORDSMAN = 3;
	public final int CHANCE_ATTACK_AGAIN_SPEARMAN = 7;
	public final int CHANCE_ATTACK_AGAIN_CROSSBOW = 45;
	public final int CHANCE_ATTACK_AGAIN_CANNON = 70;

	// Defense chance to attack again
	public final int CHANCE_ATTACK_AGAIN_ARROWTOWER = 5;
	public final int CHANCE_ATTACK_AGAIN_CATAPULT = 12;
	public final int CHANCE_ATTACK_AGAIN_ROCKETLAUNCHERTOWER = 30;
	public final int CHANCE_ATTACK_AGAIN_MAGICIAN = 75;
	public final int CHANCE_ATTACK_AGAIN_PRIEST = 0;

	// CHANCE ATTACK EVERY UNIT
	// SWORDSMAN, SPEARMAN, CROSSBOW, CANNON, ARROWTOWER, CATAPULT, ROCKETLAUNCHERTOWER, MAGICIAN, PRIEST
	public final int[] CHANCE_ATTACK_CIVILIZATION_UNITS = { 4, 9, 13, 37, 4, 9, 14, 10, 0 };
	// SWORDSMAN, SPEARMAN, CROSSBOW, CANNON
	public final int[] CHANCE_ATTACK_ENEMY_UNITS = { 10, 20, 30, 40 };

	// percentage of waste that will be generated with respect to the cost of the units
	public final int PERCENTATGE_WASTE = 70;

}
