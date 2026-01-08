package net.mcreator.verse.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.mcreator.verse.util.EndlagDataLoader;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber(modid = VerseMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EndlagReloadListener {

	/**
	 * Load endlag data during common setup.
	 * This happens during mod initialization and will read from the data folder.
	 */
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			VerseMod.LOGGER.info("Common setup - loading endlag data");
			EndlagDataLoader.loadEndlagData();
		});
	}
}