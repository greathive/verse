package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.network.VerseModVariables;

public class GetTextureProcedure {
	public static ResourceLocation execute(Entity entity) {
		if (entity == null)
			return null;
		ResourceLocation topLayer = null;
		ResourceLocation bottomLayer = null;
		if (!(entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("")) {
			topLayer = ResourceLocation.parse(("verse:textures/entities/X.png".replace("X", entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit)));
			bottomLayer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity) instanceof LivingEntityRenderer renderer1 ? renderer1.getTextureLocation((LivingEntity) entity) : null;
			return topLayer;
		}
		return Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity) instanceof LivingEntityRenderer renderer2 ? renderer2.getTextureLocation((LivingEntity) entity) : null;
	}
}