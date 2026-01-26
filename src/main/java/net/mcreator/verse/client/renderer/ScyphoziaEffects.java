package net.mcreator.verse.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScyphoziaEffects extends DimensionSpecialEffects {

	public ScyphoziaEffects() {
		super(
				Float.NaN, // cloudLevel - NaN means no clouds
				true,      // hasGround
				SkyType.NONE, // skyType - no sky rendering
				false,     // forceBrightLightmap
				false      // constantAmbientLight
		);
	}

	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
		// Apply depth-based darkness to fog color
		Minecraft mc = Minecraft.getInstance();
		if (mc.gameRenderer != null && mc.gameRenderer.getMainCamera() != null) {
			double cameraY = mc.gameRenderer.getMainCamera().getPosition().y;
			float darknessFactor = calculateDarknessFactor(cameraY);

			// Base deep blue fog color with darkness applied
			return new Vec3(
					0.043 * darknessFactor,
					0.078 * darknessFactor,
					0.286 * darknessFactor
			);
		}

		// Fallback
		return new Vec3(0.043, 0.078, 0.286);
	}

	@Override
	public boolean isFoggyAt(int x, int y) {
		// Always foggy below y-level 16
		return y < 16;
	}

	@Override
	public float[] getSunriseColor(float timeOfDay, float partialTicks) {
		// No sunrise in this dimension
		return null;
	}

	/**
	 * Calculate how dark it should be based on Y level
	 */
	private float calculateDarknessFactor(double y) {
		if (y >= 40) {
			return 1.0f; // No darkness above Y=40
		} else if (y <= 20) {
			return 0.2f; // Maximum darkness at Y=20 and below (80% darker)
		} else {
			// Linear interpolation between Y=20 (0.2) and Y=40 (1.0)
			float t = (float)((y - 20) / 20.0);
			return 0.2f + (0.8f * t);
		}
	}
}