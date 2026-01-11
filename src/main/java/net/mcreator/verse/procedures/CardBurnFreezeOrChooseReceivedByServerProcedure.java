package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.init.VerseModMobEffects;
import net.mcreator.verse.VerseMod;

public class CardBurnFreezeOrChooseReceivedByServerProcedure {
	public static void execute(Entity entity, String inboundString) {
		if (entity == null || inboundString == null)
			return;
		String outcome = "";
		String cardchoice = "";
		String burntcard = "";
		String frozencard = "";
		double acecost = 0;
		VerseMod.LOGGER.info(inboundString);
		if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.contains("[burn:") || (entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome
				.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("[burn:") + "[burn:".length(), (int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("b]")))
				.equals(entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("[freeze:") + "[freeze:".length(),
						(int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("f]")))) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.cardoutcome = "[burn:()b]" + "[freeze:()f]";
				_vars.markSyncDirty();
			}
		}
		if (inboundString.contains("[freeze:")) {
			frozencard = inboundString.substring((int) inboundString.indexOf("("), (int) inboundString.indexOf(")") + ")".length());
			if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("[freeze:") + "[freeze:".length(),
					(int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("f]"))).equals(frozencard)) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.cardoutcome = entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.replace(entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome
							.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("[freeze:") + "[freeze:".length(), (int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("f]")), "()");
					_vars.markSyncDirty();
				}
			} else {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.cardoutcome = entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.replace(entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome
							.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("[freeze:") + "[freeze:".length(), (int) entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome.indexOf("f]")), frozencard);
					_vars.markSyncDirty();
				}
			}
		}
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(VerseModMobEffects.CLICKCD, 5, 0, false, false));
		VerseMod.LOGGER.info(entity.getData(VerseModVariables.PLAYER_VARIABLES).cardoutcome);
	}
}