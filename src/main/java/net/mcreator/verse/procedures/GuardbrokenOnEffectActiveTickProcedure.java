package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.verse.init.VerseModMobEffects;

public class GuardbrokenOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD, entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.GUARDBROKEN) ? _livEnt.getEffect(VerseModMobEffects.GUARDBROKEN).getDuration() : 0, 0));
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(VerseModMobEffects.NO_ATTACK, entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.GUARDBROKEN) ? _livEnt.getEffect(VerseModMobEffects.GUARDBROKEN).getDuration() : 0, 0));
	}
}