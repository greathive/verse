package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class TempRightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (!entity.isShiftKeyDown()) {
			if (entity.getData(VerseModVariables.PLAYER_VARIABLES).power < 20) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.power = entity.getData(VerseModVariables.PLAYER_VARIABLES).power + 1;
					_vars.markSyncDirty();
				}
			}
		} else {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.power = 1;
				_vars.str = 0;
				_vars.fort = 0;
				_vars.intel = 0;
				_vars.agility = 0;
				_vars.Flamecharm = 0;
				_vars.Galebreathe = 0;
				_vars.Thundercall = 0;
				_vars.Frostdraw = 0;
				_vars.Shadowcast = 0;
				_vars.Ironsing = 0;
				_vars.Lifeweave = 0;
				_vars.Bloodrend = 0;
				_vars.strExp = 0;
				_vars.fortExp = 0;
				_vars.aglExp = 0;
				_vars.intExp = 0;
				_vars.attuneExp = 0;
				_vars.attunementchooserguivariable = "(flamecharm)(galebreathe)(frostdraw)(thundercall)(shadowcast)(ironsing)(bloodrend)(lifeweave)";
				_vars.talentlist = "";
				_vars.validdraw = "";
				_vars.burn = "";
				_vars.freeze = "";
				_vars.choseattunement = false;
				_vars.hasCard = false;
				_vars.pickedcards = false;
				_vars.markSyncDirty();
			}
		}
	}
}