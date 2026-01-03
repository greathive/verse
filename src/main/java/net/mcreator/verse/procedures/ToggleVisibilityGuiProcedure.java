package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ToggleVisibilityGuiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).outfitVisible == true) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.outfitVisible = false;
				_vars.markSyncDirty();
			}
		} else {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.outfitVisible = true;
				_vars.markSyncDirty();
			}
		}
	}
}