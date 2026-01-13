package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class BurnthatcardProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String outcome = "";
		String cardchoice = "";
		String burntcard = "";
		String frozencard = "";
		double acecost = 0;
		burntcard = entity.getData(VerseModVariables.PLAYER_VARIABLES).burn.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).burn.lastIndexOf("("),
				(int) entity.getData(VerseModVariables.PLAYER_VARIABLES).burn.lastIndexOf(")") + ")".length());
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.contains(burntcard) && !entity.getData(VerseModVariables.PLAYER_VARIABLES).freeze.contains(burntcard)) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.validdraw = entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.replace(burntcard, "");
				_vars.markSyncDirty();
			}
		}
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.secondaryguitick = 0;
			_vars.markSyncDirty();
		}
	}
}