package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class AttunementchoicebuttonresetProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.choice = "";
			_vars.markSyncDirty();
		}
	}
}