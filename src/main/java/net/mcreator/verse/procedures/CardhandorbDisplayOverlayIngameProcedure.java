package net.mcreator.verse.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.verse.world.inventory.CardHandMenu;
import net.mcreator.verse.network.VerseModVariables;

public class CardhandorbDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity instanceof Player _plr0 && _plr0.containerMenu instanceof CardHandMenu) {
			return false;
		}
		if (false == entity.getData(VerseModVariables.PLAYER_VARIABLES).hasCard) {
			return false;
		}
		return true;
	}
}