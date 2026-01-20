package net.mcreator.verse.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class EndpowerProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player)
			_player.closeContainer();
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.pickedcards = false;
			_vars.hasCard = false;
			_vars.markSyncDirty();
		}
	}
}