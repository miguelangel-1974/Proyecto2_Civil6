package M03.Excepciones;

import java.util.TimerTask;

import M03.Civilization;
import M03.Variables;

public class ResourceGenerator extends TimerTask implements Variables {

	private Civilization civilization;

	public ResourceGenerator(Civilization civilization) {
		this.civilization = civilization;
	}

	public Civilization getCivilization() {
		return this.civilization;
	}

	public void setCivilization(Civilization civilization) {
		this.civilization = civilization;
	}

	public void run() {
		int newWood = this.civilization.getWood()
				+ CIVILIZATION_WOOD_GENERATED
				+ this.civilization.getCarpentry() * CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY;

		int newIron = this.civilization.getIron()
				+ CIVILIZATION_IRON_GENERATED
				+ this.civilization.getSmithy() * CIVILIZATION_IRON_GENERATED_PER_SMITHY;

		int newFood = this.civilization.getFood()
				+ CIVILIZATION_FOOD_GENERATED
				+ this.civilization.getFarm() * CIVILIZATION_FOOD_GENERATED_PER_FARM;

		int newMana = this.civilization.getMana()
				+ this.civilization.getMagicTower() * CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER;

		this.civilization.setWood(newWood);
		this.civilization.setIron(newIron);
		this.civilization.setFood(newFood);
		this.civilization.setMana(newMana);

	}

}