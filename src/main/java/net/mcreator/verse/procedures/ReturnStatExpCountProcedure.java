package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnStatExpCountProcedure {
	public static double execute(Entity entity, String stat) {
		if (entity == null || stat == null)
			return 0;
		double var = 0;
		if ((stat).equals("str")) {
			var = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp;
		} else if ((stat).equals("fort")) {
			var = entity.getData(VerseModVariables.PLAYER_VARIABLES).fortExp;
		} else if ((stat).equals("agl")) {
			var = entity.getData(VerseModVariables.PLAYER_VARIABLES).aglExp;
		} else if ((stat).equals("agl")) {
			var = entity.getData(VerseModVariables.PLAYER_VARIABLES).intExp;
		} else if ((stat).equals("attune")) {
			var = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
		}
		return var / (100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power));
	}
}