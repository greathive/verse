
package net.mcreator.verse.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.verse.client.renderer.DeeplightBlockRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class VerseModBlockEntityRenderers {
	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(VerseModBlockEntities.DEEPLIGHT.get(), DeeplightBlockRenderer::new);
	}
}