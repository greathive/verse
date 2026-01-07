package net.mcreator.verse.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.mcreator.verse.util.EndlagDataLoader;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber(modid = VerseMod.MODID, value = Dist.CLIENT)
public class EndlagReloadListener {

	/**
	 * Reload endlag data when the player logs in or changes dimensions.
	 * This ensures data is loaded on world join and respects datapacks.
	 */
	@SubscribeEvent
	public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
		VerseMod.LOGGER.info("Player logged in, reloading endlag data");
		EndlagDataLoader.reload();
	}
}