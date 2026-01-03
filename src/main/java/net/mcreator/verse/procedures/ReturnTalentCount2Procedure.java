package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnTalentCount2Procedure {
	public static double execute(Entity entity, String talents) {
		if (entity == null || talents == null)
			return 0;
		return entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.indexOf(talents, 0);
	}
}