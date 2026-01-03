package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnchoiceProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		String textss = "";
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.contains("(") && entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.contains(")")) {
			return entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.indexOf("(") + "(".length(),
					(int) entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.indexOf(")"));
		}
		return "No Attunement";
	}
}