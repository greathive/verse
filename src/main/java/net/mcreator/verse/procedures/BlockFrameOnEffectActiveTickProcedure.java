package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.verse.init.VerseModMobEffects;

public class BlockFrameOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(VerseModMobEffects.PARRY_FRAME))) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD, 3, 0));
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(VerseModMobEffects.NO_ATTACK, 3, 0));
		}
		if (entity instanceof LivingEntity _livEnt3 && _livEnt3.hasEffect(VerseModMobEffects.BLOCK_FRAME)) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(VerseModMobEffects.BLOCK_FRAME, 5, 0));
		}
	}
}