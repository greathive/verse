package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class IwantthiscardpleaseProcedure {
	public static void execute(Entity entity, String inboundString) {
		if (entity == null || inboundString == null)
			return;
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.contains(inboundString)) {
			GrantTalentProcedure.execute(entity, inboundString);
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.secondaryguitick = entity.tickCount;
				_vars.pickedcards = true;
				_vars.markSyncDirty();
			}
		}
	}
}