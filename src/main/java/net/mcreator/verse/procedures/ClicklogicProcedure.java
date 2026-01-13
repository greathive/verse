package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.VerseMod;

public class ClicklogicProcedure {
	public static void execute(Entity entity, String inboundString) {
		if (entity == null || inboundString == null)
			return;
		String outcome = "";
		String cardchoice = "";
		String burntcard = "";
		String frozencard = "";
		double acecost = 0;
		if ((inboundString).equals("clickstart")) {
			VerseMod.LOGGER.info("clicking start!");
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.clicklogic = true;
				_vars.markSyncDirty();
			}
		}
		if ((inboundString).equals("clickend")) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.clicklogic = false;
				_vars.markSyncDirty();
			}
			VerseMod.LOGGER.info("clicking end!");
		}
	}
}