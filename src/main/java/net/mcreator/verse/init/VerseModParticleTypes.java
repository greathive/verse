/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.verse.VerseMod;

public class VerseModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, VerseMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARRYPARTICLE = REGISTRY.register("parryparticle", () -> new SimpleParticleType(true));
}