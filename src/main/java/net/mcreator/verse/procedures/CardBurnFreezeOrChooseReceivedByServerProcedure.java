package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.init.VerseModMobEffects;

public class CardBurnFreezeOrChooseReceivedByServerProcedure {
	public static void execute(Entity entity, String inboundString) {
		if (entity == null || inboundString == null)
			return;
		String outcome = "";
		String cardchoice = "";
		String burntcard = "";
		String frozencard = "";
		double acecost = 0;
		if (inboundString.contains("burn")) {
			if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).freeze.contains(inboundString.substring((int) inboundString.indexOf("("), (int) inboundString.indexOf(")") + ")".length()))) {
				burntcard = inboundString.substring((int) inboundString.indexOf("("), (int) inboundString.indexOf(")") + ")".length());
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.burn = entity.getData(VerseModVariables.PLAYER_VARIABLES).burn + "" + burntcard;
					_vars.secondaryguitick = entity.tickCount;
					_vars.markSyncDirty();
				}
			}
		}
		if (inboundString.contains("freeze")) {
			if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).freeze.contains(inboundString.substring((int) inboundString.indexOf("("), (int) inboundString.indexOf(")") + ")".length()))) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.freeze = inboundString.substring((int) inboundString.indexOf("("), (int) inboundString.indexOf(")") + ")".length());
					_vars.markSyncDirty();
				}
			} else {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.freeze = "";
					_vars.markSyncDirty();
				}
			}
		}
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(VerseModMobEffects.CLICKCD, 10, 0, false, false));
	}
}