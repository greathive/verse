package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class Attunementcount3Procedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		double count = 0;
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave > 0) {
			count = count + 1;
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend > 0) {
			count = count + 1;
		}
		if (count > 2) {
			return true;
		}
		return false;
	}
}