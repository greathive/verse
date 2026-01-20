/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.verse.block.entity.DeeplightBlockEntity;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class VerseModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VerseMod.MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> DEEPLIGHT = REGISTRY.register("deeplight",
			() -> BlockEntityType.Builder.of(DeeplightBlockEntity::new, VerseModBlocks.DEEPLIGHT.get()).build(null));

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		// Register capabilities here if needed
	}
}