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
import net.mcreator.verse.network.ParryMessage;

@EventBusSubscriber(Dist.CLIENT)
public class VerseModKeyMappings {
	public static final KeyMapping PARRY = new KeyMapping("key.verse.parry", GLFW.GLFW_KEY_V, "key.categories.verse") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new ParryMessage(0, 0));
				ParryMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				PARRY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - PARRY_LASTPRESS);
				PacketDistributor.sendToServer(new ParryMessage(1, dt));
				ParryMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
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
	private static long PARRY_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(PARRY);
		event.register(STAT_MENU);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				PARRY.consumeClick();
				STAT_MENU.consumeClick();
			}
		}
	}
}