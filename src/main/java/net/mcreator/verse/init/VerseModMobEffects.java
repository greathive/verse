/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.verse.procedures.GuardbrokenEffectExpiresProcedure;
import net.mcreator.verse.procedures.ClickcdEffectExpiresProcedure;
import net.mcreator.verse.potion.*;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public class VerseModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, VerseMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> SCREENSHAKING = REGISTRY.register("screenshaking", () -> new ScreenshakingMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> PARRY_FRAME = REGISTRY.register("parry_frame", () -> new ParryFrameMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> PARRY_CD = REGISTRY.register("parry_cd", () -> new ParryCDMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> NO_ATTACK = REGISTRY.register("no_attack", () -> new NoAttackMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> BLOCK_FRAME = REGISTRY.register("block_frame", () -> new BlockFrameMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> AGILITY_TRAINING = REGISTRY.register("agility_training", () -> new AgilityTrainingMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> STRENGTH_TRAINING = REGISTRY.register("strength_training", () -> new StrengthTrainingMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> GUARDBROKEN = REGISTRY.register("guardbroken", () -> new GuardbrokenMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> CLICKCD = REGISTRY.register("clickcd", () -> new ClickcdMobEffect());

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	private static void expireEffects(Entity entity, MobEffectInstance effectInstance) {
		if (effectInstance.getEffect().is(GUARDBROKEN)) {
			GuardbrokenEffectExpiresProcedure.execute(entity);
		} else if (effectInstance.getEffect().is(CLICKCD)) {
			ClickcdEffectExpiresProcedure.execute(entity);
		}
	}
}