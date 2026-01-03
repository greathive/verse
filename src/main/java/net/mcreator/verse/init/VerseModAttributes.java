/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.verse.VerseMod;

import java.util.stream.Collectors;
import java.util.List;

@EventBusSubscriber
public class VerseModAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, VerseMod.MODID);
	public static final DeferredHolder<Attribute, Attribute> MANTRA_SPEED = REGISTRY.register("mantra_speed", () -> new RangedAttribute("attribute.verse.mantra_speed", 1, 0.1, 4).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> SWING_SPEED = REGISTRY.register("swing_speed", () -> new RangedAttribute("attribute.verse.swing_speed", 1, 0, 10).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> POSTURE = REGISTRY.register("posture", () -> new RangedAttribute("attribute.verse.posture", 0, 0, 50).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> MAX_POSTURE = REGISTRY.register("max_posture", () -> new RangedAttribute("attribute.verse.max_posture", 5, 0, 50).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> POSTURE_BONUS = REGISTRY.register("posture_bonus", () -> new RangedAttribute("attribute.verse.posture_bonus", 1, 0, 50).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		List.of(VerseModEntities.NPC.get()).stream().filter(DefaultAttributes::hasSupplier).map(entityType -> (EntityType<? extends LivingEntity>) entityType).collect(Collectors.toList()).forEach(entity -> event.add(entity, MANTRA_SPEED));
		event.add(EntityType.PLAYER, MANTRA_SPEED);
		List.of(VerseModEntities.NPC.get()).stream().filter(DefaultAttributes::hasSupplier).map(entityType -> (EntityType<? extends LivingEntity>) entityType).collect(Collectors.toList()).forEach(entity -> event.add(entity, SWING_SPEED));
		event.add(EntityType.PLAYER, POSTURE);
		event.add(EntityType.PLAYER, MAX_POSTURE);
		event.getTypes().forEach(entity -> event.add(entity, POSTURE_BONUS));
	}
}