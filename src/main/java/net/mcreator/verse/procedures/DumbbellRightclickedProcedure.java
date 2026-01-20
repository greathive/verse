package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class DumbbellRightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.strExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp + 200;
			_vars.markSyncDirty();
		}
	}
}