package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class AvailablestatcheckerProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if ((((((((((((entity.getData(VerseModVariables.PLAYER_VARIABLES).power * 6 - entity.getData(VerseModVariables.PLAYER_VARIABLES).str) - entity.getData(VerseModVariables.PLAYER_VARIABLES).fort)
				- entity.getData(VerseModVariables.PLAYER_VARIABLES).intel) - entity.getData(VerseModVariables.PLAYER_VARIABLES).agility) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm)
				- entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw)
				- entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave)
				- entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend > 0) {
			return true;
		}
		return false;
	}
}