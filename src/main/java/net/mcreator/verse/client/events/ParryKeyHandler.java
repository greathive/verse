package net.mcreator.verse.client.events;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.network.ParryPacket;
import net.mcreator.verse.init.VerseModKeyMappings;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ParryKeyHandler {

	@SubscribeEvent
	public static void onKeyPress(InputEvent.Key event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;

		if (player == null || mc.screen != null) {
			return;
		}

		// Check if parry key was pressed
		if (event.getKey() == VerseModKeyMappings.PARRY.getKey().getValue() &&
				event.getAction() == 1) { // 1 = GLFW_PRESS

			// Send packet to server to initiate parry
			PacketDistributor.sendToServer(new ParryPacket());
		}
	}
}