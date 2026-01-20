package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class MantratestingitemRightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String name = "";
		if (name.contains("strong_left")) {
			name = "rapid_punches";
		} else {
			name = "strong_left";
		}
		if (entity.isShiftKeyDown()) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.mantrastorage = "";
				_vars.markSyncDirty();
			}
		} else {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.mantrastorage = entity.getData(VerseModVariables.PLAYER_VARIABLES).mantrastorage + "" + "[\"" + name + "\"{none,1,2,3,4}]";
				_vars.markSyncDirty();
			}
		}
	}
}