package net.mcreator.verse.procedures;

public class BubbleParticleVisualScaleProcedure {
	public static double execute(double age) {
		return (-0.000001) * age * Math.pow(age - 55, 3);
	}
}