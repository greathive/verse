package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnRequiredExpProcedure {
	public static double execute(Entity entity) {
		if (entity == null)
			return 0;
		return Math.round(112 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power));
	}
}