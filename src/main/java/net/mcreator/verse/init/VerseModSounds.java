/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.verse.VerseMod;

public class VerseModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, VerseMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> PARRY = REGISTRY.register("parry", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "parry")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ATTRIBUTEINCREASE = REGISTRY.register("attributeincrease", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "attributeincrease")));
	public static final DeferredHolder<SoundEvent, SoundEvent> POWERUP = REGISTRY.register("powerup", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "powerup")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SWORDSWING1 = REGISTRY.register("swordswing1", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "swordswing1")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SWORDSWING2 = REGISTRY.register("swordswing2", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "swordswing2")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SWORDSWING3 = REGISTRY.register("swordswing3", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "swordswing3")));
	public static final DeferredHolder<SoundEvent, SoundEvent> AXESWING1 = REGISTRY.register("axeswing1", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "axeswing1")));
	public static final DeferredHolder<SoundEvent, SoundEvent> AXESWING2 = REGISTRY.register("axeswing2", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "axeswing2")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BLOCKEDATTACK = REGISTRY.register("blockedattack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "blockedattack")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BLOCKATTEMPT = REGISTRY.register("blockattempt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "blockattempt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> POSTUREBREAK = REGISTRY.register("posturebreak", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "posturebreak")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FREEZE = REGISTRY.register("freeze", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "freeze")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BURN = REGISTRY.register("burn", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("verse", "burn")));
}