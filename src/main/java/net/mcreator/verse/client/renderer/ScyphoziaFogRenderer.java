package net.mcreator.verse.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ScyphoziaFogRenderer {

	private static final ResourceLocation SCYPHOZIA_DIMENSION = ResourceLocation.fromNamespaceAndPath("verse", "scyphozia");
	private static final float FOG_TRANSITION_START = 24.0f;
	private static final float FOG_TRANSITION_END = 8.0f;
	private static final float MAX_FOG_DISTANCE = 12.0f;
	private static final float MIN_FOG_DISTANCE = 2.0f;

	@SubscribeEvent
	public static void onRenderFog(ViewportEvent.RenderFog event) {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;

		if (level == null) {
			return;
		}

		if (!level.dimension().location().equals(SCYPHOZIA_DIMENSION)) {
			return;
		}

		Camera camera = event.getCamera();
		double cameraY = camera.getPosition().y;

		float fogDensity = calculateFogDensity((float) cameraY);

		if (fogDensity > 0.0f) {
			event.setCanceled(true);

			float farPlaneDistance = lerp(event.getFarPlaneDistance(), MAX_FOG_DISTANCE, fogDensity);
			float nearPlaneDistance = Math.max(MIN_FOG_DISTANCE, farPlaneDistance * 0.1f);

			RenderSystem.setShaderFogStart(nearPlaneDistance);
			RenderSystem.setShaderFogEnd(farPlaneDistance);
		}
	}

	@SubscribeEvent
	public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;

		if (level == null) {
			return;
		}

		if (!level.dimension().location().equals(SCYPHOZIA_DIMENSION)) {
			return;
		}

		Camera camera = event.getCamera();
		double cameraY = camera.getPosition().y;

		float fogDensity = calculateFogDensity((float) cameraY);

		if (fogDensity > 0.0f) {
			float baseRed = 11f / 255f;
			float baseGreen = 20f / 255f;
			float baseBlue = 116f / 255f;

			float originalRed = (float) event.getRed();
			float originalGreen = (float) event.getGreen();
			float originalBlue = (float) event.getBlue();

			event.setRed(lerp(originalRed, baseRed, fogDensity));
			event.setGreen(lerp(originalGreen, baseGreen, fogDensity));
			event.setBlue(lerp(originalBlue, baseBlue, fogDensity));
		}
	}

	private static float calculateFogDensity(float y) {
		if (y >= FOG_TRANSITION_START) {
			return 0.0f;
		} else if (y <= FOG_TRANSITION_END) {
			return 1.0f;
		} else {
			float transitionRange = FOG_TRANSITION_START - FOG_TRANSITION_END;
			float transitionProgress = (FOG_TRANSITION_START - y) / transitionRange;
			return transitionProgress;
		}
	}

	private static float lerp(float start, float end, float delta) {
		return start + (end - start) * delta;
	}
}