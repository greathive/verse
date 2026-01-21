package net.mcreator.verse.procedures;

public class BubbleParticleVisualScaleProcedure {
	public static double execute(double age) {
		return (-0.05) * age * Math.pow(age - 4, 3);
	}
}