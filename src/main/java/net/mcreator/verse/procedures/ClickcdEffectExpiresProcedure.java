package net.mcreator.verse.procedures;

import org.lwjgl.glfw.GLFW;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.init.VerseModMobEffects;

public class ClickcdEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(VerseModMobEffects.CLICKCD, 4, 0, false, false));
		}
	}
}