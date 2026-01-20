package net.mcreator.verse.procedures;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class BubbleParticleVisualScaleProcedure {
	public static double execute() {
		return Mth.nextDouble(RandomSource.create(), 0.5, 1.25);
	}
}