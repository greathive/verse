package net.mcreator.verse.client.renderer;

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
		// Deep blue-green fog color matching your dimension's aesthetic
		// This is the base fog color that will be modified by distance
		return new Vec3(0.043, 0.078, 0.286); // #0b1474 converted to 0-1 range
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
}