package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class CheckForTalentProcedure {
	public static boolean execute(Entity entity, String talentrequest) {
		if (entity == null || talentrequest == null)
			return false;
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.contains(talentrequest)) {
			return true;
		}
		return false;
	}
}