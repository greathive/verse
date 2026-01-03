package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class OutfitReseterRightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.isShiftKeyDown()) {
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
		} else {
			if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.outfit = "corsairoutlaw";
					_vars.markSyncDirty();
				}
			} else if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("corsairoutlaw")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.outfit = "stranded";
					_vars.markSyncDirty();
				}
			} else if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("stranded")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.outfit = "bountyhunter";
					_vars.markSyncDirty();
				}
			} else if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("bountyhunter")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.outfit = "eagertradesman";
					_vars.markSyncDirty();
				}
			} else {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.outfit = "";
					_vars.markSyncDirty();
				}
			}
		}
	}
}