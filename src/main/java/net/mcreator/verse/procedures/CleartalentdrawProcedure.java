package net.mcreator.verse.procedures;

import net.minecraft.world.entity.player.Player;
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
			_vars.markSyncDirty();
		}
		if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.contains("{")) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.hasCard = false;
				_vars.pickedcards = false;
				_vars.markSyncDirty();
			}
			if (entity instanceof Player _player)
				_player.closeContainer();
		} else {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.validdraw = entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.replace(entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw
						.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.indexOf("{"), (int) entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw.indexOf("}") + "}".length()), "");
				_vars.markSyncDirty();
			}
		}
	}
}