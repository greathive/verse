package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class CleartalentdrawProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.pickedcards = false;
			_vars.guiOpenedTick = entity.tickCount;
			_vars.secondaryguitick = 0;
			_vars.markSyncDirty();
		}
		entity.getPersistentData().putString("lastchoice", "none");
	}
}