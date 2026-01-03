package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnStatValueProcedure {
	public static double execute(Entity entity, String stat) {
		if (entity == null || stat == null)
			return 0;
		double attunement = 0;
		if ((stat).equals("flamecharm")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm;
		} else if ((stat).equals("galebreathe")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe;
		} else if ((stat).equals("frostdraw")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw;
		} else if ((stat).equals("thundercall")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall;
		} else if ((stat).equals("shadowcast")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast;
		} else if ((stat).equals("ironsing")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing;
		} else if ((stat).equals("lifeweave")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave;
		} else if ((stat).equals("bloodrend")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend;
		} else if ((stat).equals("strength")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).str;
		} else if ((stat).equals("fortitude")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).fort;
		} else if ((stat).equals("agility")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).agility;
		} else if ((stat).equals("mind")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).intel;
		}
		return 0;
	}
}