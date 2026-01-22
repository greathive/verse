package net.mcreator.verse.client.events;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.mcreator.verse.client.renderer.ScyphoziaEffects;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DimensionEffectsRegistry {

	@SubscribeEvent
	public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
		event.register(
				ResourceLocation.fromNamespaceAndPath("verse", "scyphozia"),
				new ScyphoziaEffects()
		);
	}
}