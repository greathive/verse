package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class GrantTalentProcedure {
	public static void execute(Entity entity, String grant) {
		if (entity == null || grant == null)
			return;
		if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.contains(grant)) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.talentlist = entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist + "" + grant;
				_vars.markSyncDirty();
			}
		}
	}
}