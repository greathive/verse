/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.verse.network.StatMenuMessage;
import net.mcreator.verse.network.MantraSelectionMessage;

@EventBusSubscriber(Dist.CLIENT)
public class VerseModKeyMappings {
	public static final KeyMapping PARRY = new KeyMapping("key.verse.parry", GLFW.GLFW_KEY_V, "key.categories.verse");
	public static final KeyMapping STAT_MENU = new KeyMapping("key.verse.stat_menu", GLFW.GLFW_KEY_N, "key.categories.verse") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new StatMenuMessage(0, 0));
				StatMenuMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping MANTRA_SELECTION = new KeyMapping("key.verse.mantra_selection", GLFW.GLFW_KEY_B, "key.categories.verse") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new MantraSelectionMessage(0, 0));
				MantraSelectionMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(PARRY);
		event.register(STAT_MENU);
		event.register(MANTRA_SELECTION);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				STAT_MENU.consumeClick();
				MANTRA_SELECTION.consumeClick();
			}
		}
	}
}