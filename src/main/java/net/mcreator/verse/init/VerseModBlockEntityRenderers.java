package net.mcreator.verse.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.mcreator.verse.client.renderer.DeeplightBlockRenderer;
import net.mcreator.verse.block.entity.DeeplightBlockEntity;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VerseModBlockEntityRenderers {
	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer((BlockEntityType<DeeplightBlockEntity>) VerseModBlockEntities.DEEPLIGHT.get(), DeeplightBlockRenderer::new);
	}
}