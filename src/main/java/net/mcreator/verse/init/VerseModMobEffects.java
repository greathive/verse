/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.verse.potion.ScreenshakingMobEffect;
import net.mcreator.verse.potion.ParryFrameMobEffect;
import net.mcreator.verse.potion.ParryCDMobEffect;
import net.mcreator.verse.potion.NoAttackMobEffect;
import net.mcreator.verse.potion.BlockFrameMobEffect;
import net.mcreator.verse.VerseMod;

public class VerseModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, VerseMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> SCREENSHAKING = REGISTRY.register("screenshaking", () -> new ScreenshakingMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> PARRY_FRAME = REGISTRY.register("parry_frame", () -> new ParryFrameMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> PARRY_CD = REGISTRY.register("parry_cd", () -> new ParryCDMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> NO_ATTACK = REGISTRY.register("no_attack", () -> new NoAttackMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> BLOCK_FRAME = REGISTRY.register("block_frame", () -> new BlockFrameMobEffect());
}