package net.mcreator.verse.client.events;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ScrollDisable {

	@SubscribeEvent
	public static void onScroll(InputEvent.MouseScrollingEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return;
		}

		// Check if player has guardbroken effect
		if (player.hasEffect(net.mcreator.verse.init.VerseModMobEffects.GUARDBROKEN)) {
			event.setCanceled(true);
			return;
		}

		// Check if currently in a swing
		CompoundTag data = player.getPersistentData();

		if (data.contains("CustomAttackCooldownUntil")) {
			long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
			long currentTime = player.level().getGameTime();
			long ticksRemaining = cooldownUntil - currentTime;

			// Prevent scroll if on cooldown (except last 5 ticks)
			if (ticksRemaining > 5) {
				event.setCanceled(true);
			}
		}
	}
}