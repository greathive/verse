package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.init.VerseModMobEffects;

public class CardClickPacketProcedure {
	public static void execute(Entity entity, String inboundString) {
		if (entity == null || inboundString == null)
			return;
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.ace = inboundString;
			_vars.markSyncDirty();
		}
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(VerseModMobEffects.CLICKCD, 4, 0, false, false));
	}
}