package net.mcreator.verse.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.procedures.CardhandorbDisplayOverlayIngameProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class CardhandorbOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		if (CardhandorbDisplayOverlayIngameProcedure.execute(entity)) {

			event.getGuiGraphics().blit(ResourceLocation.parse("verse:textures/screens/cardhandindicator.png"), w / 2 + -8, h - 55, 0, 0, 16, 16, 16, 128);

		}
	}
}